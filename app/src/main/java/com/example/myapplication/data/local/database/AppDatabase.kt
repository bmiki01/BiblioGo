package  com.example.myapplication.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.local.dao.BookDao
import com.example.myapplication.data.local.entities.BookEntity


@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}