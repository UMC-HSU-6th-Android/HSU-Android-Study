package com.example.floClone.core.data.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.floClone.core.data.model.local.entities.UserEntity

@Dao
interface UserDAO {

    @Insert
    fun insert(user: UserEntity)

    @Query("SELECT * FROM UserTable")
    fun getUser(): List<UserEntity>

    @Query("SELECT * FROM UserTable WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String): UserEntity?
}