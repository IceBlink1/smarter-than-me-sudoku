package com.smarterthanmedigits.server.dto

import lombok.Data

@Data
data class ResetPasswordCodeRequestDto(val code: String)