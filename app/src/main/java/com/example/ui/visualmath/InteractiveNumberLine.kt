package com.example.ui.visualmath

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange

@Composable
fun InteractiveNumberLine(
  startNumber: Int = 0,
  maxNumber: Int = 12,
  initialPosition: Int = 0,
  jumpSteps: Int = 4,
  markerEmoji: String = "🐰",
  onPositionSelected: (Int) -> Unit = {}
) {
  var currentPos by remember { mutableIntStateOf(initialPosition) }
  val targetPosition = (initialPosition + jumpSteps).coerceAtMost(maxNumber)
  val animatedPosition by animateFloatAsState(
    targetValue = currentPos.toFloat(),
    animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
    label = "markerHop"
  )

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp),
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    tonalElevation = 2.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Tap numbers or hop $markerEmoji along the line!",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.SemiBold
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Canvas for the curved hops and line
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(110.dp)
      ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(100.dp)) {
          val count = maxNumber - startNumber
          val stepWidth = size.width / (count + 1)
          val lineY = size.height * 0.75f

          // Main horizontal baseline
          drawLine(
            color = Color(0xFF3D5AFE),
            start = Offset(stepWidth * 0.5f, lineY),
            end = Offset(size.width - stepWidth * 0.5f, lineY),
            strokeWidth = 6.dp.toPx(),
            cap = androidx.compose.ui.graphics.StrokeCap.Round
          )

          // Tick marks and hop arches
          for (i in startNumber..maxNumber) {
            val x = (i - startNumber + 1) * stepWidth
            // Tick mark
            drawLine(
              color = Color(0xFF1E2238),
              start = Offset(x, lineY - 12.dp.toPx()),
              end = Offset(x, lineY + 12.dp.toPx()),
              strokeWidth = 3.dp.toPx(),
              cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
          }

          // Draw jumping arc if jumping
          if (jumpSteps > 0) {
            val startX = (initialPosition - startNumber + 1) * stepWidth
            val endX = (targetPosition - startNumber + 1) * stepWidth
            val midX = (startX + endX) / 2f
            val arcTopY = lineY - 45.dp.toPx()

            val path = androidx.compose.ui.graphics.Path().apply {
              moveTo(startX, lineY - 10.dp.toPx())
              quadraticTo(midX, arcTopY, endX, lineY - 10.dp.toPx())
            }

            drawPath(
              path = path,
              color = Color(0xFFFF6D00),
              style = Stroke(
                width = 4.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
              )
            )
          }
        }

        // Tappable numbers below the line
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          for (i in startNumber..maxNumber) {
            val isSelected = (currentPos == i)
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (isSelected) MathOrange else Color.Transparent)
                .clickable {
                  currentPos = i
                  onPositionSelected(i)
                }
                .testTag("number_node_$i"),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "$i",
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp
              )
            }
          }
        }

        // Animated Mascot Position
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopStart)
            .padding(top = 4.dp)
        ) {
          Text(
            text = markerEmoji,
            fontSize = 28.sp,
            modifier = Modifier.padding(start = (animatedPosition * 22).dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Status chip
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MathGreen.copy(alpha = 0.15f)
      ) {
        Text(
          text = "Bunny is at number $currentPos",
          color = MathGreen,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
          fontSize = 13.sp
        )
      }
    }
  }
}
