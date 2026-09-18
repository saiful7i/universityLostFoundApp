package com.example.data.repository

import com.example.data.local.LostFoundDao
import com.example.data.model.ItemStatus
import com.example.data.model.ItemType
import com.example.data.model.LostFoundItem
import kotlinx.coroutines.flow.Flow

class LostFoundRepository(private val dao: LostFoundDao) {

  val allItems: Flow<List<LostFoundItem>> = dao.getAllItems()

  fun getItemsByType(type: ItemType): Flow<List<LostFoundItem>> = dao.getItemsByType(type)

  fun getItemById(id: Long): Flow<LostFoundItem?> = dao.getItemById(id)

  suspend fun insertItem(item: LostFoundItem): Long = dao.insertItem(item)

  suspend fun updateStatus(id: Long, status: ItemStatus) = dao.updateStatus(id, status)

  suspend fun updateItem(item: LostFoundItem) = dao.updateItem(item)

  suspend fun deleteItem(item: LostFoundItem) = dao.deleteItem(item)

  suspend fun deleteItemById(id: Long) = dao.deleteItemById(id)
}
