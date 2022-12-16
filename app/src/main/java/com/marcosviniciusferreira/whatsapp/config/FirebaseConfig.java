package com.marcosviniciusferreira.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseConfig {

    private static FirebaseAuth authentication;
    private static DatabaseReference database;
    private static StorageReference storage;

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

    public static StorageReference getFirebaseStorage() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }
}
