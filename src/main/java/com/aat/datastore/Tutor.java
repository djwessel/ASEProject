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
  public Tutor(String email, String password) {
    super(email, password);
  }

  /**
   * Getters and Setters
   */
  public String getPin() { return pin; }
  public void setPin(String pin) {this.pin = pin; }
}
