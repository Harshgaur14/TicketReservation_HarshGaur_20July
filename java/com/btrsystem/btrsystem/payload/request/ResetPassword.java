package com.btrsystem.btrsystem.payload.request;

import jakarta.validation.constraints.NotBlank;

public class ResetPassword {
	@NotBlank
	private String tokenurl;

	@NotBlank
	private String password;

	

	public String getTokenurl() {
		return tokenurl;
	}

	public void setTokenurl(String tokenurl) {
		this.tokenurl = tokenurl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}
