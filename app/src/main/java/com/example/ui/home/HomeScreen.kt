package com.example.ui.home

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.data.model.Lesson
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathCoral
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange
import com.example.ui.theme.MathPurple
import com.example.ui.theme.MathYellow

@Composable
fun HomeScreen(
  childProfile: ChildProfile?,
  nextLesson: Lesson?,
  completedCount: Int,
  onStartLearning: () -> Unit,
  onPlayGame: () -> Unit,
  onOpenDrawing: () -> Unit,
  onOpenStories: () -> Unit,
  onOpenTreasure: () -> Unit,
  onOpenVoiceTranscribe: () -> Unit = {},
  onOpenSearchExplorer: () -> Unit = {}
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Hero Adventure Card
    item {
      Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = MathBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(22.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Hello, ${childProfile?.name ?: "Explorer"}! 👋",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Ready for today's math adventure?",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
              )
            }
            Text(text = childProfile?.avatar ?: "🦁", fontSize = 42.sp)
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Big "START LEARNING" Button
          Button(
            onClick = onStartLearning,
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = MathYellow,
              contentColor = Color(0xFF3E2723)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(56.dp)
              .testTag("start_learning_button")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Text(text = "🚀", fontSize = 22.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (nextLesson != null) "PLAY: ${nextLesson.title.uppercase()}" else "CONTINUE ADVENTURE",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Daily Streak & Stars Banner
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = Color.White.copy(alpha = 0.2f)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = "🔥", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "${childProfile?.streakDays ?: 1} Day Streak!",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(14.dp),
              color = Color.White.copy(alpha = 0.2f)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = "⭐", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "${childProfile?.totalStars ?: 15} Total Stars",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              }
            }
          }
        }
      }
    }

    // New AI & Gemini Features Showcase Row
    item {
      Text(
        text = "⚡ Voice & Grounded Explorer",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        QuickZoneCard(
          title = "Voice Transcribe",
          emoji = "🎙️",
          subtitle = "gemini-3.5-transcribe",
          color = MathPurple,
          modifier = Modifier.weight(1f),
          onClick = onOpenVoiceTranscribe
        )
        QuickZoneCard(
          title = "Search Explorer",
          emoji = "🔍",
          subtitle = "gemini-3.5-flash & Google",
          color = Color(0xFF1976D2),
          modifier = Modifier.weight(1f),
          onClick = onOpenSearchExplorer
        )
      }
    }

    // Quick Activity Zones
    item {
      Text(
        text = "Explore Math Worlds",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        QuickZoneCard(
          title = "Math Games",
          emoji = "🎮",
          subtitle = "Play & win stars",
          color = MathOrange,
          modifier = Modifier.weight(1f),
          onClick = onPlayGame
        )
        QuickZoneCard(
          title = "Drawing Math",
          emoji = "🎨",
          subtitle = "Shapes & canvas",
          color = MathCoral,
          modifier = Modifier.weight(1f),
          onClick = onOpenDrawing
        )
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        QuickZoneCard(
          title = "Math Stories",
          emoji = "📚",
          subtitle = "Barnaby Bear & quests",
          color = MathGreen,
          modifier = Modifier.weight(1f),
          onClick = onOpenStories
        )
        QuickZoneCard(
          title = "My Collection",
          emoji = "🦖",
          subtitle = "Dinos & space toys",
          color = MathYellow,
          modifier = Modifier.weight(1f),
          onClick = onOpenTreasure
        )
      }
    }
  }
}

@Composable
private fun QuickZoneCard(
  title: String,
  emoji: String,
  subtitle: String,
  color: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier.clickable(onClick = onClick)
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(52.dp)
          .clip(CircleShape)
          .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Text(text = emoji, fontSize = 28.sp)
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = subtitle,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
