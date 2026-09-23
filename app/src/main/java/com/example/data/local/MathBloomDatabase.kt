package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.Achievement
import com.example.data.model.ChildProfile
import com.example.data.model.CollectibleItem
import com.example.data.model.Lesson
import com.example.data.model.ParentSettings
import com.example.data.model.UserProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    ChildProfile::class,
    Lesson::class,
    UserProgress::class,
    Achievement::class,
    CollectibleItem::class,
    ParentSettings::class,
    com.example.data.model.DailyChallenge::class,
    com.example.data.model.BadgeAward::class
  ],
  version = 2,
  exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MathBloomDatabase : RoomDatabase() {
  abstract fun childProfileDao(): ChildProfileDao
  abstract fun lessonDao(): LessonDao
  abstract fun progressDao(): ProgressDao
  abstract fun achievementDao(): AchievementDao
  abstract fun collectibleDao(): CollectibleDao
  abstract fun parentSettingsDao(): ParentSettingsDao
  abstract fun dailyChallengeDao(): DailyChallengeDao
  abstract fun badgeAwardDao(): BadgeAwardDao

  companion object {
    @Volatile
    private var INSTANCE: MathBloomDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): MathBloomDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          MathBloomDatabase::class.java,
          "mathbloom_database"
        )
          .fallbackToDestructiveMigration()
          .addCallback(DatabaseCallback(scope))
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback(
      private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateDatabase(database)
          }
        }
      }

      suspend fun populateDatabase(db: MathBloomDatabase) {
        db.childProfileDao().insertProfile(InitialData.defaultProfile)
        db.parentSettingsDao().saveSettings(InitialData.defaultSettings)
        db.lessonDao().insertLessons(InitialData.initialLessons)
        db.achievementDao().insertAchievements(InitialData.sampleAchievements)
        db.collectibleDao().insertCollectibles(InitialData.sampleCollectibles)
        db.badgeAwardDao().insertBadges(InitialData.sampleBadges)
        db.dailyChallengeDao().insertChallenge(InitialData.sampleDailyChallenge)
      }
    }
  }
}
