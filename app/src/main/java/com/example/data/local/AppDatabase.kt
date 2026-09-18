package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CampusBuilding
import com.example.data.model.ItemCategory
import com.example.data.model.ItemStatus
import com.example.data.model.ItemType
import com.example.data.model.LostFoundItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [LostFoundItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun lostFoundDao(): LostFoundDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "campus_lost_found_database"
        )
          .addCallback(DatabaseCallback(scope))
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateInitialCampusData(database.lostFoundDao())
          }
        }
      }
    }

    private suspend fun populateInitialCampusData(dao: LostFoundDao) {
      val now = System.currentTimeMillis()
      val hour = 3600_000L

      val sampleItems = listOf(
        LostFoundItem(
          title = "Black North Face Backpack",
          description = "Left in Lecture Hall 101 after CS301 mid-term. Contains spiral notebooks, TI-84 calculator, and blue water bottle.",
          type = ItemType.LOST,
          category = ItemCategory.BAGS_BACKPACKS,
          building = CampusBuilding.LECTURE_HALLS,
          specificLocation = "Lecture Hall 101, Row E seat 14",
          status = ItemStatus.OPEN,
          reporterName = "Marcus Vance",
          reporterContact = "mvance@campus.edu • (555) 342-8911",
          reportedTimestamp = now - 2 * hour,
          rewardNote = "Reward offered: \$25 or free lunch!"
        ),
        LostFoundItem(
          title = "University Student ID Card (Sophia Miller)",
          description = "Found on the floor near the self-checkout kiosk in the campus bookstore. Has student number ending in 8419.",
          type = ItemType.FOUND,
          category = ItemCategory.STUDENT_ID_CARDS,
          building = CampusBuilding.STUDENT_UNION,
          specificLocation = "Student Union 1st floor bookstore lobby",
          status = ItemStatus.OPEN,
          reporterName = "Student Desk Assistant",
          reporterContact = "union-info@campus.edu • Ext 4400",
          reportedTimestamp = now - 4 * hour
        ),
        LostFoundItem(
          title = "AirPods Pro (2nd Gen) with Orange Case",
          description = "White earbuds in neon orange silicone case. Connected to 'Jordan's AirPods'. Missing since Wednesday evening.",
          type = ItemType.LOST,
          category = ItemCategory.ELECTRONICS,
          building = CampusBuilding.MAIN_LIBRARY,
          specificLocation = "3rd Floor Quiet Study Pod #12",
          status = ItemStatus.OPEN,
          reporterName = "Jordan Reed",
          reporterContact = "jreed@campus.edu • (555) 901-4420",
          reportedTimestamp = now - 8 * hour,
          rewardNote = "\$30 coffee card reward"
        ),
        LostFoundItem(
          title = "Keyring with Dorm FOB & Brass Key",
          description = "Found on a wooden bench outside the science quad. Has a red lanyard saying 'State Owls Pride'. Turned in to safety desk.",
          type = ItemType.FOUND,
          category = ItemCategory.KEYS_LOCKS,
          building = CampusBuilding.SCIENCE_CENTER,
          specificLocation = "Courtyard bench near Science Center atrium",
          status = ItemStatus.OPEN,
          reporterName = "Prof. Martinez",
          reporterContact = "martinez@campus.edu",
          reportedTimestamp = now - 14 * hour
        ),
        LostFoundItem(
          title = "MacBook Pro 67W USB-C Charger & Cable",
          description = "Left plugged into wall outlet near the treadmill area. Has gray tape marked 'A.T.' on the brick.",
          type = ItemType.FOUND,
          category = ItemCategory.ELECTRONICS,
          building = CampusBuilding.CAMPUS_REC_CENTER,
          specificLocation = "2nd Floor Cardio Deck, outlet 4B",
          status = ItemStatus.OPEN,
          reporterName = "Rec Center Staff (Kevin)",
          reporterContact = "recreation@campus.edu • Ext 2150",
          reportedTimestamp = now - 22 * hour
        ),
        LostFoundItem(
          title = "Navy Blue Columbia Fleece Jacket (Size M)",
          description = "Forgotten on the back of chair during dinner rush. Left pocket has student bus pass.",
          type = ItemType.FOUND,
          category = ItemCategory.CLOTHING_ACCESSORIES,
          building = CampusBuilding.DINING_COMMONS,
          specificLocation = "Central Dining Hall booth 18",
          status = ItemStatus.CLAIMED_RETURNED,
          reporterName = "Dining Services Lost & Found",
          reporterContact = "dining-lostfound@campus.edu",
          reportedTimestamp = now - 48 * hour
        )
      )
      dao.insertAll(sampleItems)
    }
  }
}
