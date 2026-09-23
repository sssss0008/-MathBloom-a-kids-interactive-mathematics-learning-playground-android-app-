package com.example.ui.treasure

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Achievement
import com.example.data.model.BadgeAward
import com.example.data.model.CollectibleItem
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathCoral
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange
import com.example.ui.theme.MathPurple
import com.example.ui.theme.MathYellow

@Composable
fun TreasureScreen(
  collectibles: List<CollectibleItem>,
  achievements: List<Achievement>,
  badgeAwards: List<BadgeAward> = emptyList(),
  streakDays: Int
) {
  var selectedTab by remember { mutableStateOf("Badges & Trophies") } // "Badges & Trophies", "Collectibles", "Milestones"

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header & Streak Banner
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MathOrange.copy(alpha = 0.15f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(18.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "🔥", fontSize = 42.sp)
          Spacer(modifier = Modifier.width(16.dp))
          Column {
            Text(
              text = "$streakDays Day Adventure Streak!",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MathOrange
            )
            Text(
              text = "Every day you discover math, your flower blooms brighter!",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // Tab Switcher: Badges & Trophies vs Collectibles vs Milestones
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf("Badges & Trophies", "Collectibles", "Milestones").forEach { tab ->
          val isSelected = (selectedTab == tab)
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (isSelected) MathPurple else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
              .weight(1f)
              .clickable { selectedTab = tab }
          ) {
            Box(
              modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = tab,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 1,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }

    if (selectedTab == "Badges & Trophies") {
      // Badges & Virtual Stickers & Trophies (Stored in Firestore)
      item {
        val unlockedCount = badgeAwards.count { it.isUnlocked }
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MathPurple.copy(alpha = 0.1f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "☁️", fontSize = 28.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Virtual Stickers & Trophies ($unlockedCount of ${badgeAwards.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MathPurple
              )
              Text(
                text = "Earned across daily math quests & synced in Firebase Firestore",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      items(badgeAwards) { badge ->
        BadgeAwardCard(badge = badge)
      }
    } else if (selectedTab == "Collectibles") {
      // Collectibles Gallery
      item {
        val unlockedCount = collectibles.count { it.isUnlocked }
        Text(
          text = "🎁 Unlocked: $unlockedCount of ${collectibles.size}",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
      }

      val chunked = collectibles.chunked(3)
      items(chunked) { rowItems ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          rowItems.forEach { item ->
            Box(modifier = Modifier.weight(1f)) {
              CollectibleCard(item = item)
            }
          }
          if (rowItems.size < 3) {
            for (i in 0 until (3 - rowItems.size)) {
              Spacer(modifier = Modifier.weight(1f))
            }
          }
        }
      }
    } else {
      // Achievements & Milestones
      items(achievements) { ach ->
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(if (ach.isUnlocked) MathYellow.copy(alpha = 0.2f) else Color(0xFFECEFF1)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = if (ach.isUnlocked) ach.iconEmoji else "🔒",
                fontSize = 26.sp
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = ach.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = if (ach.isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray
              )
              Text(
                text = ach.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            if (ach.isUnlocked) {
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = MathGreen.copy(alpha = 0.15f)
              ) {
                Text(
                  text = "Unlocked! ✨",
                  color = MathGreen,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun BadgeAwardCard(badge: BadgeAward) {
  val isTrophy = badge.badgeType == "TROPHY"
  val accentColor = if (isTrophy) MathYellow else MathCoral

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (badge.isUnlocked) Color.White else Color(0xFFF7F8FA)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = if (badge.isUnlocked) 2.dp else 0.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(54.dp)
          .clip(if (isTrophy) CircleShape else RoundedCornerShape(14.dp))
          .background(
            if (badge.isUnlocked) accentColor.copy(alpha = 0.2f) else Color(0xFFECEFF1)
          )
          .then(
            if (badge.isUnlocked && !isTrophy) Modifier.border(2.dp, accentColor, RoundedCornerShape(14.dp))
            else Modifier
          ),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = if (badge.isUnlocked) badge.stickerOrTrophyEmoji else "🔒",
          fontSize = 28.sp
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = badge.title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall,
            color = if (badge.isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray
          )
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (isTrophy) MathYellow.copy(alpha = 0.2f) else MathCoral.copy(alpha = 0.15f)
          ) {
            Text(
              text = if (isTrophy) "TROPHY 🏆" else "STICKER ✨",
              fontSize = 9.sp,
              fontWeight = FontWeight.ExtraBold,
              color = if (isTrophy) Color(0xFF795548) else MathCoral,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = badge.description,
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Category: ${badge.category} • Lvl ${badge.milestoneLevel}",
            fontSize = 10.sp,
            color = Color.Gray
          )
          if (badge.isUnlocked) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "☁️ Synced to Firestore",
              fontSize = 10.sp,
              color = MathGreen,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@Composable
private fun CollectibleCard(item: CollectibleItem) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (item.isUnlocked) Color.White else Color(0xFFF0F2F5)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = if (item.isUnlocked) 2.dp else 0.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(54.dp)
          .clip(CircleShape)
          .background(if (item.isUnlocked) MathBlue.copy(alpha = 0.1f) else Color(0xFFE0E0E0)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = if (item.isUnlocked) item.emoji else "❓",
          fontSize = 28.sp
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = if (item.isUnlocked) item.name else "Secret Item",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = if (item.isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray,
        maxLines = 1
      )

      val rarityColor = when (item.rarity) {
        "Legendary" -> MathPurple
        "Rare" -> MathBlue
        else -> MathGreen
      }

      if (item.isUnlocked) {
        Text(
          text = item.rarity,
          fontSize = 9.sp,
          fontWeight = FontWeight.Bold,
          color = rarityColor
        )
      }
    }
  }
}

