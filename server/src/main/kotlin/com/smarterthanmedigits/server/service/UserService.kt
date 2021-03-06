package com.smarterthanmedigits.server.service

import com.smarterthanmedigits.server.model.User

interface UserService {
    fun register(user: User): User

    fun getAll(): List<User?>

    fun findByUsername(username: String): User?

    fun findByEmail(email: String) : User?

    fun findById(id: Long): User?

    fun delete(id: Long)

    fun save(user: User)

    fun resetPassword(user: User, newPassword: String)

    fun findUserByResetCode(code: String) : User?

    fun deleteResetCode(user: User)
}