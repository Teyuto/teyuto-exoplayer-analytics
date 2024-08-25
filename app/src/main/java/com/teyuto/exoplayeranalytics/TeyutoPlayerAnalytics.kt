package com.teyuto.exoplayeranalytics

import android.os.Handler
import android.os.Looper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

abstract class TeyutoPlayerAnalytics(private val token: String) {
    private val apiUrl = "https://api.teyuto.tv/v1"
    private var videoId: String? = null
    protected var player: Any? = null
    private var currentAction: String? = null
    private var secondsPlayed: Double = 0.0
    private var updateHandler: Handler? = null
    private var firstTimeEnter = true

    private val client = OkHttpClient()

    fun init(player: Any, videoId: String) {
        this.player = player
        this.videoId = videoId
        attachEventListeners(player)
        startUpdateInterval()
    }

    protected abstract fun attachEventListeners(player: Any)

    private fun startUpdateInterval() {
        updateHandler = Handler(Looper.getMainLooper())
        updateHandler?.postDelayed(object : Runnable {
            override fun run() {
                incrementSeconds()
                updateHandler?.postDelayed(this, 500)
            }
        }, 500)
    }

    private fun incrementSeconds() {
        if (isPlaying()) {
            secondsPlayed += 0.5
            if (secondsPlayed >= 20) {
                updateTimeVideo(getCurrentTime(), 0)
                secondsPlayed = 0.0
            }
        }
    }

    private fun updateTimeVideo(time: Long, end: Int) {
        val formData = "id=${videoId}&time=${time}&action=${currentAction}&end=${end}&sp=${secondsPlayed}"

        val request = Request.Builder()
            .url("$apiUrl/video/?f=action_update")
            .post(RequestBody.create("application/x-www-form-urlencoded".toMediaTypeOrNull(), formData))
            .addHeader("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle response
            }
        })
    }

    private fun timeEnter(time: Long) {
        val formData = "id=${videoId}&time=${time}&firstTime=${if (firstTimeEnter) 1 else 0}"

        val request = Request.Builder()
            .url("$apiUrl/video/?f=action_enter")
            .post(RequestBody.create("application/x-www-form-urlencoded".toMediaTypeOrNull(), formData))
            .addHeader("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { responseBody ->
                    val jsonResponse = JSONArray(responseBody)
                    currentAction = jsonResponse.getJSONObject(0).getString("action")
                    firstTimeEnter = false
                }
            }
        })
    }

    fun onPlay() {
        timeEnter(getCurrentTime())
    }

    fun onPause() {
        updateTimeVideo(getCurrentTime(), 1)
    }

    fun onEnded() {
        updateTimeVideo(getDuration(), 1)
    }

    protected abstract fun isPlaying(): Boolean
    protected abstract fun getCurrentTime(): Long
    protected abstract fun getDuration(): Long

    open fun destroy() {
        updateHandler?.removeCallbacksAndMessages(null)
    }
}