package com.shoppinglist.firebase.didoy.shoper.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.shoppinglist.firebase.didoy.shoper.R;
import com.shoppinglist.firebase.didoy.shoper.model.ShoppingList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Didoy on 11/23/2016.
 */

public class CustomArrayAdapter extends ArrayAdapter {

    public CustomArrayAdapter(Context context, int resource, ArrayList<ShoppingList> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View v = layoutInflater.inflate(R.layout.single_active_list, parent, false);

        TextView mTitleTextView = (TextView) v.findViewById(R.id.list_title_textView);
                TextView mOwnerTextView = (TextView) v.findViewById(R.id.list_owner_textView);
                TextView mTimeStampTextView = (TextView) v.findViewById(R.id.list_time_stamp);

        ShoppingList shoppingList = (ShoppingList) getItem(position);

                if (shoppingList != null) {
                    mTitleTextView.setText(shoppingList.getListName());
                    mOwnerTextView.setText(shoppingList.getOwnerName());

                    long longDate = (long) shoppingList.getTimestampLastChanged().get(Constant.FIRBASE_DATE_PROPERTY);
                    String date = Utility.formatDate(longDate);
                    mTimeStampTextView.setText(date);
                    mOwnerTextView.setText(email);
                }

        return v;
    }
}
