package com.aat.restlet;

import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import com.aat.utils.Constants;

import java.util.List;



public class AATApplication extends Application {
	
	@Override
	public Restlet createInboundRoot() {
		// Change the Jackson converter to use the ObjectifyJacksonModule mapper
		List<ConverterHelper> converters = Engine.getInstance().getRegisteredConverters();
		JacksonConverter jc = null;
		for (ConverterHelper converter: converters) {
			if (converter instanceof JacksonConverter) {
				jc = (JacksonConverter) converter;
			}
		}
		if (jc != null) {
			converters.remove(jc);
			converters.add(new ObjectifyJacksonConverter());
		}

		Router router = new Router(getContext());
        
		router.attach("/course/{course_id}/group", GroupResource.class);
		router.attach("/course/{course_id}/group/{group_id}", GroupResource.class);
		router.attach("/course", CourseResource.class);
		router.attach("/course/{courseID}", CourseResource.class);
		router.attach("/course/{courseID}/groups", GroupsResource.class);
		router.attach("/course/{course_id}/group/{group_id}/attendance", AttendanceResource.class);
		router.attach("/course/{course_id}/group/{group_id}/attendance/{attendance_id}", AttendanceResource.class);
		router.attach("/courses", CoursesResource.class);
		router.attach("/user/login", UserLogin.class);
		router.attach("/user/{id}/logout", UserLogout.class);
		router.attach("/user", UserResource.class);
		router.attach("/user/{" + Constants.userId + "}", UserResource.class);

		return router;

	}

}
