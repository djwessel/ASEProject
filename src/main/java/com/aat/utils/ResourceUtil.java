package com.aat.utils;

import org.restlet.data.Form;
import org.restlet.resource.ResourceException;

public class ResourceUtil {
	public static String getParam(Form params, String key, boolean required) {
		String val = params.getFirstValue(key, true, "");
		if (required && "".equals(val))
			throw new ResourceException(404, "Not found", "Missing required parameter: " + key, null);
		return val;
	}
}
