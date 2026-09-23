package com.example.ui.challenge

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyChallenge
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathCoral
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange
import com.example.ui.theme.MathPurple
import com.example.ui.theme.MathYellow
import com.example.util.MathBloomSoundManager
import com.example.util.SoundEffectType
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyChallengeScreen(
  dailyChallenge: DailyChallenge?,
  isGenerating: Boolean,
  onGenerateNew: () -> Unit,
  onCompleteChallenge: (challengeId: String, score: Int, starsEarned: Int) -> Unit,
  onBack: () -> Unit
) {
  var currentProblemIndex by remember { mutableIntStateOf(0) }
  var selectedAnswer by remember { mutableStateOf<String?>(null) }
  var hasSubmittedAnswer by remember { mutableStateOf(false) }
  var isCorrectAnswer by remember { mutableStateOf(false) }
  var showHint by remember { mutableStateOf(false) }
  var correctCount by remember { mutableIntStateOf(0) }
  var challengeFinished by remember { mutableStateOf(false) }

  val context = LocalContext.current
  val soundManager = remember { MathBloomSoundManager.getInstance(context) }

  val problems = dailyChallenge?.problems.orEmpty()
  val currentProblem = problems.getOrNull(currentProblemIndex)

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Daily Math Challenge",
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp
            )
            Text(
              text = "Powered by Gemini AI (gemini-3.5-flash)",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("daily_challenge_back_btn")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back to Home"
            )
          }
        },
        actions = {
          IconButton(
            onClick = onGenerateNew,
            enabled = !isGenerating,
            modifier = Modifier.testTag("daily_challenge_refresh_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "Generate New Challenge with Gemini",
              tint = MathPurple
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            listOf(
              MathPurple.copy(alpha = 0.08f),
              MaterialTheme.colorScheme.background
            )
          )
        )
        .padding(paddingValues)
    ) {
      if (isGenerating) {
        // Loading State: Gemini Generating
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .size(80.dp)
              .clip(CircleShape)
              .background(MathPurple.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            CircularProgressIndicator(
              color = MathPurple,
              modifier = Modifier.size(46.dp),
              strokeWidth = 4.dp
            )
          }
          Spacer(modifier = Modifier.height(20.dp))
          Text(
            text = "Gemini is Crafting Today's Arithmetic Quest!",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Tailoring age-appropriate problems, storylines, and hints...",
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      } else if (dailyChallenge == null || problems.isEmpty()) {
        // Empty state
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text = "🧩", fontSize = 56.sp)
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "No Daily Challenge for Today Yet",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = onGenerateNew,
            colors = ButtonDefaults.buttonColors(containerColor = MathPurple),
            shape = RoundedCornerShape(16.dp)
          ) {
            Text("Generate with Gemini AI")
          }
        }
      } else if (challengeFinished || dailyChallenge.isCompleted) {
        // Finished Screen
        ChallengeCompletionView(
          challenge = dailyChallenge,
          score = if (challengeFinished) correctCount else dailyChallenge.score,
          total = problems.size,
          onRestart = {
            challengeFinished = false
            currentProblemIndex = 0
            selectedAnswer = null
            hasSubmittedAnswer = false
            isCorrectAnswer = false
            correctCount = 0
            showHint = false
          },
          onDone = onBack
        )
      } else if (currentProblem != null) {
        // Active Problem Playing Screen
        val scrollState = rememberScrollState()
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Challenge Theme Banner
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MathPurple),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Text(text = dailyChallenge.themeEmoji, fontSize = 26.sp)
              }
              Spacer(modifier = Modifier.width(14.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = dailyChallenge.title,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  fontSize = 16.sp
                )
                Text(
                  text = "Question ${currentProblemIndex + 1} of ${problems.size} • Level ${dailyChallenge.targetLevel}",
                  color = Color.White.copy(alpha = 0.9f),
                  fontSize = 12.sp
                )
              }
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = MathYellow
              ) {
                Text(
                  text = "+10 ⭐",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = Color(0xFF3E2723),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }

          // Progress indicator
          LinearProgressIndicator(
            progress = { (currentProblemIndex + 1) / problems.size.toFloat() },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = MathPurple,
            trackColor = MathPurple.copy(alpha = 0.15f)
          )

          // Question Card
          Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MathPurple.copy(alpha = 0.08f)),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, MathPurple.copy(alpha = 0.25f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(20.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = currentProblem.visualEmoji,
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 8.dp)
              )

              Text(
                text = currentProblem.question,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }

          // Options Grid (Multiple Choice)
          Text(
            text = "Choose your answer:",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
          )

          val chunkedOptions = currentProblem.options.chunked(2)
          chunkedOptions.forEach { rowOpts ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              rowOpts.forEach { opt ->
                val isSelected = (selectedAnswer == opt)
                val isCorrect = (opt == currentProblem.correctAnswer)

                val backgroundColor = when {
                  hasSubmittedAnswer && isCorrect -> MathGreen.copy(alpha = 0.2f)
                  hasSubmittedAnswer && isSelected && !isCorrect -> MathCoral.copy(alpha = 0.2f)
                  isSelected -> MathPurple.copy(alpha = 0.15f)
                  else -> Color.White
                }

                val borderColor = when {
                  hasSubmittedAnswer && isCorrect -> MathGreen
                  hasSubmittedAnswer && isSelected && !isCorrect -> MathCoral
                  isSelected -> MathPurple
                  else -> Color(0xFFE0E0E0)
                }

                Surface(
                  shape = RoundedCornerShape(16.dp),
                  color = backgroundColor,
                  modifier = Modifier
                    .weight(1f)
                    .height(64.dp)
                    .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                    .clickable(enabled = !hasSubmittedAnswer) {
                      selectedAnswer = opt
                    }
                ) {
                  Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = opt,
                      fontWeight = FontWeight.Bold,
                      fontSize = 22.sp,
                      color = when {
                        hasSubmittedAnswer && isCorrect -> MathGreen
                        hasSubmittedAnswer && isSelected && !isCorrect -> MathCoral
                        isSelected -> MathPurple
                        else -> MaterialTheme.colorScheme.onSurface
                      }
                    )
                  }
                }
              }
            }
          }

          // Hint Section
          if (!hasSubmittedAnswer && currentProblem.hint.isNotBlank()) {
            if (showHint) {
              Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MathYellow.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(text = "💡", fontSize = 20.sp)
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    text = currentProblem.hint,
                    fontSize = 13.sp,
                    color = Color(0xFF5D4037)
                  )
                }
              }
            } else {
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
                modifier = Modifier
                  .align(Alignment.CenterHorizontally)
                  .clickable {
                    soundManager.playSound(SoundEffectType.HINT_CLICK)
                    showHint = true
                  }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(text = "💡", fontSize = 16.sp)
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Need a Hint?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MathOrange
                  )
                }
              }
            }
          }

          // Feedback & Explanation Banner after submission
          AnimatedVisibility(
            visible = hasSubmittedAnswer,
            enter = fadeIn() + slideInVertically()
          ) {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isCorrectAnswer) MathGreen.copy(alpha = 0.15f) else MathOrange.copy(alpha = 0.15f)
              ),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = if (isCorrectAnswer) "🎉 Hooray! Correct!" else "💪 Keep going!", fontSize = 20.sp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = if (isCorrectAnswer) "Awesome math power!" else "Nice effort!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (isCorrectAnswer) MathGreen else MathOrange
                  )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = currentProblem.explanation,
                  fontSize = 13.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Action Button: Check Answer or Next Problem
          if (!hasSubmittedAnswer) {
            Button(
              onClick = {
                val correct = (selectedAnswer == currentProblem.correctAnswer)
                isCorrectAnswer = correct
                if (correct) {
                  correctCount++
                  soundManager.playSound(SoundEffectType.CORRECT_ANSWER)
                }
                hasSubmittedAnswer = true
              },
              enabled = selectedAnswer != null,
              colors = ButtonDefaults.buttonColors(containerColor = MathPurple),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("submit_daily_answer_btn")
            ) {
              Text(
                text = "Check My Answer ✨",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
            }
          } else {
            Button(
              onClick = {
                if (currentProblemIndex + 1 < problems.size) {
                  currentProblemIndex++
                  selectedAnswer = null
                  hasSubmittedAnswer = false
                  isCorrectAnswer = false
                  showHint = false
                } else {
                  challengeFinished = true
                  val starsEarned = (correctCount * 10).coerceAtLeast(10)
                  soundManager.playSound(SoundEffectType.BADGE_UNLOCKED)
                  onCompleteChallenge(dailyChallenge.id, correctCount, starsEarned)
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = MathGreen),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("next_daily_problem_btn")
            ) {
              Text(
                text = if (currentProblemIndex + 1 < problems.size) "Next Problem ➡️" else "Finish Quest 🏆",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(20.dp))
        }
      }
    }
  }
}

