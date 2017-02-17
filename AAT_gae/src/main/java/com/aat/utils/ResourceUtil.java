package com.aat.utils;

import org.restlet.data.Form;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Resource;
import org.restlet.data.Cookie;
import org.restlet.Request;

import com.aat.datastore.User;
import com.googlecode.objectify.ObjectifyService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.util.Date;

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
		User user = ObjectifyService.ofy().load().type(User.class).filter("token", token).first().now();
		if (user == null || user.getTimeout().before(new Date())) {
			throw new ResourceException(401, "Not logged in", "You are not logged in", null);
		}
		return user;
	}

	public static User checkToken(Resource r, Long userId) {
		User check = getUserByToken(r);

		if (!check.getId().equals(userId)) {
			throw new ResourceException(403, "Incorrect Permissions", "You do not have access to given resource", null);
		}
		// TODO: update timeout on cookie and in user?

		return check;
	}

	public static User checkTokenPermissions(Resource r, Class userType) {
		User check = getUserByToken(r);

		if (!userType.isInstance(check)) {
			throw new ResourceException(403, "Incorrect Permissions", "You do not have access to given resource", null);
		}
		// TODO: update timeout on cookie and in user?

		return check;
	}

	public static byte[] hash(String password, byte[] salt) {
		try {
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
			SecretKeyFactory f = SecretKeyFactory.getInstance(Constants.ALGORITHM);
			return f.generateSecret(spec).getEncoded();
		}
		catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("Missing algorithm: " + Constants.ALGORITHM, ex);
		}
		catch (InvalidKeySpecException ex) {
			throw new IllegalStateException("Invalid SecretKeyFactory", ex);
		}
	}

}
