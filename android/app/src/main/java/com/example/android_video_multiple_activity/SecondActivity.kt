package com.example.android_video_multiple_activity

import android.app.ActionBar
import android.app.Activity
import android.app.PictureInPictureParams
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.ima.ImaAdsLoader
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.ui.PlayerView

class SecondActivity : Activity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val playerView:PlayerView=findViewById(R.id.playerView)
        val fullscreenButton:Button=findViewById(R.id.fullscreen_button)
        playerView.addOnLayoutChangeListener { _, left, top, right, bottom,
                                               oldLeft, oldTop, oldRight, oldBottom ->
            if (left != oldLeft
                    || right != oldRight
                    || top != oldTop
                    || bottom != oldBottom) {
                // The playerView's bounds changed, update the source hint rect to
                // reflect its new bounds.
                val sourceRectHint = Rect()
                playerView.getGlobalVisibleRect(sourceRectHint)
                setPictureInPictureParams(
                        PictureInPictureParams.Builder()
                                .setSourceRectHint(sourceRectHint)
                                .setAspectRatio(Rational(16,9))
                                .build()
                )
            }
        }
        var fullscreen = false
        var adsLoader: ImaAdsLoader? = ImaAdsLoader.Builder( /* context= */this)
                .build()

        fullscreenButton.setOnClickListener(View.OnClickListener {  if (fullscreen) {
//                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_fullscreen_open))
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
//                if (getSupportActionBar() != null) {
//                    getSupportActionBar().show()
//                }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val params = playerView.layoutParams as LinearLayout.LayoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = (200 * applicationContext.resources.displayMetrics.density).toInt()
            playerView.layoutParams = params
            fullscreen = false
        }else{
//            fullscreenButton.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, android.R.drawable.ic_fullscreen_close))
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
//            if (getSupportActionBar() != null) {
//                getSupportActionBar().hide()
//            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            val params = playerView.layoutParams as LinearLayout.LayoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            playerView.layoutParams = params
            fullscreen = true
        }
        })
        var mediaSourceFactory:MediaSource.Factory=DefaultMediaSourceFactory(this)
                .setLocalAdInsertionComponents({ adsLoader },playerView)

        val adTagUri="https://pubads.g.doubleclick.net/gampad/ads?iu=/21775744923/external/single_preroll_skippable&sz=640x480&ciu_szs=300x250%2C728x90&gdfp_req=1&output=vast&unviewed_position_start=1&env=vp&impl=s&correlator="
        val mediaItemBuilder = MediaItem.Builder()
        mediaItemBuilder.setUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")

        var mediaItem = mediaItemBuilder.build()
        var mediaSource=mediaSourceFactory.createMediaSource(mediaItem)
         mediaSource=if(adTagUri!=null){ mediaSourceFactory.createMediaSource(mediaSource.mediaItem.buildUpon().setAdsConfiguration(
                MediaItem.AdsConfiguration.Builder(Uri.parse(adTagUri)).build()).build())}else{mediaSource}

        val player = ExoPlayer.Builder(this)
                .build()
        adsLoader?.setPlayer(player)


        playerView.useController=true


        playerView.player=player
        player.setMediaSource(mediaSource)
        player.prepare()
        player.play()


    }

    override fun onUserLeaveHint() {
        enterPictureInPictureMode()
        super.onUserLeaveHint()
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if(isInPictureInPictureMode){
            actionBar?.hide()
        }else{
            actionBar?.show()
        }
    }
}