package com.example.islamicguidance.data.local

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String, // e.g. "ayah:1_1", "hadith:bukhari_1", "dua:dua_rabbana_1"
    val type: String, // "ayah", "hadith", "dua"
    val title: String,
    val subtitle: String,
    val targetId: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_history")
data class ReadingHistoryEntity(
    @PrimaryKey val id: Int = 1, // Singleton current reading position
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahName: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "worship_records")
data class WorshipRecordEntity(
    @PrimaryKey val dateKey: String, // e.g. "2026-10-27"
    val fajrDone: Boolean = false,
    val dhuhrDone: Boolean = false,
    val asrDone: Boolean = false,
    val maghribDone: Boolean = false,
    val ishaDone: Boolean = false,
    val quranRead: Boolean = false,
    val morningAzkarDone: Boolean = false,
    val eveningAzkarDone: Boolean = false,
    val istighfarDone: Boolean = false,
    val mulkDone: Boolean = false,
    val tasbihCount: Int = 0
)

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE id = :id)")
    fun isBookmarked(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: String)

    @Query("DELETE FROM bookmarks")
    suspend fun clearAllBookmarks()
}

@Dao
interface ReadingHistoryDao {
    @Query("SELECT * FROM reading_history WHERE id = 1 LIMIT 1")
    fun getLastReadPosition(): Flow<ReadingHistoryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReadingPosition(history: ReadingHistoryEntity)

    @Query("DELETE FROM reading_history")
    suspend fun clearHistory()
}

@Dao
interface WorshipRecordDao {
    @Query("SELECT * FROM worship_records WHERE dateKey = :dateKey LIMIT 1")
    fun getRecordForDate(dateKey: String): Flow<WorshipRecordEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(record: WorshipRecordEntity)

    @Query("DELETE FROM worship_records")
    suspend fun clearAllRecords()
}

@Database(
    entities = [BookmarkEntity::class, ReadingHistoryEntity::class, WorshipRecordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class IslamicGuidanceDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun readingHistoryDao(): ReadingHistoryDao
    abstract fun worshipRecordDao(): WorshipRecordDao

    companion object {
        @Volatile
        private var INSTANCE: IslamicGuidanceDatabase? = null

        fun getDatabase(context: Context): IslamicGuidanceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IslamicGuidanceDatabase::class.java,
                    "islamic_guidance_room.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
