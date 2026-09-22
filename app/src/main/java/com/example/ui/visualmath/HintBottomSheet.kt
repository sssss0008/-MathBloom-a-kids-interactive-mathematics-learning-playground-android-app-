package com.example.ui.visualmath

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HintBottomSheet(
  hint1: String,
  hint2: String,
  hint3: String,
  onDismiss: () -> Unit,
  onViewStepByStep: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var currentTier by remember { mutableIntStateOf(1) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Text(text = "💡", fontSize = 28.sp)
        Spacer(modifier = Modifier.size(8.dp))
        Text(
          text = "Need a Little Hint?",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MathOrange
        )
      }

      Text(
        text = "Hints are great helpers! Choose how much help you need.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
      )

      // Tier 1 Hint
      HintCard(
        tier = 1,
        title = "Little Clue",
        content = hint1.ifEmpty { "Look closely at the numbers or objects." },
        isRevealed = currentTier >= 1,
        color = MathGreen
      )

      if (currentTier >= 2) {
        Spacer(modifier = Modifier.height(10.dp))
        HintCard(
          tier = 2,
          title = "Concept Helper",
          content = hint2.ifEmpty { "Break it down into two smaller steps." },
          isRevealed = true,
          color = MathBlue
        )
      }

      if (currentTier >= 3) {
        Spacer(modifier = Modifier.height(10.dp))
        HintCard(
          tier = 3,
          title = "Almost There",
          content = hint3.ifEmpty { "Count carefully and check each choice." },
          isRevealed = true,
          color = MathOrange
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        if (currentTier < 3) {
          Button(
            onClick = { currentTier++ },
            colors = ButtonDefaults.buttonColors(containerColor = MathOrange),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text(text = "Next Hint 🔍", fontWeight = FontWeight.Bold)
          }
        } else {
          Button(
            onClick = {
              onDismiss()
              onViewStepByStep()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MathBlue),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text(text = "Full Solution 🚀", fontWeight = FontWeight.Bold)
          }
        }

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECEFF1)),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.weight(1f)
        ) {
          Text(text = "Got It! 👍", color = Color(0xFF37474F), fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun HintCard(
  tier: Int,
  title: String,
  content: String,
  isRevealed: Boolean,
  color: Color
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = color.copy(alpha = 0.1f),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.Top
    ) {
      Box(
        modifier = Modifier
          .size(28.dp)
          .background(color, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "$tier",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      }
      Spacer(modifier = Modifier.size(12.dp))
      Column {
        Text(
          text = title,
          fontWeight = FontWeight.Bold,
          color = color,
          fontSize = 14.sp
        )
        Text(
          text = content,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.padding(top = 2.dp)
        )
      }
    }
  }
}
