package com.smarterthanmedigits.server.service.impl

import com.smarterthanmedigits.server.service.EmailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*


@Service("emailService")
class EmailServiceImpl(
    val mailSender: JavaMailSender = getJavaMailSender()
) : EmailService {

    @Async
    override fun sendEmail(emailMsg: SimpleMailMessage) {
        mailSender.send(emailMsg)
    }

    companion object {
        @JvmStatic
        fun getJavaMailSender(): JavaMailSender {
            val mailSender = JavaMailSenderImpl()
            mailSender.host = "smtp.yandex.ru"
            mailSender.port = 465

            mailSender.username = "smarterthanmedigits@yandex.ru"
            mailSender.password = ""

            val props: Properties = mailSender.javaMailProperties
            props["mail.transport.protocol"] = "smtp"
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.starttls.enable"] = "true"
            props["mail.debug"] = "true"
            props["mail.smtp.ssl.enable"] = "true"
            props["mail.smtp.ssl.checkserveridentity"] = "true";
            props["mail.smtp.ssl.trust"] = "*";

            return mailSender
        }
    }
}