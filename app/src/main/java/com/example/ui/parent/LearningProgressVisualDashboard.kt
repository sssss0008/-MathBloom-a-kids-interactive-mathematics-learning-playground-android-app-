package com.example.ui.parent

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathCoral
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange
import com.example.ui.theme.MathPurple
import com.example.ui.theme.MathTeal
import com.example.ui.theme.MathYellow

// Data models for analytics
data class WeeklyProgressPoint(
  val weekLabel: String,
  val problemsSolved: Int,
  val accuracyPercent: Int,
  val timeSpentMinutes: Int
)

val sampleWeeklyLearningData = listOf(
  WeeklyProgressPoint("W1", 18, 72, 65),
  WeeklyProgressPoint("W2", 26, 78, 85),
  WeeklyProgressPoint("W3", 34, 84, 110),
  WeeklyProgressPoint("W4", 42, 88, 125),
  WeeklyProgressPoint("W5", 38, 82, 95),
  WeeklyProgressPoint("W6", 52, 94, 150),
  WeeklyProgressPoint("W7", 60, 96, 175)
)

/**
 * Visual Learning Progress Dashboard
 * Inspired by D3/Recharts data-visualization primitives:
 * - Interactive multi-metric toggles (Problems Solved vs Accuracy Trend % vs Time Spent)
 * - Rich non-white deep theme palette (Midnight Slate / Indigo Navy) avoiding pure white backgrounds
 * - Smooth cubic bezier smoothed line paths with gradient fill under curve (Area chart style)
 * - Rounded bar chart with interactive point selection
 * - Data point inspection tooltips
 */
