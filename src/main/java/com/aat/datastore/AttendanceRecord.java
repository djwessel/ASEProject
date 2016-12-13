package com.aat.datastore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

import java.util.ArrayList;
import java.util.Date;

@Entity
public class AttendanceRecord {
  @Parent Key<Group> group;
  @Id public Long id;

  @Load private Ref<Student> student;
  private ArrayList<Date> attendanceList;
  private ArrayList<Date> presentationList;

  /**
   * Default Constructor
   */
  public AttendanceRecord() {
    attendanceList = new ArrayList<Date>();
    presentationList = new ArrayList<Date>();
  }

  /**
   * Constructor with all relevant information
   */
  public AttendanceRecord(String groupId, String studentId) {
    this();
    if (groupId == null)
      throw new InvalidParameterException("AttendanceRecord's Parent groupId must not be null");
    else
      group = Key.create(Group.class, groupId);  // Creating the Ancestor key

    if (studentId == null)
      throw new InvalidParameterException("AttendanceRecord's studentId must not be null");
    else
      user = Ref.create(studentId);
  }

  /**
   * Getters and Setters
   */
  public List<Date> getAttendance() {
    return attendanceList;
  }

  public List<Date> getPresentation() {
    return presentationList;
  }

  public Student getStudent() {
    return student.get();
  }
}
