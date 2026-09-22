package com.example.ui.visualmath

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathCoral
import com.example.ui.theme.MathOrange
import com.example.ui.theme.MathYellow
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FractionPizzaView(
  totalSlices: Int = 4,
  initialActiveSlices: Int = 1,
  isInteractive: Boolean = true,
  onSlicesChanged: (Int) -> Unit = {}
) {
  var activeSlices by remember { mutableIntStateOf(initialActiveSlices) }

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
        text = if (isInteractive) "Tap the pizza to change slices!" else "Look at the pizza fraction slices!",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(16.dp))

      Box(
        modifier = Modifier
          .size(190.dp)
          .clickable(enabled = isInteractive) {
            activeSlices = if (activeSlices < totalSlices) activeSlices + 1 else 1
            onSlicesChanged(activeSlices)
          }
          .testTag("pizza_canvas"),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.size(170.dp)) {
          val sweep = 360f / totalSlices
          val center = Offset(size.width / 2f, size.height / 2f)
          val radius = size.minDimension / 2f

          // Crust layer
          drawCircle(
            color = Color(0xFFE08C38),
            radius = radius,
            center = center
          )

          // Inner cheese & sauce slices
          for (i in 0 until totalSlices) {
            val startAngle = i * sweep - 90f
            val isHighlighted = i < activeSlices

            val sliceColor = if (isHighlighted) Color(0xFFFFCC00) else Color(0xFFFDE8A8)

            drawArc(
              color = sliceColor,
              startAngle = startAngle,
              sweepAngle = sweep,
              useCenter = true,
              topLeft = Offset(center.x - radius * 0.88f, center.y - radius * 0.88f),
              size = Size(radius * 1.76f, radius * 1.76f)
            )

            // Slice separator line
            val rad = Math.toRadians((startAngle).toDouble())
            val edgeX = (center.x + radius * cos(rad)).toFloat()
            val edgeY = (center.y + radius * sin(rad)).toFloat()
            drawLine(
              color = Color(0xFF9E5316),
              start = center,
              end = Offset(edgeX, edgeY),
              strokeWidth = 3.dp.toPx()
            )

            // Pepperoni topping if highlighted
            if (isHighlighted) {
              val midAngleRad = Math.toRadians((startAngle + sweep / 2f).toDouble())
              val pepDist = radius * 0.52f
              val pepX = (center.x + pepDist * cos(midAngleRad)).toFloat()
              val pepY = (center.y + pepDist * sin(midAngleRad)).toFloat()
              drawCircle(
                color = Color(0xFFD32F2F),
                radius = radius * 0.12f,
                center = Offset(pepX, pepY)
              )
            }
          }

          // Outer border
          drawCircle(
            color = Color(0xFF9E5316),
            radius = radius,
            center = center,
            style = Stroke(width = 3.dp.toPx())
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Fraction Representation Box
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(horizontal = 8.dp)
        ) {
          Text(
            text = "$activeSlices",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MathCoral
          )
          Box(
            modifier = Modifier
              .size(width = 44.dp, height = 3.dp)
              .padding(vertical = 1.dp)
          ) {
            Surface(color = Color(0xFF1E2238), modifier = Modifier.matchParentSize()) {}
          }
          Text(
            text = "$totalSlices",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Text(
          text = "= $activeSlices out of $totalSlices slices",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }
  }
}
