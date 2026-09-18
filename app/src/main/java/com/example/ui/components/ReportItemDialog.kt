package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CampusBuilding
import com.example.data.model.ItemCategory
import com.example.data.model.ItemStatus
import com.example.data.model.ItemType
import com.example.data.model.LostFoundItem
import com.example.ui.theme.StatusFound
import com.example.ui.theme.StatusLost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportItemDialog(
  initialType: ItemType = ItemType.LOST,
  onDismiss: () -> Unit,
  onSubmit: (LostFoundItem) -> Unit
) {
  var itemType by remember { mutableStateOf(initialType) }
  var title by remember { mutableStateOf("") }
  var category by remember { mutableStateOf(ItemCategory.ELECTRONICS) }
  var building by remember { mutableStateOf(CampusBuilding.MAIN_LIBRARY) }
  var specificLocation by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var reporterName by remember { mutableStateOf("") }
  var reporterContact by remember { mutableStateOf("") }
  var rewardNote by remember { mutableStateOf("") }

  var titleError by remember { mutableStateOf(false) }
  var contactError by remember { mutableStateOf(false) }
  var buildingDropdownExpanded by remember { mutableStateOf(false) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .testTag("report_item_dialog"),
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
          Text(
            text = "Post Campus Report",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close form")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Type Segment Selection: Lost vs Found
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          FilterChip(
            selected = itemType == ItemType.LOST,
            onClick = { itemType = ItemType.LOST },
            label = {
              Text(
                "I Lost Something",
                fontWeight = if (itemType == ItemType.LOST) FontWeight.Bold else FontWeight.Normal
              )
            },
            modifier = Modifier
              .weight(1f)
              .testTag("type_lost_chip"),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = StatusLost.copy(alpha = 0.15f),
              selectedLabelColor = StatusLost
            )
          )

          FilterChip(
            selected = itemType == ItemType.FOUND,
            onClick = { itemType = ItemType.FOUND },
            label = {
              Text(
                "I Found Something",
                fontWeight = if (itemType == ItemType.FOUND) FontWeight.Bold else FontWeight.Normal
              )
            },
            modifier = Modifier
              .weight(1f)
              .testTag("type_found_chip"),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = StatusFound.copy(alpha = 0.15f),
              selectedLabelColor = StatusFound
            )
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Item Title
        OutlinedTextField(
          value = title,
          onValueChange = {
            title = it
            if (it.isNotBlank()) titleError = false
          },
          label = { Text("Item Name / Title *") },
          placeholder = { Text(if (itemType == ItemType.LOST) "e.g. Hydro Flask 32oz with stickers" else "e.g. Student ID Card found in Lounge") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("item_title_input"),
          isError = titleError,
          singleLine = true,
          keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        if (titleError) {
          Text(
            text = "Please enter an item name",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category Selection
        Text(
          text = "Category",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          ItemCategory.values().forEach { cat ->
            FilterChip(
              selected = category == cat,
              onClick = { category = cat },
              label = { Text(cat.displayName, fontSize = 12.sp) }
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Campus Building Dropdown
        ExposedDropdownMenuBox(
          expanded = buildingDropdownExpanded,
          onExpandedChange = { buildingDropdownExpanded = it },
          modifier = Modifier.fillMaxWidth()
        ) {
          OutlinedTextField(
            value = building.buildingName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Campus Building / Facility *") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = buildingDropdownExpanded) },
            modifier = Modifier
              .fillMaxWidth()
              .menuAnchor()
              .testTag("building_selector")
          )
          ExposedDropdownMenu(
            expanded = buildingDropdownExpanded,
            onDismissRequest = { buildingDropdownExpanded = false }
          ) {
            CampusBuilding.values().forEach { bld ->
              DropdownMenuItem(
                text = {
                  Column {
                    Text(bld.buildingName, fontWeight = FontWeight.Medium)
                    Text(bld.area, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                },
                onClick = {
                  building = bld
                  buildingDropdownExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Specific room / desk
        OutlinedTextField(
          value = specificLocation,
          onValueChange = { specificLocation = it },
          label = { Text("Specific Area or Room") },
          placeholder = { Text("e.g. 2nd Floor Study Cubicle #14, near elevator") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("specific_location_input"),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Detailed Description
        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Description & Identifiers *") },
          placeholder = { Text("Color, brand, contents, scratches, distinctive tags or stickers...") },
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .testTag("description_input"),
          maxLines = 4
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Optional Reward (for lost items)
        if (itemType == ItemType.LOST) {
          OutlinedTextField(
            value = rewardNote,
            onValueChange = { rewardNote = it },
            label = { Text("Reward Offered (Optional)") },
            placeholder = { Text("e.g. \$20 coffee treat / cash reward") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("reward_input"),
            singleLine = true
          )
          Spacer(modifier = Modifier.height(12.dp))
        }

        // Contact Section
        Text(
          text = "Your Contact Details (for campus members to reach you)",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = reporterName,
          onValueChange = { reporterName = it },
          label = { Text("Your Name or Nickname") },
          placeholder = { Text("e.g. Alex (Engineering student)") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("reporter_name_input"),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = reporterContact,
          onValueChange = {
            reporterContact = it
            if (it.isNotBlank()) contactError = false
          },
          label = { Text("Campus Email or Phone *") },
          placeholder = { Text("e.g. yourname@university.edu or 555-0123") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("reporter_contact_input"),
          isError = contactError,
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        if (contactError) {
          Text(
            text = "Please provide contact info so people can reach you",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("Cancel")
          }

          Button(
            onClick = {
              var hasError = false
              if (title.isBlank()) {
                titleError = true
                hasError = true
              }
              if (reporterContact.isBlank()) {
                contactError = true
                hasError = true
              }
              if (!hasError) {
                val newItem = LostFoundItem(
                  title = title.trim(),
                  description = if (description.isBlank()) "No additional description provided." else description.trim(),
                  type = itemType,
                  category = category,
                  building = building,
                  specificLocation = if (specificLocation.isBlank()) "On premises" else specificLocation.trim(),
                  status = ItemStatus.OPEN,
                  reporterName = if (reporterName.isBlank()) "Campus Member" else reporterName.trim(),
                  reporterContact = reporterContact.trim(),
                  rewardNote = rewardNote.trim().ifEmpty { null }
                )
                onSubmit(newItem)
                onDismiss()
              }
            },
            modifier = Modifier
              .weight(1.4f)
              .testTag("submit_report_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (itemType == ItemType.LOST) MaterialTheme.colorScheme.primary else StatusFound
            ),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("Publish Report", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
