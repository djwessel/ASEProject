package com.aat.restlet;

import org.restlet.data.Form;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.restlet.resource.ResourceException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import com.aat.datastore.Course;
import com.aat.datastore.Tutor;
import com.aat.utils.ResourceUtil;
import com.googlecode.objectify.ObjectifyService;

public class CourseResource extends ServerResource{
		
   @Get
   public Course retrieve(){
	   String courseId = getAttribute("course_id");
	   Course course = retrieveCourse(courseId);
	   return course;
   }
   
   @Post
   public Representation create(Representation entity){
		// Check if of type Tutor
		ResourceUtil.checkTokenPermissions(this, Tutor.class);
	   Form params = new Form(entity);
	  		
	   String courseTitle =ResourceUtil.getParam(params,"title",true);
	   int reqAtten = Integer.parseInt(ResourceUtil.getParam(params,"reqAtten",true));
	   int reqPresent = Integer.parseInt(ResourceUtil.getParam(params,"reqPresent",true));
	   
	   Course course = new Course(courseTitle,reqAtten,reqPresent);	
	   ObjectifyService.ofy().save().entity(course).now();
	   
	   return new StringRepresentation(course.getId().toString());
	}
   
   @Put
   public void update(){
		// Check if of type Tutor
		ResourceUtil.checkTokenPermissions(this, Tutor.class);
	   String courseId = getAttribute("course_id");
	   Course course = retrieveCourse(courseId);
	   
	   Form params = getQuery();
	   String courseTitle =ResourceUtil.getParam(params,"title",false);
	   String reqAttendance = ResourceUtil.getParam(params,"reqAtten",false);
	   
	   int reqAtten;
	   if (reqAttendance.length()>0){
		    reqAtten = Integer.parseInt(reqAttendance);
		    course.setReqAtten(reqAtten);
	   }
	   int reqPresent;
	   String reqPresentations= ResourceUtil.getParam(params,"reqPresent",false); 
	   if (reqPresentations.length()>0){
		   reqPresent = Integer.parseInt(reqPresentations);
		   course.setReqPresent(reqPresent);
	   }
	   course.setTitle(courseTitle);
	   ObjectifyService.ofy().save().entity(course);
	}
    
   @Delete
   public void remove(){
		// Check if of type Tutor
		ResourceUtil.checkTokenPermissions(this, Tutor.class);
	   String courseId = getAttribute("course_id");
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

	   if (course == null)
		throw new ResourceException(404, "Not found", "Cannot find course with id: " + courseId, null);
	   return course;
   } 
}
