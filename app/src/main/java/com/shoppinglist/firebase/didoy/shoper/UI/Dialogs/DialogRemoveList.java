package com.shoppinglist.firebase.didoy.shoper.UI.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.shoppinglist.firebase.didoy.shoper.R;
import com.shoppinglist.firebase.didoy.shoper.util.Constant;
import com.shoppinglist.firebase.didoy.shoper.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Didoy on 11/9/2016s.
 */

public class DialogRemoveList extends Dialog {

    @BindView(R.id.remove_ok_btn)
    Button removeBtn;
    private DatabaseReference ref;

    public DialogRemoveList(Context context, DatabaseReference ref) {
        super(context);
        this.ref = ref;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remove_list);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.remove_ok_btn)
    void removeList() {

        // ActiveList/<UID>/listID this is our URL right now.
        // we need to get the UID so we call the parent path and getKey() method
        final String listID = ref.getKey();
        final String UID = ref.getParent().getKey();

        ref.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                    // TODO in firebase the list must also be deleted from the listOfShoppingList node of users

                    // we now move to /items/<listID> so we can delete all the data associated with the list
                    ref.getParent().getParent().child(Constant.FIREBASE_LOCATION_ITEMS)
                            .child(UID)
                            .child(listID)
                            .removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        Utility.ToastMessage(getContext(), "List is successfully remove");
                                    }

                                }
                            });
                }
            }
        });
    }

}
