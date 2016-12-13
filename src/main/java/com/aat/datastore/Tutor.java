package com.aat.datastore;

import com.googlecode.objectify.annotation.Subclass;

@Subclass(index=true)
public class Tutor {
  private String pin;

  /**
   * Getters and Setters
   */
  public String getPin() { return pin; }
  public void setPin(String pin) {this.pin = pin; }
}
