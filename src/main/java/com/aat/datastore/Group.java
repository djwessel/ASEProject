package com.aat.datastore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import java.security.InvalidParameterException;

@Entity
public class Group {
  @Parent Key<Course> course;
  @Id private Long id;
  private String name;

  /**
   * Default Constructor
   */
  public Group() {
  }

  /**
   * Constructor with all relevant information
   */
  public Group(String courseId, String name) {
    if (courseId == null)
      throw new InvalidParameterException("Group's Parent courseId must not be null");
    else
      course = Key.create(Course.class, courseId);  // Creating the Ancestor key
    
    this.name = name;
  }

  /**
   * Getters and Setters
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  } 
}
