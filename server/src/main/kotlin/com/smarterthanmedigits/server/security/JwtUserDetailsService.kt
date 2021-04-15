package com.smarterthanmedigits.server.security

import com.smarterthanmedigits.server.service.UserService
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Slf4j
@Service
class JwtUserDetailsService @Autowired constructor(@Autowired private val userService: UserService) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userService.findByUsername(username)
                ?: throw UsernameNotFoundException("User with username: $username not found")
        return JwtUserFactory.create(user)
    }
}