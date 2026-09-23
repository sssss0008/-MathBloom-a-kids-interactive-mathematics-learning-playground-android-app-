package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.PI
import kotlin.math.sin

/**
 * SoundEffectType represents different game and learning events.
 */
enum class SoundEffectType {
  CORRECT_ANSWER,     // Cheerful rising major triad chime
  STREAK_BONUS,       // Sparkling celebration arpeggio
  BADGE_UNLOCKED,     // Fanfare chord with victory flourish
  HINT_CLICK,         // Gentle soft bell ping
  STAR_EARNED,        // High-pitched bright star sparkle
  ENCOURAGEMENT_VOICE // Uplifting voice affirmation
}

/**
 * MathBloomSoundManager:
 * Generates and plays synthesized, melodic audio clips and fun encouraging sound effects
 * using Android's native AudioTrack with PCM tone synthesis (no external asset downloads required!).
 * Also incorporates native TextToSpeech for energetic positive reinforcement vocal clips.
 */
class MathBloomSoundManager private constructor(private val context: Context) {

  private val coroutineScope = CoroutineScope(Dispatchers.Default)
  private var textToSpeech: TextToSpeech? = null
  private var isTtsReady = false
  private val mainHandler = Handler(Looper.getMainLooper())

  private val encouragingPhrases = listOf(
    "Fantastic job! You're a math superstar!",
    "Woohoo! That's correct!",
    "Brilliant thinking! You nailed it!",
    "You got it right! Awesome work!",
    "Spot on! Keep up the amazing math power!",
    "Way to go! You're blooming in math!"
  )

  init {
    try {
      textToSpeech = TextToSpeech(context.applicationContext) { status ->
        if (status == TextToSpeech.SUCCESS) {
          textToSpeech?.language = Locale.US
          textToSpeech?.setSpeechRate(1.05f)
          textToSpeech?.setPitch(1.2f) // Playful, upbeat pitch suitable for kids
          isTtsReady = true
        } else {
          Log.w("MathBloomSoundManager", "TTS initialization failed with status $status")
        }
      }
    } catch (e: Exception) {
      Log.e("MathBloomSoundManager", "Error initializing TTS", e)
    }
  }

  /**
   * Plays a synthesized sound effect based on event type.
   * Checks soundEnabled flag from ParentSettings.
   */
  fun playSound(type: SoundEffectType, soundEnabled: Boolean = true, voiceEnabled: Boolean = true) {
    if (!soundEnabled) return

    coroutineScope.launch {
      when (type) {
        SoundEffectType.CORRECT_ANSWER -> {
          // Play energetic major chord arpeggio (C5 -> E5 -> G5 -> C6)
          playArpeggio(listOf(523.25, 659.25, 783.99, 1046.50), noteDurationMs = 90)
          if (voiceEnabled) {
            mainHandler.postDelayed({
              speakEncouragement()
            }, 300)
          }
        }
        SoundEffectType.STREAK_BONUS -> {
          // Sparkling high-frequency chime
          playArpeggio(listOf(659.25, 783.99, 987.77, 1318.51, 1567.98), noteDurationMs = 70)
        }
        SoundEffectType.BADGE_UNLOCKED -> {
          // Victory fanfare
          playArpeggio(listOf(440.0, 554.37, 659.25, 880.0, 1108.73), noteDurationMs = 120)
        }
        SoundEffectType.HINT_CLICK -> {
          playSineTone(freq = 880.0, durationMs = 140, decay = true)
        }
        SoundEffectType.STAR_EARNED -> {
          playArpeggio(listOf(880.0, 1174.66, 1760.0), noteDurationMs = 80)
        }
        SoundEffectType.ENCOURAGEMENT_VOICE -> {
          speakEncouragement()
        }
      }
    }
  }

  /**
   * Speaks a random fun encouraging affirmation using child-friendly TTS.
   */
  fun speakEncouragement(phrase: String? = null) {
    if (!isTtsReady) return
    val textToSpeak = phrase ?: encouragingPhrases.random()
    mainHandler.post {
      try {
        textToSpeech?.speak(textToSpeak, TextToSpeech.QUEUE_ADD, null, "math_cheer_${System.currentTimeMillis()}")
      } catch (e: Exception) {
        Log.e("MathBloomSoundManager", "Error in speakEncouragement", e)
      }
    }
  }

  /**
   * Synthesizes and plays a sequence of frequencies in rapid succession.
   */
  private fun playArpeggio(frequencies: List<Double>, noteDurationMs: Int) {
    frequencies.forEach { freq ->
      playSineTone(freq, noteDurationMs, decay = true)
    }
  }

  /**
   * Generates a pure sinusoidal tone with smooth attack and exponential decay envelope
   * to eliminate clicks and produce a warm, musical chime.
   */
  private fun playSineTone(freq: Double, durationMs: Int, decay: Boolean = true) {
    try {
      val sampleRate = 44100
      val numSamples = (durationMs * sampleRate) / 1000
      val buffer = ShortArray(numSamples)

      for (i in 0 until numSamples) {
        val time = i.toDouble() / sampleRate
        val rawSine = sin(2.0 * PI * freq * time)

        // Envelope shaping (smooth fade-in and smooth fade-out to prevent pop/clicking)
        val envelope = when {
          i < 300 -> i / 300.0 // Fast 7ms attack
          decay -> {
            val remaining = (numSamples - i).toDouble() / numSamples
            remaining * remaining // Quadratic decay curve
          }
          else -> 1.0
        }

        buffer[i] = (rawSine * envelope * Short.MAX_VALUE * 0.75).toInt().toShort()
      }

      val audioTrack = AudioTrack.Builder()
        .setAudioAttributes(
          AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        )
        .setAudioFormat(
          AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(sampleRate)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()
        )
        .setBufferSizeInBytes(buffer.size * 2)
        .setTransferMode(AudioTrack.MODE_STATIC)
        .build()

      audioTrack.write(buffer, 0, buffer.size)
      audioTrack.play()

      // Give AudioTrack time to finish playback before releasing
      Thread.sleep(durationMs.toLong() + 30)
      audioTrack.stop()
      audioTrack.release()
    } catch (e: Exception) {
      Log.w("MathBloomSoundManager", "AudioTrack synthesis error", e)
    }
  }

  fun release() {
    try {
      textToSpeech?.stop()
      textToSpeech?.shutdown()
      textToSpeech = null
      isTtsReady = false
    } catch (e: Exception) {
      Log.w("MathBloomSoundManager", "Error releasing TTS", e)
    }
  }

  companion object {
    @Volatile
    private var INSTANCE: MathBloomSoundManager? = null

    fun getInstance(context: Context): MathBloomSoundManager {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: MathBloomSoundManager(context.applicationContext).also { INSTANCE = it }
      }
    }
  }
}
