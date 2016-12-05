package com.shoppinglist.firebase.didoy.shoper.util;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoppinglist.firebase.didoy.shoper.model.User;

/**
 * Created by Didoy on 11/22/2016.
 */

public class FirebaseUtility {

    public static DatabaseReference getFireBaseRootReference() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constant.FIREBASE_URL);
        return ref;
    }

    public static DatabaseReference getFireBaseUsersReference() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constant.FIREBASE_URL_USERS);
        return ref;
    }

    public static DatabaseReference getFireBaseActiveListReference() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constant.FIREBASE_URL_ACTIVE_LIST);
        return ref;
    }

    public static DatabaseReference getFireBaseItemsListReference() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constant.FIREBASE_URL_ITEMS);
        return ref;
    }

    // if the user login via google we need to add his/her information in our firebase database

    public static void saveUsersInformation(FirebaseUser firebaseUser){
        String email = firebaseUser.getEmail();
        String name = firebaseUser.getDisplayName();

        User user = new User(name, email);
        email =  Utility.formatEmailToFbChild(email);
        DatabaseReference ref =  FirebaseUtility.getFireBaseRootReference().child(Constant.FIREBASE_LOCATION_USERS).child(email);

        ref.setValue(user);
    }

    public static void saveUsersInformation(User user ){

        String email =  Utility.formatEmailToFbChild(user.getEmail());

        DatabaseReference ref =  FirebaseUtility.getFireBaseRootReference().child(Constant.FIREBASE_LOCATION_USERS).child(email);

        ref.setValue(user);
    }

}
