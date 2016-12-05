package com.shoppinglist.firebase.didoy.shoper.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoppinglist.firebase.didoy.shoper.model.ShoppingList;
import com.shoppinglist.firebase.didoy.shoper.model.User;

import java.util.ArrayList;

/**
 * Created by Didoy on 11/9/2016.
 */

public class FirebaseLoader extends AsyncTaskLoader<ArrayList<ShoppingList>> {

    private String email;

    private ArrayList shoppingListKey;
    private ArrayList<ShoppingList> shoppingList = new ArrayList<>();

    private ValueEventListener userValueEventListener;
    private ValueEventListener shopingListValueEventListener;

    DatabaseReference activeListReference;
    DatabaseReference userDatabaseReference;

    public FirebaseLoader(Context context, String email) {
        super(context);
        this.email = email;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        super.onStartLoading();
    }


    @Override
    public ArrayList<ShoppingList> loadInBackground() {

        shopingListValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // we get the main object and add it in arraylist
                ShoppingList shoppingListModel = dataSnapshot.getValue(ShoppingList.class);
                shoppingList.add(shoppingListModel);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (shoppingListKey == null) {
                    // let's get the all key associated from the user.
                    User user = dataSnapshot.getValue(User.class);
                    shoppingListKey = user.getListOfShoppingList();

                    int ctr = 1;
                    // get reference from all the Activelist node in firebase
                    activeListReference = FirebaseUtility.getFireBaseActiveListReference();

                    // We can now setup listener to get the main data from the active list
                    if (shoppingListKey != null){
                        while (ctr <= shoppingListKey.size()) {
                            activeListReference.addValueEventListener(shopingListValueEventListener);
                            ctr ++;
                        }

                        FirebaseLoader.super.deliverResult(shoppingList);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        userDatabaseReference = FirebaseUtility.getFireBaseUsersReference()
                .child(Utility.formatEmailToFbChild(email));

        // set up listeners to fetch list
        userDatabaseReference.addValueEventListener(userValueEventListener);


        return shoppingList;

    }



//    public static void clearListners(){
//        if (shopingListValueEventListener != null) {
//            activeListReference.removeEventListener(shopingListValueEventListener);
//        }
//
//        if (userValueEventListener != null) {
//            activeListReference.removeEventListener(userValueEventListener);
//        }
//
//    }
}
