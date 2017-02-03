package com.ase.aat_android.data;

import com.aat.datastore.*;
import com.aat.datastore.Group;

import org.restlet.data.Cookie;

import java.util.HashMap;

/**
 * Singleton class for storing restlet SessionData data object
 * Created by anahitik on 15.01.17.
 */

public class SessionData {
    /// Store currently loged in user
    private static com.aat.datastore.User user;

    /// Store sessionToken associated with currently loged in user.
    private static Cookie sessionToken;

    /// Caching user attendances
    private static HashMap<String, com.aat.datastore.Group> userAttendances;

    public static void updateSessionToken(Cookie token) {
        sessionToken = token;
    }

    public static void updateUser(com.aat.datastore.User currentUser) {
        user = currentUser;
    }

    public static void updateAttendances(HashMap<String, Group> attendances) {
        userAttendances = attendances;
    }
    public  static com.aat.datastore.User getUser() {
        return user;
    }

    public static Cookie getSessionToken() {
        return sessionToken;
    }

    public static HashMap<String, Group> getUserAttendances() {
        return userAttendances;
    }

    public static void clear() {
        user = null;
        sessionToken = null;
        userAttendances.clear();
    }
}
