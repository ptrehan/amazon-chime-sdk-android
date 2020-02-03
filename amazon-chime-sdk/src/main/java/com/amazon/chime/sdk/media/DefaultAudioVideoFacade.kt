package com.amazon.chime.sdk.media

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.amazon.chime.sdk.media.mediacontroller.AudioVideoControllerFacade
import com.amazon.chime.sdk.media.mediacontroller.AudioVideoObserver
import com.amazon.chime.sdk.media.mediacontroller.RealtimeControllerFacade
import com.amazon.chime.sdk.media.mediacontroller.RealtimeObserver

class DefaultAudioVideoFacade(
    private val context: Context,
    private val audioVideoController: AudioVideoControllerFacade,
    private val realtimeController: RealtimeControllerFacade
) : AudioVideoFacade {

    private val permissions = arrayOf(
        Manifest.permission.MODIFY_AUDIO_SETTINGS,
        Manifest.permission.RECORD_AUDIO
    )

    override fun addObserver(observer: AudioVideoObserver) {
        audioVideoController.addObserver(observer)
    }

    override fun removeObserver(observer: AudioVideoObserver) {
        audioVideoController.removeObserver(observer)
    }

    override fun start() {
        val hasPermission: Boolean = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        if (hasPermission) {
            audioVideoController.start()
        } else {
            throw SecurityException(
                "Missing necessary permissions for WebRTC: ${permissions.joinToString(
                    separator = ", ",
                    prefix = "",
                    postfix = ""
                )}"
            )
        }
    }

    override fun stop() {
        audioVideoController.stop()
    }

    override fun realtimeLocalMute(): Boolean {
        return realtimeController.realtimeLocalMute()
    }

    override fun realtimeLocalUnmute(): Boolean {
        return realtimeController.realtimeLocalUnmute()
    }

    override fun realtimeAddObserver(observer: RealtimeObserver) {
        realtimeController.realtimeAddObserver(observer)
    }

    override fun realtimeRemoveObserver(observer: RealtimeObserver) {
        realtimeController.realtimeRemoveObserver(observer)
    }
}
