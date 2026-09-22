package com.example.ui.games

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
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange
import com.example.ui.theme.MathPurple
import com.example.ui.theme.MathTeal

data class GameCardItem(
  val id: String,
  val title: String,
  val description: String,
  val emoji: String,
  val topic: String,
  val color: Color
)

@Composable
fun GameCenterScreen(
  onPlayGame: (gameId: String) -> Unit = {}
) {
  val gamesList = listOf(
    GameCardItem(
      "catcher",
      "Number Catcher",
      "Catch and pop matching sum bubbles before they float away!",
      "🎈",
      "Counting & Sums",
      MathBlue
    ),
    GameCardItem(
      "pizza_chef",
      "Fraction Pizza Chef",
      "Slice and serve delicious pizzas to match customer fraction orders!",
      "🍕",
      "Fractions & Sharing",
      MathOrange
    ),
    GameCardItem(
      "time_master",
      "Time Master Train",
      "Adjust the clock hands so the animals catch the departure train!",
      "⏰",
      "Clocks & Time",
      MathTeal
    ),
    GameCardItem(
      "money_cashier",
      "Toy Market Cashier",
      "Count dollar bills and coins to buy toys and give exact change!",
      "🧸",
      "Money & Budgets",
      MathGreen
    ),
    GameCardItem(
      "scale_balance",
      "Balance the Scales",
      "Weigh gems and numbers to keep the golden scale perfectly balanced!",
      "⚖️",
      "Logic & Equality",
      MathPurple
    )
  )

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
          text = "🎮 Math Arcade",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Play exciting mini-games and earn shiny reward stars!",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    items(gamesList) { game ->
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onPlayGame(game.id) }
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(62.dp)
              .clip(CircleShape)
              .background(game.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Text(text = game.emoji, fontSize = 32.sp)
          }

          Spacer(modifier = Modifier.size(16.dp))

          Column(modifier = Modifier.weight(1f)) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = game.color.copy(alpha = 0.12f)
            ) {
              Text(
                text = game.topic,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = game.color,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
              )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = game.title,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )

            Text(
              text = game.description,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 2
            )
          }

          Spacer(modifier = Modifier.size(8.dp))

          Surface(
            shape = CircleShape,
            color = game.color,
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
