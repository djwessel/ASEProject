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

  public User() {

  }

  /**
   * Constructor with all relevant information
   */
  public User(String email, String password) {
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
  public Long getId() { return id; }
}
