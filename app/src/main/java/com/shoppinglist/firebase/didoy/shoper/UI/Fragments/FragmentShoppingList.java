package com.shoppinglist.firebase.didoy.shoper.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.shoppinglist.firebase.didoy.shoper.R;
import com.shoppinglist.firebase.didoy.shoper.UI.Activities.DetailActivity;
import com.shoppinglist.firebase.didoy.shoper.UI.Dialogs.DialogAddList;
import com.shoppinglist.firebase.didoy.shoper.model.ShoppingList;
import com.shoppinglist.firebase.didoy.shoper.model.User;
import com.shoppinglist.firebase.didoy.shoper.util.CustomArrayAdapter;
import com.shoppinglist.firebase.didoy.shoper.util.FirebaseUtility;
import com.shoppinglist.firebase.didoy.shoper.util.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Didoy on 11/5/2016.
 */

public class FragmentShoppingList extends Fragment {

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.active_listView) ListView mlistView;

    final private int loaderID = 0;
    private String mEmail;
    private ArrayList<String> mShoppingListKey = new ArrayList();
    private ArrayList<ShoppingList> mShoppingListArray = new ArrayList();

    private FirebaseAuth mFireBaseAuth;

    private ValueEventListener valueEventListener;
    private ValueEventListener userValueEventListener;
    private ListAdapter listAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        ButterKnife.bind(this, rootView);

        getUsersKeyForList();

        return rootView;
    }


    @OnClick(R.id.fab)
    void showDialogAddList() {
        DialogAddList dialogAddList = new DialogAddList(getActivity());
        dialogAddList.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void fetchUserList(){

        if (valueEventListener == null){

            valueEventListener  = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mShoppingListArray.clear();
                    for ( DataSnapshot snapshot : dataSnapshot.getChildren() ){

                        if (mShoppingListKey.contains(snapshot.getKey())  ){
                            ShoppingList shoppingListModel = snapshot.getValue(ShoppingList.class);
                            shoppingListModel.setKey(snapshot.getKey());
                            mShoppingListArray.add(shoppingListModel);
                        }

                    }

                    // we can now present the data to list
                    if (mShoppingListArray != null && !mShoppingListArray.isEmpty()){

                        listAdapter = new CustomArrayAdapter(getActivity(), R.layout.single_active_list, mShoppingListArray);
                        mlistView.setAdapter(listAdapter);

                        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(getActivity(), DetailActivity.class);

                            ShoppingList shoppingList = (ShoppingList) listAdapter.getItem(position);
                                intent.putExtra(Intent.EXTRA_TEXT, shoppingList.getKey());
                                startActivity(intent);
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }

        // now we can use the valueEventListener
        DatabaseReference activeListReference = FirebaseUtility.getFireBaseActiveListReference();
        activeListReference.addValueEventListener(valueEventListener);

    }

    private void getUsersKeyForList(){

        FirebaseUtility.getFireBaseRootReference();
        mFireBaseAuth = FirebaseAuth.getInstance();

        mEmail = mFireBaseAuth.getCurrentUser().getEmail();

        userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                if (user.getListOfShoppingList() != null){
                    mShoppingListKey = user.getListOfShoppingList();

                    fetchUserList();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        DatabaseReference  userRef = FirebaseUtility.getFireBaseUsersReference()
                .child(Utility.formatEmailToFbChild(mEmail));

        userRef.addValueEventListener(userValueEventListener);

    }

}
