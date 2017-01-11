package com.aat.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;


public class AATApplication extends Application {
	
	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
        
		router.attach("/course/{course_id}/group", GroupResource.class);
		router.attach("/course/{course_id}/group/{group_id}", GroupResource.class);
		router.attach("/user", UserCreateResource.class);
		router.attach("/course", CourseResource.class);
		router.attach("/course/{course_id}", CourseResource.class);
		router.attach("/course/{course_id}/groups", GroupsResource.class);
		router.attach("/course/{course_id}/group/{group_id}/attendance", AttendanceResource.class);
		router.attach("/course/{course_id}/group/{group_id}/attendance/{attendance_id}", AttendanceResource.class);
		router.attach("/user/{id}",UserLogin.class);
		router.attach("/user/{user_id}/group/{group_id}",QRCodeResource.class);
		router.attach("/courses", CoursesResource.class);
		router.attach("/user/login", UserLogin.class);
		router.attach("/user/{id}/logout", UserLogout.class);


		return router;

	}

}
