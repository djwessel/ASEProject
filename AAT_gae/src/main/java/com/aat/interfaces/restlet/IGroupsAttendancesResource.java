package com.aat.interfaces.restlet;

import java.util.List;

import org.restlet.resource.Get;

import com.aat.pojos.CourseGroupPOJO;
	
public interface IGroupsAttendancesResource {
	
	@Get	
	public List <CourseGroupPOJO>retrieve();
}	
