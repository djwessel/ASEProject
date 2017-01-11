	package com.aat.interfaces.reslet;

import java.util.List;

import org.restlet.resource.Get;
import com.aat.datastore.Course;

/**
 * Interface to expose Courses
 * */
public interface ICoursesResource {
	@Get
	public  List<Course> retrieve();
}
