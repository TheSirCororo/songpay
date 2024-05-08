package ru.cororo.songpay.service

import jakarta.annotation.PostConstruct
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeBodyPart
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.MimeMultipart
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmailService(
    @Value("\${auth.email_address}")
    private val emailAddress: String,
    @Value("\${auth.email_password}")
    private val emailPassword: String,
    @Value("\${auth.email_port}")
    private val emailPort: Int,
    @Value("\${auth.email_host}")
    private val emailHost: String,
    @Value("\${auth.email_subject}")
    private val emailSubject: String,
    @Value("\${auth.email_text}")
    private val emailText: String,
    @Value("\${server.host}")
    private val serverHost: String
) {
    private lateinit var session: Session

    @PostConstruct
    fun init() {
        val properties = Properties()
        properties["mail.smtp.auth"] = true
        properties["mail.smtp.starttls.enable"] = true
        properties["mail.smtp.host"] = emailHost
        properties["mail.smtp.port"] = emailPort
        properties["mail.smtp.ssl.trust"] = emailHost
        session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(emailAddress, emailPassword)
            }
        })
    }

    fun sendConfirmationMessage(email: String, id: UUID) {
        val message = MimeMessage(session)
        message.setFrom(InternetAddress(emailAddress))
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
        message.subject = emailSubject

        val mimeBodyPart = MimeBodyPart()
        mimeBodyPart.setContent(emailText.format(serverHost, id.toString()), "text/html; charset=utf-8")

        val multipart = MimeMultipart()
        multipart.addBodyPart(mimeBodyPart)

        message.setContent(multipart)

        Transport.send(message)
    }
}