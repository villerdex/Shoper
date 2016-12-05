package com.shoppinglist.firebase.didoy.shoper.util;

/**
 * Created by Didoy on 11/6/2016.
 */

public class Constant {

    public static final String FIREBASE_URL = "https://shoplistplusplus-orv.firebaseio.com";

    public static final String FIREBASE_LOCATION_ACTIVE_LIST = "ActiveList";
    public static final String FIREBASE_LOCATION_ITEMS = "Items";
    public static final String FIREBASE_LOCATION_USERS = "Users";

    public static final String FIREBASE_URL_ACTIVE_LIST = FIREBASE_URL + "/" + FIREBASE_LOCATION_ACTIVE_LIST;
    public static final String FIREBASE_URL_ITEMS = FIREBASE_URL + "/" + FIREBASE_LOCATION_ITEMS;
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;




    public static final String FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED = "timestampLastChanged";
    public static final String FIRBASE_DATE_PROPERTY = "date";
    public static final String FIRBASE_DATE_CREATION_PROPERTY= "dateCreation";
    public static final String FIREBASE_PROPERTY_LIST_NAME = "listName";
    public static final String FIREBASE_ITEM_PROPERTY = "itemName";


}
