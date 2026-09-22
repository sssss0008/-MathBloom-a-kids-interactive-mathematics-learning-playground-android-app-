package com.example.ui.curriculum

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.TopicCategory
import com.example.data.model.UserProgress
import com.example.ui.theme.MathGreen

@Composable
fun WorldMapScreen(
  progressList: List<UserProgress>,
  onTopicSelected: (topicId: String) -> Unit
) {
  val topics = TopicCategory.values()

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Column(modifier = Modifier.padding(bottom = 6.dp)) {
        Text(
          text = "🗺️ Math Explorer Worlds",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Explore magical lands to discover and practice new math skills!",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    items(topics) { topic ->
      val topicCompletedCount = progressList.count { it.topicId == topic.id && it.isCompleted }
      val topicColor = Color(topic.colorHex)

      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onTopicSelected(topic.id) }
          .testTag("world_topic_${topic.id}")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // World Badge
          Box(
            modifier = Modifier
              .size(62.dp)
              .clip(CircleShape)
              .background(topicColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Text(text = topic.emoji, fontSize = 32.sp)
          }

          Spacer(modifier = Modifier.width(16.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = topic.worldName,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )

            Text(
              text = topic.description,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 2
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (topicCompletedCount > 0) MathGreen.copy(alpha = 0.15f) else Color(0xFFECEFF1)
            ) {
              Text(
                text = if (topicCompletedCount > 0) "🌟 $topicCompletedCount Completed" else "✨ Ready to explore",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (topicCompletedCount > 0) MathGreen else Color(0xFF546E7A),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.width(8.dp))

          Surface(
            shape = CircleShape,
            color = topicColor,
            modifier = Modifier.size(36.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(text = "▶", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
          }
        }
      }
    }
  }
}
