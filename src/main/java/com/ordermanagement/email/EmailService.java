package com.ordermanagement.email;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    IEmailRepository iEmailRepository;
    @Autowired
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Transactional
    public Email sendEmail(EmailDTO emailDTO) {
        Email email = new Email();
        email.setSendDateEmail(LocalDateTime.now());
        email.setOwnerRef(emailDTO.getOwnerRef());
        email.setEmailFrom(emailDTO.getEmailFrom());
        email.setEmailTo(emailDTO.getEmailTo());
        email.setSubject(emailDTO.getSubject());
        email.setText(emailDTO.getText());
        BeanUtils.copyProperties(emailDTO, email);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(email.getEmailFrom());
            message.setTo(email.getEmailTo());
            message.setSubject(email.getSubject());
            message.setText(email.getText());
            javaMailSender.send(message);

            email.setEmailStatusEnum(EmailStatusEnum.SENT);
        } catch (MailException e) {
            email.setEmailStatusEnum(EmailStatusEnum.ERROR);
        } finally {
            return iEmailRepository.save(email);
        }
    }
}