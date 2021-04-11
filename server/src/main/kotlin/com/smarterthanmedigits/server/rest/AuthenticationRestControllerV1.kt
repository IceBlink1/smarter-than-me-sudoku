package com.smarterthanmedigits.server.rest

import com.smarterthanmedigits.server.config.SecurityConfig
import com.smarterthanmedigits.server.dto.AuthRequestDto
import com.smarterthanmedigits.server.dto.RegisterRequestDto
import com.smarterthanmedigits.server.dto.ResetPasswordRequestDto
import com.smarterthanmedigits.server.model.User
import com.smarterthanmedigits.server.security.JwtTokenProvider
import com.smarterthanmedigits.server.security.JwtUser
import com.smarterthanmedigits.server.service.EmailService
import com.smarterthanmedigits.server.service.UserService
import org.apache.tomcat.util.compat.TLS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.SimpleMailMessage
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
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
    @Autowired val userService: UserService,
    @Autowired val emailService: EmailService
) {
    @PostMapping("unlogged/login")
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

    @PostMapping("unlogged/register")
    fun register(@RequestBody request: RegisterRequestDto): ResponseEntity<Any?> {
        val user = User().apply {
            username = request.username
            password = request.password
            email = request.email
            created = Date()
            updated = Date()
        }

        return try {
            userService.register(user)
            val response = mutableMapOf<Any?, Any?>()
            response.put("username", request.username)
            val token = jwtTokenProvider.createToken(request.username, user.roles)
            response.put("token", token)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (pe: PersistenceException) {
            if (user.username == null) {
                ResponseEntity(HttpStatus.BAD_REQUEST)
            } else {
                ResponseEntity(HttpStatus.CONFLICT)
            }
        }
    }

    @PostMapping("refresh_token")
    fun refreshToken(): ResponseEntity<Any?> {
        val user = getUser() ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        val token = jwtTokenProvider.createToken(user.username, user.roles)
        return ResponseEntity.ok(mutableMapOf<Any, Any?>("token" to token, "username" to user.username))
    }

    @PostMapping("unlogged/reset_password")
    fun resetPassword(@RequestBody body: ResetPasswordRequestDto): ResponseEntity<Any?> {
        val user = userService.findByEmail(body.email) ?: return ResponseEntity.notFound().build()
        user.resetCode = generateResetCode()
        userService.save(user)
        emailService.sendEmail(
            SimpleMailMessage().apply {
                setFrom("smarterthanmedigits@yandex.ru")
                setTo(user.email)
                setSubject("Восстановление пароля")
                setText(
                    "Ваш код восстановления для приложения \"Умнее меня - цифры\": ${user.resetCode}\n" +
                            "---------------------------------------------------------\n" +
                            "С уважением,\n" +
                            "Команда разработчиков приложения \"Умнее меня - цифры\""
                )

            }
        )
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("unlogged/reset_password_code")
    fun resetPasswordCode(@RequestBody code: String): ResponseEntity<Any?> {
        val user = userService.findUserByResetCode(code)
        return if (user == null) {
            ResponseEntity.notFound().build()
        } else {
            val token = jwtTokenProvider.createToken(user.username, user.roles)
            userService.deleteResetCode(user)
            ResponseEntity.ok(mutableMapOf<Any, Any?>("token" to token, "username" to user.username))
        }
    }

    @PostMapping("reset_password")
    fun resetPasswordNewPassword(@RequestBody newPassword: String): ResponseEntity<Any?> {
        val user = getUser()
        return if (user != null) {
            userService.resetPassword(user, newPassword)
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    private fun getUser(): User? {
        return (SecurityContextHolder.getContext().authentication.principal as JwtUser).username?.let {
            userService.findByUsername(it)
        }
    }

    private fun generateResetCode(): String {
        val sb = StringBuilder()
        repeat((0..6).count()) { sb.append(rand.nextInt(10)) }
        return sb.substring(0)
    }

    companion object {
        val rand = Random()
    }
}