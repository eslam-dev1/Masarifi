package com.example.masarifi.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val amount: Double,
    val category: String,
    /** ISO date: yyyy-MM-dd */
    val date: String,
    val note: String
)

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC, id DESC")
    suspend fun getAll(): List<ExpenseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Database(entities = [ExpenseEntity::class], version = 1, exportSchema = false)
abstract class MasarifiDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: MasarifiDatabase? = null

        fun getInstance(context: Context): MasarifiDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MasarifiDatabase::class.java,
                    "masarifi.db"
                ).build().also { INSTANCE = it }
            }
    }
}
