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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CampusBuilding
import com.example.data.model.ItemCategory
import com.example.data.model.ItemStatus
import com.example.data.model.ItemType

@Composable
fun FilterSection(
  selectedType: ItemType?,
  selectedCategory: ItemCategory?,
  selectedBuilding: CampusBuilding?,
  selectedStatus: ItemStatus?,
  onTypeSelected: (ItemType?) -> Unit,
  onCategorySelected: (ItemCategory?) -> Unit,
  onBuildingSelected: (CampusBuilding?) -> Unit,
  onStatusSelected: (ItemStatus?) -> Unit,
  onClearFilters: () -> Unit,
  modifier: Modifier = Modifier
) {
  val hasActiveFilters = selectedType != null || selectedCategory != null || selectedBuilding != null || selectedStatus != null

  Column(modifier = modifier.fillMaxWidth()) {
    // Top Row: Type Pills (All, Lost, Found, Claimed) + Clear Filter
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (hasActiveFilters) {
        AssistChip(
          onClick = onClearFilters,
          label = { Text("Reset Filters", fontSize = 12.sp) },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Clear,
              contentDescription = "Reset all filters",
              modifier = Modifier.size(16.dp)
            )
          }
        )
      }

      // Type Pills
      FilterChip(
        selected = selectedType == null,
        onClick = { onTypeSelected(null) },
        label = { Text("All Reports") }
      )

      FilterChip(
        selected = selectedType == ItemType.LOST,
        onClick = { onTypeSelected(if (selectedType == ItemType.LOST) null else ItemType.LOST) },
        label = { Text("Lost by Students") },
        leadingIcon = if (selectedType == ItemType.LOST) {
          { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
        } else null
      )

      FilterChip(
        selected = selectedType == ItemType.FOUND,
        onClick = { onTypeSelected(if (selectedType == ItemType.FOUND) null else ItemType.FOUND) },
        label = { Text("Found Items") },
        leadingIcon = if (selectedType == ItemType.FOUND) {
          { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(16.dp)) }
        } else null
      )

      FilterChip(
        selected = selectedStatus == ItemStatus.CLAIMED_RETURNED,
        onClick = {
          onStatusSelected(if (selectedStatus == ItemStatus.CLAIMED_RETURNED) null else ItemStatus.CLAIMED_RETURNED)
        },
        label = { Text("Resolved & Claimed") }
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Second Row: Categories
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Category:",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(end = 4.dp)
      )

      ItemCategory.values().forEach { category ->
        val isSelected = selectedCategory == category
        FilterChip(
          selected = isSelected,
          onClick = { onCategorySelected(if (isSelected) null else category) },
          label = { Text(category.displayName, fontSize = 12.sp) }
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Third Row: Campus Buildings
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Location:",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(end = 4.dp)
      )

      CampusBuilding.values().take(6).forEach { building ->
        val isSelected = selectedBuilding == building
        FilterChip(
          selected = isSelected,
          onClick = { onBuildingSelected(if (isSelected) null else building) },
          label = { Text(building.buildingName.split("(").first().trim(), fontSize = 12.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
          )
        )
      }
    }
  }
}
