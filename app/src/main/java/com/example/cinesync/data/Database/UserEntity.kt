package com.example.cinesync.data.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cinesync.domain.Entity.Movie

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val username:String?="",
    val password:String?="",
    val movies:List<Movie>?= emptyList<Movie>()
)
