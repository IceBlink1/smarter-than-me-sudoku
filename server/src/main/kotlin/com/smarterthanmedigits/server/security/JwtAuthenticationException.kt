package com.smarterthanmedigits.server.security

import org.springframework.security.core.AuthenticationException

class JwtAuthenticationException(msg: String? = null, throwable: Throwable? = null) :
    AuthenticationException(msg, throwable) {
}