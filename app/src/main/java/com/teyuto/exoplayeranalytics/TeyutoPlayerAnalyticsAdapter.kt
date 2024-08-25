package com.teyuto.exoplayeranalytics

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player

class CompositePlayerListener(private val listeners: List<Player.Listener>) : Player.Listener {
    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        listeners.forEach { it.onPlayWhenReadyChanged(playWhenReady, reason) }
    }

    override fun onPlaybackStateChanged(state: Int) {
        listeners.forEach { it.onPlaybackStateChanged(state) }
    }

    // ...
}

class TeyutoPlayerAnalyticsAdapter(token: String) : TeyutoPlayerAnalytics(token) {
    private lateinit var exoPlayer: ExoPlayer
    private val analyticsListener = object : Player.Listener {
        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            if (playWhenReady) onPlay() else onPause()
        }

        override fun onPlaybackStateChanged(state: Int) {
            if (state == Player.STATE_ENDED) {
                onEnded()
            }
        }
    }

    override fun attachEventListeners(player: Any) {
        exoPlayer = player as ExoPlayer
        
        // Recupera i listener esistenti
        //val existingListeners = exoPlayer.listeners
        
        // Crea una lista combinata di listener
        //val combinedListeners = mutableListOf<Player.Listener>()
        //combinedListeners.addAll(existingListeners)
        //combinedListeners.add(analyticsListener)
        
        // Sostituisci i listener esistenti con un CompositePlayerListener
        //exoPlayer.clearListeners()
        //exoPlayer.addListener(CompositePlayerListener(combinedListeners))
        exoPlayer.addListener(analyticsListener)
    }

    override fun isPlaying(): Boolean = exoPlayer.isPlaying

    override fun getCurrentTime(): Long = exoPlayer.currentPosition / 1000

    override fun getDuration(): Long = exoPlayer.duration

    override fun destroy() {
        super.destroy()
        exoPlayer.removeListener(analyticsListener)
    }
}
