package com.btrsystem.btrsystem.services;

import java.util.Collection;
import java.util.Collections;

import java.util.Objects;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.btrsystem.btrsystem.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	 private String username;
	  
	  private String email;
	  
	  @JsonIgnore
	  private String password;
	  
	  
	  private String role;
	  
	
	
	

	public UserDetailsImpl(int id, String username, String email, String password, String role) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	public static UserDetailsImpl build(User users) {
	  	
	    return new UserDetailsImpl(
	    		users.getId(), 
	    		users.getUsername(),
	    		users.getEmail(),
	    		users.getPassword(),
	    		users.getRole());
  }
	
	

	@Override
	public String toString() {
		return "UserDetailsImpl [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", role=" + role + "]";
	}

	@Override
	  public Collection<? extends GrantedAuthority> getAuthorities() {
		  return Collections.singleton(new SimpleGrantedAuthority(role));
	  }


	public int getId() {
		return id;
	}
	
	@Override
	  public String getUsername() {
	    return username;
	  }
	  
	  @Override
	  public String getPassword() {
	    return password;
	  }
	  
	  
	  public String getEmail() {
		  return email;
	  }
	  
	  @Override
	  public boolean isAccountNonExpired() {
	    return true;
	  }
	
	  @Override
	  public boolean isAccountNonLocked() {
	    return true;
	  }
	
	  @Override
	  public boolean isCredentialsNonExpired() {
	    return true;
	  }
	
	  @Override
	  public boolean isEnabled() {
	    return true;
	  }
	
	  @Override
	  public boolean equals(Object o) {
	    if (this == o)
	      return true;
	    if (o == null || getClass() != o.getClass())
	      return false;
	    UserDetailsImpl user = (UserDetailsImpl) o;
	    return Objects.equals(id, user.id);
	  }
	

}
