package com.smarterthanmedigits.server.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.smarterthanmedigits.server.config.SecurityConfig
import com.smarterthanmedigits.server.dto.AuthRequestDto
import com.smarterthanmedigits.server.dto.RegisterRequestDto
import com.smarterthanmedigits.server.model.User
import com.smarterthanmedigits.server.security.JwtTokenProvider
import com.smarterthanmedigits.server.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.naming.AuthenticationException
import javax.persistence.PersistenceException

@RestController
@RequestMapping(value = arrayOf("/api/v1/auth/"))
@Import(SecurityConfig::class)
class AuthenticationRestControllerV1
@Autowired constructor(
        @Autowired val authenticationManager: AuthenticationManager,
        @Autowired val jwtTokenProvider: JwtTokenProvider,
        @Autowired val userService: UserService
) {
    @PostMapping("login")
    fun login(@RequestBody requestDto: AuthRequestDto): ResponseEntity<Any?> {
        try {
            val username = requestDto.username
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, requestDto.password))
            val user = username?.let { userService.findByUsername(it) }
                    ?: throw UsernameNotFoundException("User with username $username not found")
            val token = jwtTokenProvider.createToken(username, user.roles)
            val response = mutableMapOf<Any?, Any?>()
            response.put("username", username)
            response.put("token", token)
            return ResponseEntity.ok(response)
        } catch (e: AuthenticationException) {
            throw (BadCredentialsException("Invalid username or password"))
        }
    }

    @PostMapping("register")
    fun register(@RequestBody request: RegisterRequestDto) : ResponseEntity<String> {
        val user = User().apply {
            username = request.username
            password = request.password
            email = request.email
            created = Date()
            updated = Date()
        }

        return try {
            userService.register(user)
            ResponseEntity(HttpStatus.CREATED)
        } catch (pe: PersistenceException) {
            if(user.username == null) {
                ResponseEntity(HttpStatus.BAD_REQUEST)
            } else {
                ResponseEntity(HttpStatus.CONFLICT)
            }
        }
    }
}