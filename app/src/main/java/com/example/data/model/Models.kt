package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child_profiles")
data class ChildProfile(
  @PrimaryKey val id: String = "child_default",
  val name: String = "Leo",
  val avatar: String = "🦁", // Lion, Panda 🐼, Astronaut 🚀, Fox 🦊, Owl 🦉, Dino 🦖
  val level: Int = 1, // 1 to 8
  val totalStars: Int = 15,
  val streakDays: Int = 3,
  val lastActiveDate: Long = System.currentTimeMillis(),
  val favoriteInterest: String = "Animals" // Space, Dinosaurs, Animals, Superheroes, Art
)

enum class TopicCategory(
  val id: String,
  val title: String,
  val emoji: String,
  val worldName: String,
  val description: String,
  val colorHex: Long
) {
  NUMBERS("numbers", "Number Counting", "🔢", "Number Village", "Learn counting, place value & number lines", 0xFF3D5AFE),
  ADDITION("addition", "Fun Addition", "➕", "Addition Forest", "Combine objects & add numbers together", 0xFF00C853),
  SUBTRACTION("subtraction", "Take Away Subtraction", "➖", "Subtraction Springs", "Take away items & find differences", 0xFFFF6D00),
  MULTIPLICATION("multiplication", "Multiplication Arrays", "✖️", "Multiplication Mountain", "Repeated groups & times tables", 0xFF7C4DFF),
  DIVISION("division", "Equal Sharing Division", "➗", "Division Delta", "Share toys & objects into equal groups", 0xFF00B4D8),
  FRACTIONS("fractions", "Fraction Pizza & Pies", "🍕", "Fraction Island", "Cut pizzas, compare halves & quarters", 0xFFFF5252),
  GEOMETRY("geometry", "Shapes & Symmetry", "🔺", "Shape City", "Explore 2D/3D shapes, angles & symmetry", 0xFFFFB300),
  TIME("time", "Tick-Tock Clocks", "⏰", "Time Town", "Read analog clocks, hours & calendar days", 0xFF2EC4B6),
  MONEY("money", "Coin & Dollar Shop", "🪙", "Money Market", "Count coins, buy items & make change", 0xFF4CAF50),
  LOGIC("logic", "Brain Puzzles & Balance", "⚖️", "Puzzle Castle", "Pattern sequences, deduction & scales", 0xFF9C27B0)
}

enum class VisualMathType {
  COUNTING_ITEMS,
  NUMBER_LINE,
  ADDITION_ITEMS,
  SUBTRACTION_ITEMS,
  MULTIPLICATION_ARRAY,
  FRACTION_PIE,
  BALANCE_SCALE,
  CLOCK_TIME,
  MONEY_SHOP,
  SHAPE_IDENTIFY
}

@Entity(tableName = "lessons")
data class Lesson(
  @PrimaryKey val id: String,
  val topicId: String,
  val level: Int,
  val title: String,
  val prompt: String,
  val visualType: VisualMathType,
  val visualParam1: Int = 0, // e.g. first number or hour
  val visualParam2: Int = 0, // e.g. second number or minute / slices
  val itemEmoji: String = "🍎",
  val options: List<String> = emptyList(),
  val correctAnswer: String,
  val explanationSteps: List<String> = emptyList(),
  val hint1: String = "",
  val hint2: String = "",
  val hint3: String = ""
)

@Entity(tableName = "user_progress")
data class UserProgress(
  @PrimaryKey val id: String, // childId_lessonId or childId_topicId
  val childId: String,
  val lessonId: String,
  val topicId: String,
  val isCompleted: Boolean = false,
  val starsEarned: Int = 0,
  val attemptsCount: Int = 1,
  val hintsUsed: Int = 0,
  val accuracyPercentage: Int = 100,
  val lastPracticedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "achievements")
data class Achievement(
  @PrimaryKey val id: String,
  val title: String,
  val description: String,
  val iconEmoji: String,
  val requiredCount: Int,
  val currentCount: Int = 0,
  val isUnlocked: Boolean = false,
  val unlockedAt: Long? = null
)

@Entity(tableName = "collectibles")
data class CollectibleItem(
  @PrimaryKey val id: String,
  val name: String,
  val category: String, // Animals, Dinosaurs, Space, Vehicles, Fantasy
  val emoji: String,
  val rarity: String = "Common", // Common, Rare, Legendary
  val isUnlocked: Boolean = false,
  val unlockedAt: Long? = null
)

@Entity(tableName = "parent_settings")
data class ParentSettings(
  @PrimaryKey val id: String = "default_parent",
  val pinCode: String = "1234",
  val dailyGoalMinutes: Int = 15,
  val soundEnabled: Boolean = true,
  val narrationEnabled: Boolean = true,
  val screenTimeLimitMinutes: Int = 30,
  val cloudSyncEnabled: Boolean = true,
  val isParentEmailLinked: Boolean = false,
  val parentEmail: String = ""
)

data class DailyAdventureStep(
  val stepNumber: Int,
  val title: String,
  val typeName: String, // "Puzzle", "Visual Math", "Game", "Story"
  val icon: String,
  val isCompleted: Boolean,
  val targetLessonOrGameId: String
)
