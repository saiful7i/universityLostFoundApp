package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.ItemStatus
import com.example.data.model.ItemType
import com.example.data.model.LostFoundItem
import com.example.ui.components.CampusDesksDialog
import com.example.ui.components.FilterSection
import com.example.ui.components.ItemCard
import com.example.ui.components.ItemDetailDialog
import com.example.ui.components.ReportItemDialog
import com.example.ui.theme.StatusFound
import com.example.ui.theme.StatusLost
import com.example.ui.viewmodel.LostFoundViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  viewModel: LostFoundViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  var selectedItemForDetail by remember { mutableStateOf<LostFoundItem?>(null) }
  var showReportDialog by remember { mutableStateOf(false) }
  var showCampusDesksDialog by remember { mutableStateOf(false) }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Campus Lost & Found",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "University Community Registry",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
          }
        },
        actions = {
          IconButton(
            onClick = { showCampusDesksDialog = true },
            modifier = Modifier.testTag("official_desks_button")
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = "Official Campus Desks",
              tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer,
          titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
      )
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = { showReportDialog = true },
        icon = { Icon(Icons.Default.Add, contentDescription = null) },
        text = { Text("Report Item", fontWeight = FontWeight.Bold) },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.testTag("report_item_fab")
      )
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      contentPadding = PaddingValues(bottom = 96.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Hero Card Header with Stats
      item {
        CampusHeroCard(
          totalCount = uiState.totalCount,
          lostCount = uiState.lostCount,
          foundCount = uiState.foundCount,
          claimedCount = uiState.claimedCount,
          onViewDesks = { showCampusDesksDialog = true }
        )
      }

      // Search Bar
      item {
        OutlinedTextField(
          value = uiState.searchQuery,
          onValueChange = { viewModel.onSearchQueryChanged(it) },
          placeholder = { Text("Search by item name, room, building, or reporter...") },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search items")
          },
          trailingIcon = {
            if (uiState.searchQuery.isNotEmpty()) {
              IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear search")
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(16.dp),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("search_bar")
        )
      }

      // Filter Chips Section
      item {
        FilterSection(
          selectedType = uiState.selectedType,
          selectedCategory = uiState.selectedCategory,
          selectedBuilding = uiState.selectedBuilding,
          selectedStatus = uiState.selectedStatus,
          onTypeSelected = { viewModel.setTypeFilter(it) },
          onCategorySelected = { viewModel.setCategoryFilter(it) },
          onBuildingSelected = { viewModel.setBuildingFilter(it) },
          onStatusSelected = { viewModel.setStatusFilter(it) },
          onClearFilters = { viewModel.clearAllFilters() }
        )
      }

      // Section Header with count
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = when {
              uiState.selectedType == ItemType.LOST -> "Lost Items Reported (${uiState.items.size})"
              uiState.selectedType == ItemType.FOUND -> "Found Items Turned In (${uiState.items.size})"
              uiState.selectedStatus == ItemStatus.CLAIMED_RETURNED -> "Resolved & Claimed (${uiState.items.size})"
              else -> "Recent Campus Reports (${uiState.items.size})"
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
        }
      }

      // Empty State or List of items
      if (uiState.items.isEmpty()) {
        item {
          EmptyStateView(
            hasFilter = uiState.searchQuery.isNotEmpty() || uiState.selectedType != null || uiState.selectedCategory != null,
            onClearFilter = { viewModel.clearAllFilters() },
            onReportClick = { showReportDialog = true }
          )
        }
      } else {
        items(
          items = uiState.items,
          key = { it.id }
        ) { item ->
          Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            ItemCard(
              item = item,
              onItemClick = { selectedItemForDetail = item },
              onToggleStatus = { viewModel.toggleItemClaimed(item) }
            )
          }
        }
      }
    }
  }

  // Item Detail Dialog
  selectedItemForDetail?.let { item ->
    ItemDetailDialog(
      item = item,
      onDismiss = { selectedItemForDetail = null },
      onToggleStatus = { viewModel.toggleItemClaimed(it) },
      onDeleteItem = { viewModel.deleteItem(it) }
    )
  }

  // Report Item Dialog
  if (showReportDialog) {
    ReportItemDialog(
      initialType = if (uiState.selectedType == ItemType.FOUND) ItemType.FOUND else ItemType.LOST,
      onDismiss = { showReportDialog = false },
      onSubmit = { viewModel.addItem(it) }
    )
  }

  // Campus Desks Dialog
  if (showCampusDesksDialog) {
    CampusDesksDialog(
      onDismiss = { showCampusDesksDialog = false }
    )
  }
}

@Composable
fun CampusHeroCard(
  totalCount: Int,
  lostCount: Int,
  foundCount: Int,
  claimedCount: Int,
  onViewDesks: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      // Banner Image with subtle gradient overlay
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.hero_campus_lostfound),
          contentDescription = "Campus Lost and Found desk banner",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Transparent,
                  Color.Black.copy(alpha = 0.65f)
                )
              )
            )
        )
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(14.dp)
        ) {
          Text(
            text = "Reconnecting Students with Belongings",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
          Text(
            text = "Report lost IDs, electronics, keys, & notes across campus",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 12.sp
          )
        }
      }

      // Campus Stats Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        StatBadge(label = "Lost", count = lostCount, color = StatusLost)
        StatBadge(label = "Found", count = foundCount, color = StatusFound)
        StatBadge(label = "Returned", count = claimedCount, color = MaterialTheme.colorScheme.primary)
        StatBadge(label = "Total Listed", count = totalCount, color = MaterialTheme.colorScheme.secondary)
      }
    }
  }
}

@Composable
fun StatBadge(label: String, count: Int, color: Color) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.padding(horizontal = 4.dp)
  ) {
    Text(
      text = count.toString(),
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.ExtraBold,
      color = color
    )
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Composable
fun EmptyStateView(
  hasFilter: Boolean,
  onClearFilter: () -> Unit,
  onReportClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(24.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(28.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = null,
        modifier = Modifier.size(48.dp),
        tint = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = if (hasFilter) "No matching campus items" else "No items reported yet",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = if (hasFilter) {
          "Try searching with different keywords or clearing your active filters."
        } else {
          "Be the first to report a lost or found item to help campus members."
        },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(16.dp))

      if (hasFilter) {
        Button(
          onClick = onClearFilter,
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("Reset Filters")
        }
      } else {
        Button(
          onClick = onReportClick,
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("Report an Item")
        }
      }
    }
  }
}
