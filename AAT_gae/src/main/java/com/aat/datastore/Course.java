package com.aat.datastore;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Represents a Course in the datastore. Courses are parents to Groups.
 */
@Entity
@Cache
public class Course {
	@Id private Long id;
	@Index private String title;
	private int reqAtten;
	private int reqPresent;

	public Course() {

	}

	/**
	 * Constructor with all relevant information
	 */
	public Course(String title, int reqAtten, int reqPresent) {
		this.title = title;
		this.reqAtten = reqAtten;
		this.reqPresent = reqPresent;
	}

	/**
	 * Getters and Setters
	 */
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public int getReqAtten() { return reqAtten; }
	public void setReqAtten(int reqAtten) { this.reqAtten = reqAtten; }
	public int getReqPresent() { return reqPresent; }
	public void setReqPresent(int reqPresent) { this.reqPresent = reqPresent; }
	public Long getId() { return id; }
}
