package com.example.cinesync.data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cinesync.domain.Entity.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Database(entities = [User::class], version = 2, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE user_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                username TEXT,
                password TEXT,
                movies TEXT
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO user_new (id, username, password, movies)
            SELECT id, username, password, movies FROM user
        """.trimIndent())

        db.execSQL("DROP TABLE user")

        db.execSQL("ALTER TABLE user_new RENAME TO user")
    }
}


class DataConverter {

    @TypeConverter
    fun fromMovieList(value: String?): List<Movie>? {
        if (value == null) {
            return null
        }
        val listType = object : TypeToken<List<Movie>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toMovieList(list: List<Movie>?): String? {
        if (list == null) {
            return null
        }
        return Gson().toJson(list)
    }
}

