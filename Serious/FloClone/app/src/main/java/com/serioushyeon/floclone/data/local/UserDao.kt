package com.serioushyeon.floclone.data.local

import androidx.room.*
import com.serioushyeon.floclone.data.entities.User


@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM UserTable")
    fun getUsers(): List<User>

    @Query("SELECT * FROM UserTable WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String): User?
}