package com.example.ui.parent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.firebase.AuthUserState
import com.example.data.firebase.SyncState
import com.example.data.model.ChildProfile
import com.example.data.model.ParentSettings
import com.example.data.model.UserProgress
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathCoral
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange
import com.example.ui.theme.MathPurple
import com.example.ui.theme.MathTeal
import com.example.ui.theme.MathYellow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ParentDashboardScreen(
  childProfile: ChildProfile?,
  parentSettings: ParentSettings?,
  progressList: List<UserProgress>,
  syncState: SyncState,
  authState: AuthUserState,
  onUpdateLevel: (Int) -> Unit,
  onUpdateSettings: (ParentSettings) -> Unit,
  onSignInGoogle: () -> Unit,
  onSignInEmail: (String, String) -> Unit,
  onSignOut: () -> Unit,
  onTriggerSync: () -> Unit,
  onExitParentArea: () -> Unit
) {
  var selectedLevel by remember(childProfile?.level) { mutableIntStateOf(childProfile?.level ?: 1) }
  var soundEnabled by remember(parentSettings?.soundEnabled) { mutableStateOf(parentSettings?.soundEnabled ?: true) }
  var narrationEnabled by remember(parentSettings?.narrationEnabled) { mutableStateOf(parentSettings?.narrationEnabled ?: true) }
  var dailyGoal by remember(parentSettings?.dailyGoalMinutes) { mutableIntStateOf(parentSettings?.dailyGoalMinutes ?: 15) }

  // Modern Non-White Deep Slate Theme
  val parentCanvasBg = Color(0xFF0B132B)     // Dark Midnight Blue Canvas
  val cardBg = Color(0xFF1C2541)             // Navy Slate Card
  val innerCardBg = Color(0xFF151D34)        // Deep Inner Container
  val textLight = Color(0xFFF1F5F9)          // Clean Crisp Off-White / Silver Text
  val textMuted = Color(0xFF94A3B8)          // Slate 400

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(parentCanvasBg)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "🛡️ Parent Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = textLight
          )
          Text(
            text = "Learning insights, Firebase Auth & Firestore sync",
            style = MaterialTheme.typography.bodySmall,
            color = textMuted
          )
        }

        Button(
          onClick = onExitParentArea,
          colors = ButtonDefaults.buttonColors(containerColor = MathBlue),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("Child Mode 👶", fontWeight = FontWeight.Bold)
        }
      }
    }

    // VISUAL LEARNING DASHBOARD (Recharts / D3 inspired Interactive Component)
    item {
      LearningProgressVisualDashboard()
    }

    // Firebase Auth & Google Sign-In Card
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Surface(
                shape = CircleShape,
                color = Color(0xFF332014),
                modifier = Modifier.size(36.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text("🔥", fontSize = 18.sp)
                }
              }
              Text(
                text = "Firebase Auth & User Identity",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textLight
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (authState.isAuthenticated) MathGreen.copy(alpha = 0.2f) else Color(0x33FF5252)
            ) {
              Text(
                text = if (authState.isAuthenticated) "● Authenticated" else "● Guest Mode",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (authState.isAuthenticated) MathGreen else Color(0xFFFF8A80),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Surface(
            shape = RoundedCornerShape(14.dp),
            color = innerCardBg,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "Signed in as: ${authState.displayName ?: "Parent / Guardian"}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = textLight
              )
              Text(
                text = "Email: ${authState.email ?: "awiskaracharya@gmail.com"}",
                fontSize = 13.sp,
                color = textMuted
              )
              Text(
                text = "User UID: ${authState.uid}",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = textMuted.copy(alpha = 0.8f)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = onSignInGoogle,
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4)),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text("Google Sign-In 🌐", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            OutlinedButton(
              onClick = onSignOut,
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = textLight),
              modifier = Modifier.weight(1f)
            ) {
              Text("Switch Account 🔄", fontSize = 12.sp)
            }
          }
        }
      }
    }

    // Cloud Firestore Data Persistence Card
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Surface(
                shape = CircleShape,
                color = Color(0xFF0F382A),
                modifier = Modifier.size(36.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text("☁️", fontSize = 18.sp)
                }
              }
              Text(
                text = "Cloud Firestore Persistence",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textLight
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MathGreen.copy(alpha = 0.2f)
            ) {
              Text(
                text = "${syncState.persistedRecordCount} Records Synced",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MathGreen,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Text(
            text = "Data persistence is active. Child profiles, lesson attempts, audio transcriptions (gemini-3.5-transcribe), and grounded search entries are automatically stored in Cloud Firestore.",
            style = MaterialTheme.typography.bodySmall,
            color = textMuted,
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
          )

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MathGreen.copy(alpha = 0.15f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "✓", color = MathGreen, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = syncState.message,
                color = MathGreen,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Button(
            onClick = onTriggerSync,
            colors = ButtonDefaults.buttonColors(containerColor = MathGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Sync All to Cloud Firestore Now 🔄", fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // Child Profile & Adaptive Level Adjuster
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "🎓 Child Learning Level",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = textLight
          )
          Text(
            text = "Adjust the curriculum difficulty level (1 to 8):",
            style = MaterialTheme.typography.bodySmall,
            color = textMuted
          )

          Spacer(modifier = Modifier.height(12.dp))

          val levelLabels = listOf(
            "1: Early Counting",
            "2: Basic Addition",
            "3: Subtraction",
            "4: Multiplication",
            "5: Fractions & Pies",
            "6: Geometry & Clocks",
            "7: Word Problems",
            "8: Logic & Balance"
          )

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            levelLabels.forEachIndexed { index, label ->
              val lvl = index + 1
              val isSelected = (selectedLevel == lvl)

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) MathBlue else innerCardBg,
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    selectedLevel = lvl
                    onUpdateLevel(lvl)
                  }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = if (isSelected) "●" else "○",
                    color = if (isSelected) Color.White else MathBlue,
                    fontSize = 16.sp
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    text = label,
                    color = if (isSelected) Color.White else textLight,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                  )
                }
              }
            }
          }
        }
      }
    }

    // Password / PIN Protection Management Card
    item {
      var isEditingPin by remember { mutableStateOf(false) }
      var currentPinInput by remember { mutableStateOf("") }
      var newPinInput by remember { mutableStateOf("") }
      var confirmPinInput by remember { mutableStateOf("") }
      var pinStatusMessage by remember { mutableStateOf<String?>(null) }
      var pinIsError by remember { mutableStateOf(false) }

      val currentSavedPin = parentSettings?.pinCode ?: "1234"

      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Surface(
                shape = CircleShape,
                color = MathPurple.copy(alpha = 0.2f),
                modifier = Modifier.size(36.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(text = "🔐", fontSize = 18.sp)
                }
              }
              Column {
                Text(
                  text = "Parent Area Passcode & Security",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = textLight
                )
                Text(
                  text = "Protects adult settings and activity logs",
                  fontSize = 12.sp,
                  color = textMuted
                )
              }
            }

            TextButton(onClick = { isEditingPin = !isEditingPin }) {
              Text(
                text = if (isEditingPin) "Close" else "Change PIN",
                color = MathTeal,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Surface(
            shape = RoundedCornerShape(14.dp),
            color = innerCardBg,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Active Gate Protection",
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 13.sp,
                  color = textLight
                )
                Text(
                  text = "Password-protected via 4-digit PIN (${currentSavedPin.replace(Regex("."), "•")})",
                  fontSize = 11.sp,
                  color = textMuted
                )
              }
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = MathGreen.copy(alpha = 0.2f)
              ) {
                Text(
                  text = "PROTECTED",
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  color = MathGreen,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }

          if (isEditingPin) {
            Spacer(modifier = Modifier.height(14.dp))
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .background(innerCardBg, RoundedCornerShape(14.dp))
                .padding(14.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Text(
                text = "Update Parent Gate Passcode",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = textLight
              )

              OutlinedTextField(
                value = currentPinInput,
                onValueChange = { if (it.length <= 6) currentPinInput = it },
                label = { Text("Current PIN", color = textMuted) },
                placeholder = { Text("Enter current PIN (default: 1234)", color = textMuted.copy(alpha = 0.6f)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = textLight,
                  unfocusedTextColor = textLight,
                  focusedBorderColor = MathTeal,
                  unfocusedBorderColor = Color(0xFF334155)
                ),
                modifier = Modifier.fillMaxWidth()
              )

              OutlinedTextField(
                value = newPinInput,
                onValueChange = { if (it.length <= 6) newPinInput = it },
                label = { Text("New 4-Digit PIN", color = textMuted) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = textLight,
                  unfocusedTextColor = textLight,
                  focusedBorderColor = MathTeal,
                  unfocusedBorderColor = Color(0xFF334155)
                ),
                modifier = Modifier.fillMaxWidth()
              )

              OutlinedTextField(
                value = confirmPinInput,
                onValueChange = { if (it.length <= 6) confirmPinInput = it },
                label = { Text("Confirm New PIN", color = textMuted) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = textLight,
                  unfocusedTextColor = textLight,
                  focusedBorderColor = MathTeal,
                  unfocusedBorderColor = Color(0xFF334155)
                ),
                modifier = Modifier.fillMaxWidth()
              )

              pinStatusMessage?.let { msg ->
                Text(
                  text = msg,
                  color = if (pinIsError) MathCoral else MathGreen,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }

              Button(
                onClick = {
                  if (currentPinInput != currentSavedPin) {
                    pinStatusMessage = "Current PIN does not match."
                    pinIsError = true
                  } else if (newPinInput.length < 4) {
                    pinStatusMessage = "New PIN must be at least 4 digits."
                    pinIsError = true
                  } else if (newPinInput != confirmPinInput) {
                    pinStatusMessage = "New PIN and confirmation do not match."
                    pinIsError = true
                  } else {
                    parentSettings?.let { s ->
                      onUpdateSettings(s.copy(pinCode = newPinInput))
                    }
                    pinStatusMessage = "PIN successfully updated!"
                    pinIsError = false
                    currentPinInput = ""
                    newPinInput = ""
                    confirmPinInput = ""
                    isEditingPin = false
                  }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MathPurple),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text("Save New Passcode", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }

    // Comprehensive Activity Logs Card (Monitors problem solving, timestamps, hints, and accuracy)
    item {
      val dateFormat = remember { SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()) }

      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Surface(
                shape = CircleShape,
                color = MathTeal.copy(alpha = 0.2f),
                modifier = Modifier.size(36.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(text = "📜", fontSize = 18.sp)
                }
              }
              Column {
                Text(
                  text = "Live Activity Logs",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = textLight
                )
                Text(
                  text = "Track practice attempts, scores, and hints used",
                  fontSize = 12.sp,
                  color = textMuted
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFF2E3B52)
            ) {
              Text(
                text = "${progressList.size} Sessions",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = MathTeal,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          if (progressList.isEmpty()) {
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = innerCardBg,
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(text = "🌱", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "No practice sessions recorded yet",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = textLight
                )
                Text(
                  text = "As your child completes arithmetic problems and daily quests, session logs will appear here.",
                  fontSize = 12.sp,
                  color = textMuted,
                  textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
              }
            }
          } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              progressList.take(6).forEach { prog ->
                Surface(
                  shape = RoundedCornerShape(14.dp),
                  color = innerCardBg,
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                      Surface(
                        shape = CircleShape,
                        color = if (prog.isCompleted) MathGreen.copy(alpha = 0.2f) else MathOrange.copy(alpha = 0.2f),
                        modifier = Modifier.size(36.dp)
                      ) {
                        Box(contentAlignment = Alignment.Center) {
                          Text(text = if (prog.isCompleted) "✓" else "⏳", color = if (prog.isCompleted) MathGreen else MathOrange, fontWeight = FontWeight.Bold)
                        }
                      }
                      Column {
                        Text(
                          text = "Lesson: ${prog.lessonId.replace("_", " ").capitalize(Locale.ROOT)}",
                          fontWeight = FontWeight.Bold,
                          fontSize = 13.sp,
                          color = textLight
                        )
                        Text(
                          text = "${dateFormat.format(Date(prog.lastPracticedTimestamp))} • ${prog.hintsUsed} hints used",
                          fontSize = 11.sp,
                          color = textMuted
                        )
                      }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                      Text(
                        text = "${prog.accuracyPercentage}% Acc",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = if (prog.accuracyPercentage >= 80) MathGreen else MathOrange
                      )
                      Text(
                        text = "★ ${prog.starsEarned} stars",
                        fontSize = 11.sp,
                        color = MathYellow,
                        fontWeight = FontWeight.SemiBold
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

    // App Preferences Card
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "⚙️ Learning Settings",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = textLight
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Narration
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Voice Narration & Audio Hints", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = textLight)
              Text("Speaks problem text out loud for early readers", fontSize = 12.sp, color = textMuted)
            }
            Switch(
              checked = narrationEnabled,
              onCheckedChange = {
                narrationEnabled = it
                parentSettings?.let { s -> onUpdateSettings(s.copy(narrationEnabled = it)) }
              }
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Sound Effects
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Playful Sound Effects & Audio Clips", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = textLight)
              Text("Fun chimes & encouraging voice praise for correct answers", fontSize = 12.sp, color = textMuted)
            }
            Switch(
              checked = soundEnabled,
              onCheckedChange = {
                soundEnabled = it
                parentSettings?.let { s -> onUpdateSettings(s.copy(soundEnabled = it)) }
              }
            )
          }
        }
      }
    }


    // Privacy & Child Safety Statement
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = innerCardBg),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "🔒 Child Safety & Privacy Commitment",
            fontWeight = FontWeight.Bold,
            color = textLight,
            fontSize = 13.sp
          )
          Text(
            text = "• Zero public chat, messaging, or social feeds\n• Secure Firebase Auth and encrypted Firestore persistence\n• Audio transcription powered by gemini-3.5-transcribe\n• Real-world search verified by gemini-3.5-flash & Google Search",
            fontSize = 12.sp,
            color = textMuted,
            lineHeight = 18.sp,
            modifier = Modifier.padding(top = 4.dp)
          )
        }
      }
    }
  }
}

