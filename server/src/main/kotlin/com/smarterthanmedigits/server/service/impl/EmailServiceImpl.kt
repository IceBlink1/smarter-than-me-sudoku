package com.smarterthanmedigits.server.service.impl

import com.smarterthanmedigits.server.service.EmailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service


@Service("emailService")
class EmailServiceImpl(
    @Autowired val mailSender: JavaMailSender
) : EmailService {

    @Async
    override fun sendEmail(emailMsg: SimpleMailMessage) {
        mailSender.send(emailMsg)
    }
}