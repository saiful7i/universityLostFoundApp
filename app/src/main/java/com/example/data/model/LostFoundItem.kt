package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ItemType(val label: String) {
  LOST("Lost"),
  FOUND("Found")
}

enum class ItemCategory(val displayName: String, val iconName: String) {
  ELECTRONICS("Electronics", "devices"),
  STUDENT_ID_CARDS("Student IDs & Cards", "badge"),
  KEYS_LOCKS("Keys & Badges", "key"),
  BAGS_BACKPACKS("Backpacks & Bags", "backpack"),
  BOOKS_NOTEBOOKS("Books & Notes", "menu_book"),
  CLOTHING_ACCESSORIES("Jackets & Apparel", "checkroom"),
  PERSONAL_ITEMS("Wallets & Glasses", "account_balance_wallet"),
  OTHER("Other Campus Items", "category")
}

enum class CampusBuilding(val buildingName: String, val area: String) {
  MAIN_LIBRARY("Main Library (William T. Young)", "North Quad"),
  STUDENT_UNION("Student Union & Hub", "Central Campus"),
  SCIENCE_CENTER("Science & Tech Complex", "East Campus"),
  ENGINEERING_HALL("Engineering & Computer Science", "South Quad"),
  CAMPUS_REC_CENTER("Alumni Recreation Center", "West Campus"),
  DINING_COMMONS("Central Dining Commons", "Central Campus"),
  RESIDENCE_HALLS("Campus Residence Halls", "North/South Dorms"),
  LECTURE_HALLS("Central Lecture Auditoriums", "Central Campus"),
  CAMPUS_SHUTTLE("Campus Blue/White Shuttle", "Transit"),
  OUTDOOR_CAMPUS("Campus Green & Walkways", "Outdoors")
}

enum class ItemStatus(val displayStatus: String) {
  OPEN("Active"),
  CLAIMED_RETURNED("Returned & Claimed")
}

@Entity(tableName = "lost_found_items")
data class LostFoundItem(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val title: String,
  val description: String,
  val type: ItemType,
  val category: ItemCategory,
  val building: CampusBuilding,
  val specificLocation: String,
  val status: ItemStatus = ItemStatus.OPEN,
  val reporterName: String,
  val reporterContact: String,
  val reportedTimestamp: Long = System.currentTimeMillis(),
  val rewardNote: String? = null
)
