package com.example.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
  onCompleteOnboarding: (name: String, avatar: String, level: Int, interest: String) -> Unit
) {
  var step by remember { mutableIntStateOf(1) } // 1: Welcome, 2: Avatar & Name, 3: Level, 4: Interest

  var childName by remember { mutableStateOf("Leo") }
  var selectedAvatar by remember { mutableStateOf("🦁") }
  var selectedLevel by remember { mutableIntStateOf(1) }
  var selectedInterest by remember { mutableStateOf("Animals") }

  val avatars = listOf("🦁", "🐼", "🚀", "🦊", "🦉", "🦖", "🦄")
  val interests = listOf("Animals", "Dinosaurs", "Space", "Superheroes", "Art", "Sports")

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // Top step dots
    Row(
      modifier = Modifier.padding(top = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      for (i in 1..4) {
        Box(
          modifier = Modifier
            .size(if (step == i) 24.dp else 10.dp, 10.dp)
            .clip(CircleShape)
            .background(if (step == i) MathBlue else Color(0xFFCFD8DC))
        )
      }
    }

    AnimatedContent(targetState = step, label = "onboardingStep") { currentStep ->
      when (currentStep) {
        1 -> {
          // Welcome Card
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(text = "🌸", fontSize = 72.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "MathBloom",
              style = MaterialTheme.typography.headlineLarge,
              fontWeight = FontWeight.ExtraBold,
              color = MathBlue
            )
            Text(
              text = "“Learn Math. Play. Discover.”",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.SemiBold,
              color = MathOrange,
              modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )
            Text(
              text = "Welcome to a safe, joyful math adventure world built specially for curious young minds!",
              style = MaterialTheme.typography.bodyLarge,
              textAlign = TextAlign.Center,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
        2 -> {
          // Mascot & Name
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "Choose Your Explorer!",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
            Text(
              text = "Pick a friendly mascot to guide your adventures",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            // Mascot Avatar Row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              avatars.take(4).forEach { av ->
                val isSel = (selectedAvatar == av)
                Box(
                  modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(if (isSel) MathBlue else Color(0xFFECEFF1))
                    .clickable { selectedAvatar = av },
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = av, fontSize = 32.sp)
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              avatars.drop(4).forEach { av ->
                val isSel = (selectedAvatar == av)
                Box(
                  modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(if (isSel) MathBlue else Color(0xFFECEFF1))
                    .clickable { selectedAvatar = av },
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = av, fontSize = 32.sp)
                }
              }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
              value = childName,
              onValueChange = { childName = it },
              label = { Text("Child's First Name or Nickname") },
              singleLine = true,
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
        3 -> {
          // Learning Level
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "What would you like to start with?",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground,
              textAlign = TextAlign.Center
            )
            Text(
              text = "Parents can change this anytime in settings",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            val levels = listOf(
              1 to "Early Number Counting (Ages 3-5)",
              2 to "Basic Addition & Sums (Ages 5-6)",
              3 to "Take-Away Subtraction (Ages 6-7)",
              4 to "Multiplication Arrays (Ages 7-8)"
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              levels.forEach { (lvl, title) ->
                val isSel = (selectedLevel == lvl)
                Card(
                  shape = RoundedCornerShape(16.dp),
                  colors = CardDefaults.cardColors(
                    containerColor = if (isSel) MathBlue else Color.White
                  ),
                  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedLevel = lvl }
                ) {
                  Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(16.dp)
                  )
                }
              }
            }
          }
        }
        4 -> {
          // Interests
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "What is your favorite topic?",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
            Text(
              text = "We will personalize story problems with your favorites!",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
              textAlign = TextAlign.Center
            )

            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              interests.forEach { interest ->
                val isSel = (selectedInterest == interest)
                Surface(
                  shape = RoundedCornerShape(14.dp),
                  color = if (isSel) MathOrange else Color.White,
                  shadowElevation = 2.dp,
                  modifier = Modifier.clickable { selectedInterest = interest }
                ) {
                  Text(
                    text = interest,
                    fontWeight = FontWeight.Bold,
                    color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                  )
                }
              }
            }
          }
        }
      }
    }

    // Bottom Navigation Buttons
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      if (step > 1) {
        Button(
          onClick = { step-- },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECEFF1)),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .weight(1f)
            .height(52.dp)
        ) {
          Text("Back", color = Color(0xFF455A64), fontWeight = FontWeight.Bold)
        }
      }

      Button(
        onClick = {
          if (step < 4) {
            step++
          } else {
            onCompleteOnboarding(
              childName.ifBlank { "Explorer" },
              selectedAvatar,
              selectedLevel,
              selectedInterest
            )
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = MathGreen),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
          .weight(if (step > 1) 2f else 1f)
          .height(52.dp)
          .testTag("onboarding_next_button")
      ) {
        Text(
          text = if (step < 4) "Next ➡️" else "Start Math Adventure! 🚀",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
      }
    }
  }
}
