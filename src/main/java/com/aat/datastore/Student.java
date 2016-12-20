package com.aat.datastore;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;
import com.googlecode.objectify.annotation.Index;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a Student User. Extends the User class. Students have a list 
 * of AttendanceRecords, one for each Tutorial Group they have signed up for.
 */
@Subclass(index=true)
public class Student extends User {
	@Index private List<Ref<AttendanceRecord>> groups = new ArrayList<Ref<AttendanceRecord>>();

	public Student() {
		super();
	}

	/**
	 * Constructor with all relevant information
	 */
	public Student(String email, String password, String first, String last) {
		super(email, password, first, last);
	}

	/**
	 * Getters and Setters
	 */
	public List<Ref<AttendanceRecord>> getGroups() { return groups; }
	public void addGroup(Long attendanceRecordId) { groups.add(Ref.create(Key.create(AttendanceRecord.class, attendanceRecordId))); }
}
