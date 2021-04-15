package com.smarterthanmedigits.server.repository

import com.smarterthanmedigits.server.model.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role?, Long?> {
    fun findByName(name: String): Role?
}