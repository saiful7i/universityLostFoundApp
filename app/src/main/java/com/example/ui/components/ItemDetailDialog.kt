package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import com.example.data.model.ItemStatus
import com.example.data.model.ItemType
import com.example.data.model.LostFoundItem
import com.example.ui.theme.StatusFound
import com.example.ui.theme.StatusLost
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ItemDetailDialog(
  item: LostFoundItem,
  onDismiss: () -> Unit,
  onToggleStatus: (LostFoundItem) -> Unit,
  onDeleteItem: (Long) -> Unit
) {
  val context = LocalContext.current
  val isResolved = item.status == ItemStatus.CLAIMED_RETURNED
  val badgeColor = when {
    isResolved -> MaterialTheme.colorScheme.outline
    item.type == ItemType.LOST -> StatusLost
    else -> StatusFound
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .testTag("item_detail_dialog"),
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
        // Header with Close icon
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            color = badgeColor.copy(alpha = 0.12f),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = if (isResolved) "CLAIMED & RETURNED" else "${item.type.name} ITEM",
              color = badgeColor,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_detail_button")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close details")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Title
        Text(
          text = item.title,
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Time and Category
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Schedule,
              contentDescription = null,
              modifier = Modifier.size(14.dp),
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = SimpleDateFormat("EEE, MMM d, yyyy • h:mm a", Locale.getDefault())
                .format(Date(item.reportedTimestamp)),
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Location Box
        Surface(
          color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
          ) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = "Location",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = item.building.buildingName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Text(
                text = item.specificLocation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
              )
              Text(
                text = "Campus Area: ${item.building.area}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description Section
        Text(
          text = "Description & Distinctive Marks",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = item.description,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 22.sp
        )

        // Reward if available
        if (!item.rewardNote.isNullOrBlank()) {
          Spacer(modifier = Modifier.height(12.dp))
          Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "★",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = item.rewardNote,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // Reporter / Contact Section
        Text(
          text = if (item.type == ItemType.LOST) "Reported By (Owner)" else "Turned in / Reported By (Finder)",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(40.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
              )
            }
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = item.reporterName,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = item.reporterContact,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          // Copy button
          IconButton(
            onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("Reporter Contact", "${item.reporterName}: ${item.reporterContact}")
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "Contact copied to clipboard", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.testTag("copy_contact_button")
          ) {
            Icon(Icons.Default.ContentCopy, contentDescription = "Copy contact info", modifier = Modifier.size(18.dp))
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Actions to contact
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = {
              val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(extractEmail(item.reporterContact)))
                putExtra(Intent.EXTRA_SUBJECT, "Regarding Campus Lost & Found: ${item.title}")
              }
              try {
                context.startActivity(Intent.createChooser(emailIntent, "Send Email via"))
              } catch (e: Exception) {
                Toast.makeText(context, "No email client installed", Toast.LENGTH_SHORT).show()
              }
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Email", fontSize = 13.sp)
          }

          OutlinedButton(
            onClick = {
              val phone = extractPhone(item.reporterContact)
              if (phone.isNotBlank()) {
                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                try {
                  context.startActivity(dialIntent)
                } catch (e: Exception) {
                  Toast.makeText(context, "Cannot open dialer", Toast.LENGTH_SHORT).show()
                }
              } else {
                Toast.makeText(context, "No phone number specified", Toast.LENGTH_SHORT).show()
              }
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Call", fontSize = 13.sp)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // Status action button: Mark claimed or reopen
        Button(
          onClick = {
            onToggleStatus(item)
            onDismiss()
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("resolve_item_button"),
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isResolved) MaterialTheme.colorScheme.primary else StatusFound
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isResolved) "Reopen as Active Report" else "Mark as Claimed / Returned",
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Delete item option
        OutlinedButton(
          onClick = {
            onDeleteItem(item.id)
            onDismiss()
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("delete_item_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
          Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Delete This Listing")
        }
      }
    }
  }
}

private fun extractEmail(text: String): String {
  val emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
  return emailRegex.find(text)?.value ?: ""
}

private fun extractPhone(text: String): String {
  val phoneRegex = "(\\+?[0-9\\-\\s()]{7,})".toRegex()
  return phoneRegex.find(text)?.value?.replace(Regex("[^0-9+]"), "") ?: ""
}
