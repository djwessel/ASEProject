package com.aat.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;


public class AATApplication extends Application {
	
	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());

		router.attach("/course/{course_id}/group/{group_name}", GroupResource.class);
		router.attach("/courses", CoursesResource.class);
		router.attach("/course/{courseID}/groups", GroupsResource.class);
		router.attach("/course",CourseResource.class);		
		router.attach("/user/{email}/id/{id}/password/{password}",UserLogin.class);
		router.attach("/user", UserCreateResource.class);
		
        return router;
	}

}
