package com.shoppinglist.firebase.didoy.shoper.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoppinglist.firebase.didoy.shoper.R;

/**
 * Created by Didoy on 11/5/2016.
 */

public class FragmentMeal extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal, container, false);

    }
}
