package com.ase.aat_android.data;

import org.restlet.Context;
import org.restlet.data.Cookie;

/**
 * Singleton class for storing restlet User data object
 * Created by anahitik on 15.01.17.
 */

public class User {
    private static com.aat.datastore.User user;
    private static Cookie sessionToken;

    public static void updateSessionToken(Cookie token) {
        sessionToken = token;
    }

    public static void updateUser(com.aat.datastore.User currentUser) {
        user = currentUser;
    }

    public  static com.aat.datastore.User getUser() {
        return user;
    }

    public static Cookie getSessionToken() {
        return sessionToken;
    }

}
