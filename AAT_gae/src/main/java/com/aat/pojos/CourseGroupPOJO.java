package com.aat.pojos;

import com.aat.datastore.Group;

public class CourseGroupPOJO {
	
	private long courseId;
	private String courseName;
	private Group group;
	
	public CourseGroupPOJO(long courseId, String courseName, Group group){
		this.courseId = courseId;
		this.courseName = courseName;
		this.group = group;
	}
	
	public long getCourseId() {
		return courseId;
	}
	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
}
