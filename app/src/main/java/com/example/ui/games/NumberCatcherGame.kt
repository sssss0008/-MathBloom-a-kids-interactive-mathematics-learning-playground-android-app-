package com.example.ui.games

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange
import com.example.ui.theme.MathYellow

data class BubbleItem(
  val id: Int,
  val value: Int,
  val startXFraction: Float,
  val color: Color
)

@Composable
fun NumberCatcherGame(
  onGameFinished: (starsWon: Int) -> Unit = {},
  onExit: () -> Unit = {}
) {
  var score by remember { mutableIntStateOf(0) }
  var round by remember { mutableIntStateOf(1) }
  val targetTargetSum = remember(round) { 2 + round * 2 }
  val targetPrompt = remember(round) { "Catch numbers that equal: $targetTargetSum!" }

  // Generate bubbles with one guaranteed correct answer and others random
  val bubbles = remember(round) {
    listOf(
      BubbleItem(1, targetTargetSum, 0.2f, Color(0xFFFF5252)),
      BubbleItem(2, targetTargetSum - 1, 0.5f, Color(0xFF3D5AFE)),
      BubbleItem(3, targetTargetSum + 2, 0.8f, Color(0xFF00C853)),
      BubbleItem(4, targetTargetSum + 1, 0.35f, Color(0xFFFF6D00))
    ).shuffled()
  }

  var gameOver by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF0F4FF))
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top Score & Round Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(
        onClick = onExit,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECEFF1)),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("⬅️ Exit", color = Color(0xFF455A64), fontWeight = FontWeight.Bold)
      }

      Surface(
        shape = RoundedCornerShape(14.dp),
        color = MathYellow.copy(alpha = 0.25f)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "⭐ Score: $score", fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
        }
      }

      Surface(
        shape = RoundedCornerShape(14.dp),
        color = MathBlue.copy(alpha = 0.15f)
      ) {
        Text(
          text = "Round $round/5",
          fontWeight = FontWeight.Bold,
          color = MathBlue,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Goal Banner
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "🎯 Target Goal:",
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = "$targetTargetSum",
          fontSize = 38.sp,
          fontWeight = FontWeight.ExtraBold,
          color = MathBlue
        )
        Text(
          text = "Tap the bubble with this exact number!",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    if (!gameOver) {
      // Floating Number Bubbles Area
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .background(Color.White, RoundedCornerShape(24.dp))
          .padding(16.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.SpaceEvenly,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            bubbles.take(2).forEach { bubble ->
              BubbleWidget(bubble = bubble) {
                if (bubble.value == targetTargetSum) {
                  score += 10
                  if (round < 5) round++ else gameOver = true
                }
              }
            }
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            bubbles.drop(2).forEach { bubble ->
              BubbleWidget(bubble = bubble) {
                if (bubble.value == targetTargetSum) {
                  score += 10
                  if (round < 5) round++ else gameOver = true
                }
              }
            }
          }
        }
      }
    } else {
      // Game Complete Celebration
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text = "🏆", fontSize = 54.sp)
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "Awesome Job, Number Catcher!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MathGreen
          )
          Text(
            text = "You scored $score points!",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
          )

          Button(
            onClick = {
              onGameFinished(score / 10)
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MathGreen)
          ) {
            Text(text = "Claim Star Reward! ⭐", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
private fun BubbleWidget(bubble: BubbleItem, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .size(90.dp)
      .clip(CircleShape)
      .background(bubble.color.copy(alpha = 0.85f))
      .clickable(onClick = onClick)
      .testTag("bubble_${bubble.value}"),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = "${bubble.value}",
      fontSize = 28.sp,
      fontWeight = FontWeight.ExtraBold,
      color = Color.White
    )
  }
}
