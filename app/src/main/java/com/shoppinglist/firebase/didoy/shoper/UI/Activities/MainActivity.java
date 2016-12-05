package com.shoppinglist.firebase.didoy.shoper.UI.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.shoppinglist.firebase.didoy.shoper.R;
import com.shoppinglist.firebase.didoy.shoper.UI.Dialogs.DialogLinkAccount;
import com.shoppinglist.firebase.didoy.shoper.UI.Fragments.FragmentMeal;
import com.shoppinglist.firebase.didoy.shoper.UI.Fragments.FragmentShoppingList;
import com.shoppinglist.firebase.didoy.shoper.util.FirebaseUtility;
import com.shoppinglist.firebase.didoy.shoper.util.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    final static String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.app_bar)
    Toolbar toolBar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private FirebaseAuth mFireBaseAuth;
    private ArrayList<String> listProvider = new ArrayList<String>();
    private DialogLinkAccount dialogLinkAccount;
    private String email;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);

        initializeFirebase();
        saveUserBasicReferences();

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPageAdapter);

        tabLayout.setupWithViewPager(viewPager);


    }

    private class ViewPageAdapter extends FragmentPagerAdapter {

        public ViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            if (position == 0) {
                fragment = new FragmentShoppingList();
            }
            if (position == 1) {
                fragment = new FragmentMeal();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            if (position == 0) {
                title = "Shopping List";
        }
            if (position == 1) {
                title = "Meal";
            }
            return title;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuID = item.getItemId();
        if (menuID == R.id.link_account_menu) {
            linkAccount();
        }

        if (menuID == R.id.sign_out) {
            FirebaseAuth.getInstance().signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    private void linkAccount() {

        /*
         The firebase has a default providerID if  "firebase" so
         so the list has already the size of 1.
         so we must check if the list have the size of >=3 to ensure that
         the account is already linked to other
         */
        if (listProvider.size() >= 3) {
            Utility.ToastMessage(this, "Your account is already linked");

        } else {
            // the index 0 will return "firebase" as default

            String providerID = listProvider.get(1);
            dialogLinkAccount = new DialogLinkAccount();
            Bundle bundle = new Bundle();
            bundle.putString("provider", providerID);
            dialogLinkAccount.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            dialogLinkAccount.show(fragmentManager, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DialogLinkAccount.requestCode) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                // Google Sign In was successful
                Log.d(LOG_TAG, "Successfully Google login");

                GoogleSignInAccount account = result.getSignInAccount();
                googleLinkAccount(account);

            } else {

                Log.d(LOG_TAG, "Failed to get Google Auth Token:");
            }

            dialogLinkAccount.dismiss();

        }
    }

    public void googleLinkAccount(GoogleSignInAccount googleSignInAccount) {

        final ProgressDialog progressDialog = Utility.showProgressDialog(this, "Linking Account..");

        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        mFireBaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d(LOG_TAG, "Link success");
                            Utility.ToastMessage(MainActivity.this, "Link is successful");

                        } else {
                            Utility.ToastMessage(MainActivity.this, task.getException().getLocalizedMessage());
                            Log.d(LOG_TAG, task.getException().getLocalizedMessage());
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    // We need to store user data to shared preference,
    // so we can access user information throughout the application
    private void saveUserBasicReferences(){

       String userID =  mFireBaseAuth.getCurrentUser().getUid();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getString(R.string.shared_email), email);
        editor.putString(getString(R.string.shared_uid), userID);
        editor.apply();
    }

    private void initializeFirebase(){

        // initialize firebase
        FirebaseUtility.getFireBaseRootReference();
        mFireBaseAuth = FirebaseAuth.getInstance();

        for (UserInfo userInfo : mFireBaseAuth.getCurrentUser().getProviderData()){
            Log.d(LOG_TAG, userInfo.getProviderId());
            listProvider.add(userInfo.getProviderId());
        }

        email = mFireBaseAuth.getCurrentUser().getEmail();

        // We setup listener state listener to confirm if the sign out was successful
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                else {

                    if (!user.isEmailVerified()){
                        user.sendEmailVerification();
                        Utility.ToastMessage(MainActivity.this, "Please check your email for verification");
                    }else {

                    }

                }
            }
        };

        mFireBaseAuth.addAuthStateListener(mAuthListener);
    }
}
