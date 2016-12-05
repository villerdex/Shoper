package com.shoppinglist.firebase.didoy.shoper.UI.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shoppinglist.firebase.didoy.shoper.R;
import com.shoppinglist.firebase.didoy.shoper.util.FirebaseUtility;
import com.shoppinglist.firebase.didoy.shoper.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_btn) Button loginButton;
    @BindView(R.id.login_with_google) SignInButton mGoogleLogin;
    @BindView(R.id.login_signUp_editText) TextView mSignUpEditText;
    @BindView(R.id.login_email_editext) EditText mEmailEditText;
    @BindView(R.id.login_password_editext) EditText mPasswordEdiText;
    @BindView(R.id.forgot_password_textview) TextView mForgotPassword;

    private View rootView;
    private FirebaseAuth mFireBaseAuth;

    private String email, password;
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = LayoutInflater.from(this).inflate(R.layout.activity_login, null);

        ButterKnife.bind(this, rootView);

        Utility.blurBackgroundActivity(this,
                rootView,
                R.drawable.login_activity_bg,
                R.drawable.login_activity_bg_land,
                this );

        // initialize the firebase
        FirebaseUtility.getFireBaseRootReference();
        mFireBaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Utility.haveConnection(this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setItems(new CharSequence[]
                    {"Turn on Wifi", "Turn on data"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            WifiManager wifi=(WifiManager)getSystemService(Context.WIFI_SERVICE);
                            wifi.setWifiEnabled(true);
                            dialog.dismiss();
                            break;

                        case 1:
                            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            dialog.dismiss();
                            break;
                    }
                }
            });

            builder.setCancelable(false);
            builder.setTitle("No Internet connection.");
            builder.create().show();
        }
    }

    @OnClick(R.id.login_with_google) void signInWithGoogle(){
        googleSignIn();
    }

    @OnClick(R.id.login_btn) void loginUser(){

        if (validateUserCredentials()){

            final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Signing In please wait..", true);

            mFireBaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        FirebaseUser user = mFireBaseAuth.getCurrentUser();

                        if (task.isSuccessful()){

                            //TODO delete this after test, and use the commented code
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            //TODO delete this after test, and use the commented code

//                            if (!user.isEmailVerified()){
//
//                               // user.sendEmailVerification();
//                                Utility.ToastMessage(LoginActivity.this, "Please check your email for verification", Toast.LENGTH_LONG);
//                            }else {
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                            }

                        }else {
                            Utility.ToastMessage(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG);
                        }
                        progressDialog.dismiss();
                    }
                });
        }

    }

    @OnClick(R.id.login_signUp_editText) void showCreateAccountActivity(){
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.forgot_password_textview) void showResetPasswordActivity(){
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }


    private boolean validateUserCredentials(){
        email = mEmailEditText.getText().toString();
        password =  mPasswordEdiText.getText().toString();

        if (email.trim().equals("")){
            Utility.ToastMessage(this, "Please put a valid email");
            return false;
        }
        else if (password.trim().equals("")){
            Utility.ToastMessage(this, "Please put a valid password");
            return false;
        }else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                Log.d(LOG_TAG, "Successfully Google login");

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                mGoogleApiClient.stopAutoManage(this);
                mGoogleApiClient.disconnect();
                Log.d(LOG_TAG, "Failed to get Google Auth Token:");
            }
        }
    }

    private void googleSignIn(){

        // We ned to create GoogleSignInOption object to use from building googleApiClient
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                if (!connectionResult.isSuccess()){
                    Log.d(LOG_TAG, " onConnectionFailed ");
                }

            }
        };

        // Now we can create GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , onConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        // we can now use GoogleApiClient to create Intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount){
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Waiting for Google to response..", true);

        mFireBaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (task.isSuccessful()){

                            email = mFireBaseAuth.getCurrentUser().getEmail();

                            FirebaseUtility.getFireBaseUsersReference()
                                    .child(Utility.formatEmailToFbChild(email))
                                    .addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot == null){
                                                // Since the user login in by Google we need to save it's information on firebase database
                                                FirebaseUtility.saveUsersInformation(mFireBaseAuth.getCurrentUser());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                            });

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        if (!task.isSuccessful()) {
                            Log.d(LOG_TAG, "signInWithCredential" +task.getException().getLocalizedMessage());
                            Utility.ToastMessage(LoginActivity.this, "Please try again later");
                        }
                        progressDialog.dismiss();
                    }
                });

        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }


}
