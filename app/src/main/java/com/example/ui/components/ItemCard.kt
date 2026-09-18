package com.example.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ItemStatus
import com.example.data.model.ItemType
import com.example.data.model.LostFoundItem
import com.example.ui.theme.StatusFound
import com.example.ui.theme.StatusLost
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ItemCard(
  item: LostFoundItem,
  onItemClick: () -> Unit,
  onToggleStatus: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isResolved = item.status == ItemStatus.CLAIMED_RETURNED
  val badgeColor = when {
    isResolved -> MaterialTheme.colorScheme.outline
    item.type == ItemType.LOST -> StatusLost
    else -> StatusFound
  }

  val badgeText = when {
    isResolved -> "CLAIMED / RETURNED"
    item.type == ItemType.LOST -> "LOST ITEM"
    else -> "FOUND ITEM"
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("item_card_${item.id}")
      .clickable(onClick = onItemClick),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isResolved) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
      } else {
        MaterialTheme.colorScheme.surface
      }
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = if (isResolved) 0.dp else 2.dp
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Header: Type Badge, Category, and Timestamp
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            color = badgeColor.copy(alpha = 0.12f),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = badgeText,
              color = badgeColor,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Text(
            text = item.category.displayName,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = "Reported time",
            modifier = Modifier.size(12.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = formatRelativeTime(item.reportedTimestamp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Item Title
      Text(
        text = item.title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Description
      Text(
        text = item.description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      // Reward banner if available
      if (!item.rewardNote.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
          color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = "★ ${item.rewardNote}",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Location row
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = "Location",
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "${item.building.buildingName} • ${item.specificLocation}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Medium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Action row: Contact / Details button & Status toggle
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        TextButton(
          onClick = onItemClick,
          modifier = Modifier.testTag("view_details_${item.id}")
        ) {
          Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Details & Contact",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
          )
        }

        OutlinedButton(
          onClick = onToggleStatus,
          modifier = Modifier.testTag("toggle_status_${item.id}"),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(
            imageVector = if (isResolved) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = if (isResolved) StatusFound else MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (isResolved) "Resolved" else "Mark Claimed",
            style = MaterialTheme.typography.labelMedium
          )
        }
      }
    }
  }
}

private fun formatRelativeTime(timestamp: Long): String {
  val diff = System.currentTimeMillis() - timestamp
  val minute = 60_000L
  val hour = 60 * minute
  val day = 24 * hour

  return when {
    diff < 5 * minute -> "Just now"
    diff < hour -> "${diff / minute}m ago"
    diff < day -> "${diff / hour}h ago"
    diff < 7 * day -> "${diff / day}d ago"
    else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timestamp))
  }
}
