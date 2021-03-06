package com.aat.datastore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import java.security.InvalidParameterException;

/**
 * Represents a Group. Groups are children to Courses and parents to AttendanceRecords.
 */
@Entity
@Cache
public class Group {
	@Parent Key<Course> course;
	@Id private Long id;
	@Index private String name;

	public Group() {

	}

	/**
	 * Constructor with all relevant information
	 */
	public Group(Long courseId, String name) {
		if (courseId == null)
			throw new InvalidParameterException("Group's Parent courseId must not be null");
		else
			course = Key.create(Course.class, courseId);	// Creating the Parent key
		this.name = name;
	}

	/**
	 * Getters and Setters
	 */
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public Long getId() { return id; }
	@JsonIgnore
	public Key<Course> getParent(){return course;}

	@Override
	public String toString() {
		return name;
	}
}
