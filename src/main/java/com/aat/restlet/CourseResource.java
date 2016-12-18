package com.aat.restlet;

import org.restlet.data.Form;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.aat.datastore.Course;
import com.googlecode.objectify.ObjectifyService;

public class CourseResource extends ServerResource{
		
   @Get
   public String retrieve(){
	   String courseId = getAttribute("courseID");
	   Course course = retrieveCourse(courseId);
	   
	   return course.getTitle();
   }
   
   @Post
   public void create(){
	   Form queryParams = getQuery();
	  		
	   String courseTitle = queryParams.getFirstValue("courseName");
	   int reqAtten = Integer.parseInt(queryParams.getFirstValue("attendNum"));
	   int reqPresent = Integer.parseInt(queryParams.getFirstValue("presentNum"));
	   
	   Course course = new Course(courseTitle,reqAtten,reqPresent);	
	   ObjectifyService.ofy().save().entity(course).now();
	}
   
   @Put
   public void update(){
	   String courseId = getAttribute("courseID");
	   Course course = retrieveCourse(courseId);
	   ObjectifyService.ofy().delete().entity(course);
	}
   
  
   
   @Delete
   public void remove(){
	   String courseId = getAttribute("courseID");
	   Course course = retrieveCourse(courseId);
	   ObjectifyService.ofy().delete().entity(course);
	}
   
   /**
    * @param String courseId
    * @return Course course
    * Retrieves a Course by Id.
    * */
   private Course retrieveCourse (String courseId){
	   Course course = ObjectifyService.ofy()
			   .load()
			   .type(Course.class)
			   .id(Long.parseLong(courseId)).now();
	   	   
	   return course;
   } 
}
