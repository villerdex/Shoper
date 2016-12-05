package com.shoppinglist.firebase.didoy.shoper.model;

import com.google.firebase.database.ServerValue;
import com.shoppinglist.firebase.didoy.shoper.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Didoy on 11/15/2016.
 */

public class User {

    private String name, email;
    private HashMap<String, Object> dateCreation = new HashMap<>();
    private ArrayList<String> listOfShoppingList = new ArrayList<String>();

    public User() {
        dateCreation.put(Constant.FIRBASE_DATE_CREATION_PROPERTY, ServerValue.TIMESTAMP);
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        dateCreation.put(Constant.FIRBASE_DATE_CREATION_PROPERTY, ServerValue.TIMESTAMP);
    }

    public HashMap<String, Object> getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(HashMap<String, Object> dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList getListOfShoppingList() {
        return listOfShoppingList;
    }

    public void setListOfShoppingList(ArrayList<String> listOfShoppingList) {
        this.listOfShoppingList = listOfShoppingList;
    }
}
