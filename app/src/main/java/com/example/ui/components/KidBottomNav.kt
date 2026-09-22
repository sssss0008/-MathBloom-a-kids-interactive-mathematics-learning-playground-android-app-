package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathBlue

enum class BottomTab(val title: String, val emoji: String) {
  ADVENTURE("Adventure", "🌟"),
  WORLD("World", "🗺️"),
  GAMES("Games", "🎮"),
  STUDIO("Studio", "🎨"),
  TREASURE("Treasure", "🏆")
}

@Composable
fun KidBottomNav(
  currentTab: BottomTab,
  onTabSelected: (BottomTab) -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .navigationBarsPadding(),
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 8.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      BottomTab.values().forEach { tab ->
        val isSelected = (currentTab == tab)
        Column(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) MathBlue.copy(alpha = 0.12f) else Color.Transparent)
            .clickable { onTabSelected(tab) }
            .padding(horizontal = 12.dp, vertical = 6.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(if (isSelected) MathBlue else Color.Transparent),
            contentAlignment = Alignment.Center
          ) {
            Text(text = tab.emoji, fontSize = if (isSelected) 20.sp else 18.sp)
          }

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = tab.title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MathBlue else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}
