package com.example.ui.visualmath

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MathGreen

@Composable
fun MoneyShopRegister(
  targetPrice: Int = 7,
  onPaidTotalChanged: (Int) -> Unit = {}
) {
  var currentTotal by remember { mutableIntStateOf(0) }

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp),
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 3.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(text = "Price to pay:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(text = "$$targetPrice", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MathGreen)
        }
        Column(horizontalAlignment = Alignment.End) {
          Text(text = "Your Tray:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(
            text = "$$currentTotal",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (currentTotal == targetPrice) MathGreen else Color(0xFF3D5AFE)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "Tap money to place in the register tray:",
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Dollar Bills
      Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        BillItem(amount = 1) {
          currentTotal += 1
          onPaidTotalChanged(currentTotal)
        }
        BillItem(amount = 5) {
          currentTotal += 5
          onPaidTotalChanged(currentTotal)
        }
        BillItem(amount = 10) {
          currentTotal += 10
          onPaidTotalChanged(currentTotal)
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      ElevatedButton(
        onClick = {
          currentTotal = 0
          onPaidTotalChanged(0)
        },
        colors = ButtonDefaults.elevatedButtonColors(
          containerColor = Color(0xFFECEFF1)
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.height(36.dp)
      ) {
        Text(text = "Clear Tray 🔄", fontSize = 12.sp, color = Color(0xFF455A64))
      }
    }
  }
}

@Composable
private fun BillItem(amount: Int, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .size(width = 90.dp, height = 50.dp)
      .clip(RoundedCornerShape(8.dp))
      .background(Color(0xFFE8F5E9))
      .clickable(onClick = onClick),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(text = "💵 $$amount", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32), fontSize = 15.sp)
      Text(text = "+$$amount", fontSize = 10.sp, color = Color(0xFF388E3C))
    }
  }
}
