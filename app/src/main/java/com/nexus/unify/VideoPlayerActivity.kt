package com.nexus.unify


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.EventLogger
import com.nexus.unify.databinding.ActivityVideoPlayerBinding


class VideoPlayerActivity : AppCompatActivity() {
    // Saved instance state keys.
    private val KEY_TRACK_SELECTION_PARAMETERS = "track_selection_parameters"
    private val KEY_SERVER_SIDE_ADS_LOADER_STATE = "server_side_ads_loader_state"
    private val KEY_ITEM_INDEX = "item_index"
    private val KEY_POSITION = "position"
    private val KEY_AUTO_PLAY = "auto_play"


    private lateinit var binding: ActivityVideoPlayerBinding
    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    private var player: ExoPlayer? = null
    private var playerView: StyledPlayerView? = null
    var isVideoPlaying = false
    var isVideoPlayingEnd = false
    private var fullscreenButton: ImageView? = null
    private val selectTracksButton: Button? = null
    // private var exoPlay: ImageButton? = null
    // private var exoPause: ImageButton? = null
    private var exoPlay: ImageView? = null
    private var exoPause: ImageView? = null
    private var exoRestart: ImageView? = null
    private var volumeBtn: ImageView? = null
    private var ivVideoQuality: ImageView? = null

    private var exoPosition: TextView? = null
    private var exoDuration: TextView? = null

    private var startTime: Long? = 0
    private var lastSeenTracks: Tracks? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private var fullscreen = false
    var isShowingTrackSelectionDialog: Boolean =false
    private val trackSelector: DefaultTrackSelector? = null
    private var trackSelectionParameters: TrackSelectionParameters? = null

