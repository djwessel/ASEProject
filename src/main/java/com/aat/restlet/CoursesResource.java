package com.aat.restlet;


import java.util.List;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import com.aat.datastore.Course;
import com.googlecode.objectify.ObjectifyService;

public class CoursesResource extends ServerResource{

	@Get
	public List<Course> retrieve(){
		List<Course> Courses =  ObjectifyService.ofy()
				.load()
				.type(Course.class)
				.list();
		return  Courses;
	}
		
	
}
