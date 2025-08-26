package com.example.cinesync.data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.cinesync.domain.Entity.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Database(entities = [User::class], version = 2, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
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

