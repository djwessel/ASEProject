package com.aat.restlet;

import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import com.aat.restlet.GroupsResource;
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
        
		router.attach("/course/{" + Constants.courseId + "}/group", GroupResource.class);
		router.attach("/course/{" + Constants.courseId + "}/group/{" + Constants.groupId + "}", GroupResource.class);
		router.attach("/course", CourseResource.class);
		router.attach("/course/{" + Constants.courseId + "}", CourseResource.class);
		router.attach("/course/{" + Constants.courseId + "}/groups", GroupsResource.class);
		router.attach("/course/{" + Constants.courseId + "}/group/{" + Constants.groupId + "}/attendance", AttendanceResource.class);
		router.attach("/course/{" + Constants.courseId + "}/group/{" + Constants.groupId + "}/attendance/{" + Constants.attendanceId + "}", AttendanceResource.class);
		router.attach("/courses", CoursesResource.class);
		router.attach("/user", UserResource.class);
		router.attach("/user/login", UserLogin.class);
		router.attach("/user/{" + Constants.userId + "}", UserResource.class);
		router.attach("/user/{" + Constants.userId + "}/logout", UserLogout.class);
		router.attach("/user/{" + Constants.userId + "}/attendances", GroupsAttendancesResource.class);
		router.attach("/user/{" + Constants.userId + "}/group/{" + Constants.groupId + "}", QRCodeResource.class);

		return router;

	}

}
