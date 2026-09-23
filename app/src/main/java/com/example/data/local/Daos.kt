package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Achievement
import com.example.data.model.ChildProfile
import com.example.data.model.CollectibleItem
import com.example.data.model.Lesson
import com.example.data.model.ParentSettings
import com.example.data.model.UserProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface ChildProfileDao {
  @Query("SELECT * FROM child_profiles")
  fun getAllProfiles(): Flow<List<ChildProfile>>

  @Query("SELECT * FROM child_profiles WHERE id = :id LIMIT 1")
  fun getProfileById(id: String): Flow<ChildProfile?>

  @Query("SELECT * FROM child_profiles WHERE id = :id LIMIT 1")
  suspend fun getProfileByIdSync(id: String): ChildProfile?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProfile(profile: ChildProfile)

  @Query("UPDATE child_profiles SET totalStars = totalStars + :stars WHERE id = :childId")
  suspend fun addStars(childId: String, stars: Int)

  @Query("UPDATE child_profiles SET level = :newLevel WHERE id = :childId")
  suspend fun updateLevel(childId: String, newLevel: Int)

  @Query("UPDATE child_profiles SET name = :name, avatar = :avatar, favoriteInterest = :interest WHERE id = :childId")
  suspend fun updateProfileDetails(childId: String, name: String, avatar: String, interest: String)
}

@Dao
interface LessonDao {
  @Query("SELECT * FROM lessons WHERE topicId = :topicId ORDER BY level ASC")
  fun getLessonsForTopic(topicId: String): Flow<List<Lesson>>

  @Query("SELECT * FROM lessons")
  fun getAllLessons(): Flow<List<Lesson>>

  @Query("SELECT * FROM lessons WHERE id = :lessonId LIMIT 1")
  suspend fun getLessonById(lessonId: String): Lesson?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLessons(lessons: List<Lesson>)
}

@Dao
interface ProgressDao {
  @Query("SELECT * FROM user_progress WHERE childId = :childId")
  fun getProgressForChild(childId: String): Flow<List<UserProgress>>

  @Query("SELECT * FROM user_progress WHERE childId = :childId AND lessonId = :lessonId LIMIT 1")
  suspend fun getProgressForLesson(childId: String, lessonId: String): UserProgress?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveProgress(progress: UserProgress)

  @Query("SELECT COUNT(*) FROM user_progress WHERE childId = :childId AND isCompleted = 1")
  fun getCompletedCountForChild(childId: String): Flow<Int>
}

@Dao
interface AchievementDao {
  @Query("SELECT * FROM achievements")
  fun getAllAchievements(): Flow<List<Achievement>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAchievements(achievements: List<Achievement>)

  @Update
  suspend fun updateAchievement(achievement: Achievement)

  @Query("UPDATE achievements SET isUnlocked = 1, unlockedAt = :timestamp WHERE id = :id")
  suspend fun unlockAchievement(id: String, timestamp: Long = System.currentTimeMillis())
}

@Dao
interface CollectibleDao {
  @Query("SELECT * FROM collectibles")
  fun getAllCollectibles(): Flow<List<CollectibleItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCollectibles(items: List<CollectibleItem>)

  @Query("UPDATE collectibles SET isUnlocked = 1, unlockedAt = :timestamp WHERE id = :id")
  suspend fun unlockCollectible(id: String, timestamp: Long = System.currentTimeMillis())

  @Query("SELECT * FROM collectibles WHERE isUnlocked = 0 ORDER BY RANDOM() LIMIT 1")
  suspend fun getRandomLockedCollectible(): CollectibleItem?
}

@Dao
interface ParentSettingsDao {
  @Query("SELECT * FROM parent_settings WHERE id = 'default_parent' LIMIT 1")
  fun getSettings(): Flow<ParentSettings?>

  @Query("SELECT * FROM parent_settings WHERE id = 'default_parent' LIMIT 1")
  suspend fun getSettingsSync(): ParentSettings?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveSettings(settings: ParentSettings)
}

@Dao
interface DailyChallengeDao {
  @Query("SELECT * FROM daily_challenges ORDER BY dateString DESC")
  fun getAllDailyChallenges(): Flow<List<com.example.data.model.DailyChallenge>>

  @Query("SELECT * FROM daily_challenges WHERE dateString = :dateString LIMIT 1")
  fun getChallengeForDate(dateString: String): Flow<com.example.data.model.DailyChallenge?>

  @Query("SELECT * FROM daily_challenges WHERE dateString = :dateString LIMIT 1")
  suspend fun getChallengeForDateSync(dateString: String): com.example.data.model.DailyChallenge?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChallenge(challenge: com.example.data.model.DailyChallenge)

  @Query("UPDATE daily_challenges SET isCompleted = 1, score = :score, starsEarned = :stars, completedAt = :timestamp WHERE id = :id")
  suspend fun markCompleted(id: String, score: Int, stars: Int, timestamp: Long = System.currentTimeMillis())
}

@Dao
interface BadgeAwardDao {
  @Query("SELECT * FROM badge_awards WHERE childId = :childId ORDER BY milestoneLevel ASC, isUnlocked DESC")
  fun getBadgesForChild(childId: String): Flow<List<com.example.data.model.BadgeAward>>

  @Query("SELECT * FROM badge_awards WHERE id = :badgeId LIMIT 1")
  suspend fun getBadgeById(badgeId: String): com.example.data.model.BadgeAward?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBadges(badges: List<com.example.data.model.BadgeAward>)

  @Query("UPDATE badge_awards SET isUnlocked = 1, unlockedAt = :timestamp, syncedToFirestore = :synced WHERE id = :badgeId")
  suspend fun unlockBadge(badgeId: String, timestamp: Long = System.currentTimeMillis(), synced: Boolean = false)

  @Query("UPDATE badge_awards SET syncedToFirestore = 1 WHERE id = :badgeId")
  suspend fun markSynced(badgeId: String)
}

