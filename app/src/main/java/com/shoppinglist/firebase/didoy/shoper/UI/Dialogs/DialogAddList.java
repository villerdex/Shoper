package com.shoppinglist.firebase.didoy.shoper.UI.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoppinglist.firebase.didoy.shoper.R;
import com.shoppinglist.firebase.didoy.shoper.model.ShoppingList;
import com.shoppinglist.firebase.didoy.shoper.model.User;
import com.shoppinglist.firebase.didoy.shoper.util.Constant;
import com.shoppinglist.firebase.didoy.shoper.util.FirebaseUtility;
import com.shoppinglist.firebase.didoy.shoper.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Didoy on 11/6/2016.
 */

public class DialogAddList extends Dialog {

    final String LOG_TAG = DialogAddList.class.getSimpleName();

    private Context mContext;
    private DatabaseReference ref;
    private User user;
    ValueEventListener valueEventListener;

    @BindView(R.id.dialog_btn_addlist) Button mAddListButton;
    @BindView(R.id.dialog_edit_text_list_name) EditText mAddListName;

    public DialogAddList(Context context) {
        super(context);
        View rootView =  View.inflate(context, R.layout.dialog_add_list, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        mContext = context;
    }

    @OnClick(R.id.dialog_btn_addlist) void addShopList(){

        String listName = mAddListName.getText().toString();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        String email = sharedPreferences.getString(mContext.getString(R.string.shared_email), "");

        ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Constant.FIREBASE_URL).child(Constant.FIREBASE_LOCATION_ACTIVE_LIST);

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setListName(listName);
        shoppingList.setOwnerName(email);

        // lets get a unique ID first
        final String key = ref.push().getKey();

        ref.child(key).setValue(shoppingList);

        // the users own this list, we must add this list to user's list of list
        ref =  FirebaseUtility.getFireBaseUsersReference().child(Utility.formatEmailToFbChild(email));

        valueEventListener  = new ValueEventListener() {
            // this will fetch data once right away and will fetch again after some data change
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(User.class);

                if (user.getListOfShoppingList() != null){
                    // now we add the key for the list
                    if (!user.getListOfShoppingList().contains(key)){
                        user.getListOfShoppingList().add(key);
                        ref.setValue(user);

                        ref.removeEventListener(valueEventListener);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.addValueEventListener(valueEventListener);

        dismiss();
    }

    @Override
    public void dismiss() {


        super.dismiss();
    }
}
