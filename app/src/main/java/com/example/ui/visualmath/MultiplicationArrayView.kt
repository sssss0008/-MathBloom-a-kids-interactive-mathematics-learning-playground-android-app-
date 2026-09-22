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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathPurple
import com.example.ui.theme.MathYellow

@Composable
fun MultiplicationArrayView(
  rows: Int = 3,
  columns: Int = 4,
  itemEmoji: String = "🌸"
) {
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
        text = "$rows Rows of $columns Items",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MathPurple
      )

      Spacer(modifier = Modifier.height(14.dp))

      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .background(MathPurple.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
          .padding(12.dp)
      ) {
        for (r in 1..rows) {
          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Row $r:",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.SemiBold
            )
            for (c in 1..columns) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .background(Color.White, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
              ) {
                Text(text = itemEmoji, fontSize = 20.sp)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MathYellow.copy(alpha = 0.2f)
      ) {
        Text(
          text = "$rows × $columns = ${rows * columns} total",
          color = Color(0xFFE65100),
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
      }
    }
  }
}
