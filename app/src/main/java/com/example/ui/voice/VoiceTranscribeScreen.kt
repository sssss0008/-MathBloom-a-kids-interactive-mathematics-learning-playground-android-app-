package com.example.ui.voice

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.gemini.TranscriptionResult
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange
import com.example.ui.theme.MathPurple
import kotlinx.coroutines.delay
import java.io.File

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoiceTranscribeScreen(
  isRecording: Boolean,
  isTranscribing: Boolean,
  transcriptionResult: TranscriptionResult?,
  onStartRecording: () -> Unit,
  onStopRecording: () -> Unit,
  onTranscribeSample: (String) -> Unit
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  var hasPermission by remember {
    mutableStateOf(
      ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO
      ) == PackageManager.PERMISSION_GRANTED
    )
  }

  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    hasPermission = isGranted
    if (isGranted) {
      onStartRecording()
    } else {
      Toast.makeText(
        context,
        "Microphone permission is needed to record audio.",
        Toast.LENGTH_SHORT
      ).show()
    }
  }

  // Timer for recording duration
  var recordSeconds by remember { mutableIntStateOf(0) }
  LaunchedEffect(isRecording) {
    if (isRecording) {
      recordSeconds = 0
      while (isRecording) {
        delay(1000)
        recordSeconds++
      }
    } else {
      recordSeconds = 0
    }
  }

  // Pulsing animation when recording
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(600),
      repeatMode = RepeatMode.Reverse
    ),
    label = "micPulse"
  )

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Header Banner
    item {
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Surface(
              shape = CircleShape,
              color = MathPurple.copy(alpha = 0.15f),
              modifier = Modifier.size(36.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text("🎙️", fontSize = 18.sp)
              }
            }
            Text(
              text = "Voice Math Transcriber",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFEDE7F6)
          ) {
            Text(
              text = "MODEL: gemini-3.5-transcribe",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MathPurple,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              fontFamily = FontFamily.Monospace
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Speak any math problem or question aloud! The microphone records your voice and gemini-3.5-transcribe turns your speech into crystal-clear math text.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
          )
        }
      }
    }

    // Microphone Action Center
    item {
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isRecording) Color(0xFFFFEBEE) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = when {
              isRecording -> "Listening to your voice... (${recordSeconds}s)"
              isTranscribing -> "Transcribing with gemini-3.5-transcribe..."
              else -> "Tap the microphone to speak"
            },
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = if (isRecording) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(20.dp))

          // Big Record Button with Pulse
          Box(
            modifier = Modifier.size(110.dp),
            contentAlignment = Alignment.Center
          ) {
            if (isRecording) {
              Box(
                modifier = Modifier
                  .size(100.dp)
                  .scale(pulseScale)
                  .clip(CircleShape)
                  .background(Color(0xFFFFCDD2).copy(alpha = 0.5f))
              )
            }

            Surface(
              modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .clickable {
                  if (isRecording) {
                    onStopRecording()
                  } else {
                    if (hasPermission) {
                      onStartRecording()
                    } else {
                      permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                  }
                },
              shape = CircleShape,
              color = if (isRecording) Color(0xFFD32F2F) else MathBlue,
              shadowElevation = 6.dp
            ) {
              Box(contentAlignment = Alignment.Center) {
                if (isTranscribing) {
                  CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(32.dp),
                    strokeWidth = 3.dp
                  )
                } else {
                  Text(
                    text = if (isRecording) "⏹️" else "🎙️",
                    fontSize = 32.sp
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (isRecording) {
              Button(
                onClick = onStopRecording,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(12.dp)
              ) {
                Text("Stop & Transcribe ✓", fontWeight = FontWeight.Bold)
              }
            } else {
              OutlinedButton(
                onClick = {
                  if (hasPermission) onStartRecording() else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                },
                shape = RoundedCornerShape(12.dp)
              ) {
                Text("Start Recording 🎤", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }

    // Transcription Output Card
    if (transcriptionResult != null) {
      item {
        Card(
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "📝 Transcribed Speech",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }

              Surface(
                shape = RoundedCornerShape(8.dp),
                color = MathGreen.copy(alpha = 0.15f)
              ) {
                Text(
                  text = "✓ Synced to Firestore",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = MathGreen,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
              shape = RoundedCornerShape(14.dp),
              color = Color(0xFFF7F9FC),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = transcriptionResult.transcribedText.ifEmpty { "Speech transcribed successfully." },
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF263238),
                modifier = Modifier.padding(14.dp),
                lineHeight = 22.sp
              )
            }

            if (!transcriptionResult.detectedMathEquation.isNullOrBlank()) {
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "Detected Math Formula:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MathPurple
              )
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = MathPurple.copy(alpha = 0.08f),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
              ) {
                Text(
                  text = transcriptionResult.detectedMathEquation,
                  fontFamily = FontFamily.Monospace,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = MathPurple,
                  modifier = Modifier.padding(10.dp)
                )
              }
            }

            if (!transcriptionResult.solutionAnswer.isNullOrBlank()) {
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "Step-by-Step Solution:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MathGreen
              )
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = MathGreen.copy(alpha = 0.08f),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
              ) {
                Text(
                  text = transcriptionResult.solutionAnswer,
                  fontSize = 13.sp,
                  color = Color(0xFF1B5E20),
                  modifier = Modifier.padding(10.dp),
                  lineHeight = 18.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End
            ) {
              OutlinedButton(
                onClick = {
                  clipboardManager.setText(AnnotatedString(transcriptionResult.transcribedText))
                  Toast.makeText(context, "Copied transcription to clipboard!", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(10.dp)
              ) {
                Text("Copy Text 📋", fontSize = 12.sp)
              }
            }
          }
        }
      }
    }

    // Try Pre-Recorded Samples Pill List
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "🧪 Try Sample Voice Questions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Click any test audio prompt to immediately verify gemini-3.5-transcribe in action:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
          )

          val samples = listOf(
            "What is seven times eight plus fifteen?",
            "If I have three quarters of a pizza and eat one slice, how much remains?",
            "How many vertices and edges does a cube have?",
            "What is twenty-four divided by six?"
          )

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            samples.forEach { sample ->
              Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF1F5F9),
                modifier = Modifier.clickable { onTranscribeSample(sample) }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("▶️", fontSize = 12.sp)
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = sample,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E293B)
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