@Composable
private fun ChallengeCompletionView(
  challenge: DailyChallenge,
  score: Int,
  total: Int,
  onRestart: () -> Unit,
  onDone: () -> Unit
) {
  val starsWon = (score * 10).coerceAtLeast(10)

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(100.dp)
        .clip(CircleShape)
        .background(MathYellow.copy(alpha = 0.2f)),
      contentAlignment = Alignment.Center
    ) {
      Text(text = "👑", fontSize = 54.sp)
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Daily Challenge Completed!",
      fontWeight = FontWeight.ExtraBold,
      fontSize = 22.sp,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "You scored $score out of $total problems!",
      fontSize = 16.sp,
      fontWeight = FontWeight.SemiBold,
      color = MathPurple
    )

    Spacer(modifier = Modifier.height(16.dp))

    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "🌟 Rewards Added:",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "⭐ +$starsWon", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MathOrange)
            Text(text = "Golden Stars", fontSize = 11.sp, color = Color.Gray)
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "🏆 Unlocked", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MathGreen)
            Text(text = "Firestore Trophy", fontSize = 11.sp, color = Color.Gray)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Button(
      onClick = onDone,
      colors = ButtonDefaults.buttonColors(containerColor = MathGreen),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .testTag("daily_challenge_done_btn")
    ) {
      Text("Back to Adventures 🌟", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }

    Spacer(modifier = Modifier.height(10.dp))

    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .clickable(onClick = onRestart)
    ) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
          text = "Play Again 🔄",
          fontWeight = FontWeight.Bold,
          color = MathPurple,
          fontSize = 14.sp
        )
      }
    }
  }
}
