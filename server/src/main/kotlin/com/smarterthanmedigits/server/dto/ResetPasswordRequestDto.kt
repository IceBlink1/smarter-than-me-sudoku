package com.smarterthanmedigits.server.dto

import lombok.Data

@Data
data class ResetPasswordRequestDto(val email: String)