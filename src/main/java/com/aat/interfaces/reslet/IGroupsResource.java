	package com.aat.interfaces.reslet;

import java.util.List;

import org.restlet.resource.Get;
import com.aat.datastore.Group;

/**
 * Interface to expose groups by Course
 * */
public interface IGroupsResource {
	@Get
	public  List<Group> retrieve();
}
