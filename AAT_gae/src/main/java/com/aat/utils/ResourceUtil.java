package com.aat.utils;

import org.restlet.data.Form;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Resource;
import org.restlet.data.Cookie;
import org.restlet.Request;

import com.aat.datastore.User;
import com.googlecode.objectify.ObjectifyService;

public class ResourceUtil {
	public static String getParam(Form params, String key, boolean required) {
		String val = params.getFirstValue(key, true, "");
		if (required && "".equals(val))
			throw new ResourceException(404, "Not found", Constants.missingParam + key, null);
		return val;
	}
	
	public static void checkAttributeValue(String attrName, String value, boolean required) {
		if (!required) {
			return;
		}
		if (value == null) {
			throw new ResourceException(404, "Not found", Constants.missingAttribute + attrName, null);
		}
	}

	private static User getUserByToken(Resource r) {
		Cookie c = r.getRequest().getCookies().getFirst("sessionToken");
		if (c == null) {
			throw new ResourceException(401, "Not logged in", "You are not logged in", null);
		}
		String token = c.getValue();
		if (token == null) {
			throw new ResourceException(401, "Not logged in", "You are not logged in", null);
		}
		return ObjectifyService.ofy().load().type(User.class).filter("token", token).first().now();
	}

	public static void checkToken(Resource r, Long userId) {
		User check = getUserByToken(r);

		if (check == null || !check.getId().equals(userId)) {
			throw new ResourceException(403, "Incorrect Permissions", "You do not have access to given resource", null);
		}
	}

	public static void checkTokenPermissions(Resource r, Class userType) {
		User check = getUserByToken(r);

		if (check == null || !userType.isInstance(check)) {
			throw new ResourceException(403, "Incorrect Permissions", "You do not have access to given resource", null);
		}
	}

}
