//package com.example.musicplayer.service
//
//import android.app.PendingIntent.FLAG_IMMUTABLE
//import android.app.PendingIntent.FLAG_UPDATE_CURRENT
//import android.content.Intent
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import androidx.annotation.OptIn
//import androidx.core.app.TaskStackBuilder
//import androidx.media3.common.AudioAttributes
//import androidx.media3.common.C
//import androidx.media3.common.MediaItem
//import androidx.media3.common.Timeline
//import androidx.media3.common.util.UnstableApi
//import androidx.media3.datasource.ResolvingDataSource
//import androidx.media3.datasource.TransferListener
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.exoplayer.source.*
//import androidx.media3.exoplayer.upstream.Allocator
//import androidx.media3.exoplayer.upstream.Loader
//import androidx.media3.exoplayer.upstream.ParsingLoadable
//import androidx.media3.session.LibraryResult
//import androidx.media3.session.MediaLibraryService
//import androidx.media3.session.MediaSession
//import androidx.media3.session.SessionResult
//import com.example.musicplayer.MainActivity
//import com.google.common.collect.ImmutableList
//import com.google.common.util.concurrent.Futures
//import com.google.common.util.concurrent.ListenableFuture
//import java.io.IOException
//import java.io.InputStream
//
//class MusicService: MediaLibraryService() {
//    private lateinit var player: ExoPlayer
//    private lateinit var mediaLibrarySession: MediaLibrarySession
//    private val librarySessionCallback = CustomMediaLibrarySessionCallback()
//
//    companion object {
//        private const val SEARCH_QUERY_PREFIX_COMPAT = "androidx://media3-session/playFromSearch"
//        private const val SEARCH_QUERY_PREFIX = "androidx://media3-session/setMediaUri"
//    }
//
//    private inner class CustomMediaLibrarySessionCallback :
//        MediaLibrarySession.MediaLibrarySessionCallback {
//        override fun onGetLibraryRoot(
//            session: MediaLibrarySession,
//            browser: MediaSession.ControllerInfo,
//            params: LibraryParams?
//        ): ListenableFuture<LibraryResult<MediaItem>> {
//            return Futures.immediateFuture(LibraryResult.ofItem(MediaItemTree.getRootItem(), params))
//        }
//
//        override fun onGetItem(
//            session: MediaLibrarySession,
//            browser: MediaSession.ControllerInfo,
//            mediaId: String
//        ): ListenableFuture<LibraryResult<MediaItem>> {
//            val item =
//                MediaItemTree.getItem(mediaId)
//                    ?: return Futures.immediateFuture(
//                        LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)
//                    )
//            return Futures.immediateFuture(LibraryResult.ofItem(item, /* params= */ null))
//        }
//
//        override fun onGetChildren(
//            session: MediaLibrarySession,
//            browser: MediaSession.ControllerInfo,
//            parentId: String,
//            page: Int,
//            pageSize: Int,
//            params: LibraryParams?
//        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
//            val children =
//                MediaItemTree.getChildren(parentId)
//                    ?: return Futures.immediateFuture(
//                        LibraryResult.ofError(LibraryResult.RESULT_ERROR_BAD_VALUE)
//                    )
//
//            return Futures.immediateFuture(LibraryResult.ofItemList(children, params))
//        }
//
//        private fun setMediaItemFromSearchQuery(query: String) {
//            // Only accept query with pattern "play [Title]" or "[Title]"
//            // Where [Title]: must be exactly matched
//            // If no media with exact name found, play a random media instead
//            val mediaTitle =
//                if (query.startsWith("play ", ignoreCase = true)) {
//                    query.drop(5)
//                } else {
//                    query
//                }
//
//            val item = MediaItemTree.getItemFromTitle(mediaTitle) ?: MediaItemTree.getRandomItem()
//            player.setMediaItem(item)
//        }
//
//        override fun onSetMediaUri(
//            session: MediaSession,
//            controller: MediaSession.ControllerInfo,
//            uri: Uri,
//            extras: Bundle
//        ): Int {
//
//            if (uri.toString().startsWith(SEARCH_QUERY_PREFIX) ||
//                uri.toString().startsWith(SEARCH_QUERY_PREFIX_COMPAT)
//            ) {
//                var searchQuery =
//                    uri.getQueryParameter("query") ?: return SessionResult.RESULT_ERROR_NOT_SUPPORTED
//                setMediaItemFromSearchQuery(searchQuery)
//
//                return SessionResult.RESULT_SUCCESS
//            } else {
//                return SessionResult.RESULT_ERROR_NOT_SUPPORTED
//            }
//        }
//    }
//
//    private class CustomMediaItemFiller : MediaSession.MediaItemFiller {
//        override fun fillInLocalConfiguration(
//            session: MediaSession,
//            controller: MediaSession.ControllerInfo,
//            mediaItem: MediaItem
//        ): MediaItem {
//            return MediaItemTree.getItem(mediaItem.mediaId) ?: mediaItem
//        }
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        initializeSessionAndPlayer()
//    }
//
//    override fun onDestroy() {
//        player.release()
//        mediaLibrarySession.release()
//        super.onDestroy()
//    }
//
//    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession {
//        return mediaLibrarySession
//    }
//
//    private fun initializeSessionAndPlayer() {
//        player =
//            ExoPlayer.Builder(this)
//                .setAudioAttributes(AudioAttributes.DEFAULT, /* handleAudioFocus= */ true)
//                .build()
//        MediaItemTree.initialize(assets)
//
//        val parentScreenIntent = Intent(this, MainActivity::class.java)
//        val intent = Intent(this, PlayerActivity::class.java)
//
//        val pendingIntent =
//            TaskStackBuilder.create(this).run {
//                addNextIntent(parentScreenIntent)
//                addNextIntent(intent)
//
//                val immutableFlag = if (Build.VERSION.SDK_INT >= 23) FLAG_IMMUTABLE else 0
//                getPendingIntent(0, immutableFlag or FLAG_UPDATE_CURRENT)
//            }
//
//        mediaLibrarySession =
//            MediaLibrarySession.Builder(this, player, librarySessionCallback)
//                .setMediaItemFiller(CustomMediaItemFiller())
//                .setSessionActivity(pendingIntent!!)
//                .build()
//    }
//}
//
//@UnstableApi class DynamicMediaSource(
//    private val mediaItem: MediaItem,
//    private val resolvingDataSourceFactory: ResolvingDataSource.Factory,
//    private val delegate: DefaultMediaSourceFactory
//) : CompositeMediaSource<Int>() {
//
//    private val loader = Loader("-DynamicSource")
//
//    private var actualSource: MediaSource? = null
//
//    @OptIn(UnstableApi::class)
//    override fun prepareSourceInternal(mediaTransferListener: TransferListener?) {
//        super.prepareSourceInternal(mediaTransferListener)
//
//        val loadable = ParsingLoadable<String>(
//            resolvingDataSourceFactory.createDataSource(),
//            mediaItem.localConfiguration?.uri ?: return,
//            C.DATA_TYPE_MEDIA_INITIALIZATION,
//            StreamLinkParser()
//        )
//
//        val loaderCallback = object : Loader.Callback<ParsingLoadable<String>> {
//            override fun onLoadCompleted(
//                loadable: ParsingLoadable<String>,
//                elapsedRealtimeMs: Long,
//                loadDurationMs: Long
//            ) {
//                val mediaItemWithStreamLink = mediaItem.buildUpon()
//                    .setUri(loadable.result)
//                    .build()
//
//                val actualMediaSource = delegate.createMediaSource(mediaItemWithStreamLink)
//
//                actualSource = actualMediaSource
//
//                prepareChildSource(null, actualMediaSource)
//            }
//
//            override fun onLoadCanceled(
//                loadable: ParsingLoadable<String>,
//                elapsedRealtimeMs: Long,
//                loadDurationMs: Long,
//                released: Boolean
//            ) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onLoadError(
//                loadable: ParsingLoadable<String>,
//                elapsedRealtimeMs: Long,
//                loadDurationMs: Long,
//                error: IOException,
//                errorCount: Int
//            ): Loader.LoadErrorAction {
//                TODO("Not yet implemented")
//            }
//        }
//
//        loader.startLoading(
//            loadable,
//            loaderCallback,
//            3
//        )
//    }
//
//    override fun getMediaItem(): MediaItem {
//        return mediaItem
//    }
//
//    override fun createPeriod(
//        id: MediaSource.MediaPeriodId,
//        allocator: Allocator,
//        startPositionUs: Long
//    ): MediaPeriod {
//        return actualSource?.createPeriod(id, allocator, startPositionUs) ?: MaskingMediaPeriod(
//            id,
//            allocator,
//            startPositionUs
//        )
//    }
//
//    override fun releasePeriod(mediaPeriod: MediaPeriod) {
//        actualSource?.releasePeriod(mediaPeriod)
//    }
//
//    override fun onChildSourceInfoRefreshed(
//        id: Int?,
//        mediaSource: MediaSource,
//        timeline: Timeline
//    ) = refreshSourceInfo(timeline)
//
//
//    private inner class StreamLinkParser : ParsingLoadable.Parser<String> {
//        override fun parse(uri: Uri, inputStream: InputStream): String = uri.toString()
//    }
//
//}
