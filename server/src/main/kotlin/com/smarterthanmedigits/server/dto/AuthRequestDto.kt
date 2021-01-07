package com.smarterthanmedigits.server.dto

import lombok.Data

@Data
class AuthRequestDto {
    var username: String? = null
    var password: String? = null
}