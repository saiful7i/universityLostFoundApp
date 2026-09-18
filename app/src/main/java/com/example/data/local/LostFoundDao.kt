package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ItemStatus
import com.example.data.model.ItemType
import com.example.data.model.LostFoundItem
import kotlinx.coroutines.flow.Flow

@Dao
interface LostFoundDao {
  @Query("SELECT * FROM lost_found_items ORDER BY reportedTimestamp DESC")
  fun getAllItems(): Flow<List<LostFoundItem>>

  @Query("SELECT * FROM lost_found_items WHERE type = :type ORDER BY reportedTimestamp DESC")
  fun getItemsByType(type: ItemType): Flow<List<LostFoundItem>>

  @Query("SELECT * FROM lost_found_items WHERE id = :id LIMIT 1")
  fun getItemById(id: Long): Flow<LostFoundItem?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertItem(item: LostFoundItem): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(items: List<LostFoundItem>)

  @Update
  suspend fun updateItem(item: LostFoundItem)

  @Query("UPDATE lost_found_items SET status = :status WHERE id = :id")
  suspend fun updateStatus(id: Long, status: ItemStatus)

  @Delete
  suspend fun deleteItem(item: LostFoundItem)

  @Query("DELETE FROM lost_found_items WHERE id = :id")
  suspend fun deleteItemById(id: Long)

  @Query("SELECT COUNT(*) FROM lost_found_items")
  suspend fun getCount(): Int
}
