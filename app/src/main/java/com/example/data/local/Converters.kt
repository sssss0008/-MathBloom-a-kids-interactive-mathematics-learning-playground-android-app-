package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.VisualMathType

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
}
