package com.ordermanagement.email;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping("/sendmail")
    public ResponseEntity<Email> sendEmail(@RequestBody @Valid EmailDTO emailDTO) {
        Email sendEmail = emailService.sendEmail(emailDTO);
        return new ResponseEntity<>(sendEmail, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Email>> getAllEmails(){
        List<Email> emails = emailService.getAllEmails();
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }
    @GetMapping("/{emailId}")
    public ResponseEntity<Email> getEmailById(@PathVariable UUID emailId) {
        Optional<Email> email = emailService.getEmailById(emailId);
        return email.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}