package com.aat.interfaces.restlet;

import java.util.HashMap;
import java.util.List;

import org.restlet.resource.Get;

import com.aat.datastore.Group;

public interface IGroupsAttendancesResource {
	
	@Get	
	public HashMap <String,Group>retrieve();
}	
