package com.marcosviniciusferreira.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfig {

    private static FirebaseAuth authentication;
    private static DatabaseReference database;

    public static FirebaseAuth getFirebaseAuth() {
        if (authentication == null) {
            authentication = FirebaseAuth.getInstance();
        }
        return authentication;
    }

    public static DatabaseReference getFirebaseDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance("https://whatsapp-6384c-default-rtdb.firebaseio.com").getReference();
        }
        return database;
    }
}
