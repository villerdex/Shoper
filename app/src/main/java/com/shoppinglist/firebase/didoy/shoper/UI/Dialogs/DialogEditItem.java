package com.shoppinglist.firebase.didoy.shoper.UI.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoppinglist.firebase.didoy.shoper.R;
import com.shoppinglist.firebase.didoy.shoper.util.Constant;
import com.shoppinglist.firebase.didoy.shoper.util.Utility;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Didoy on 11/10/2016.
 */

public class DialogEditItem extends Dialog {

    private String[] arguments;
    private Context context;
    @BindView(R.id.edit_itemName) TextView editTextView;
    @BindView(R.id.edit_item_btn) Button button;

    /**
     *
     * @param context
     * @param arguments [ PushID, itemName, itemPID]
     */
    public DialogEditItem(Context context, String[] arguments) {
        super(context);
        this.arguments = arguments;
        this.context = context;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit__item);
        ButterKnife.bind(this);

        editTextView.setText(arguments[1]);

    }

    @OnClick(R.id.edit_item_btn) void editItemName(){
        String newItemName = editTextView.getText().toString();

        if (!newItemName.equals(arguments[1])){

            // Create reference pointing to Items/<ActiveListPushID>/<ItemPushID>
            DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constant.FIREBASE_URL_ITEMS).child(arguments[0]).child(arguments[2]);

            HashMap<String, Object> updateItemMap = new HashMap<String, Object>();
            updateItemMap.put(Constant.FIREBASE_ITEM_PROPERTY, newItemName);

            ref.updateChildren(updateItemMap);
            dismiss();
        }
    }
}
