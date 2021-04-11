package com.smarterthanmedigits.server.service.impl

import com.smarterthanmedigits.server.model.Role
import com.smarterthanmedigits.server.model.Status
import com.smarterthanmedigits.server.model.User
import com.smarterthanmedigits.server.repository.RoleRepository
import com.smarterthanmedigits.server.repository.UserRepository
import com.smarterthanmedigits.server.service.UserService
import lombok.extern.slf4j.Slf4j
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.persistence.PersistenceException

@Service
@Slf4j
class UserServiceImpl(
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val passwordEncoder: BCryptPasswordEncoder
) : UserService {

    override fun register(user: User): User {
        val roleUser = roleRepository.findByName("ROLE_USER")
        val userRoles: MutableList<Role> = mutableListOf()
        userRoles.add(roleUser!!)
        val username = user.username
        user.password = passwordEncoder.encode(user.password)
        user.status = Status.ACTIVE
        user.roles = userRoles
        if (username == null || userRepository.findByUsername(username) != null) {
            throw PersistenceException()
        }
        val registeredUser = userRepository.save(user)
        return registeredUser
    }

    override fun getAll(): List<User?> {
        return userRepository.findAll()
    }

    override fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    override fun findById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    override fun delete(id: Long) {

    }

    override fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    override fun save(user: User) {
        userRepository.save(user)
    }

    override fun resetPassword(user: User, newPassword: String) {
        user.apply {
            password = passwordEncoder.encode(newPassword)
            resetCode = null
            save(this)
        }
    }

    override fun findUserByResetCode(code: String): User? {
        return userRepository.findByResetCode(code)
    }

    override fun deleteResetCode(user: User) {
        user.resetCode = null
        userRepository.save(user)
    }
}