    private var startAutoPlay = false
    private var startItemIndex = 0
    private var startPosition: Long = 0
    companion object {
        //const val URL = "http://185.184.208.112/contents/7715ADCD-3948-4973-9561-580D2B72BA75/HLS/IOS-MOB-HLS-FP/master.m3u8"
        //  var URL = "https://dxnqhjm6zg0n4.cloudfront.net/file_library/videos/vod_non_drm_ios/62948/downloads/1653279046176_151586827198633860_video_VOD.m3u8"
        var URL = "https://dxnqhjm6zg0n4.cloudfront.net/file_library/videos/vod_non_drm_ios/74762/active_forum_-_final_videos/1655192575290_184433_VOD.m3u8"
        //const val URL = "https://dxnqhjm6zg0n4.cloudfront.net/file_library/videos/vod_non_drm/15575/630202687021640800_video_VOD.mpd"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)




        if (savedInstanceState != null) {
            trackSelectionParameters = TrackSelectionParameters.fromBundle(
                savedInstanceState.getBundle(KEY_TRACK_SELECTION_PARAMETERS)!!
            )
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY)
            startItemIndex = savedInstanceState.getInt(KEY_ITEM_INDEX)

        } else {
            trackSelectionParameters = TrackSelectionParameters.Builder( /* context= */this).build()

        }
        preparePlayer()
    }

    private fun preparePlayer() {
        //  exoPlayer = ExoPlayer.Builder(this).build()

        playerView = findViewById(R.id.playerView)


        fullscreenButton = playerView?.findViewById(R.id.bt_full)
        exoPlay = playerView?.findViewById(R.id.exo_play)
        exoPause = playerView?.findViewById(R.id.exo_pause)
        exoPosition = playerView?.findViewById(R.id.exo_position)
        exoDuration = playerView?.findViewById(R.id.exo_duration)
        exoRestart = playerView?.findViewById(R.id.exo_restart)
        volumeBtn = playerView?.findViewById(R.id.volumebtn)
        ivVideoQuality = playerView?.findViewById(R.id.ivVideoQuality)


        lastSeenTracks = Tracks.EMPTY
        exoPlayer = ExoPlayer.Builder(this)
            //   .setTrackSelector(trackSelector)
            .build()
        exoPlayer!!.trackSelectionParameters = trackSelectionParameters!!
        //exoPlayer!!.addListener(playerEventListener)
        exoPlayer!!.addListener(PlayerEventListener())
        exoPlayer!!.addAnalyticsListener(EventLogger())
        exoPlayer?.playWhenReady = true
        binding.playerView.player = exoPlayer

        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()

        val mediaItem = MediaItem.fromUri(URL)
        //   val mediaSource = HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)

        exoPlayer?.addMediaItem(mediaItem)
        //   exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.seekTo(playbackPosition)
        exoPlayer?.playWhenReady = playWhenReady
        exoPlayer?.prepare()
        //  playerView?.player = player



        /*exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer!!.trackSelectionParameters = trackSelectionParameters!!
        exoPlayer!!.addListener(PlayerEventListener())
        exoPlayer!!.addAnalyticsListener(EventLogger())
        exoPlayer!!.setAudioAttributes(AudioAttributes.DEFAULT,  *//* handleAudioFocus= *//*true)
        exoPlayer!!.playWhenReady = startAutoPlay
        playerView?.player = player*/


        fullscreenButton?.setOnClickListener {
            if (fullscreen) {
                normalVideo()
            } else {
                fullscreenVideo()

            }

        }

        ivVideoQuality?.setOnClickListener {
            /* if (it === selectTracksButton && !isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(
                     player
                 )
             ) {*/

            isShowingTrackSelectionDialog = true
            val trackSelectionDialog = TrackSelectionDialog.createForPlayer(
                exoPlayer
            )  /* onDismissListener= */
            { dismissedDialog -> isShowingTrackSelectionDialog = false }
            trackSelectionDialog.show(supportFragmentManager,  /* tag= */null)
            /* }*/
        }
    }

    private fun releasePlayer() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            playWhenReady = player.playWhenReady
            player.release()
            exoPlayer = null
        }
    }
    private fun updateTrackSelectorParameters() {
        if (player != null) {
            trackSelectionParameters = player!!.trackSelectionParameters
        }
    }
    private class PlayerEventListener : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: @Player.State Int) {

        }

        override fun onPlayerError(error: PlaybackException) {

        }

        override fun onTracksChanged(tracks: Tracks) {

        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updateTrackSelectorParameters()

        outState.putBundle(
            KEY_TRACK_SELECTION_PARAMETERS,
            trackSelectionParameters!!.toBundle()
        )
        outState.putBoolean(
            KEY_AUTO_PLAY,
            startAutoPlay
        )
        outState.putInt(
            KEY_ITEM_INDEX,
            startItemIndex
        )
        outState.putLong(
            KEY_POSITION,
            startPosition
        )
        // saveServerSideAdsLoaderState(outState)
    }

    // User controls
    private fun updateButtonVisibility() {
        binding.selectTracksButton.isEnabled = player != null && TrackSelectionDialog.willHaveContent(
            player
        )
    }
    private fun showToast(messageId: Int) {
        showToast(getString(messageId))
    }
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
    // Fields used only for ad playback. The ads loader is loaded via reflection.
    private val playerEventListener: Player.Listener = object : Player.Listener {
        override fun onTimelineChanged(timeline: Timeline, reason: Int) {}
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {}

        override fun onTracksChanged(tracks: Tracks) {
            super.onTracksChanged(tracks)
            updateButtonVisibility()
            if (tracks === lastSeenTracks) {
                return
            }
            if (tracks.containsType(C.TRACK_TYPE_VIDEO)
                && !tracks.isTypeSupported(C.TRACK_TYPE_VIDEO,  /* allowExceedsCapabilities= */true)
            ) {
                showToast("error_unsupported_video")
            }
            if (tracks.containsType(C.TRACK_TYPE_AUDIO)
                && !tracks.isTypeSupported(C.TRACK_TYPE_AUDIO,  /* allowExceedsCapabilities= */true)
            ) {
                showToast("error_unsupported_audio")
            }
            lastSeenTracks = tracks
        }
        override fun onIsLoadingChanged(isLoading: Boolean) {}
        override fun onPlaybackStateChanged(playbackState: Int) {
            /*if (playbackState == Player.STATE_READY) {

            } else*/
            when (playbackState) {
                Player.STATE_IDLE -> {
                    isVideoPlayingEnd = false
                    // loaderPB.setVisibility(View.GONE);
                    //   window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    ///   if (mProgress != null && mProgress.isShowing()) mProgress.dismiss();

                }
                Player.STATE_BUFFERING -> {
                    // L.l("onPlaybackStateChanged_2: State_Buffering:$isVideoPlaying")
                    isVideoPlayingEnd = false
                    if (isVideoPlaying) {
                        exoPause?.visibility = View.VISIBLE
                        exoPlay?.visibility = View.GONE
                    } else {
                        exoPause?.visibility = View.GONE
                        exoPlay?.visibility = View.VISIBLE
                    }

                    exoRestart?.visibility = View.GONE
                }
                Player.STATE_ENDED -> {
                    //  L.l("onPlaybackStateChanged_4: " + "State_Ended")
                    isVideoPlayingEnd = true
                    exoPause?.visibility = View.GONE
                    exoPlay?.visibility = View.GONE

                    exoRestart?.visibility = View.VISIBLE
                    var totalVideoDuration: Long = 0
                    var startPauseTime: Long = 0

                }
                Player.STATE_READY -> {

                }
            }

        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            // val totalVideoDuration = CommonMethods.convertTimeInSecond(exoDuration!!.text.toString())
            // L.l("onPlaybackStateChanged_5: " + "onPlayWhenReadyChanged:$playWhenReady $totalVideoDuration")

        }

        override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {}
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            isVideoPlaying = isPlaying
            if (isVideoPlayingEnd) {
                exoPause?.visibility = View.GONE
                exoPlay?.visibility = View.GONE
            } else {
                if (isPlaying) {
                    exoPause?.visibility = View.VISIBLE
                    exoPlay?.visibility = View.GONE

                } else {
                    exoPause?.visibility = View.GONE
                    exoPlay?.visibility = View.VISIBLE
                }
            }

        }

        override fun onRepeatModeChanged(repeatMode: Int) {}
        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

        }

        override fun onEvents(player: Player, events: Player.Events) {}

    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun normalVideo() {
        fullscreenButton?.setImageDrawable(
            ContextCompat.getDrawable(
                this@VideoPlayerActivity,
                com.google.android.exoplayer2.R.drawable.exo_ic_fullscreen_enter
            )
        )
        //   window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        exitFullscreen()
        if (supportActionBar != null) {
            supportActionBar!!.show()
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val params = playerView?.layoutParams as RelativeLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = (200 * applicationContext.resources.displayMetrics.density).toInt()
        playerView?.layoutParams = params
        fullscreen = false

    }

    private fun fullscreenVideoAuto() {

        fullscreenButton?.setImageDrawable(
            ContextCompat.getDrawable(
                this@VideoPlayerActivity,
                com.google.android.exoplayer2.ui.R.drawable.exo_ic_fullscreen_exit
            )
        )
        /*  window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                  or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                  or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)*/
        fullscreen()

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val params = playerView?.layoutParams as RelativeLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        playerView?.layoutParams = params
        fullscreen = true
    }
    private fun fullscreen() {
        with(WindowInsetsControllerCompat(window, window.decorView)) {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.systemBars())
            //
        }
    }

    private fun exitFullscreen() {
        WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.systemBars())
    }
    private fun fullscreenVideo() {

        fullscreenButton?.setImageDrawable(
            ContextCompat.getDrawable(
                this@VideoPlayerActivity,
                com.google.android.exoplayer2.R.drawable.exo_ic_fullscreen_exit
            )
        )
        /*window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)*/
        fullscreen()
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val params = playerView?.layoutParams as RelativeLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        playerView?.layoutParams = params
        fullscreen = true

    }
}
