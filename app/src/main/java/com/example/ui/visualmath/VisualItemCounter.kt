package com.example.ui.visualmath

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VisualItemCounter(
  count1: Int,
  count2: Int = 0,
  isSubtraction: Boolean = false,
  itemEmoji: String = "🍎",
  onCountTapped: (Int) -> Unit = {}
) {
  val tappedItems = remember { mutableStateMapOf<Int, Boolean>() }
  val scope = rememberCoroutineScope()

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp),
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 3.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = if (count2 > 0) {
          if (isSubtraction) "Tap to count what is left after taking away!"
          else "Count the two groups together!"
        } else "Tap each $itemEmoji to count them!",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Medium
      )

      Spacer(modifier = Modifier.height(16.dp))

      if (count2 == 0) {
        // Single group counting
        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          for (i in 1..count1) {
            CountingItem(
              index = i,
              emoji = itemEmoji,
              isTapped = tappedItems[i] == true,
              onTap = {
                tappedItems[i] = !(tappedItems[i] ?: false)
                onCountTapped(tappedItems.count { it.value })
              }
            )
          }
        }
      } else if (!isSubtraction) {
        // Addition: Two groups
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Group 1
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MathBlue.copy(alpha = 0.1f)),
            modifier = Modifier.padding(4.dp)
          ) {
            Column(
              modifier = Modifier.padding(10.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              FlowRow(
                maxItemsInEachRow = 3,
                horizontalArrangement = Arrangement.Center
              ) {
                for (i in 1..count1) {
                  Text(text = itemEmoji, fontSize = 26.sp, modifier = Modifier.padding(3.dp))
                }
              }
              Text(text = "$count1", fontWeight = FontWeight.Bold, color = MathBlue, fontSize = 18.sp)
            }
          }

          Text(
            text = "➕",
            fontSize = 24.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
          )

          // Group 2
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MathGreen.copy(alpha = 0.1f)),
            modifier = Modifier.padding(4.dp)
          ) {
            Column(
              modifier = Modifier.padding(10.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              FlowRow(
                maxItemsInEachRow = 3,
                horizontalArrangement = Arrangement.Center
              ) {
                for (i in 1..count2) {
                  Text(text = itemEmoji, fontSize = 26.sp, modifier = Modifier.padding(3.dp))
                }
              }
              Text(text = "$count2", fontWeight = FontWeight.Bold, color = MathGreen, fontSize = 18.sp)
            }
          }
        }
      } else {
        // Subtraction: Show total with some crossed out
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MathOrange.copy(alpha = 0.1f)),
          modifier = Modifier.padding(4.dp)
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            FlowRow(
              horizontalArrangement = Arrangement.Center,
              modifier = Modifier.fillMaxWidth()
            ) {
              for (i in 1..count1) {
                val isTakenAway = i > (count1 - count2)
                Box(
                  modifier = Modifier.padding(4.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = itemEmoji,
                    fontSize = 28.sp,
                    modifier = Modifier.scale(if (isTakenAway) 0.8f else 1.0f)
                  )
                  if (isTakenAway) {
                    Text(
                      text = "❌",
                      fontSize = 22.sp,
                      color = Color.Red
                    )
                  }
                }
              }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "$count1 take away $count2",
              fontWeight = FontWeight.Bold,
              color = MathOrange,
              fontSize = 16.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
      val counted = tappedItems.count { it.value }
      if (counted > 0 && count2 == 0) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MathBlue.copy(alpha = 0.15f)
        ) {
          Text(
            text = "You counted $counted $itemEmoji so far!",
            color = MathBlue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 13.sp
          )
        }
      }
    }
  }
}

@Composable
private fun CountingItem(
  index: Int,
  emoji: String,
  isTapped: Boolean,
  onTap: () -> Unit
) {
  val scale = remember { Animatable(1f) }
  val scope = rememberCoroutineScope()

  Box(
    modifier = Modifier
      .padding(6.dp)
      .size(54.dp)
      .scale(scale.value)
      .clickable {
        scope.launch {
          scale.animateTo(1.3f, spring(dampingRatio = 0.5f))
          scale.animateTo(1f, spring(dampingRatio = 0.7f))
        }
        onTap()
      }
      .testTag("item_counter_$index"),
    contentAlignment = Alignment.Center
  ) {
    if (isTapped) {
      Box(
        modifier = Modifier
          .size(46.dp)
          .background(MathGreen.copy(alpha = 0.2f), CircleShape)
      )
    }
    Text(text = emoji, fontSize = 32.sp)
    if (isTapped) {
      Box(
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .size(18.dp)
          .background(MathGreen, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "$index", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
      }
    }
  }
}
