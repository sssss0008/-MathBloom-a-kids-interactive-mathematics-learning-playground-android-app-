package com.example.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.gemini.SearchGroundingResult
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathGreen
import com.example.ui.theme.MathOrange
import com.example.ui.theme.MathPurple

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RealWorldExplorerScreen(
  isSearching: Boolean,
  searchResult: SearchGroundingResult?,
  onExecuteSearch: (String) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header Hero
    item {
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Surface(
              shape = CircleShape,
              color = Color(0xFFE8F0FE),
              modifier = Modifier.size(36.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text("🔍", fontSize = 18.sp)
              }
            }
            Text(
              text = "Real-World Math & STEM",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFE8F0FE)
            ) {
              Text(
                text = "MODEL: gemini-3.5-flash",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1967D2),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontFamily = FontFamily.Monospace
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFE6F4EA)
            ) {
              Text(
                text = "TOOL: googleSearch",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF137333),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontFamily = FontFamily.Monospace
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Ask real-world math and science questions! MathBloom connects with Google Search Grounding to deliver live, verified scientific measurements, numbers, and facts.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
          )
        }
      }
    }

    // Search Query Bar
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Ask about speed, distance, planets, buildings...") },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = "Search", tint = MathBlue)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(12.dp))

          Button(
            onClick = {
              if (searchQuery.isNotBlank()) {
                onExecuteSearch(searchQuery)
              }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSearching && searchQuery.isNotBlank(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MathBlue)
          ) {
            if (isSearching) {
              CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text("Grounded Search with Google...", fontWeight = FontWeight.Bold)
            } else {
              Text("Explore with Google Search Grounding 🌐", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Suggested Questions
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "💡 Popular Grounded Inquiries",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Tap any topic to fetch real-world data grounded by Google Search:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
          )

          val suggestions = listOf(
            "🚀 How many miles to Mars and the Moon right now?",
            "🏔️ What is the height of Mount Everest in feet and meters?",
            "⚡ What is the top speed of the world's fastest bullet train?",
            "🪐 How many moons does Saturn have right now?",
            "🌊 What is the deepest ocean trench depth in feet?"
          )

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            suggestions.forEach { prompt ->
              Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF0F4F8),
                modifier = Modifier.clickable {
                  searchQuery = prompt.substringAfter(" ")
                  onExecuteSearch(searchQuery)
                }
              ) {
                Text(
                  text = prompt,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color(0xFF243B53),
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
              }
            }
          }
        }
      }
    }

    // Grounded Results Card
    if (searchResult != null) {
      item {
        Card(
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "🌐 Grounded Answer",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }

              Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE6F4EA)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "✓ Google Search Grounding",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF137333)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
              shape = RoundedCornerShape(14.dp),
              color = Color(0xFFF8FAFC),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = searchResult.answer.ifEmpty { "Grounded facts retrieved." },
                fontSize = 14.sp,
                color = Color(0xFF1E293B),
                modifier = Modifier.padding(16.dp),
                lineHeight = 22.sp
              )
            }

            // Sources
            if (searchResult.sources.isNotEmpty()) {
              Spacer(modifier = Modifier.height(14.dp))
              Text(
                text = "Grounding Web Citations & Sources:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color(0xFF475569)
              )
              Spacer(modifier = Modifier.height(6.dp))

              FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                searchResult.sources.forEach { source ->
                  Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFEFF6FF)
                  ) {
                    Text(
                      text = "🔗 ${source.title}",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = Color(0xFF1D4ED8),
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                  }
                }
              }
            }

            // Search queries executed
            if (searchResult.searchQueries.isNotEmpty()) {
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "Search Queries: " + searchResult.searchQueries.joinToString(", "),
                fontSize = 11.sp,
                color = Color(0xFF64748B),
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }
      }
    }
  }
}
