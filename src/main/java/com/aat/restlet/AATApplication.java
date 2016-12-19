package com.aat.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;


public class AATApplication extends Application {
	
	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
        
		router.attach("/course/{course_id}/group/{group_name}", GroupResource.class);//add a group to a course
		router.attach("/course", CourseResource.class);//view all the courses
		router.attach("/course/{courseID}/groups", GroupsResource.class);//view all groups in a course
		router.attach("/course?name={courseName}&attend={attendNum}&present={presentNum}",CourseResource.class);//add a course		
		
		router.attach("/user", UserCreateResource.class);
		
        return router;
	}

}
