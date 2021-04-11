package com.smarterthanmedigits.server.dto

import lombok.Data

@Data
data class ResetPasswordNewPasswordRequestDto(val password: String)