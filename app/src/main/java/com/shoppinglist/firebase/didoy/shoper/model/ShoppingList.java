package com.shoppinglist.firebase.didoy.shoper.model;

import com.google.firebase.database.ServerValue;
import com.shoppinglist.firebase.didoy.shoper.util.Constant;

import java.util.HashMap;

/**
 * Created by Didoy on 11/6/2016.
 */

public class ShoppingList {

    private String listName;
    private String ownerName;
    private String key;

    private HashMap<String, Object> dateCreation = new HashMap<>();
    private HashMap<String, Object> timestampLastChanged = new HashMap<>();

    public ShoppingList(String listName, String ownerName) {
        this.listName = listName;
        this.ownerName = ownerName;
        timestampLastChanged.put(Constant.FIRBASE_DATE_PROPERTY,  ServerValue.TIMESTAMP);
        dateCreation.put(Constant.FIRBASE_DATE_CREATION_PROPERTY, ServerValue.TIMESTAMP);

    }

    public ShoppingList() {
        timestampLastChanged.put(Constant.FIRBASE_DATE_PROPERTY, ServerValue.TIMESTAMP);
        dateCreation.put(Constant.FIRBASE_DATE_CREATION_PROPERTY, ServerValue.TIMESTAMP);
    }

    public HashMap<String, Object> getDateCreation() {
        return dateCreation;
    }
    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public HashMap<String, Object> getTimestampLastChanged() {
        return timestampLastChanged;
    }

    public void setTimestampLastChanged(HashMap<String, Object> timestampLastChanged) {
        this.timestampLastChanged = timestampLastChanged;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
