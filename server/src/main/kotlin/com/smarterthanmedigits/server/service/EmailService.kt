package com.smarterthanmedigits.server.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service

@Service("emailService")
interface EmailService {

    fun sendEmail(emailMsg: SimpleMailMessage)

}