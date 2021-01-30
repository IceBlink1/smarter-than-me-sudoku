package com.smarterthanmedigits.server.repository

import com.smarterthanmedigits.server.model.Role
import com.smarterthanmedigits.server.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User?, Long?> {
    fun findByUsername(name: String): User?
}