package com.smarterthanmedigits.server.dto

import lombok.Data


@Data
data class RegisterRequestDto(val username: String, val password: String, val email: String)