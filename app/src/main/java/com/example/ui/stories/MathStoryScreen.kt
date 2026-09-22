package com.example.ui.stories

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange

data class StoryPage(
  val illustration: String,
  val narrative: String,
  val mathQuestion: String,
  val options: List<String>,
  val correctAnswer: String,
  val reactionText: String
)

data class MathStory(
  val id: String,
  val title: String,
  val character: String,
  val coverEmoji: String,
  val pages: List<StoryPage>
)

@Composable
fun MathStoryScreen(
  onStoryFinished: (stars: Int) -> Unit = {}
) {
  val stories = listOf(
    MathStory(
      id = "bear_apples",
      title = "Barnaby Bear's Apple Harvest",
      character = "Barnaby Bear 🐻",
      coverEmoji = "🍎",
      pages = listOf(
        StoryPage(
          illustration = "🐻 🧺 🌳",
          narrative = "Barnaby Bear walks into the sunny orchard with an empty basket. He spots 4 golden apples on the low branch and gently picks them!",
          mathQuestion = "How many apples does Barnaby have in his basket now?",
          options = listOf("2", "4", "6"),
          correctAnswer = "4",
          reactionText = "Yum! Barnaby places 4 apples safely in his basket."
        ),
        StoryPage(
          illustration = "🐻 🍎 🐿️",
          narrative = "Along comes Sammy the Squirrel! Sammy shares 3 crunchy red apples with Barnaby. Barnaby smiles cheerfully.",
          mathQuestion = "Barnaby had 4 apples and got 3 more. How many apples now?",
          options = listOf("5", "7", "8"),
          correctAnswer = "7",
          reactionText = "4 + 3 = 7 apples! What a bountiful basket!"
        ),
        StoryPage(
          illustration = "🐻 🥧 ✨",
          narrative = "Barnaby gets home and bakes delicious apple tarts. He divides his pie into 4 equal slices to share with Sammy and Penny Penguin.",
          mathQuestion = "If Barnaby eats 1 slice out of 4, what fraction did he eat?",
          options = listOf("1/2", "1/4", "3/4"),
          correctAnswer = "1/4",
          reactionText = "1 slice out of 4 is 1/4! Sweet and cozy harvest!"
        )
      )
    )
  )

  var activeStory by remember { mutableStateOf<MathStory?>(null) }
  var currentPageIndex by remember { mutableIntStateOf(0) }
  var selectedChoice by remember { mutableStateOf<String?>(null) }
  var isCorrectAnswer by remember { mutableStateOf(false) }

  if (activeStory == null) {
    // Stories List
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        Column(modifier = Modifier.padding(bottom = 8.dp)) {
          Text(
            text = "📚 Math Adventures & Stories",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Join friendly characters on story journeys where math solves everyday quests!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      items(stories.size) { idx ->
        val story = stories[idx]
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              activeStory = story
              currentPageIndex = 0
              selectedChoice = null
            }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MathOrange.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Text(text = story.coverEmoji, fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.size(16.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = story.character,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MathOrange
              )
              Text(
                text = story.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "${story.pages.size} Interactive Story Chapters",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Button(
              onClick = {
                activeStory = story
                currentPageIndex = 0
                selectedChoice = null
              },
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MathOrange)
            ) {
              Text(text = "Read 📖", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  } else {
    // Active Story Reader
    val story = activeStory!!
    val page = story.pages[currentPageIndex]

    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFFFBF7))
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Button(
          onClick = { activeStory = null },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECEFF1)),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("⬅️ Stories", color = Color(0xFF455A64), fontWeight = FontWeight.Bold)
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MathOrange.copy(alpha = 0.15f)
        ) {
          Text(
            text = "Page ${currentPageIndex + 1} of ${story.pages.size}",
            color = MathOrange,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Story Illustration Stage
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceAround
        ) {
          Text(
            text = page.illustration,
            fontSize = 48.sp
          )

          Text(
            text = page.narrative,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Normal,
            lineHeight = 24.sp
          )

          Surface(
            shape = RoundedCornerShape(16.dp),
            color = MathBlue.copy(alpha = 0.1f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "🤔 " + page.mathQuestion,
                fontWeight = FontWeight.Bold,
                color = MathBlue,
                fontSize = 16.sp
              )
            }
          }

          // Options
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            page.options.forEach { opt ->
              val isSelected = (selectedChoice == opt)
              val isCorrect = (opt == page.correctAnswer)

              val buttonBg = when {
                isSelected && isCorrect -> MathGreen
                isSelected && !isCorrect -> Color(0xFFFF5252)
                else -> Color(0xFFF0F4FF)
              }

              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(48.dp)
                  .clip(RoundedCornerShape(14.dp))
                  .background(buttonBg)
                  .clickable {
                    selectedChoice = opt
                    isCorrectAnswer = isCorrect
                  },
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = opt,
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }

          if (selectedChoice != null) {
            Text(
              text = if (isCorrectAnswer) "🎉 " + page.reactionText else "Let's check and try again!",
              color = if (isCorrectAnswer) MathGreen else Color(0xFFFF5252),
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Next Page button
      if (selectedChoice == page.correctAnswer) {
        Button(
          onClick = {
            if (currentPageIndex < story.pages.size - 1) {
              currentPageIndex++
              selectedChoice = null
              isCorrectAnswer = false
            } else {
              onStoryFinished(3)
              activeStory = null
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = MathGreen),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
        ) {
          Text(
            text = if (currentPageIndex < story.pages.size - 1) "Continue Story ➡️" else "Finish Story Adventure! 🌟",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
        }
      }
    }
  }
}
