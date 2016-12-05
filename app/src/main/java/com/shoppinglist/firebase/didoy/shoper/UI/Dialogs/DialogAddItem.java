package com.shoppinglist.firebase.didoy.shoper.UI.Dialogs;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoppinglist.firebase.didoy.shoper.R;
import com.shoppinglist.firebase.didoy.shoper.model.ShoppingListItem;
import com.shoppinglist.firebase.didoy.shoper.util.Constant;
import com.shoppinglist.firebase.didoy.shoper.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Didoy on 11/10/2016.
 */

public class DialogAddItem extends Dialog {

    @BindView(R.id.add_item_btn) Button addItemButton;
    @BindView(R.id.add_item_edtText) EditText addItemEditText;

    private Context context;
    private DatabaseReference ref;

    /**
     *
     * @param context
     * @param ref the firebase reference object to be used
     */
    public DialogAddItem(Context context, DatabaseReference ref) {
        super(context);
        this.context = context;
        this.ref = ref;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_item);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.add_item_btn) void addItem(){

        String itemName = addItemEditText.getText().toString();

        if (itemName.equals("")){
            Utility.ToastMessage(context, "Please specify item name");
        }

        ShoppingListItem shoppingListItem = new ShoppingListItem();
        shoppingListItem.setItemName(itemName);
        shoppingListItem.setOwner("Anonymous");

        ref.push().setValue(shoppingListItem, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null){
                    Utility.ToastMessage(getContext(), "Successfully added item");
                }else {
                    Utility.ToastMessage(getContext(), "Please try again later");
                }
            }
        });

        dismiss();

    }


}
