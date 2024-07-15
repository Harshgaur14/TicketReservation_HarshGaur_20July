package com.pmu.pmu.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    private String id;

    private String email;
    private String token;
    private Date expiryDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public PasswordResetToken(String id, String email, String token, Date expiryDate) {
		super();
		this.id = id;
		this.email = email;
		this.token = token;
		this.expiryDate = expiryDate;
	}
	public PasswordResetToken() {
		// TODO Auto-generated constructor stub
	}

    // Constructors, getters, setters
}