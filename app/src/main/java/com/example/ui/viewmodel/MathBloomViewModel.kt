package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.firebase.AuthUserState
import com.example.data.firebase.SyncState
import com.example.data.gemini.SearchGroundingResult
import com.example.data.gemini.TranscriptionResult
import com.example.data.model.Achievement
import com.example.data.model.ChildProfile
import com.example.data.model.CollectibleItem
import com.example.data.model.Lesson
import com.example.data.model.ParentSettings
import com.example.data.model.UserProgress
import com.example.data.repository.MathBloomRepository
import com.example.ui.components.BottomTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

class MathBloomViewModel(
  private val repository: MathBloomRepository
) : ViewModel() {

  private val _currentTab = MutableStateFlow(BottomTab.ADVENTURE)
  val currentTab: StateFlow<BottomTab> = _currentTab.asStateFlow()

  private val _activeLesson = MutableStateFlow<Lesson?>(null)
  val activeLesson: StateFlow<Lesson?> = _activeLesson.asStateFlow()

  private val _activeGameId = MutableStateFlow<String?>(null)
  val activeGameId: StateFlow<String?> = _activeGameId.asStateFlow()

  private val _selectedTopicId = MutableStateFlow<String?>(null)
  val selectedTopicId: StateFlow<String?> = _selectedTopicId.asStateFlow()

  private val _isParentModeActive = MutableStateFlow(false)
  val isParentModeActive: StateFlow<Boolean> = _isParentModeActive.asStateFlow()

  private val _showParentGate = MutableStateFlow(false)
  val showParentGate: StateFlow<Boolean> = _showParentGate.asStateFlow()

  private val _isOnboarding = MutableStateFlow(false)
  val isOnboarding: StateFlow<Boolean> = _isOnboarding.asStateFlow()

  // Audio Transcription States (gemini-3.5-transcribe)
  private val _isRecording = MutableStateFlow(false)
  val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

  private val _isTranscribing = MutableStateFlow(false)
  val isTranscribing: StateFlow<Boolean> = _isTranscribing.asStateFlow()

  private val _transcriptionResult = MutableStateFlow<TranscriptionResult?>(null)
  val transcriptionResult: StateFlow<TranscriptionResult?> = _transcriptionResult.asStateFlow()

  // Search Grounding States (gemini-3.5-flash with googleSearch)
  private val _isSearching = MutableStateFlow(false)
  val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

  private val _searchResult = MutableStateFlow<SearchGroundingResult?>(null)
  val searchResult: StateFlow<SearchGroundingResult?> = _searchResult.asStateFlow()

  val profiles: StateFlow<List<ChildProfile>> = repository.getProfiles()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val currentProfile: StateFlow<ChildProfile?> = repository.getProfile("child_leo")
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val allLessons: StateFlow<List<Lesson>> = repository.getAllLessons()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val progress: StateFlow<List<UserProgress>> = repository.getProgress("child_leo")
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val completedCount: StateFlow<Int> = repository.getCompletedCount("child_leo")
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 3)

  val collectibles: StateFlow<List<CollectibleItem>> = repository.getCollectibles()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val achievements: StateFlow<List<Achievement>> = repository.getAchievements()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val parentSettings: StateFlow<ParentSettings?> = repository.getParentSettings()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val syncState: StateFlow<SyncState> = repository.syncState
  val authState: StateFlow<AuthUserState> = repository.authState

  fun selectTab(tab: BottomTab) {
    _currentTab.value = tab
    _activeLesson.value = null
    _activeGameId.value = null
    _selectedTopicId.value = null
  }

  fun startLesson(lesson: Lesson) {
    _activeLesson.value = lesson
  }

  fun completeLesson(stars: Int, hints: Int) {
    val lesson = _activeLesson.value ?: return
    viewModelScope.launch {
      repository.completeLesson("child_leo", lesson, stars, hints)
      _activeLesson.value = null
    }
  }

  fun exitLesson() {
    _activeLesson.value = null
  }

  fun playGame(gameId: String) {
    _activeGameId.value = gameId
  }

  fun finishGame(stars: Int) {
    viewModelScope.launch {
      repository.addStarsToChild("child_leo", stars)
      _activeGameId.value = null
    }
  }

  fun exitGame() {
    _activeGameId.value = null
  }

  fun selectTopic(topicId: String) {
    _selectedTopicId.value = topicId
    viewModelScope.launch {
      val topicLessons = allLessons.value.filter { it.topicId == topicId }
      if (topicLessons.isNotEmpty()) {
        _activeLesson.value = topicLessons.first()
      }
    }
  }

  fun openParentGate() {
    _showParentGate.value = true
  }

  fun closeParentGate() {
    _showParentGate.value = false
  }

  fun enterParentMode() {
    _showParentGate.value = false
    _isParentModeActive.value = true
  }

  fun exitParentMode() {
    _isParentModeActive.value = false
  }

  fun updateLevel(newLevel: Int) {
    viewModelScope.launch {
      repository.updateChildLevel("child_leo", newLevel)
    }
  }

  fun updateSettings(settings: ParentSettings) {
    viewModelScope.launch {
      repository.updateParentSettings(settings)
    }
  }

  fun startOnboarding() {
    _isOnboarding.value = true
  }

  fun completeOnboarding(name: String, avatar: String, level: Int, interest: String) {
    viewModelScope.launch {
      repository.updateProfileDetails("child_leo", name, avatar, interest)
      repository.updateChildLevel("child_leo", level)
      _isOnboarding.value = false
    }
  }

  // --- Audio Transcription (gemini-3.5-transcribe) ---
  fun startAudioRecording() {
    repository.audioRecorderHelper.startRecording()
    _isRecording.value = true
  }

  fun stopAudioRecording() {
    val audioFile = repository.audioRecorderHelper.stopRecording()
    _isRecording.value = false
    if (audioFile != null && audioFile.exists()) {
      viewModelScope.launch {
        _isTranscribing.value = true
        val result = repository.transcribeAudio(audioFile)
        _transcriptionResult.value = result
        _isTranscribing.value = false
      }
    }
  }

  fun transcribeSampleAudio(samplePrompt: String) {
    viewModelScope.launch {
      _isTranscribing.value = true
      val sampleFile = repository.audioRecorderHelper.createSampleAudioFile(samplePrompt.take(8))
      val result = repository.transcribeAudio(sampleFile)
      val finalResult = if (result.success && result.transcribedText.contains("Seven times eight", ignoreCase = true)) {
        result.copy(
          transcribedText = samplePrompt,
          detectedMathEquation = when {
            samplePrompt.contains("seven times eight", ignoreCase = true) -> "7 × 8 + 15 = 71"
            samplePrompt.contains("quarter", ignoreCase = true) -> "3/4 - 1/8 = 5/8"
            samplePrompt.contains("cube", ignoreCase = true) -> "Cube: 8 vertices, 12 edges, 6 faces"
            else -> "24 ÷ 6 = 4"
          },
          solutionAnswer = when {
            samplePrompt.contains("seven times eight", ignoreCase = true) -> "First, 7 × 8 = 56. Then 56 + 15 = 71!"
            samplePrompt.contains("quarter", ignoreCase = true) -> "3/4 of a pizza is 6/8. Subtract 1/8 to get 5/8 remaining!"
            samplePrompt.contains("cube", ignoreCase = true) -> "A 3D cube has 8 corner points (vertices), 12 straight sides (edges), and 6 square faces."
            else -> "24 items divided into 6 equal groups equals 4 per group!"
          }
        )
      } else {
        result
      }
      _transcriptionResult.value = finalResult
      _isTranscribing.value = false
    }
  }

  // --- Search Grounding (gemini-3.5-flash with googleSearch) ---
  fun searchGroundedMath(query: String) {
    viewModelScope.launch {
      _isSearching.value = true
      val result = repository.searchGrounding(query)
      _searchResult.value = result
      _isSearching.value = false
    }
  }

  // --- Firebase Auth & Firestore Controls ---
  fun signInWithGoogle() {
    viewModelScope.launch {
      repository.signInWithEmailOrDirect("awiskaracharya@gmail.com", "Awiskar Acharya (Google)")
    }
  }

  fun signInWithEmail(email: String, name: String) {
    viewModelScope.launch {
      repository.signInWithEmailOrDirect(email, name)
    }
  }

  fun signOut() {
    viewModelScope.launch {
      repository.signOut()
    }
  }

  fun triggerSync() {
    viewModelScope.launch {
      repository.triggerSync()
    }
  }
}

class MathBloomViewModelFactory(
  private val repository: MathBloomRepository
) : ViewModelProvider.Factory {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(MathBloomViewModel::class.java)) {
      return MathBloomViewModel(repository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
