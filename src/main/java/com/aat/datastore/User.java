package com.aat.datastore;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Represents a User. Users have an email and a password. Known subclasses are Students and Tutors.
 */
@Entity
public abstract class User {
	@Id private Long id;
	private String email;
	private String password;
	private String first;
	private String last;

	public User() {

	}

	/**
	 * Constructor with all relevant information
	 */
	public User(String email, String password, String first, String last) {
		this.email = email;
		this.password = password;
		this.first = first;
		this.last = last;
	}

	/**
	 * Getters and Setters
	 */
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getFirstName() { return first; }
	public void setFirstName(String first) { this.first = first; }
	public String getLastName() { return last; }
	public void setLastName(String last) { this.last = last; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public Long getId() { return id; }
}
