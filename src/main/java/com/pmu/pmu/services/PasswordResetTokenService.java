package com.pmu.pmu.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.pmu.pmu.emailservice.EmailSenderService;
import com.pmu.pmu.models.PasswordResetToken;

@Service
public class PasswordResetTokenService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void createPasswordResetToken(String email, String token, LocalDateTime expiryDateTime) {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        System.out.println(Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        resetToken.setExpiryDate(Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        mongoTemplate.save(resetToken);
    }

    public PasswordResetToken findByToken(String token) {
        Query query = new Query(Criteria.where("token").is(token));
        return mongoTemplate.findOne(query, PasswordResetToken.class);
    }

    public void deleteToken(String token) {
        Query query = new Query(Criteria.where("token").is(token));
        mongoTemplate.remove(query, PasswordResetToken.class);
    }

    public void deleteExpiredTokens() {
        Query query = new Query(Criteria.where("expiryDate").lt(new Date()));
        mongoTemplate.remove(query, PasswordResetToken.class);
    }
    
    
 
    
    
}