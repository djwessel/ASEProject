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
		router.attach("/course/{course_id}/groups", GroupsResource.class);
		router.attach("/user", UserCreateResource.class);

		return router;
	}

}
