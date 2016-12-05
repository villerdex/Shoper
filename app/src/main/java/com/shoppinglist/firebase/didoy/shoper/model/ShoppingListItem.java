package com.shoppinglist.firebase.didoy.shoper.model;

/**
 * Created by Didoy on 11/9/2016.
 */

public class ShoppingListItem  {

    String itemName;
    String owner;

    public ShoppingListItem(String itemName, String owner) {
        this.itemName = itemName;
        this.owner = owner;
    }

    public ShoppingListItem() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
