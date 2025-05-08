package com.acroninspector.app.presentation.activity.mediaplayer

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.acroninspector.app.R
import com.acroninspector.app.common.constants.Constants.KEY_FILE_MEDIA_TYPE
import com.acroninspector.app.common.constants.Constants.KEY_FILE_URI
import com.acroninspector.app.common.constants.Constants.MEDIA_TYPE_AUDIO
import com.acroninspector.app.common.constants.Constants.MEDIA_TYPE_VIDEO
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import kotlinx.android.synthetic.main.activity_player.*

class MediaPlayerActivity : AppCompatActivity() {

    private lateinit var player: SimpleExoPlayer
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        uri = intent.getParcelableExtra(KEY_FILE_URI)!!

        val mediaType = intent.getIntExtra(KEY_FILE_MEDIA_TYPE, MEDIA_TYPE_VIDEO)
        if (mediaType == MEDIA_TYPE_AUDIO) {
            imageAudio.visibility = View.VISIBLE
        } else imageAudio.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this)
            .setTrackSelector(DefaultTrackSelector(this))
            .setLoadControl(DefaultLoadControl())
            .build()

        player_view.player = player
        player.playWhenReady = true

        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    override fun onPause() {
        super.onPause()
        player.release()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}