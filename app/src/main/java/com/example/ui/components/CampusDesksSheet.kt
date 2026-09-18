package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

data class CampusDeskInfo(
  val name: String,
  val location: String,
  val hours: String,
  val phone: String,
  val note: String
)

val campusDesks = listOf(
  CampusDeskInfo(
    name = "Campus Public Safety & Police",
    location = "Public Safety Building, Room 101",
    hours = "Open 24 Hours / 7 Days",
    phone = "555-0100",
    note = "Primary repository for high-value items (laptops, wallets, cash, jewelry)."
  ),
  CampusDeskInfo(
    name = "Student Union Central Info Desk",
    location = "Student Union, 1st Floor Concourse",
    hours = "Mon - Fri: 8:00 AM - 9:00 PM • Sat - Sun: 10:00 AM - 6:00 PM",
    phone = "555-0144",
    note = "Receives items found in dining, bookstore, lounges, and event spaces."
  ),
  CampusDeskInfo(
    name = "Main Library Circulation Desk",
    location = "William T. Young Library, 1st Floor",
    hours = "Mon - Thu: 7:30 AM - Midnight • Fri: 7:30 AM - 8:00 PM",
    phone = "555-0188",
    note = "Items found across library floors, group study rooms, and computer labs."
  ),
  CampusDeskInfo(
    name = "Alumni Recreation Member Services",
    location = "Rec Center Lobby Front Desk",
    hours = "Mon - Sun: 6:00 AM - 11:00 PM",
    phone = "555-0125",
    note = "Gym gear, water bottles, lockers, fitness trackers, and locker rooms."
  )
)

@Composable
fun CampusDesksDialog(
  onDismiss: () -> Unit
) {
  val context = LocalContext.current

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .testTag("campus_desks_dialog"),
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Official Campus Desks",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Found an item or looking for high-value belongings? You can also turn in or check items at these campus locations:",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Desk list
        campusDesks.forEach { desk ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            )
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = desk.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "📍 ${desk.location}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
              )
              Text(
                text = "🕒 ${desk.hours}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = desk.note,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Spacer(modifier = Modifier.height(10.dp))

              OutlinedButton(
                onClick = {
                  val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${desk.phone}"))
                  try {
                    context.startActivity(dialIntent)
                  } catch (e: Exception) {
                    // Fallback
                  }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
              ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Call Desk (${desk.phone})", fontSize = 12.sp)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Student ID Tip
        Surface(
          color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSecondaryContainer,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Lost your Student ID / Meal Card?",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Immediately log into your University Student Portal to deactivate building card access and hold your meal plan debit.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
              )
            }
          }
        }
      }
    }
  }
}
