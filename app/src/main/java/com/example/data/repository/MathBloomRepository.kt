package com.example.data.repository

import com.example.data.audio.AudioRecorderHelper
import com.example.data.firebase.AuthUserState
import com.example.data.firebase.FirebaseAuthManager
import com.example.data.firebase.FirebaseSyncManager
import com.example.data.firebase.SyncState
import com.example.data.gemini.GeminiApiClient
import com.example.data.gemini.SearchGroundingResult
import com.example.data.gemini.TranscriptionResult
import com.example.data.local.MathBloomDatabase
import com.example.data.model.Achievement
import com.example.data.model.ChildProfile
import com.example.data.model.CollectibleItem
import com.example.data.model.Lesson
import com.example.data.model.ParentSettings
import com.example.data.model.UserProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class MathBloomRepository(
  private val database: MathBloomDatabase,
  private val syncManager: FirebaseSyncManager,
  private val authManager: FirebaseAuthManager,
  private val geminiApiClient: GeminiApiClient,
  val audioRecorderHelper: AudioRecorderHelper,
  private val appScope: CoroutineScope
) {
  val syncState: StateFlow<SyncState> = syncManager.syncState
  val authState: StateFlow<AuthUserState> = authManager.userState

  fun getProfiles(): Flow<List<ChildProfile>> = database.childProfileDao().getAllProfiles()

  fun getProfile(id: String): Flow<ChildProfile?> = database.childProfileDao().getProfileById(id)

  fun getLessonsForTopic(topicId: String): Flow<List<Lesson>> =
    database.lessonDao().getLessonsForTopic(topicId)

  fun getAllLessons(): Flow<List<Lesson>> = database.lessonDao().getAllLessons()

  suspend fun getLesson(id: String): Lesson? = database.lessonDao().getLessonById(id)

  fun getProgress(childId: String): Flow<List<UserProgress>> =
    database.progressDao().getProgressForChild(childId)

  fun getCompletedCount(childId: String): Flow<Int> =
    database.progressDao().getCompletedCountForChild(childId)

  fun getAchievements(): Flow<List<Achievement>> = database.achievementDao().getAllAchievements()

  fun getCollectibles(): Flow<List<CollectibleItem>> = database.collectibleDao().getAllCollectibles()

  fun getParentSettings(): Flow<ParentSettings?> = database.parentSettingsDao().getSettings()

  suspend fun updateParentSettings(settings: ParentSettings) {
    database.parentSettingsDao().saveSettings(settings)
    appScope.launch(Dispatchers.IO) {
      syncManager.syncSettingsToCloud(settings)
    }
  }

  suspend fun updateProfileDetails(childId: String, name: String, avatar: String, interest: String) {
    database.childProfileDao().updateProfileDetails(childId, name, avatar, interest)
    val updated = database.childProfileDao().getProfileByIdSync(childId)
    if (updated != null) {
      appScope.launch(Dispatchers.IO) {
        syncManager.syncChildProfileToCloud(updated)
      }
    }
  }

  suspend fun updateChildLevel(childId: String, level: Int) {
    database.childProfileDao().updateLevel(childId, level)
    val updated = database.childProfileDao().getProfileByIdSync(childId)
    if (updated != null) {
      appScope.launch(Dispatchers.IO) {
        syncManager.syncChildProfileToCloud(updated)
      }
    }
  }

  suspend fun completeLesson(
    childId: String,
    lesson: Lesson,
    starsEarned: Int,
    hintsUsed: Int
  ): CollectibleItem? {
    val progressId = "${childId}_${lesson.id}"
    val progress = UserProgress(
      id = progressId,
      childId = childId,
      lessonId = lesson.id,
      topicId = lesson.topicId,
      isCompleted = true,
      starsEarned = starsEarned,
      attemptsCount = 1,
      hintsUsed = hintsUsed,
      accuracyPercentage = if (hintsUsed == 0) 100 else if (hintsUsed == 1) 85 else 70,
      lastPracticedTimestamp = System.currentTimeMillis()
    )
    database.progressDao().saveProgress(progress)
    database.childProfileDao().addStars(childId, starsEarned)

    // Check unlocks
    var unlockedPrize: CollectibleItem? = null
    val lockedPrize = database.collectibleDao().getRandomLockedCollectible()
    if (lockedPrize != null) {
      database.collectibleDao().unlockCollectible(lockedPrize.id)
      unlockedPrize = lockedPrize.copy(isUnlocked = true, unlockedAt = System.currentTimeMillis())
    }

    // Sync to Cloud Firestore
    appScope.launch(Dispatchers.IO) {
      syncManager.syncProgressToCloud(progress)
      val updatedProfile = database.childProfileDao().getProfileByIdSync(childId)
      if (updatedProfile != null) {
        syncManager.syncChildProfileToCloud(updatedProfile)
      }
    }

    return unlockedPrize
  }

  suspend fun addStarsToChild(childId: String, stars: Int) {
    database.childProfileDao().addStars(childId, stars)
    val updatedProfile = database.childProfileDao().getProfileByIdSync(childId)
    if (updatedProfile != null) {
      appScope.launch(Dispatchers.IO) {
        syncManager.syncChildProfileToCloud(updatedProfile)
      }
    }
  }

  // --- Audio Transcription Feature (model: gemini-3.5-transcribe) ---
  suspend fun transcribeAudio(audioFile: File): TranscriptionResult {
    val result = geminiApiClient.transcribeAudio(audioFile)
    if (result.success && result.transcribedText.isNotBlank()) {
      appScope.launch(Dispatchers.IO) {
        syncManager.saveAudioTranscriptionToCloud(
          transcription = result.transcribedText,
          detectedEquation = result.detectedMathEquation,
          solution = result.solutionAnswer
        )
      }
    }
    return result
  }

  // --- Google Search Grounding Feature (model: gemini-3.5-flash with googleSearch) ---
  suspend fun searchGrounding(query: String): SearchGroundingResult {
    val result = geminiApiClient.searchGrounding(query)
    if (result.success && result.answer.isNotBlank()) {
      appScope.launch(Dispatchers.IO) {
        syncManager.saveGroundedSearchToCloud(
          query = query,
          answer = result.answer,
          sources = result.sources.map { it.title }
        )
      }
    }
    return result
  }

  // --- Firebase Auth & Google Sign-In Actions ---
  suspend fun signInWithGoogle(idToken: String) {
    authManager.signInWithGoogleCredential(idToken)
    syncManager.triggerFullSync()
  }

  suspend fun signInWithEmailOrDirect(email: String, displayName: String) {
    authManager.signInWithEmailOrDirect(email, displayName)
    syncManager.triggerFullSync()
  }

  suspend fun signOut() {
    authManager.signOut()
  }

  suspend fun triggerSync() {
    syncManager.triggerFullSync()
  }
}
