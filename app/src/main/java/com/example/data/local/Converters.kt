package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.DailyProblem
import com.example.data.model.VisualMathType
import org.json.JSONArray
import org.json.JSONObject

class Converters {
  @TypeConverter
  fun fromVisualMathType(value: VisualMathType): String = value.name

  @TypeConverter
  fun toVisualMathType(value: String): VisualMathType = try {
    VisualMathType.valueOf(value)
  } catch (e: Exception) {
    VisualMathType.COUNTING_ITEMS
  }

  @TypeConverter
  fun fromStringList(value: List<String>): String = value.joinToString("|||")

  @TypeConverter
  fun toStringList(value: String): List<String> = if (value.isEmpty()) emptyList() else value.split("|||")

  @TypeConverter
  fun fromDailyProblems(problems: List<DailyProblem>): String {
    val array = JSONArray()
    for (p in problems) {
      val obj = JSONObject().apply {
        put("id", p.id)
        put("question", p.question)
        put("visualEmoji", p.visualEmoji)
        put("options", JSONArray(p.options))
        put("correctAnswer", p.correctAnswer)
        put("explanation", p.explanation)
        put("hint", p.hint)
      }
      array.put(obj)
    }
    return array.toString()
  }

  @TypeConverter
  fun toDailyProblems(jsonStr: String): List<DailyProblem> {
    if (jsonStr.isBlank()) return emptyList()
    return try {
      val array = JSONArray(jsonStr)
      val list = mutableListOf<DailyProblem>()
      for (i in 0 until array.length()) {
        val obj = array.getJSONObject(i)
        val optsArray = obj.optJSONArray("options")
        val options = mutableListOf<String>()
        if (optsArray != null) {
          for (j in 0 until optsArray.length()) {
            options.add(optsArray.getString(j))
          }
        }
        list.add(
          DailyProblem(
            id = obj.optString("id", "prob_$i"),
            question = obj.optString("question", ""),
            visualEmoji = obj.optString("visualEmoji", "🌟"),
            options = options,
            correctAnswer = obj.optString("correctAnswer", ""),
            explanation = obj.optString("explanation", ""),
            hint = obj.optString("hint", "")
          )
        )
      }
      list
    } catch (e: Exception) {
      emptyList()
    }
  }
}

