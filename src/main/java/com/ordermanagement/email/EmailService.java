package com.ordermanagement.email;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {

    private final IEmailRepository iEmailRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(IEmailRepository iEmailRepository, JavaMailSender javaMailSender) {
        this.iEmailRepository = iEmailRepository;
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

            email.setEmailStatus(EmailStatus.SENT);
        } catch (MailException e) {
            email.setEmailStatus(EmailStatus.ERROR);
        } finally {
            return iEmailRepository.save(email);
        }
    }

    public List<Email> getAllEmails() {
        return  iEmailRepository.findAll();
    }

    public Optional<Email> getEmailById(UUID emailId) {
        return iEmailRepository.findById(emailId);
    }
}