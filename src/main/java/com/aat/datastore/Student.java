package com.aat.datastore;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Subclass;
import com.googlecode.objectify.annotation.Index;

import java.util.List;
import java.util.ArrayList;

@Subclass(index=true)
public class Student extends User {
  @Index private ArrayList<Ref<AttendanceRecord>> groups;

  /**
   * Default Constructor
   */
  public Student() {
    super();
    groups = new ArrayList<Ref<AttendanceRecord>>();
  }

  /**
   * Getters and Setters
   */
  public List<Ref<AttendanceRecord>> getGroups() { return groups; }
}
