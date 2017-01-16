package com.ase.aat_android.data;

/**
 * Singleton class for storing restlet User data object
 * Created by anahitik on 15.01.17.
 */

public class User {
    private static com.aat.datastore.User user;

    public static void updateUser(com.aat.datastore.User currentUser) {
        user = currentUser;
    }

    public  static com.aat.datastore.User getUser() {
        return user;
    }
}
