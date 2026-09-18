package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.CampusBuilding
import com.example.data.model.ItemCategory
import com.example.data.model.ItemStatus
import com.example.data.model.ItemType

class Converters {
  @TypeConverter
  fun fromItemType(value: ItemType?): String? = value?.name

  @TypeConverter
  fun toItemType(value: String?): ItemType? = value?.let {
    try { ItemType.valueOf(it) } catch (e: Exception) { ItemType.LOST }
  }

  @TypeConverter
  fun fromItemCategory(value: ItemCategory?): String? = value?.name

  @TypeConverter
  fun toItemCategory(value: String?): ItemCategory? = value?.let {
    try { ItemCategory.valueOf(it) } catch (e: Exception) { ItemCategory.OTHER }
  }

  @TypeConverter
  fun fromCampusBuilding(value: CampusBuilding?): String? = value?.name

  @TypeConverter
  fun toCampusBuilding(value: String?): CampusBuilding? = value?.let {
    try { CampusBuilding.valueOf(it) } catch (e: Exception) { CampusBuilding.MAIN_LIBRARY }
  }

  @TypeConverter
  fun fromItemStatus(value: ItemStatus?): String? = value?.name

  @TypeConverter
  fun toItemStatus(value: String?): ItemStatus? = value?.let {
    try { ItemStatus.valueOf(it) } catch (e: Exception) { ItemStatus.OPEN }
  }
}
