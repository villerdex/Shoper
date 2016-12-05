package com.shoppinglist.firebase.didoy.shoper.UI.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.shoppinglist.firebase.didoy.shoper.R;
import com.shoppinglist.firebase.didoy.shoper.util.Constant;
import com.shoppinglist.firebase.didoy.shoper.util.Utility;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Didoy on 11/8/2016.
 */

public class DialogEditListName extends Dialog {

    private String mTitle = "";
    private String listID = "";
    private DatabaseReference ref;

    @BindView(R.id.edit_listname_edtTxt) EditText editTextListName;
    @BindView(R.id.edit_listname_cancelBtn) Button cancelButton;
    @BindView(R.id.edit_listname_edtBtn) Button editBtn;

    public DialogEditListName(Context context, String title, DatabaseReference ref) {
        super(context);
        mTitle = title;
        this.ref = ref;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_list_name);
        ButterKnife.bind(this);
        editTextListName.setText(mTitle);
    }

    @OnClick(R.id.edit_listname_cancelBtn) void cancelListName(){
        dismiss();
    }

    @OnClick(R.id.edit_listname_edtBtn) void editListName(){
        String newListName = editTextListName.getText().toString();
        if (newListName.equals("")){
            Utility.ToastMessage(getContext(), "please specify new list name");
        }
        else {

            HashMap<String, Object> listMap = new HashMap<String, Object>();
            listMap.put(Constant.FIREBASE_PROPERTY_LIST_NAME, newListName);

            // update the change time as well
            HashMap<String, java.util.Map<String, String>> updateDateMap = new HashMap<String, java.util.Map<String, String>>();
            updateDateMap.put(Constant.FIRBASE_DATE_PROPERTY, ServerValue.TIMESTAMP);

            listMap.put(Constant.FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED, updateDateMap);

            ref.updateChildren(listMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null){
                        Utility.ToastMessage(getContext(), "Successfully Edited");
                    }
                }
            });

            dismiss();
        }
    }

}
