package com.aat.datastore;

import com.googlecode.objectify.annotation.Subclass;

/**
 * Represents a Tutor User. Extends the User class. Tutor users have a secret pin.
 */
@Subclass(index=true)
public class Tutor extends User {
	private String pin;

	public Tutor() {
		super();
	}

	/**
	 * Constructor with all relevant information
	 */
	public Tutor(String email, String password, String first, String last, String pin) {
		super(email, password, first, last);
		this.pin = pin;
	}

	/**
	 * Getters and Setters
	 */
	public String getPin() { return pin; }
	public void setPin(String pin) {this.pin = pin; }
}
