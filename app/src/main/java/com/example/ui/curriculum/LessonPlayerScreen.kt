package com.example.ui.curriculum

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CollectibleItem
import com.example.data.model.Lesson
import com.example.data.model.VisualMathType
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange
import com.example.ui.theme.MathYellow
import com.example.ui.visualmath.BalanceScaleView
import com.example.ui.visualmath.FractionPizzaView
import com.example.ui.visualmath.HintBottomSheet
import com.example.ui.visualmath.InteractiveClockView
import com.example.ui.visualmath.InteractiveNumberLine
import com.example.ui.visualmath.MoneyShopRegister
import com.example.ui.visualmath.MultiplicationArrayView
import com.example.ui.visualmath.StepByStepGuidedDialog
import com.example.ui.visualmath.VisualItemCounter
import com.example.util.MathBloomSoundManager
import com.example.util.SoundEffectType

@Composable
fun LessonPlayerScreen(
  lesson: Lesson,
  onComplete: (starsEarned: Int, hintsUsed: Int) -> Unit,
  onBack: () -> Unit
) {
  var selectedAnswer by remember { mutableStateOf<String?>(null) }
  var isCorrect by remember { mutableStateOf(false) }
  var showHintSheet by remember { mutableStateOf(false) }
  var showSolutionDialog by remember { mutableStateOf(false) }
  var hintsUsedCount by remember { mutableIntStateOf(0) }
  var showCelebration by remember { mutableStateOf(false) }

  val context = LocalContext.current
  val soundManager = remember { MathBloomSoundManager.getInstance(context) }

  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(16.dp)
      .verticalScroll(scrollState),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top Bar: Back & Action buttons
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(
        onClick = onBack,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECEFF1)),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("⬅️ Back", color = Color(0xFF455A64), fontWeight = FontWeight.Bold)
      }

      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
          onClick = {
            hintsUsedCount++
            soundManager.playSound(SoundEffectType.HINT_CLICK)
            showHintSheet = true
          },
          colors = ButtonDefaults.buttonColors(containerColor = MathOrange.copy(alpha = 0.15f)),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("💡 Hint", color = MathOrange, fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = { showSolutionDialog = true },
          colors = ButtonDefaults.buttonColors(containerColor = MathBlue.copy(alpha = 0.15f)),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("🔍 Solve", color = MathBlue, fontWeight = FontWeight.Bold)
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Lesson Question Card
    Card(
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = MathBlue.copy(alpha = 0.08f)),
      border = androidx.compose.foundation.BorderStroke(1.5.dp, MathBlue.copy(alpha = 0.25f)),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = lesson.title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MathBlue
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = lesson.prompt,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Interactive Visual Math Component based on VisualMathType
    when (lesson.visualType) {
      VisualMathType.COUNTING_ITEMS -> {
        VisualItemCounter(
          count1 = lesson.visualParam1,
          itemEmoji = lesson.itemEmoji
        )
      }
      VisualMathType.ADDITION_ITEMS -> {
        VisualItemCounter(
          count1 = lesson.visualParam1,
          count2 = lesson.visualParam2,
          isSubtraction = false,
          itemEmoji = lesson.itemEmoji
        )
      }
      VisualMathType.SUBTRACTION_ITEMS -> {
        VisualItemCounter(
          count1 = lesson.visualParam1,
          count2 = lesson.visualParam2,
          isSubtraction = true,
          itemEmoji = lesson.itemEmoji
        )
      }
      VisualMathType.NUMBER_LINE -> {
        InteractiveNumberLine(
          startNumber = 0,
          maxNumber = 12,
          initialPosition = lesson.visualParam1,
          jumpSteps = lesson.visualParam2,
          markerEmoji = lesson.itemEmoji
        )
      }
      VisualMathType.FRACTION_PIE -> {
        FractionPizzaView(
          totalSlices = if (lesson.visualParam2 > 0) lesson.visualParam2 else 4,
          initialActiveSlices = if (lesson.visualParam1 > 0) lesson.visualParam1 else 1
        )
      }
      VisualMathType.MULTIPLICATION_ARRAY -> {
        MultiplicationArrayView(
          rows = lesson.visualParam1,
          columns = lesson.visualParam2,
          itemEmoji = lesson.itemEmoji
        )
      }
      VisualMathType.BALANCE_SCALE -> {
        BalanceScaleView(
          leftWeight = lesson.visualParam1,
          rightWeight = lesson.visualParam2,
          itemEmoji = lesson.itemEmoji
        )
      }
      VisualMathType.CLOCK_TIME -> {
        InteractiveClockView(
          initialHour = lesson.visualParam1,
          initialMinute = lesson.visualParam2,
          isInteractive = true
        )
      }
      VisualMathType.MONEY_SHOP -> {
        MoneyShopRegister(
          targetPrice = lesson.visualParam1
        )
      }
      VisualMathType.SHAPE_IDENTIFY -> {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          modifier = Modifier.padding(8.dp)
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(text = lesson.itemEmoji, fontSize = 72.sp)
            Text(
              text = "Look at the sides and corners of this shape!",
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Multiple Choice Answer Buttons
    Text(
      text = "Select your answer:",
      style = MaterialTheme.typography.labelLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(10.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      lesson.options.forEach { option ->
        val isSelected = (selectedAnswer == option)
        val buttonColor = when {
          isSelected && isCorrect -> MathGreen
          isSelected && !isCorrect -> Color(0xFFFF5252)
          else -> Color.White
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(buttonColor)
            .clickable {
              selectedAnswer = option
              if (option == lesson.correctAnswer) {
                isCorrect = true
                showCelebration = true
                soundManager.playSound(SoundEffectType.CORRECT_ANSWER)
              } else {
                isCorrect = false
              }
            }
            .testTag("answer_option_$option"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = option,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Feedback Message Banner
    AnimatedVisibility(visible = selectedAnswer != null) {
      if (isCorrect) {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MathGreen.copy(alpha = 0.15f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "🌟", fontSize = 32.sp)
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Spot On! Fantastic Work!",
                fontWeight = FontWeight.Bold,
                color = MathGreen,
                fontSize = 16.sp
              )
              Text(
                text = "You earned 3 shiny stars! Keep on discovering!",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      } else {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFFECEC)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "🌱", fontSize = 28.sp)
            Spacer(modifier = Modifier.size(10.dp))
            Column {
              Text(
                text = "Almost there! Let's try again.",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD32F2F)
              )
              Text(
                text = "Tap the 'Hint' button above if you want a little boost!",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (isCorrect) {
      Button(
        onClick = {
          onComplete(3, hintsUsedCount)
        },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MathGreen),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("continue_after_lesson_button")
      ) {
        Text("Claim Stars & Continue! 🚀", fontWeight = FontWeight.Bold, fontSize = 16.sp)
      }
    }
  }

  // Hint Bottom Sheet
  if (showHintSheet) {
    HintBottomSheet(
      hint1 = lesson.hint1,
      hint2 = lesson.hint2,
      hint3 = lesson.hint3,
      onDismiss = { showHintSheet = false },
      onViewStepByStep = { showSolutionDialog = true }
    )
  }

  // Step By Step Guided Dialog
  if (showSolutionDialog) {
    StepByStepGuidedDialog(
      title = lesson.title,
      steps = lesson.explanationSteps,
      correctAnswer = lesson.correctAnswer,
      onDismiss = { showSolutionDialog = false }
    )
  }
}
