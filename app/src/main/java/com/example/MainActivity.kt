package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.audio.AudioRecorderHelper
import com.example.data.firebase.FirebaseAuthManager
import com.example.data.firebase.FirebaseSyncManager
import com.example.data.gemini.GeminiApiClient
import com.example.data.local.MathBloomDatabase
import com.example.data.repository.MathBloomRepository
import com.example.ui.components.BottomTab
import com.example.ui.components.KidBottomNav
import com.example.ui.components.KidTopBar
import com.example.ui.components.ParentGateDialog
import com.example.ui.curriculum.LessonPlayerScreen
import com.example.ui.curriculum.WorldMapScreen
import com.example.ui.drawing.MathDrawingCanvas
import com.example.ui.games.GameCenterScreen
import com.example.ui.games.NumberCatcherGame
import com.example.ui.home.HomeScreen
import com.example.ui.onboarding.OnboardingScreen
import com.example.ui.parent.ParentDashboardScreen
import com.example.ui.search.RealWorldExplorerScreen
import com.example.ui.stories.MathStoryScreen
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.treasure.TreasureScreen
import com.example.ui.viewmodel.MathBloomViewModel
import com.example.ui.viewmodel.MathBloomViewModelFactory
import com.example.ui.voice.VoiceTranscribeScreen

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val database = MathBloomDatabase.getDatabase(applicationContext, lifecycleScope)
    val syncManager = FirebaseSyncManager()
    val authManager = FirebaseAuthManager(applicationContext)
    val geminiApiClient = GeminiApiClient()
    val audioRecorderHelper = AudioRecorderHelper(applicationContext)

    val repository = MathBloomRepository(
      database = database,
      syncManager = syncManager,
      authManager = authManager,
      geminiApiClient = geminiApiClient,
      audioRecorderHelper = audioRecorderHelper,
      appScope = lifecycleScope
    )

    setContent {
      MyApplicationTheme {
        val viewModel: MathBloomViewModel = viewModel(
          factory = MathBloomViewModelFactory(repository)
        )
        MathBloomApp(viewModel)
      }
    }
  }
}

