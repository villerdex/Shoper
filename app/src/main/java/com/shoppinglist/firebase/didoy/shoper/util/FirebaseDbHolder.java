package com.shoppinglist.firebase.didoy.shoper.util;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Didoy on 11/9/2016.
 */

public class FirebaseDbHolder  {

    private DatabaseReference databaseReference;

    public FirebaseDbHolder(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }
}
