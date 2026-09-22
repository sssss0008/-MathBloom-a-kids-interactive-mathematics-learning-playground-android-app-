package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
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
import com.example.data.model.ChildProfile
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathOrange
import com.example.ui.theme.MathYellow

@Composable
fun KidTopBar(
  childProfile: ChildProfile?,
  onAvatarClicked: () -> Unit = {},
  onParentGateClicked: () -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .statusBarsPadding(),
    color = MaterialTheme.colorScheme.background,
    tonalElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Child Avatar + Name + Level Pill
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .clickable(onClick = onAvatarClicked)
          .padding(4.dp)
      ) {
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(MathBlue.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Text(text = childProfile?.avatar ?: "🦁", fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
          Text(
            text = childProfile?.name ?: "Explorer",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MathBlue.copy(alpha = 0.15f)
          ) {
            Text(
              text = "Level ${childProfile?.level ?: 1}",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = MathBlue,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      }

      // Stars Counter, Streak, and Parent Lock Button
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Streak counter
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MathOrange.copy(alpha = 0.15f)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
          ) {
            Text(text = "🔥", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "${childProfile?.streakDays ?: 1}",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = MathOrange
            )
          }
        }

        // Stars counter
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MathYellow.copy(alpha = 0.2f)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(text = "⭐", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${childProfile?.totalStars ?: 0}",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color(0xFFE65100)
            )
          }
        }

        // Parent Shield button
        Surface(
          shape = CircleShape,
          color = Color(0xFFECEFF1),
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .clickable(onClick = onParentGateClicked)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              Icons.Default.Shield,
              contentDescription = "Parent Gate",
              tint = Color(0xFF455A64),
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }
  }
}