@Composable
fun MathBloomApp(viewModel: MathBloomViewModel) {
  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val activeLesson by viewModel.activeLesson.collectAsStateWithLifecycle()
  val activeGameId by viewModel.activeGameId.collectAsStateWithLifecycle()
  val isParentModeActive by viewModel.isParentModeActive.collectAsStateWithLifecycle()
  val showParentGate by viewModel.showParentGate.collectAsStateWithLifecycle()
  val isOnboarding by viewModel.isOnboarding.collectAsStateWithLifecycle()

  val currentProfile by viewModel.currentProfile.collectAsStateWithLifecycle()
  val allLessons by viewModel.allLessons.collectAsStateWithLifecycle()
  val progress by viewModel.progress.collectAsStateWithLifecycle()
  val completedCount by viewModel.completedCount.collectAsStateWithLifecycle()
  val collectibles by viewModel.collectibles.collectAsStateWithLifecycle()
  val achievements by viewModel.achievements.collectAsStateWithLifecycle()
  val parentSettings by viewModel.parentSettings.collectAsStateWithLifecycle()
  val syncState by viewModel.syncState.collectAsStateWithLifecycle()
  val authState by viewModel.authState.collectAsStateWithLifecycle()

  // Audio Transcription state
  val isRecording by viewModel.isRecording.collectAsStateWithLifecycle()
  val isTranscribing by viewModel.isTranscribing.collectAsStateWithLifecycle()
  val transcriptionResult by viewModel.transcriptionResult.collectAsStateWithLifecycle()

  // Search Grounding state
  val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
  val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()

  var studioSection by remember { mutableStateOf("Drawing") }

  // First lesson or next uncompleted lesson
  val nextLesson = allLessons.firstOrNull()

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      if (!isOnboarding && !isParentModeActive && activeLesson == null && activeGameId == null) {
        KidTopBar(
          childProfile = currentProfile,
          onAvatarClicked = { viewModel.startOnboarding() },
          onParentGateClicked = { viewModel.openParentGate() }
        )
      }
    },
    bottomBar = {
      if (!isOnboarding && !isParentModeActive && activeLesson == null && activeGameId == null) {
        KidBottomNav(
          currentTab = currentTab,
          onTabSelected = { viewModel.selectTab(it) }
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      AnimatedContent(
        targetState = when {
          isOnboarding -> "onboarding"
          isParentModeActive -> "parent"
          activeLesson != null -> "lesson"
          activeGameId != null -> "game"
          else -> currentTab.name
        },
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "mainScreenTransition"
      ) { screenState ->
        when (screenState) {
          "onboarding" -> {
            OnboardingScreen(
              onCompleteOnboarding = { name, avatar, level, interest ->
                viewModel.completeOnboarding(name, avatar, level, interest)
              }
            )
          }

          "parent" -> {
            ParentDashboardScreen(
              childProfile = currentProfile,
              parentSettings = parentSettings,
              progressList = progress,
              syncState = syncState,
              authState = authState,
              onUpdateLevel = { viewModel.updateLevel(it) },
              onUpdateSettings = { viewModel.updateSettings(it) },
              onSignInGoogle = { viewModel.signInWithGoogle() },
              onSignInEmail = { email, name -> viewModel.signInWithEmail(email, name) },
              onSignOut = { viewModel.signOut() },
              onTriggerSync = { viewModel.triggerSync() },
              onExitParentArea = { viewModel.exitParentMode() }
            )
          }

          "lesson" -> {
            activeLesson?.let { lesson ->
              LessonPlayerScreen(
                lesson = lesson,
                onComplete = { stars, hints -> viewModel.completeLesson(stars, hints) },
                onBack = { viewModel.exitLesson() }
              )
            }
          }

          "game" -> {
            NumberCatcherGame(
              onGameFinished = { stars -> viewModel.finishGame(stars) },
              onExit = { viewModel.exitGame() }
            )
          }

          BottomTab.ADVENTURE.name -> {
            HomeScreen(
              childProfile = currentProfile,
              nextLesson = nextLesson,
              completedCount = completedCount,
              onStartLearning = {
                nextLesson?.let { viewModel.startLesson(it) }
              },
              onPlayGame = { viewModel.playGame("catcher") },
              onOpenDrawing = {
                studioSection = "Drawing"
                viewModel.selectTab(BottomTab.STUDIO)
              },
              onOpenStories = {
                studioSection = "Stories"
                viewModel.selectTab(BottomTab.STUDIO)
              },
              onOpenTreasure = { viewModel.selectTab(BottomTab.TREASURE) },
              onOpenVoiceTranscribe = {
                studioSection = "Voice"
                viewModel.selectTab(BottomTab.STUDIO)
              },
              onOpenSearchExplorer = {
                studioSection = "Search"
                viewModel.selectTab(BottomTab.STUDIO)
              }
            )
          }

          BottomTab.WORLD.name -> {
            WorldMapScreen(
              progressList = progress,
              onTopicSelected = { viewModel.selectTopic(it) }
            )
          }

          BottomTab.GAMES.name -> {
            GameCenterScreen(
              onPlayGame = { viewModel.playGame(it) }
            )
          }

          BottomTab.STUDIO.name -> {
            StudioHub(
              currentSection = studioSection,
              onSectionSelected = { studioSection = it },
              isRecording = isRecording,
              isTranscribing = isTranscribing,
              transcriptionResult = transcriptionResult,
              onStartRecording = { viewModel.startAudioRecording() },
              onStopRecording = { viewModel.stopAudioRecording() },
              onTranscribeSample = { viewModel.transcribeSampleAudio(it) },
              isSearching = isSearching,
              searchResult = searchResult,
              onExecuteSearch = { viewModel.searchGroundedMath(it) }
            )
          }

          BottomTab.TREASURE.name -> {
            TreasureScreen(
              collectibles = collectibles,
              achievements = achievements,
              streakDays = currentProfile?.streakDays ?: 1
            )
          }
        }
      }

      // Parent Gate verification dialog overlay
      if (showParentGate) {
        ParentGateDialog(
          parentPin = parentSettings?.pinCode ?: "1234",
          onAuthorized = { viewModel.enterParentMode() },
          onDismiss = { viewModel.closeParentGate() }
        )
      }
    }
  }
}

@Composable
fun StudioHub(
  currentSection: String,
  onSectionSelected: (String) -> Unit,
  isRecording: Boolean,
  isTranscribing: Boolean,
  transcriptionResult: com.example.data.gemini.TranscriptionResult?,
  onStartRecording: () -> Unit,
  onStopRecording: () -> Unit,
  onTranscribeSample: (String) -> Unit,
  isSearching: Boolean,
  searchResult: com.example.data.gemini.SearchGroundingResult?,
  onExecuteSearch: (String) -> Unit
) {
  val scrollState = rememberScrollState()

  Column(modifier = Modifier.fillMaxSize()) {
    // Sub-tab switcher with horizontal scroll for comfortable touch targets
    Surface(
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(scrollState)
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf(
          "Drawing" to "🎨 Drawing Studio",
          "Stories" to "📚 Math Stories",
          "Voice" to "🎙️ Voice Transcribe",
          "Search" to "🔍 Search Explorer"
        ).forEach { (key, label) ->
          val isSelected = (currentSection == key)
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (isSelected) MathBlue else Color(0xFFF1F5F9),
            modifier = Modifier.clickable { onSectionSelected(key) }
          ) {
            Box(
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (isSelected) Color.White else Color(0xFF334155)
              )
            }
          }
        }
      }
    }

    when (currentSection) {
      "Drawing" -> MathDrawingCanvas()
      "Stories" -> MathStoryScreen()
      "Voice" -> VoiceTranscribeScreen(
        isRecording = isRecording,
        isTranscribing = isTranscribing,
        transcriptionResult = transcriptionResult,
        onStartRecording = onStartRecording,
        onStopRecording = onStopRecording,
        onTranscribeSample = onTranscribeSample
      )
      "Search" -> RealWorldExplorerScreen(
        isSearching = isSearching,
        searchResult = searchResult,
        onExecuteSearch = onExecuteSearch
      )
      else -> MathDrawingCanvas()
    }
  }
}
