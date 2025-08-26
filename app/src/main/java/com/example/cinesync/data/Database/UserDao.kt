package com.example.cinesync.data.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cinesync.domain.Entity.Movie

@Dao
interface UserDao{

    @Query("Select * from user where username = :username")
    suspend fun getUser(username: String): User?

    @Update
    suspend fun updateUser(user: User)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user:User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserList(users: List<User>): List<Long>

    @Query("Select * from user where username = :username and password = :password")
    suspend fun verifyLogin(username:String, password:String): User?

}