package com.aat.datastore;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class User {
  @Id private Long id;
  private String email;
  private String password;

  /**
   * Default Constructor
   */
  public User() {
  }

  /**
   * Constructor with all relevant information
   */
  public User(String email, String password) {
    this();
    this.email = email;
    this.password = password;
  }

  /**
   * Getters and Setters
   */
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }
}
