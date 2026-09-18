package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CampusBuilding
import com.example.data.model.ItemCategory
import com.example.data.model.ItemStatus
import com.example.data.model.ItemType
import com.example.data.model.LostFoundItem
import com.example.data.repository.LostFoundRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FilterCriteria(
  val query: String = "",
  val type: ItemType? = null,
  val category: ItemCategory? = null,
  val building: CampusBuilding? = null,
  val status: ItemStatus? = null
)

data class LostFoundUiState(
  val items: List<LostFoundItem> = emptyList(),
  val searchQuery: String = "",
  val selectedType: ItemType? = null,
  val selectedCategory: ItemCategory? = null,
  val selectedBuilding: CampusBuilding? = null,
  val selectedStatus: ItemStatus? = null,
  val totalCount: Int = 0,
  val lostCount: Int = 0,
  val foundCount: Int = 0,
  val claimedCount: Int = 0
)

class LostFoundViewModel(
  application: Application,
  private val repository: LostFoundRepository
) : AndroidViewModel(application) {

  private val _filterCriteria = MutableStateFlow(FilterCriteria())

  val uiState: StateFlow<LostFoundUiState> = combine(
    repository.allItems,
    _filterCriteria
  ) { allItems: List<LostFoundItem>, filter: FilterCriteria ->
    val totalCount = allItems.size
    val lostCount = allItems.count { it.type == ItemType.LOST && it.status == ItemStatus.OPEN }
    val foundCount = allItems.count { it.type == ItemType.FOUND && it.status == ItemStatus.OPEN }
    val claimedCount = allItems.count { it.status == ItemStatus.CLAIMED_RETURNED }

    val filteredList = allItems.filter { item ->
      val matchesQuery = if (filter.query.isBlank()) {
        true
      } else {
        val q = filter.query.trim().lowercase()
        item.title.lowercase().contains(q) ||
          item.description.lowercase().contains(q) ||
          item.specificLocation.lowercase().contains(q) ||
          item.building.buildingName.lowercase().contains(q) ||
          item.reporterName.lowercase().contains(q) ||
          item.category.displayName.lowercase().contains(q)
      }

      val matchesType = filter.type == null || item.type == filter.type
      val matchesCategory = filter.category == null || item.category == filter.category
      val matchesBuilding = filter.building == null || item.building == filter.building
      val matchesStatus = filter.status == null || item.status == filter.status

      matchesQuery && matchesType && matchesCategory && matchesBuilding && matchesStatus
    }

    LostFoundUiState(
      items = filteredList,
      searchQuery = filter.query,
      selectedType = filter.type,
      selectedCategory = filter.category,
      selectedBuilding = filter.building,
      selectedStatus = filter.status,
      totalCount = totalCount,
      lostCount = lostCount,
      foundCount = foundCount,
      claimedCount = claimedCount
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = LostFoundUiState()
  )

  fun onSearchQueryChanged(query: String) {
    _filterCriteria.update { it.copy(query = query) }
  }

  fun setTypeFilter(type: ItemType?) {
    _filterCriteria.update { it.copy(type = type) }
  }

  fun setCategoryFilter(category: ItemCategory?) {
    _filterCriteria.update { it.copy(category = category) }
  }

  fun setBuildingFilter(building: CampusBuilding?) {
    _filterCriteria.update { it.copy(building = building) }
  }

  fun setStatusFilter(status: ItemStatus?) {
    _filterCriteria.update { it.copy(status = status) }
  }

  fun clearAllFilters() {
    _filterCriteria.value = FilterCriteria()
  }

  fun addItem(item: LostFoundItem) {
    viewModelScope.launch {
      repository.insertItem(item)
    }
  }

  fun toggleItemClaimed(item: LostFoundItem) {
    viewModelScope.launch {
      val newStatus = if (item.status == ItemStatus.CLAIMED_RETURNED) {
        ItemStatus.OPEN
      } else {
        ItemStatus.CLAIMED_RETURNED
      }
      repository.updateStatus(item.id, newStatus)
    }
  }

  fun deleteItem(id: Long) {
    viewModelScope.launch {
      repository.deleteItemById(id)
    }
  }

  companion object {
    fun provideFactory(application: Application): ViewModelProvider.Factory =
      object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          val appScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
          val database = AppDatabase.getDatabase(application, appScope)
          val repository = LostFoundRepository(database.lostFoundDao())
          return LostFoundViewModel(application, repository) as T
        }
      }
  }
}
