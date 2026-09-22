package com.example.ui.drawing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange

data class DrawPathItem(
  val path: Path,
  val color: Color,
  val strokeWidth: Float,
  val isEraser: Boolean = false
)

@Composable
fun MathDrawingCanvas(
  modifier: Modifier = Modifier,
  initialGuide: String = "Triangle Guide"
) {
  val paths = remember { mutableStateListOf<DrawPathItem>() }
  var currentColor by remember { mutableStateOf(Color(0xFF3D5AFE)) }
  var isEraser by remember { mutableStateOf(false) }
  var showGrid by remember { mutableStateOf(true) }
  var selectedGuide by remember { mutableStateOf(initialGuide) } // "None", "Triangle", "Circle", "Trace 5", "Clock"

  val colors = listOf(
    Color(0xFF3D5AFE), // Blue
    Color(0xFFFF6D00), // Orange
    Color(0xFF00C853), // Green
    Color(0xFFFF5252), // Coral
    Color(0xFF7C4DFF), // Purple
    Color(0xFFFFB300), // Yellow
    Color(0xFF1E2238)  // Dark
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(16.dp)
  ) {
    // Top Bar: Guides and Controls
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "🎨 Math Drawing Studio",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Draw shapes, trace numbers & solve problems!",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        IconButton(
          onClick = { showGrid = !showGrid },
          modifier = Modifier.background(
            if (showGrid) MathBlue.copy(alpha = 0.2f) else Color.Transparent,
            CircleShape
          )
        ) {
          Icon(Icons.Default.GridOn, contentDescription = "Toggle Grid", tint = MathBlue)
        }
        IconButton(
          onClick = { if (paths.isNotEmpty()) paths.removeAt(paths.lastIndex) },
          enabled = paths.isNotEmpty()
        ) {
          Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = "Undo")
        }
        IconButton(
          onClick = { paths.clear() }
        ) {
          Icon(Icons.Default.Clear, contentDescription = "Clear Canvas", tint = Color.Red)
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Guide selector chips
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      listOf("None", "Triangle", "Circle", "Square", "Trace 7").forEach { guide ->
        val isSelected = (selectedGuide == guide)
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isSelected) MathBlue else MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier
            .clickable { selectedGuide = guide }
        ) {
          Text(
            text = guide,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Drawing Canvas Area
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      var currentPath by remember { mutableStateOf<Path?>(null) }

      Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
          modifier = Modifier
            .fillMaxSize()
            .pointerInput(isEraser, currentColor) {
              detectDragGestures(
                onDragStart = { offset ->
                  val newPath = Path().apply { moveTo(offset.x, offset.y) }
                  currentPath = newPath
                },
                onDrag = { change, _ ->
                  currentPath?.lineTo(change.position.x, change.position.y)
                },
                onDragEnd = {
                  currentPath?.let {
                    paths.add(
                      DrawPathItem(
                        path = it,
                        color = if (isEraser) Color.White else currentColor,
                        strokeWidth = if (isEraser) 32f else 12f,
                        isEraser = isEraser
                      )
                    )
                  }
                  currentPath = null
                }
              )
            }
        ) {
          // Draw Grid lines if enabled
          if (showGrid) {
            val step = 30.dp.toPx()
            val gridColor = Color(0xFFE8EEF5)
            var x = 0f
            while (x < size.width) {
              drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
              x += step
            }
            var y = 0f
            while (y < size.height) {
              drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
              y += step
            }
          }

          // Draw Shape Guide lines if selected
          val guideColor = Color(0xFFB0BEC5)
          val guideStroke = Stroke(width = 3.dp.toPx(), pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(20f, 15f)))
          val center = Offset(size.width / 2f, size.height / 2f)

          when (selectedGuide) {
            "Triangle" -> {
              val triPath = Path().apply {
                moveTo(center.x, center.y - 120.dp.toPx())
                lineTo(center.x - 120.dp.toPx(), center.y + 100.dp.toPx())
                lineTo(center.x + 120.dp.toPx(), center.y + 100.dp.toPx())
                close()
              }
              drawPath(triPath, guideColor, style = guideStroke)
            }
            "Circle" -> {
              drawCircle(guideColor, radius = 100.dp.toPx(), center = center, style = guideStroke)
            }
            "Square" -> {
              val rectPath = Path().apply {
                moveTo(center.x - 100.dp.toPx(), center.y - 100.dp.toPx())
                lineTo(center.x + 100.dp.toPx(), center.y - 100.dp.toPx())
                lineTo(center.x + 100.dp.toPx(), center.y + 100.dp.toPx())
                lineTo(center.x - 100.dp.toPx(), center.y + 100.dp.toPx())
                close()
              }
              drawPath(rectPath, guideColor, style = guideStroke)
            }
            "Trace 7" -> {
              val sevenPath = Path().apply {
                moveTo(center.x - 70.dp.toPx(), center.y - 100.dp.toPx())
                lineTo(center.x + 70.dp.toPx(), center.y - 100.dp.toPx())
                lineTo(center.x - 30.dp.toPx(), center.y + 110.dp.toPx())
              }
              drawPath(sevenPath, guideColor, style = guideStroke)
            }
          }

          // Draw all saved paths
          paths.forEach { item ->
            drawPath(
              path = item.path,
              color = item.color,
              style = Stroke(
                width = item.strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
              )
            )
          }

          // Draw currently dragging path
          currentPath?.let {
            drawPath(
              path = it,
              color = if (isEraser) Color.White else currentColor,
              style = Stroke(
                width = if (isEraser) 32f else 12f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
              )
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Bottom Color Palette and Tool Toggles
    Surface(
      shape = RoundedCornerShape(18.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 3.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Color Swatches
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          colors.forEach { c ->
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(c)
                .border(
                  width = if (currentColor == c && !isEraser) 3.dp else 1.dp,
                  color = if (currentColor == c && !isEraser) Color.Black else Color(0x33000000),
                  shape = CircleShape
                )
                .clickable {
                  currentColor = c
                  isEraser = false
                }
            )
          }
        }

        // Tool toggles: Pen vs Eraser
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (!isEraser) MathBlue else Color(0xFFECEFF1),
            modifier = Modifier.clickable { isEraser = false }
          ) {
            Text(
              text = "✏️ Pen",
              color = if (!isEraser) Color.White else Color(0xFF455A64),
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
          }

          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isEraser) MathOrange else Color(0xFFECEFF1),
            modifier = Modifier.clickable { isEraser = true }
          ) {
            Text(
              text = "🧹 Eraser",
              color = if (isEraser) Color.White else Color(0xFF455A64),
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
          }
        }
      }
    }
  }
}
