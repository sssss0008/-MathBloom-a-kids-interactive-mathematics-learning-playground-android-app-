package com.example.data.local

import com.example.data.model.Achievement
import com.example.data.model.ChildProfile
import com.example.data.model.CollectibleItem
import com.example.data.model.Lesson
import com.example.data.model.ParentSettings
import com.example.data.model.VisualMathType

object InitialData {
  val defaultProfile = ChildProfile(
    id = "child_leo",
    name = "Leo",
    avatar = "🦁",
    level = 2,
    totalStars = 42,
    streakDays = 3,
    favoriteInterest = "Animals"
  )

  val defaultSettings = ParentSettings(
    id = "default_parent",
    pinCode = "1234",
    dailyGoalMinutes = 15,
    soundEnabled = true,
    narrationEnabled = true,
    screenTimeLimitMinutes = 30,
    cloudSyncEnabled = true
  )

  val sampleAchievements = listOf(
    Achievement("ach_first", "First Adventure", "Completed your very first math lesson!", "🌟", 1, 1, true),
    Achievement("ach_counter", "Number Explorer", "Count objects up to 10 with ease!", "🔢", 5, 3, false),
    Achievement("ach_adder", "Addition Wizard", "Solved 10 visual addition puzzles!", "➕", 10, 4, false),
    Achievement("ach_fraction", "Pizza Master", "Mastered halves and quarters sharing!", "🍕", 5, 2, false),
    Achievement("ach_shapes", "Shape Detective", "Found triangles, circles & hexes in Shape City!", "🔺", 5, 5, true),
    Achievement("ach_time", "Clock Keeper", "Set the analog clock correctly 3 times!", "⏰", 3, 1, false),
    Achievement("ach_streak", "Streak Star", "Practiced math for 3 consecutive days!", "🔥", 3, 3, true),
    Achievement("ach_money", "Market Merchant", "Counted coins to buy toys in the shop!", "🪙", 5, 2, false)
  )

  val sampleBadges = listOf(
    com.example.data.model.BadgeAward(
      id = "badge_add_novice",
      childId = "child_leo",
      title = "Addition Seedling",
      description = "Solved your first 3 addition equations with flying colors!",
      badgeType = "STICKER",
      stickerOrTrophyEmoji = "🌱",
      category = "Arithmetic",
      milestoneLevel = 1,
      isUnlocked = true,
      unlockedAt = System.currentTimeMillis() - 86400000L
    ),
    com.example.data.model.BadgeAward(
      id = "badge_add_master",
      childId = "child_leo",
      title = "Golden Calculator Trophy",
      description = "Mastered 10 arithmetic challenges without a single misstep!",
      badgeType = "TROPHY",
      stickerOrTrophyEmoji = "🏆",
      category = "Arithmetic",
      milestoneLevel = 2,
      isUnlocked = false
    ),
    com.example.data.model.BadgeAward(
      id = "badge_daily_champ",
      childId = "child_leo",
      title = "Daily Quest Champion",
      description = "Conquered an entire Gemini Daily Arithmetic Challenge!",
      badgeType = "TROPHY",
      stickerOrTrophyEmoji = "👑",
      category = "Daily Challenge",
      milestoneLevel = 1,
      isUnlocked = true,
      unlockedAt = System.currentTimeMillis() - 43200000L
    ),
    com.example.data.model.BadgeAward(
      id = "badge_dino_sticker",
      childId = "child_leo",
      title = "Dino Math Explorer",
      description = "Solved arithmetic problems with friendly dinosaurs!",
      badgeType = "STICKER",
      stickerOrTrophyEmoji = "🦕",
      category = "Arithmetic",
      milestoneLevel = 1,
      isUnlocked = true,
      unlockedAt = System.currentTimeMillis() - 21600000L
    ),
    com.example.data.model.BadgeAward(
      id = "badge_streak_blaze",
      childId = "child_leo",
      title = "Solar Blaze Trophy",
      description = "Reached a blazing 5-day daily learning streak!",
      badgeType = "TROPHY",
      stickerOrTrophyEmoji = "☀️",
      category = "Streak",
      milestoneLevel = 3,
      isUnlocked = false
    ),
    com.example.data.model.BadgeAward(
      id = "badge_rainbow_sticker",
      childId = "child_leo",
      title = "Rainbow Math Sparkle",
      description = "Earned 50 total golden stars across math quests!",
      badgeType = "STICKER",
      stickerOrTrophyEmoji = "🌈",
      category = "Mastery",
      milestoneLevel = 2,
      isUnlocked = false
    ),
    com.example.data.model.BadgeAward(
      id = "badge_galaxy_trophy",
      childId = "child_leo",
      title = "Grand Cosmic Trophy",
      description = "Reached level 5 in MathBloom adventures!",
      badgeType = "TROPHY",
      stickerOrTrophyEmoji = "🪐",
      category = "Mastery",
      milestoneLevel = 5,
      isUnlocked = false
    ),
    com.example.data.model.BadgeAward(
      id = "badge_speed_sticker",
      childId = "child_leo",
      title = "Rocket Cheetah Sticker",
      description = "Finished a daily challenge set in record time!",
      badgeType = "STICKER",
      stickerOrTrophyEmoji = "🚀",
      category = "Daily Challenge",
      milestoneLevel = 2,
      isUnlocked = false
    )
  )

