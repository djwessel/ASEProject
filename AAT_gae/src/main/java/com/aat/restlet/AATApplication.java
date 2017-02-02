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
		// Following the Customizing the JacksonConverter of Reslet blog
		// http://restlet.com/blog/2016/03/23/customizing-the-jackson-converter-of-restlet-framework/
		List<ConverterHelper> converters = Engine.getInstance().getRegisteredConverters();
		JacksonConverter jc = null;
		for (ConverterHelper converter: converters) {
			if (converter instanceof JacksonConverter) {
				jc = (JacksonConverter) converter;
				break;
			}
		}
		if (jc != null) {
			converters.remove(jc);
			converters.add(new ObjectifyJacksonConverter());
		}

		// Set up router
		Router router = new Router(getContext());
        
		router.attach("/course/{" + Constants.courseId + "}/group", GroupResource.class);
		router.attach("/course/{" + Constants.courseId + "}/group/{" + Constants.groupId + "}", GroupResource.class);
		router.attach("/course", CourseResource.class);
		router.attach("/course/{" + Constants.courseId + "}", CourseResource.class);
		router.attach("/course/{" + Constants.courseId + "}/report", ReportResource.class);
		router.attach("/course/{" + Constants.courseId + "}/groups", GroupsResource.class);
		router.attach("/course/{" + Constants.courseId + "}/group/{" + Constants.groupId + "}/attendance", AttendanceResource.class);
		router.attach("/course/{" + Constants.courseId + "}/group/{" + Constants.groupId + "}/attendance/{" + Constants.attendanceId + "}", AttendanceResource.class);
		router.attach("/courses", CoursesResource.class);
		router.attach("/user", UserResource.class);
		router.attach("/user/login", UserLogin.class);
		router.attach("/user/{" + Constants.userId + "}", UserResource.class);
		router.attach("/user/{" + Constants.userId + "}/logout", UserLogout.class);
		router.attach("/user/{" + Constants.userId + "}/attendances", GroupsAttendancesResource.class);
		router.attach("/user/{" + Constants.userId + "}/course/{"+Constants.courseId +"}/group/{" + Constants.groupId + "}", QRCodeResource.class);
		//Endpoint for update AttendanceRecord
		router.attach("/user/{" + Constants.userId + "}/course/{"+Constants.courseId +"}/group/{" + Constants.groupId + "}/attendance", AttendanceResource.class);
		
		return router;

	}

}
