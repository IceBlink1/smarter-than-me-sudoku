package com.smarterthanmedigits.server.security

import com.smarterthanmedigits.server.model.Role
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Consumer
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider {
    @Value("\${jwt.token.secret}")
    private var secret: String? = null

    @Value("\${jwt.token.expired}")
    private val validityInMilliseconds: Long = 0

    @Qualifier("jwtUserDetailsService")
    @Autowired
    private val userDetailsService: UserDetailsService? = null
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @PostConstruct
    protected fun init() {
        secret = Base64.getEncoder().encodeToString(secret!!.toByteArray())
    }

    fun createToken(username: String?, roles: List<Role>): String {
        val claims = Jwts.claims().setSubject(username)
        claims["roles"] = getRoleNames(roles)
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact()
    }

    fun getAuthentication(token: String?): Authentication {
        val userDetails = userDetailsService!!.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUsername(token: String?): String {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body.subject
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            bearerToken.substring(7, bearerToken.length)
        } else null
    }

    fun validateToken(token: String?): Boolean {
        return try {
            val claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (e: JwtException) {
            throw JwtAuthenticationException("JWT token is expired or invalid", null)
        } catch (e: IllegalArgumentException) {
            throw JwtAuthenticationException("JWT token is expired or invalid", null)
        }
    }

    private fun getRoleNames(userRoles: List<Role>): List<String?> {
        val result: MutableList<String?> = ArrayList()
        userRoles.forEach(Consumer { role: Role -> result.add(role.name) })
        return result
    }
}