package com.ase.aat_android.data;

import org.restlet.data.Cookie;

import java.util.HashMap;

/**
 * Stores session related data.
 * Cached user data.
 *
 * Created by anahitik on 15.01.17.
 */

public class SessionData {
    /// Store currently loged in user
    private static com.aat.datastore.User user;

    /// Store sessionToken associated with currently loged in user.
    private static Cookie sessionToken;

    /// Caching user attendances
    private static HashMap<String, GroupPojo> userAttendances;

    public static void updateSessionToken(Cookie token) {
        sessionToken = token;
    }

    public static void updateUser(com.aat.datastore.User currentUser) {
        user = currentUser;
    }

    public static void updateAttendances(HashMap<String, GroupPojo> attendances) {
        userAttendances = attendances;
    }
    public  static com.aat.datastore.User getUser() {
        return user;
    }

    public static Cookie getSessionToken() {
        return sessionToken;
    }

    public static HashMap<String, GroupPojo> getUserAttendances() {
        return userAttendances;
    }

    public static GroupPojo getRegisteredGroup(String courseName) {
        if (userAttendances == null || userAttendances.isEmpty()) {
            return null;
        }
        return userAttendances.get(courseName);
    }
    public static void clear() {
        user = null;
        sessionToken = null;
        userAttendances.clear();
    }
}
