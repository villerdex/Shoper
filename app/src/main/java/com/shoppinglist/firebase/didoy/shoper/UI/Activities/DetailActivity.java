package com.shoppinglist.firebase.didoy.shoper.UI.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoppinglist.firebase.didoy.shoper.R;
import com.shoppinglist.firebase.didoy.shoper.UI.Dialogs.DialogEditItem;
import com.shoppinglist.firebase.didoy.shoper.UI.Dialogs.DialogEditListName;
import com.shoppinglist.firebase.didoy.shoper.UI.Dialogs.DialogAddItem;
import com.shoppinglist.firebase.didoy.shoper.UI.Dialogs.DialogRemoveList;
import com.shoppinglist.firebase.didoy.shoper.model.ShoppingList;
import com.shoppinglist.firebase.didoy.shoper.model.ShoppingListItem;
import com.shoppinglist.firebase.didoy.shoper.util.Constant;
import com.shoppinglist.firebase.didoy.shoper.util.FirebaseDbHolder;
import com.shoppinglist.firebase.didoy.shoper.util.FirebaseLoader;
import com.shoppinglist.firebase.didoy.shoper.util.FirebaseUtility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity{

    private String mTitle = "";
    private String key = "";

    private FirebaseListAdapter firebaseListAdapter;
    private ValueEventListener valueEventListener;
    private  DatabaseReference rootReference;
    private DatabaseReference listReference;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.item_listview) ListView mItem_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        key = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        // let's have reference for list items under the listID node
         listReference = FirebaseUtility.getFireBaseItemsListReference().child(key);

        initialize();
        setListAdapter();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.edit_menu){
            DialogEditListName dialogEditListName = new DialogEditListName(this, mTitle, rootReference);
            dialogEditListName.show();
        }

        if (id == R.id.remove_menu){
            DialogRemoveList dialogRemoveList = new DialogRemoveList(this, rootReference);
            dialogRemoveList.show();

        }

        return super.onOptionsItemSelected(item);
    }


    // Set the title of the activity, and add function for floating actionbar
    private void initialize(){

        rootReference = FirebaseUtility.getFireBaseActiveListReference().child(key);

        valueEventListener = rootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get the List key from firebase database then set it as title
                if (dataSnapshot != null){
                    ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);

                    if (shoppingList != null ){
                        String title = shoppingList.getListName();

                        mTitle = title;
                        setTitle(title);
                    }
                    else {
                        setTitle("Title");
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // We add a new Item in the list by showing dialog
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogAddItem dialogAddItem = new DialogAddItem(DetailActivity.this, listReference);
                dialogAddItem.show();

            }
        });

    }

    private void setListAdapter(){

         firebaseListAdapter = new FirebaseListAdapter<ShoppingListItem>(this, ShoppingListItem.class, R.layout.single_active_item, listReference ) {
            @Override
            protected void populateView(View v, ShoppingListItem model, int position) {
                TextView itemTextView = (TextView) v.findViewById(R.id.item_name);
                ImageView imageView = (ImageView) v.findViewById(R.id.item_erase_icon);

                itemTextView.setText(model.getItemName());

            }
        };
    }

//    @Override
//    public Loader<FirebaseDbHolder> onCreateLoader(int id, Bundle args) {
//        DatabaseReference  ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constant.FIREBASE_URL_ITEMS).child(UID).child(listID);
//
////        FirebaseLoader firebaseLoader = new FirebaseLoader(this, ref );
//        return firebaseLoader;
//    }
//
//    @Override
//    public void onLoadFinished(Loader<FirebaseDbHolder> loader, FirebaseDbHolder dbHolder) {
//
//        // move the firebase url to /Items node since we need items on this list view
//        final DatabaseReference reference = dbHolder.getDatabaseReference();
//
//         firebaseListAdapter = new FirebaseListAdapter<ShoppingListItem>(this, ShoppingListItem.class, R.layout.single_active_item, reference ) {
//            @Override
//            protected void populateView(View v, ShoppingListItem model, int position) {
//                TextView itemTextView = (TextView) v.findViewById(R.id.item_name);
//                ImageView imageView = (ImageView) v.findViewById(R.id.item_erase_icon);
//
//                itemTextView.setText(model.getItemName());
//
//            }
//        };
//
//        mItem_listview.setAdapter(firebaseListAdapter);
//
//        mItem_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView itemTextView = (TextView) view.findViewById(R.id.item_name);
//                String itemName = itemTextView.getText().toString();
//
//                itemPID = firebaseListAdapter.getRef(position).getKey();
//                String[] arguments = {listID, itemName, itemPID };
//
//                DialogEditItem dialogEditItem = new DialogEditItem(DetailActivity.this, arguments);
//                dialogEditItem.show();
//                return false;
//            }
//        });
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<FirebaseDbHolder> loader) {
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (firebaseListAdapter != null){
            firebaseListAdapter.cleanup();
        }
        rootReference.removeEventListener(valueEventListener);
    }
}