@Composable
fun LearningProgressVisualDashboard(
  dataPoints: List<WeeklyProgressPoint> = sampleWeeklyLearningData,
  modifier: Modifier = Modifier
) {
  var selectedMetric by remember { mutableStateOf("Accuracy Trends") } // "Accuracy Trends", "Problems Solved", "Time Spent"
  var selectedPointIndex by remember { mutableIntStateOf(dataPoints.lastIndex) }

  // Deep Midnight Navy Container Colors (No pure white backgrounds)
  val dashboardBg = Color(0xFF0F172A)      // Deep Slate 900
  val cardBg = Color(0xFF1E293B)           // Slate 800
  val innerPanelBg = Color(0xFF111C35)     // Indigo Deep 950
  val gridLineColor = Color(0xFF334155)    // Slate 700
  val textPrimary = Color(0xFFF1F5F9)      // Slate 100
  val textMuted = Color(0xFF94A3B8)        // Slate 400

  val activePoint = dataPoints.getOrElse(selectedPointIndex) { dataPoints.last() }

  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = cardBg),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag("learning_progress_visual_dashboard")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color(0xFF1E293B), // Slate 800
              Color(0xFF0F172A)  // Slate 900
            )
          )
        )
        .padding(20.dp)
    ) {
      // Header & Title
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MathTeal.copy(alpha = 0.2f),
              modifier = Modifier.padding(end = 8.dp)
            ) {
              Text(
                text = "D3 / RECHARTS ENGINE",
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MathTeal,
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
              )
            }
            Text(
              text = "Live Analytics",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = MathGreen
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Learning Progress Over Time",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
          )
        }

        // Active highlighted stat badge
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = Color(0xFF2E3B52),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF475569))
        ) {
          Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.End
          ) {
            Text(
              text = activePoint.weekLabel,
              fontSize = 11.sp,
              color = textMuted,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = when (selectedMetric) {
                "Accuracy Trends" -> "${activePoint.accuracyPercent}% Acc"
                "Problems Solved" -> "${activePoint.problemsSolved} Solved"
                else -> "${activePoint.timeSpentMinutes} mins"
              },
              fontSize = 14.sp,
              fontWeight = FontWeight.ExtraBold,
              color = when (selectedMetric) {
                "Accuracy Trends" -> MathGreen
                "Problems Solved" -> MathTeal
                else -> MathOrange
              }
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Metric Selector Pills
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf("Accuracy Trends", "Problems Solved", "Time Spent").forEach { metric ->
          val isSelected = (selectedMetric == metric)
          val metricColor = when (metric) {
            "Accuracy Trends" -> MathGreen
            "Problems Solved" -> MathTeal
            else -> MathOrange
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) metricColor.copy(alpha = 0.25f) else Color(0xFF1E293B),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isSelected) metricColor else Color(0xFF334155)
            ),
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .clickable { selectedMetric = metric }
          ) {
            Box(
              modifier = Modifier.padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = metric,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) textPrimary else textMuted
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Chart Container Canvas (Non-white, Midnight Navy background)
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = innerPanelBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF26334D)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          if (selectedMetric == "Accuracy Trends") {
            // Cubic Spline Area Chart (like Recharts <AreaChart>)
            D3AreaLineChart(
              data = dataPoints,
              selectedIndex = selectedPointIndex,
              onSelectIndex = { selectedPointIndex = it },
              gridColor = gridLineColor,
              chartColor = MathGreen,
              textColor = textMuted
            )
          } else if (selectedMetric == "Problems Solved") {
            // Gradient Rounded Bar Chart (like Recharts <BarChart>)
            D3BarChart(
              data = dataPoints,
              selectedIndex = selectedPointIndex,
              onSelectIndex = { selectedPointIndex = it },
              gridColor = gridLineColor,
              barColor = MathTeal,
              textColor = textMuted
            )
          } else {
            // Time Spent Step/Line Chart with Area Fill
            D3TimeSpentChart(
              data = dataPoints,
              selectedIndex = selectedPointIndex,
              onSelectIndex = { selectedPointIndex = it },
              gridColor = gridLineColor,
              chartColor = MathOrange,
              textColor = textMuted
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Chart summary legend
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Tap any point or bar to inspect weekly data",
              fontSize = 11.sp,
              color = textMuted
            )
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(
                    when (selectedMetric) {
                      "Accuracy Trends" -> MathGreen
                      "Problems Solved" -> MathTeal
                      else -> MathOrange
                    }
                  )
              )
              Text(
                text = "7-Week Trend",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = textPrimary
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 3 Summary Metric Cards (Problems, Accuracy, Time)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        SummaryKpiCard(
          title = "Total Solved",
          value = "${dataPoints.sumOf { it.problemsSolved }}",
          subtitle = "problems",
          accentColor = MathTeal,
          modifier = Modifier.weight(1f)
        )
        SummaryKpiCard(
          title = "Average Acc.",
          value = "${dataPoints.map { it.accuracyPercent }.average().toInt()}%",
          subtitle = "accuracy",
          accentColor = MathGreen,
          modifier = Modifier.weight(1f)
        )
        SummaryKpiCard(
          title = "Total Hours",
          value = "${String.format("%.1f", dataPoints.sumOf { it.timeSpentMinutes } / 60f)}h",
          subtitle = "practice",
          accentColor = MathOrange,
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

/**
 * Recharts / D3 inspired Smooth Spline Area Chart
 */
@Composable
private fun D3AreaLineChart(
  data: List<WeeklyProgressPoint>,
  selectedIndex: Int,
  onSelectIndex: (Int) -> Unit,
  gridColor: Color,
  chartColor: Color,
  textColor: Color
) {
  val animProgress = remember { Animatable(0f) }
  LaunchedEffect(data) {
    animProgress.snapTo(0f)
    animProgress.animateTo(1f, animationSpec = tween(700))
  }

  val maxVal = 100f
  val minVal = 50f

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(170.dp)
      .pointerInput(data) {
        detectTapGestures { offset ->
          val stepX = size.width / (data.size - 1).coerceAtLeast(1)
          val touchedIndex = ((offset.x + stepX / 2) / stepX).toInt().coerceIn(0, data.lastIndex)
          onSelectIndex(touchedIndex)
        }
      }
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height
      val padTop = 16.dp.toPx()
      val padBottom = 26.dp.toPx()
      val chartHeight = h - padTop - padBottom

      // Draw Grid Horizontal Lines (D3 Grid Lines)
      val gridLevels = listOf(100f, 80f, 60f)
      gridLevels.forEach { lvl ->
        val y = padTop + chartHeight * (1f - (lvl - minVal) / (maxVal - minVal))
        drawLine(
          color = gridColor.copy(alpha = 0.6f),
          start = Offset(0f, y),
          end = Offset(w, y),
          strokeWidth = 1.dp.toPx(),
          pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
      }

      val stepX = w / (data.size - 1).coerceAtLeast(1)
      val points = data.mapIndexed { index, pt ->
        val norm = ((pt.accuracyPercent - minVal) / (maxVal - minVal)).coerceIn(0f, 1f)
        val x = index * stepX
        val y = padTop + chartHeight * (1f - norm * animProgress.value)
        Offset(x, y)
      }

      // Build smooth cubic Bezier curve (D3 curveBasis / curveMonotoneX style)
      if (points.isNotEmpty()) {
        val linePath = Path()
        val fillPath = Path()

        linePath.moveTo(points.first().x, points.first().y)
        fillPath.moveTo(points.first().x, h - padBottom)
        fillPath.lineTo(points.first().x, points.first().y)

        for (i in 0 until points.size - 1) {
          val p0 = points[i]
          val p1 = points[i + 1]
          val midX = (p0.x + p1.x) / 2
          linePath.cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
          fillPath.cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
        }

        fillPath.lineTo(points.last().x, h - padBottom)
        fillPath.close()

        // Draw area gradient fill
        drawPath(
          path = fillPath,
          brush = Brush.verticalGradient(
            colors = listOf(
              chartColor.copy(alpha = 0.35f),
              chartColor.copy(alpha = 0.0f)
            ),
            startY = padTop,
            endY = h - padBottom
          )
        )

        // Draw outer curve
        drawPath(
          path = linePath,
          color = chartColor,
          style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw Dots and Selected indicator
        points.forEachIndexed { i, pt ->
          val isSelected = (i == selectedIndex)
          if (isSelected) {
            // Glow circle
            drawCircle(
              color = chartColor.copy(alpha = 0.3f),
              radius = 11.dp.toPx(),
              center = pt
            )
            drawCircle(
              color = Color.White,
              radius = 5.dp.toPx(),
              center = pt
            )
            drawCircle(
              color = chartColor,
              radius = 5.dp.toPx(),
              center = pt,
              style = Stroke(width = 2.dp.toPx())
            )
          } else {
            drawCircle(
              color = chartColor,
              radius = 3.5.dp.toPx(),
              center = pt
            )
          }
        }
      }
    }

    // X-Axis Week Labels
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter)
        .padding(horizontal = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      data.forEachIndexed { index, pt ->
        val isSelected = (index == selectedIndex)
        Text(
          text = pt.weekLabel,
          fontSize = 10.sp,
          fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
          color = if (isSelected) chartColor else textColor
        )
      }
    }
  }
}

/**
 * Recharts / D3 inspired Rounded Bar Chart for Problems Solved
 */
@Composable
private fun D3BarChart(
  data: List<WeeklyProgressPoint>,
  selectedIndex: Int,
  onSelectIndex: (Int) -> Unit,
  gridColor: Color,
  barColor: Color,
  textColor: Color
) {
  val animProgress = remember { Animatable(0f) }
  LaunchedEffect(data) {
    animProgress.snapTo(0f)
    animProgress.animateTo(1f, animationSpec = tween(650))
  }

  val maxVal = (data.maxOfOrNull { it.problemsSolved } ?: 60).toFloat().coerceAtLeast(60f)

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(170.dp)
      .pointerInput(data) {
        detectTapGestures { offset ->
          val colWidth = size.width / data.size
          val index = (offset.x / colWidth).toInt().coerceIn(0, data.lastIndex)
          onSelectIndex(index)
        }
      }
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height
      val padTop = 18.dp.toPx()
      val padBottom = 26.dp.toPx()
      val chartHeight = h - padTop - padBottom

      // Background horizontal reference lines
      listOf(0.75f, 0.5f, 0.25f).forEach { fraction ->
        val y = padTop + chartHeight * (1f - fraction)
        drawLine(
          color = gridColor.copy(alpha = 0.5f),
          start = Offset(0f, y),
          end = Offset(w, y),
          strokeWidth = 1.dp.toPx(),
          pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
        )
      }

      val colWidth = w / data.size
      val barWidth = colWidth * 0.55f

      data.forEachIndexed { index, pt ->
        val isSelected = (index == selectedIndex)
        val barFraction = ((pt.problemsSolved / maxVal) * animProgress.value).coerceIn(0.05f, 1f)
        val barH = chartHeight * barFraction
        val x = index * colWidth + (colWidth - barWidth) / 2
        val y = padTop + chartHeight - barH

        // Draw Bar with Top Rounded Corners
        val barBrush = if (isSelected) {
          Brush.verticalGradient(
            colors = listOf(
              barColor,
              barColor.copy(alpha = 0.6f)
            )
          )
        } else {
          Brush.verticalGradient(
            colors = listOf(
              barColor.copy(alpha = 0.5f),
              barColor.copy(alpha = 0.2f)
            )
          )
        }

        drawRoundRect(
          brush = barBrush,
          topLeft = Offset(x, y),
          size = Size(barWidth, barH),
          cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
        )

        if (isSelected) {
          drawRoundRect(
            color = Color.White.copy(alpha = 0.8f),
            topLeft = Offset(x, y),
            size = Size(barWidth, barH),
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
            style = Stroke(width = 1.5.dp.toPx())
          )
        }
      }
    }

    // X-Axis Week Labels
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter)
        .padding(horizontal = 4.dp),
      horizontalArrangement = Arrangement.SpaceAround
    ) {
      data.forEachIndexed { index, pt ->
        val isSelected = (index == selectedIndex)
        Text(
          text = pt.weekLabel,
          fontSize = 10.sp,
          fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
          color = if (isSelected) barColor else textColor
        )
      }
    }
  }
}

