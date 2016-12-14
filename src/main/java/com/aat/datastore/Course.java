package com.aat.datastore;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Represents a Course in the datastore. Courses are parents to Groups.
 */
@Entity
public class Course {
  @Id private String courseId;
  private String title;

  /**
   * Constructor with all relevant information
   */
  public Course(String courseId, String title) {
    this.courseId = courseId;
    this.title = title;
  }

  /**
   * Getters and Setters
   */
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getId() { return courseId; }
}
