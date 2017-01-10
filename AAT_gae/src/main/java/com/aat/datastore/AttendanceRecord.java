package com.aat.datastore;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

import java.security.InvalidParameterException;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents AttendanceRecord. AttendanceRecords are children to a Group and contain attendance and presentation information for a given student.
 */
@Entity
public class AttendanceRecord {
	@Parent private Key<Group> group;
	@Id private Long id;

	private Ref<Student> student;
	private ArrayList<Date> attendanceList = new ArrayList<Date>();
	private ArrayList<Date> presentationList = new ArrayList<Date>();

	public AttendanceRecord() {

	}

	/**
	 * Constructor with all relevant information
	 */
	public AttendanceRecord(Long groupId, Long studentId) {
		if (groupId == null)
			throw new InvalidParameterException("AttendanceRecord's Parent groupId must not be null");
		else
			group = Key.create(Group.class, groupId);	// Creating the Parent key

		if (studentId == null)
			throw new InvalidParameterException("AttendanceRecord's studentId must not be null");
		else
			student = Ref.create(Key.create(Student.class, studentId));
	}

	/**
	 * Getters and Setters
	 */
	public List<Date> getAttendance() { return attendanceList; }
	public List<Date> getPresentation() { return presentationList; }
	public Student getStudent() { return student.get(); }
	public Long getId() { return id; }
	public Key<Group> getParent() { return group; }
}