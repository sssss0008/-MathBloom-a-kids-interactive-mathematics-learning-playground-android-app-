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

  // --- Daily Challenge Feature (Gemini model: gemini-3.5-flash) ---
  fun getDailyChallengeForToday(dateStr: String): Flow<com.example.data.model.DailyChallenge?> =
    database.dailyChallengeDao().getChallengeForDate(dateStr)

  suspend fun getDailyChallengeForTodaySync(dateStr: String): com.example.data.model.DailyChallenge? =
    database.dailyChallengeDao().getChallengeForDateSync(dateStr)

  suspend fun fetchOrGenerateDailyChallenge(
    childName: String,
    level: Int,
    interest: String,
    forceRefresh: Boolean = false
  ): com.example.data.model.DailyChallenge {
    val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
    val existing = database.dailyChallengeDao().getChallengeForDateSync(today)

    if (existing != null && !forceRefresh) {
      return existing
    }

    val result = geminiApiClient.generateDailyChallenge(childName, level, interest)
    val challenge = result.challenge ?: geminiApiClient.createOfflineDailyChallenge(
      "challenge_$today",
      today,
      level,
      interest
    )

    database.dailyChallengeDao().insertChallenge(challenge)

    // Sync to Firestore
    appScope.launch(Dispatchers.IO) {
      syncManager.syncDailyChallengeToCloud(challenge)
    }

    return challenge
  }

  suspend fun completeDailyChallenge(
    challengeId: String,
    score: Int,
    starsEarned: Int
  ) {
    database.dailyChallengeDao().markCompleted(challengeId, score, starsEarned)
    database.childProfileDao().addStars("child_leo", starsEarned)

    // Check & reward Daily Challenge champion trophy if not unlocked
    unlockBadgeIfEligible("badge_daily_champ")
    if (score == 3) {
      unlockBadgeIfEligible("badge_speed_sticker")
    }

    val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
    val updated = database.dailyChallengeDao().getChallengeForDateSync(today)
    if (updated != null) {
      appScope.launch(Dispatchers.IO) {
        syncManager.syncDailyChallengeToCloud(updated)
        val profile = database.childProfileDao().getProfileByIdSync("child_leo")
        if (profile != null) syncManager.syncChildProfileToCloud(profile)
      }
    }
  }

  // --- Badge Award System (Virtual Stickers & Trophies in Firebase Firestore) ---
  fun getBadgeAwards(childId: String = "child_leo"): Flow<List<com.example.data.model.BadgeAward>> =
    database.badgeAwardDao().getBadgesForChild(childId)

  suspend fun unlockBadgeIfEligible(badgeId: String): com.example.data.model.BadgeAward? {
    val badge = database.badgeAwardDao().getBadgeById(badgeId) ?: return null
    if (!badge.isUnlocked) {
      database.badgeAwardDao().unlockBadge(badgeId, System.currentTimeMillis(), synced = true)
      val updated = badge.copy(isUnlocked = true, unlockedAt = System.currentTimeMillis())
      appScope.launch(Dispatchers.IO) {
        syncManager.syncBadgeAwardToCloud(updated)
      }
      return updated
    }
    return null
  }

  suspend fun awardMilestoneBadge(
    title: String,
    description: String,
    badgeType: String,
    emoji: String,
    category: String,
    milestoneLevel: Int
  ): com.example.data.model.BadgeAward {
    val id = "badge_${System.currentTimeMillis()}"
    val badge = com.example.data.model.BadgeAward(
      id = id,
      childId = "child_leo",
      title = title,
      description = description,
      badgeType = badgeType,
      stickerOrTrophyEmoji = emoji,
      category = category,
      milestoneLevel = milestoneLevel,
      isUnlocked = true,
      unlockedAt = System.currentTimeMillis(),
      syncedToFirestore = true
    )
    database.badgeAwardDao().insertBadges(listOf(badge))
    appScope.launch(Dispatchers.IO) {
      syncManager.syncBadgeAwardToCloud(badge)
    }
    return badge
  }
}

