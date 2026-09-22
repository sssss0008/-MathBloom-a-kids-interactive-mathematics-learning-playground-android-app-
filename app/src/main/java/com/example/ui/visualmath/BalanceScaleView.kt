package com.example.ui.visualmath

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange

@Composable
fun BalanceScaleView(
  leftWeight: Int,
  rightWeight: Int,
  itemEmoji: String = "💎"
) {
  val diff = (leftWeight - rightWeight).coerceIn(-10, 10)
  val tiltAngle by animateFloatAsState(
    targetValue = -diff * 1.5f,
    animationSpec = spring(dampingRatio = 0.6f),
    label = "scaleTilt"
  )

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
        text = if (leftWeight == rightWeight) "Scale is Balanced! ✨ (Equal)" else "Make both sides equal!",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = if (leftWeight == rightWeight) MathGreen else MathOrange
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Scale Drawing
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.size(240.dp, 120.dp)) {
          val centerX = size.width / 2f
          val baseY = size.height - 10.dp.toPx()
          val fulcrumTopY = 30.dp.toPx()

          // Fulcrum Triangle (Base)
          val baseTriangle = androidx.compose.ui.graphics.Path().apply {
            moveTo(centerX, fulcrumTopY)
            lineTo(centerX - 24.dp.toPx(), baseY)
            lineTo(centerX + 24.dp.toPx(), baseY)
            close()
          }
          drawPath(baseTriangle, Color(0xFF5B627D))

          // Pivoting Beam
          val beamLength = 90.dp.toPx()
          val angleRad = Math.toRadians(tiltAngle.toDouble())
          val leftX = (centerX - beamLength * Math.cos(angleRad)).toFloat()
          val leftY = (fulcrumTopY - beamLength * Math.sin(angleRad)).toFloat()
          val rightX = (centerX + beamLength * Math.cos(angleRad)).toFloat()
          val rightY = (fulcrumTopY + beamLength * Math.sin(angleRad)).toFloat()

          // Draw beam
          drawLine(
            color = Color(0xFF1E2238),
            start = Offset(leftX, leftY),
            end = Offset(rightX, rightY),
            strokeWidth = 6.dp.toPx(),
            cap = StrokeCap.Round
          )

          // Draw pivot circle
          drawCircle(Color(0xFFFFB300), radius = 7.dp.toPx(), center = Offset(centerX, fulcrumTopY))

          // Draw Left Pan hanging cords
          drawLine(Color(0xFF5B627D), Offset(leftX, leftY), Offset(leftX, leftY + 35.dp.toPx()), strokeWidth = 2.dp.toPx())
          drawLine(Color(0xFF1E2238), Offset(leftX - 25.dp.toPx(), leftY + 35.dp.toPx()), Offset(leftX + 25.dp.toPx(), leftY + 35.dp.toPx()), strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round)

          // Draw Right Pan hanging cords
          drawLine(Color(0xFF5B627D), Offset(rightX, rightY), Offset(rightX, rightY + 35.dp.toPx()), strokeWidth = 2.dp.toPx())
          drawLine(Color(0xFF1E2238), Offset(rightX - 25.dp.toPx(), rightY + 35.dp.toPx()), Offset(rightX + 25.dp.toPx(), rightY + 35.dp.toPx()), strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round)
        }
      }

      // Values indicators
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .background(Color(0xFFE8EDFF), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Text(text = "Left Pan", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(text = "$leftWeight $itemEmoji", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF3D5AFE))
        }

        Text(
          text = if (leftWeight == rightWeight) "⚖️ =" else if (leftWeight > rightWeight) ">" else "<",
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold
        )

        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .background(Color(0xFFE8F8EE), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Text(text = "Right Pan", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(text = "$rightWeight $itemEmoji", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MathGreen)
        }
      }
    }
  }
}
