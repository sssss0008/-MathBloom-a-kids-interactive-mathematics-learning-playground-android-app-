package com.example.ui.visualmath

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathCoral
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InteractiveClockView(
  initialHour: Int = 9,
  initialMinute: Int = 0,
  isInteractive: Boolean = true,
  onTimeChanged: (hour: Int, minute: Int) -> Unit = { _, _ -> }
) {
  var hour by remember { mutableIntStateOf(initialHour) }
  var minute by remember { mutableIntStateOf(initialMinute) }

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
        text = if (isInteractive) "Use buttons to set the time!" else "Read the analog and digital clock!",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Analog Clock Face
      Box(
        modifier = Modifier.size(180.dp),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.size(170.dp)) {
          val center = Offset(size.width / 2f, size.height / 2f)
          val radius = size.minDimension / 2f

          // Face background
          drawCircle(color = Color(0xFFF0F4FF), radius = radius, center = center)
          drawCircle(
            color = Color(0xFF3D5AFE),
            radius = radius,
            center = center,
            style = Stroke(width = 6.dp.toPx())
          )

          // 12 numbers / tick marks
          for (h in 1..12) {
            val angleDeg = h * 30.0 - 90.0
            val rad = Math.toRadians(angleDeg)
            val tickRadius = radius * 0.82f
            val x = (center.x + tickRadius * cos(rad)).toFloat()
            val y = (center.y + tickRadius * sin(rad)).toFloat()

            // Draw hour dot
            drawCircle(
              color = Color(0xFF1E2238),
              radius = 3.dp.toPx(),
              center = Offset(x, y)
            )
          }

          // Short Hour Hand (Red/Coral)
          val hourAngleDeg = (hour % 12 + minute / 60f) * 30f - 90f
          val hourRad = Math.toRadians(hourAngleDeg.toDouble())
          val hourHandLength = radius * 0.52f
          val hourEndX = (center.x + hourHandLength * cos(hourRad)).toFloat()
          val hourEndY = (center.y + hourHandLength * sin(hourRad)).toFloat()

          drawLine(
            color = Color(0xFFFF5252),
            start = center,
            end = Offset(hourEndX, hourEndY),
            strokeWidth = 6.dp.toPx(),
            cap = StrokeCap.Round
          )

          // Long Minute Hand (Blue)
          val minuteAngleDeg = minute * 6f - 90f
          val minRad = Math.toRadians(minuteAngleDeg.toDouble())
          val minHandLength = radius * 0.76f
          val minEndX = (center.x + minHandLength * cos(minRad)).toFloat()
          val minEndY = (center.y + minHandLength * sin(minRad)).toFloat()

          drawLine(
            color = Color(0xFF3D5AFE),
            start = center,
            end = Offset(minEndX, minEndY),
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
          )

          // Center Pin
          drawCircle(color = Color(0xFF1E2238), radius = 6.dp.toPx(), center = center)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Digital Display
      Box(
        modifier = Modifier
          .background(Color(0xFF1E2238), RoundedCornerShape(12.dp))
          .padding(horizontal = 20.dp, vertical = 6.dp)
      ) {
        val minuteFormatted = if (minute < 10) "0$minute" else "$minute"
        Text(
          text = "$hour:$minuteFormatted",
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF4EE4A2)
        )
      }

      if (isInteractive) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            FilledTonalIconButton(
              onClick = {
                hour = if (hour > 1) hour - 1 else 12
                onTimeChanged(hour, minute)
              },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(Icons.Default.Remove, contentDescription = "Decrease hour")
            }
            Text(
              text = " Hour ",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = MathCoral
            )
            FilledTonalIconButton(
              onClick = {
                hour = if (hour < 12) hour + 1 else 1
                onTimeChanged(hour, minute)
              },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(Icons.Default.Add, contentDescription = "Increase hour")
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            FilledTonalIconButton(
              onClick = {
                minute = if (minute >= 15) minute - 15 else 45
                onTimeChanged(hour, minute)
              },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(Icons.Default.Remove, contentDescription = "Decrease minutes")
            }
            Text(
              text = " Min ",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = MathBlue
            )
            FilledTonalIconButton(
              onClick = {
                minute = (minute + 15) % 60
                onTimeChanged(hour, minute)
              },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(Icons.Default.Add, contentDescription = "Increase minutes")
            }
          }
        }
      }
    }
  }
}
