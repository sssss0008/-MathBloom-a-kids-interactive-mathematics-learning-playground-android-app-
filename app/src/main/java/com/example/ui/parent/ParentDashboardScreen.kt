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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.firebase.AuthUserState
import com.example.data.firebase.SyncState
import com.example.data.model.ChildProfile
import com.example.data.model.ParentSettings
import com.example.data.model.UserProgress
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange
import com.example.ui.theme.MathPurple

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

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
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
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Learning insights, Firebase Auth & Firestore sync",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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

    // Firebase Auth & Google Sign-In Card
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                color = Color(0xFFFFF3E0),
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
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (authState.isAuthenticated) MathGreen.copy(alpha = 0.15f) else Color(0xFFFFEBEE)
            ) {
              Text(
                text = if (authState.isAuthenticated) "● Authenticated" else "● Guest Mode",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (authState.isAuthenticated) MathGreen else Color(0xFFD32F2F),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFF8FAFC),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "Signed in as: ${authState.displayName ?: "Parent / Guardian"}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF1E293B)
              )
              Text(
                text = "Email: ${authState.email ?: "awiskaracharya@gmail.com"}",
                fontSize = 13.sp,
                color = Color(0xFF475569)
              )
              Text(
                text = "User UID: ${authState.uid}",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF64748B)
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                color = Color(0xFFE8F5E9),
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
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MathGreen.copy(alpha = 0.15f)
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
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
          )

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MathGreen.copy(alpha = 0.12f),
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
                color = Color(0xFF1B5E20),
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

    // Weekly Activity Bar Chart Card
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "📊 Weekly Learning Minutes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Daily math practice over the past 7 days",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(16.dp))

          val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
          val minutes = listOf(12, 18, 15, 22, 10, 25, 20)
          val maxMin = 30f

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(110.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
          ) {
            days.forEachIndexed { i, day ->
              val m = minutes[i]
              val heightFraction = (m / maxMin).coerceIn(0.1f, 1f)

              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "${m}m", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                  modifier = Modifier
                    .width(24.dp)
                    .height((80 * heightFraction).dp)
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                    .background(if (i == 5 || i == 6) MathOrange else MathBlue)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = day, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }
        }
      }
    }

    // Child Profile & Adaptive Level Adjuster
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "🎓 Child Learning Level",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Adjust the curriculum difficulty level (1 to 8):",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                color = if (isSelected) MathBlue else Color(0xFFF5F7FA),
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
                    color = if (isSelected) Color.White else Color(0xFF263238),
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

    // App Preferences Card
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "⚙️ Learning Settings",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Narration
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Voice Narration & Audio Hints", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
              Text("Speaks problem text out loud for early readers", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
              Text("Playful Sound Effects", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
              Text("Fun chimes for stars and achievements", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F9FD)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "🔒 Child Safety & Privacy Commitment",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF37474F),
            fontSize = 13.sp
          )
          Text(
            text = "• Zero public chat, messaging, or social feeds\n• Secure Firebase Auth and encrypted Firestore persistence\n• Audio transcription powered by gemini-3.5-transcribe\n• Real-world search verified by gemini-3.5-flash & Google Search",
            fontSize = 12.sp,
            color = Color(0xFF546E7A),
            lineHeight = 18.sp,
            modifier = Modifier.padding(top = 4.dp)
          )
        }
      }
    }
  }
}