  val sampleDailyChallenge = com.example.data.model.DailyChallenge(
    id = "challenge_today",
    dateString = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date()),
    title = "Safari Safari Math Expedition",
    targetLevel = 2,
    theme = "Safari Animals",
    themeEmoji = "🦁",
    problems = listOf(
      com.example.data.model.DailyProblem(
        id = "safari_1",
        question = "3 cute baby zebras meet 4 elephant calves by the river. How many animal friends are there altogether?",
        visualEmoji = "🦓",
        options = listOf("6", "7", "8", "9"),
        correctAnswer = "7",
        explanation = "3 zebras + 4 elephants = 7 total animal friends!",
        hint = "Count: 3... then 4, 5, 6, 7!"
      ),
      com.example.data.model.DailyProblem(
        id = "safari_2",
        question = "A giraffe family had 9 green acacia branches. A hungry monkey ate 3 of them! How many branches are left?",
        visualEmoji = "🦒",
        options = listOf("5", "6", "7", "8"),
        correctAnswer = "6",
        explanation = "9 branches take away 3 leaves 6 fresh branches!",
        hint = "Start at 9 and count back 3: 8, 7, 6."
      ),
      com.example.data.model.DailyProblem(
        id = "safari_3",
        question = "There are 2 pride rocks. On each rock, 4 lion cubs are resting. How many lion cubs in total?",
        visualEmoji = "🦁",
        options = listOf("6", "7", "8", "10"),
        correctAnswer = "8",
        explanation = "2 groups of 4 lion cubs equals 8 lion cubs! (2 × 4 = 8)",
        hint = "Add 4 + 4!"
      )
    ),
    isCompleted = false,
    score = 0,
    starsEarned = 0,
    isAiGenerated = true
  )


  val sampleCollectibles = listOf(
    CollectibleItem("col_t_rex", "T-Rex Junior", "Dinosaurs", "🦖", "Legendary", true),
    CollectibleItem("col_brachio", "Gentle Brachio", "Dinosaurs", "🦕", "Rare", false),
    CollectibleItem("col_rocket", "Starlight Rocket", "Space", "🚀", "Rare", true),
    CollectibleItem("col_planet", "Saturn Ring", "Space", "🪐", "Common", false),
    CollectibleItem("col_astronaut", "Cosmo Kitty", "Space", "🐱‍🚀", "Legendary", false),
    CollectibleItem("col_panda", "Bamboo Panda", "Animals", "🐼", "Common", true),
    CollectibleItem("col_fox", "Smarty Fox", "Animals", "🦊", "Rare", true),
    CollectibleItem("col_unicorn", "Rainbow Unicorn", "Fantasy", "🦄", "Legendary", false),
    CollectibleItem("col_dragon", "Baby Dragon", "Fantasy", "🐲", "Rare", false),
    CollectibleItem("col_firetruck", "Zoom Fire Engine", "Vehicles", "🚒", "Common", true),
    CollectibleItem("col_submarine", "Deep Sea Sub", "Vehicles", "🚢", "Rare", false),
    CollectibleItem("col_star", "Golden Super Star", "Fantasy", "⭐", "Common", true)
  )

  val initialLessons = listOf(
    // NUMBERS
    Lesson(
      id = "num_1",
      topicId = "numbers",
      level = 1,
      title = "Counting Apples in the Basket",
      prompt = "How many juicy red apples do you see in the garden?",
      visualType = VisualMathType.COUNTING_ITEMS,
      visualParam1 = 5,
      itemEmoji = "🍎",
      options = listOf("3", "4", "5", "6"),
      correctAnswer = "5",
      explanationSteps = listOf(
        "Let's count each apple one by one!",
        "1... 2... 3... 4... 5!",
        "There are 5 red apples!"
      ),
      hint1 = "Tap on each apple to count along!",
      hint2 = "It is more than 4, but less than 6.",
      hint3 = "Count the apples: 1, 2, 3, 4, and 5!"
    ),
    Lesson(
      id = "num_2",
      topicId = "numbers",
      level = 1,
      title = "Hopping on the Number Line",
      prompt = "Bunny starts at 0 and hops forward 4 steps. Where does bunny land?",
      visualType = VisualMathType.NUMBER_LINE,
      visualParam1 = 0,
      visualParam2 = 4,
      itemEmoji = "🐰",
      options = listOf("2", "3", "4", "5"),
      correctAnswer = "4",
      explanationSteps = listOf(
        "Start at position 0 on the number line.",
        "Hop 1 -> 2 -> 3 -> 4!",
        "Bunny lands right on number 4!"
      ),
      hint1 = "Look at the hop marks on the line.",
      hint2 = "Each jump moves forward by 1.",
      hint3 = "4 hops forward from 0 lands on 4."
    ),
    // ADDITION
    Lesson(
      id = "add_1",
      topicId = "addition",
      level = 2,
      title = "Combining Stars",
      prompt = "You have 3 shiny stars and find 2 more. How many stars in all?",
      visualType = VisualMathType.ADDITION_ITEMS,
      visualParam1 = 3,
      visualParam2 = 2,
      itemEmoji = "⭐",
      options = listOf("4", "5", "6", "7"),
      correctAnswer = "5",
      explanationSteps = listOf(
        "We have 3 stars on the left: ⭐ ⭐ ⭐",
        "We add 2 more stars on the right: ⭐ ⭐",
        "3 + 2 = 5 shiny stars!"
      ),
      hint1 = "Count the first group, then keep counting!",
      hint2 = "Start with 3, then count 4, 5.",
      hint3 = "3 + 2 equals 5!"
    ),
    Lesson(
      id = "add_2",
      topicId = "addition",
      level = 2,
      title = "Froggy's Number Line Jump",
      prompt = "Froggy is sitting at 6 and jumps 4 steps forward. Where is froggy?",
      visualType = VisualMathType.NUMBER_LINE,
      visualParam1 = 6,
      visualParam2 = 4,
      itemEmoji = "🐸",
      options = listOf("8", "9", "10", "11"),
      correctAnswer = "10",
      explanationSteps = listOf(
        "Start at 6.",
        "Take 4 jumps: 7, 8, 9, 10!",
        "6 + 4 = 10!"
      ),
      hint1 = "Count forward by 4 starting from 6.",
      hint2 = "6 + 4 makes a friendly number 10.",
      hint3 = "Jump 6 -> 7 -> 8 -> 9 -> 10!"
    ),
    // SUBTRACTION
    Lesson(
      id = "sub_1",
      topicId = "subtraction",
      level = 3,
      title = "Hungry Dino Snack",
      prompt = "There were 6 strawberries. Dino ate 2 of them! How many are left?",
      visualType = VisualMathType.SUBTRACTION_ITEMS,
      visualParam1 = 6,
      visualParam2 = 2,
      itemEmoji = "🍓",
      options = listOf("3", "4", "5", "6"),
      correctAnswer = "4",
      explanationSteps = listOf(
        "Start with 6 juicy strawberries: 🍓 🍓 🍓 🍓 🍓 🍓",
        "Cross off the 2 that Dino ate!",
        "Count what remains: 1, 2, 3, 4 strawberries!"
      ),
      hint1 = "Take away 2 from 6.",
      hint2 = "Count backwards from 6: 5, 4.",
      hint3 = "6 minus 2 leaves 4!"
    ),
    // MULTIPLICATION
    Lesson(
      id = "mul_1",
      topicId = "multiplication",
      level = 4,
      title = "Starlight Flower Arrays",
      prompt = "There are 3 rows of flowers, with 4 flowers in each row. How many flowers in total?",
      visualType = VisualMathType.MULTIPLICATION_ARRAY,
      visualParam1 = 3,
      visualParam2 = 4,
      itemEmoji = "🌸",
      options = listOf("7", "10", "12", "14"),
      correctAnswer = "12",
      explanationSteps = listOf(
        "Row 1 has 4 flowers: 🌸 🌸 🌸 🌸",
        "Row 2 has 4 flowers: 🌸 🌸 🌸 🌸",
        "Row 3 has 4 flowers: 🌸 🌸 🌸 🌸",
        "4 + 4 + 4 = 12, or 3 × 4 = 12!"
      ),
      hint1 = "Count by 4s three times: 4, 8...",
      hint2 = "3 groups of 4 is 3 × 4.",
      hint3 = "3 times 4 equals 12."
    ),
    // FRACTIONS
    Lesson(
      id = "frac_1",
      topicId = "fractions",
      level = 5,
      title = "Pizza Party Sharing",
      prompt = "A pizza is cut into 4 equal slices. Leo eats 1 slice. What fraction did Leo eat?",
      visualType = VisualMathType.FRACTION_PIE,
      visualParam1 = 1, // eaten
      visualParam2 = 4, // total
      itemEmoji = "🍕",
      options = listOf("1/2", "1/4", "3/4", "2/4"),
      correctAnswer = "1/4",
      explanationSteps = listOf(
        "The whole pizza is divided into 4 equal slices (denominator = 4).",
        "Leo ate 1 of those slices (numerator = 1).",
        "So Leo ate 1/4 (one quarter) of the pizza!"
      ),
      hint1 = "Look at the highlighted slice out of the total slices.",
      hint2 = "1 slice out of 4 equal pieces.",
      hint3 = "1 divided by 4 is written as 1/4."
    ),
    Lesson(
      id = "frac_2",
      topicId = "fractions",
      level = 5,
      title = "Half a Delicious Pie",
      prompt = "Which picture shows exactly 1/2 (half) of the pie shaded?",
      visualType = VisualMathType.FRACTION_PIE,
      visualParam1 = 2,
      visualParam2 = 4,
      itemEmoji = "🥧",
      options = listOf("1/4", "1/2", "3/4", "1/3"),
      correctAnswer = "1/2",
      explanationSteps = listOf(
        "2 slices out of 4 equal slices are shaded.",
        "2/4 is equal to 1/2!",
        "Half of the pie is shaded."
      ),
      hint1 = "Is half of the circle colored in?",
      hint2 = "2 out of 4 is the same as half.",
      hint3 = "2/4 reduces to 1/2."
    ),
    // GEOMETRY
    Lesson(
      id = "geo_1",
      topicId = "geometry",
      level = 6,
      title = "Counting Triangle Sides",
      prompt = "How many straight sides does this cheerful triangle have?",
      visualType = VisualMathType.SHAPE_IDENTIFY,
      visualParam1 = 3, // sides
      itemEmoji = "🔺",
      options = listOf("2", "3", "4", "5"),
      correctAnswer = "3",
      explanationSteps = listOf(
        "Count the sides: Left side, right side, and bottom base!",
        "Every triangle has exactly 3 sides and 3 corners!",
        "The answer is 3."
      ),
      hint1 = "Count the pointy corners or edges.",
      hint2 = "Tri- means three!",
      hint3 = "A triangle always has 3 sides."
    ),
    // TIME
    Lesson(
      id = "time_1",
      topicId = "time",
      level = 6,
      title = "Morning School Bell",
      prompt = "The short hour hand points to 9 and the long minute hand points to 12. What time is it?",
      visualType = VisualMathType.CLOCK_TIME,
      visualParam1 = 9,
      visualParam2 = 0,
      itemEmoji = "⏰",
      options = listOf("8:00", "9:00", "12:00", "9:30"),
      correctAnswer = "9:00",
      explanationSteps = listOf(
        "Look at the short hand: It points to the 9 (hour = 9).",
        "Look at the long hand: It points directly at 12 (minutes = 00 o'clock).",
        "The clock shows 9:00!"
      ),
      hint1 = "Check which number the short hand is pointing at.",
      hint2 = "When the long hand is on 12, it is ':00' o'clock.",
      hint3 = "Short hand on 9 means 9:00."
    ),
    // MONEY
    Lesson(
      id = "money_1",
      topicId = "money",
      level = 7,
      title = "Toy Bear at the Market",
      prompt = "A cute teddy bear costs $7. You pay with a $10 bill. How much change do you get back?",
      visualType = VisualMathType.MONEY_SHOP,
      visualParam1 = 7,
      visualParam2 = 10,
      itemEmoji = "🧸",
      options = listOf("$2", "$3", "$4", "$5"),
      correctAnswer = "$3",
      explanationSteps = listOf(
        "You gave $10 to the cashier.",
        "The toy costs $7.",
        "$10 - $7 = $3 change!",
        "The cashier gives you $3 back!"
      ),
      hint1 = "Subtract $7 from $10.",
      hint2 = "10 - 7 = ?",
      hint3 = "10 minus 7 is $3."
    ),
    // LOGIC / BALANCE
    Lesson(
      id = "logic_1",
      topicId = "logic",
      level = 8,
      title = "Balancing the Magic Scale",
      prompt = "The left pan has 5 + 3 gems. How many gems must go on the right pan to balance?",
      visualType = VisualMathType.BALANCE_SCALE,
      visualParam1 = 8,
      visualParam2 = 8,
      itemEmoji = "💎",
      options = listOf("6", "7", "8", "9"),
      correctAnswer = "8",
      explanationSteps = listOf(
        "First, add what's in the left pan: 5 + 3 = 8 gems.",
        "To balance the scale evenly, both sides must be equal!",
        "So the right pan also needs 8 gems!"
      ),
      hint1 = "What is 5 + 3?",
      hint2 = "Both sides of the scale must weigh the exact same amount.",
      hint3 = "5 + 3 = 8, so the right side needs 8 gems."
    )
  )
}