/**
 * Recharts / D3 inspired Time Spent Stepped Line Chart
 */
@Composable
private fun D3TimeSpentChart(
  data: List<WeeklyProgressPoint>,
  selectedIndex: Int,
  onSelectIndex: (Int) -> Unit,
  gridColor: Color,
  chartColor: Color,
  textColor: Color
) {
  val animProgress = remember { Animatable(0f) }
  LaunchedEffect(data) {
    animProgress.snapTo(0f)
    animProgress.animateTo(1f, animationSpec = tween(650))
  }

  val maxVal = (data.maxOfOrNull { it.timeSpentMinutes } ?: 180).toFloat().coerceAtLeast(180f)

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(170.dp)
      .pointerInput(data) {
        detectTapGestures { offset ->
          val stepX = size.width / (data.size - 1).coerceAtLeast(1)
          val index = ((offset.x + stepX / 2) / stepX).toInt().coerceIn(0, data.lastIndex)
          onSelectIndex(index)
        }
      }
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height
      val padTop = 18.dp.toPx()
      val padBottom = 26.dp.toPx()
      val chartHeight = h - padTop - padBottom

      // Background horizontal dashed lines
      listOf(0.75f, 0.5f, 0.25f).forEach { fraction ->
        val y = padTop + chartHeight * (1f - fraction)
        drawLine(
          color = gridColor.copy(alpha = 0.5f),
          start = Offset(0f, y),
          end = Offset(w, y),
          strokeWidth = 1.dp.toPx(),
          pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
        )
      }

      val stepX = w / (data.size - 1).coerceAtLeast(1)
      val points = data.mapIndexed { index, pt ->
        val norm = (pt.timeSpentMinutes / maxVal).coerceIn(0f, 1f)
        val x = index * stepX
        val y = padTop + chartHeight * (1f - norm * animProgress.value)
        Offset(x, y)
      }

      if (points.isNotEmpty()) {
        val path = Path()
        val fillPath = Path()
        path.moveTo(points.first().x, points.first().y)
        fillPath.moveTo(points.first().x, h - padBottom)
        fillPath.lineTo(points.first().x, points.first().y)

        for (i in 1 until points.size) {
          path.lineTo(points[i].x, points[i].y)
          fillPath.lineTo(points[i].x, points[i].y)
        }
        fillPath.lineTo(points.last().x, h - padBottom)
        fillPath.close()

        drawPath(
          path = fillPath,
          brush = Brush.verticalGradient(
            colors = listOf(
              chartColor.copy(alpha = 0.35f),
              chartColor.copy(alpha = 0.0f)
            ),
            startY = padTop,
            endY = h - padBottom
          )
        )

        drawPath(
          path = path,
          color = chartColor,
          style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        points.forEachIndexed { i, pt ->
          val isSelected = (i == selectedIndex)
          if (isSelected) {
            drawCircle(color = chartColor.copy(alpha = 0.3f), radius = 10.dp.toPx(), center = pt)
            drawCircle(color = Color.White, radius = 5.dp.toPx(), center = pt)
            drawCircle(color = chartColor, radius = 5.dp.toPx(), center = pt, style = Stroke(width = 2.dp.toPx()))
          } else {
            drawCircle(color = chartColor, radius = 3.5.dp.toPx(), center = pt)
          }
        }
      }
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter)
        .padding(horizontal = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      data.forEachIndexed { index, pt ->
        val isSelected = (index == selectedIndex)
        Text(
          text = pt.weekLabel,
          fontSize = 10.sp,
          fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
          color = if (isSelected) chartColor else textColor
        )
      }
    }
  }
}

@Composable
private fun SummaryKpiCard(
  title: String,
  value: String,
  subtitle: String,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = Color(0xFF162032), // Deep Navy
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF26334D)),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF94A3B8)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        fontSize = 18.sp,
        fontWeight = FontWeight.ExtraBold,
        color = accentColor
      )
      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = Color(0xFF64748B)
      )
    }
  }
}
