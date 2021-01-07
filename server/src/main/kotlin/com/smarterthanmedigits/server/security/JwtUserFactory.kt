package com.smarterthanmedigits.server.security

import JwtUser
import com.smarterthanmedigits.server.model.Role
import com.smarterthanmedigits.server.model.Status
import com.smarterthanmedigits.server.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class JwtUserFactory() {

    companion object {
        fun create(user: User): JwtUser {
            return JwtUser(user.id,
                    user.username,
                    user.email,
                    user.password,
                    mapToGrantedAuthorities(user.roles),
                    user.status?.equals(Status.ACTIVE) ?: false,
                    user.updated)
        }

        private fun mapToGrantedAuthorities(userRoles: List<Role?>): List<GrantedAuthority> {
            return userRoles.mapNotNull {
                if (it?.name != null)
                    SimpleGrantedAuthority(it.name)
                else
                    null
            }
        }

    }

}