package com.example.data.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class AudioRecorderHelper(private val context: Context) {
  private val tag = "AudioRecorderHelper"
  private var mediaRecorder: MediaRecorder? = null
  private var currentOutputFile: File? = null
  var isRecording: Boolean = false
    private set

  fun startRecording(): File? {
    if (isRecording) {
      stopRecording()
    }

    return try {
      val audioDir = File(context.cacheDir, "audio_transcriptions")
      if (!audioDir.exists()) {
        audioDir.mkdirs()
      }

      val outputFile = File(audioDir, "math_voice_${System.currentTimeMillis()}.m4a")
      currentOutputFile = outputFile

      val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        MediaRecorder(context)
      } else {
        @Suppress("DEPRECATION")
        MediaRecorder()
      }

      recorder.apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setAudioSamplingRate(44100)
        setAudioEncodingBitRate(96000)
        setOutputFile(outputFile.absolutePath)
        prepare()
        start()
      }

      mediaRecorder = recorder
      isRecording = true
      Log.d(tag, "Audio recording started: ${outputFile.absolutePath}")
      outputFile
    } catch (e: Exception) {
      Log.e(tag, "Failed to start audio recording", e)
      cleanup()
      null
    }
  }

  fun stopRecording(): File? {
    if (!isRecording) return currentOutputFile
    return try {
      mediaRecorder?.apply {
        try {
          stop()
        } catch (e: Exception) {
          Log.w(tag, "MediaRecorder stop failed: ${e.message}")
        }
        release()
      }
      mediaRecorder = null
      isRecording = false
      val file = currentOutputFile
      Log.d(tag, "Audio recording finished: ${file?.absolutePath} (size: ${file?.length()} bytes)")
      file
    } catch (e: Exception) {
      Log.e(tag, "Error stopping audio recording", e)
      cleanup()
      null
    }
  }

  fun createSampleAudioFile(sampleKey: String): File {
    val audioDir = File(context.cacheDir, "audio_samples")
    if (!audioDir.exists()) audioDir.mkdirs()
    val sampleFile = File(audioDir, "sample_${sampleKey}.m4a")
    if (!sampleFile.exists()) {
      // Write sample bytes
      FileOutputStream(sampleFile).use { fos ->
        // Small mock MPEG-4 header placeholder bytes so it's a valid non-empty file
        val mockBytes = ByteArray(1024) { (it % 127).toByte() }
        fos.write(mockBytes)
      }
    }
    return sampleFile
  }

  private fun cleanup() {
    try {
      mediaRecorder?.release()
    } catch (_: Exception) {}
    mediaRecorder = null
    isRecording = false
  }
}
