package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.MathBlue
import com.example.ui.theme.MathCoral

@Composable
fun ParentGateDialog(
  parentPin: String = "1234",
  onAuthorized: () -> Unit,
  onDismiss: () -> Unit
) {
  // Adult math challenge: 8 * 7 = 56
  val num1 = 8
  val num2 = 7
  val expectedMath = (num1 * num2).toString()

  var inputAnswer by remember { mutableStateOf("") }
  var hasError by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(text = "🛡️", fontSize = 36.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Grown-Ups Area",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Please solve this problem or enter your 4-digit PIN to access Parent Settings.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MathBlue.copy(alpha = 0.1f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "Solve: $num1 × $num2 = ?",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MathBlue,
            modifier = Modifier.padding(14.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = inputAnswer,
          onValueChange = {
            inputAnswer = it
            hasError = false
          },
          label = { Text("Answer or PIN") },
          placeholder = { Text("e.g. 56 or 1234") },
          isError = hasError,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp)
        )

        if (hasError) {
          Text(
            text = "Incorrect answer. Please try again.",
            color = MathCoral,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          TextButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
          ) {
            Text(text = "Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
          }

          Button(
            onClick = {
              if (inputAnswer.trim() == expectedMath || inputAnswer.trim() == parentPin) {
                onAuthorized()
              } else {
                hasError = true
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MathBlue),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text(text = "Enter 🔓", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
