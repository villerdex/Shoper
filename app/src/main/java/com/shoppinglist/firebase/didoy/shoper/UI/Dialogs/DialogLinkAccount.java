package com.shoppinglist.firebase.didoy.shoper.UI.Dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.shoppinglist.firebase.didoy.shoper.R;
import com.shoppinglist.firebase.didoy.shoper.util.Utility;

import butterknife.ButterKnife;

/**
 * Created by Didoy on 11/16/2016.
 */

public class DialogLinkAccount extends DialogFragment implements View.OnClickListener {

    private String providerID;
    private FirebaseAuth mFireBaseAuth;

    private final static String LOG_TAG = DialogLinkAccount.class.getSimpleName();
    public final static int requestCode = 16;

    private EditText mEmailTextView;
    private TextInputEditText mPassword;
    private Button mLinkAccountButton;
    private SignInButton mGoogleSignInButton;

    // both layout file use linearlayout i'll inflate them as viewgroup
    ViewGroup viewGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this, viewGroup);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        providerID = getArguments().getString("provider");

        if (providerID.equals("google.com")) {
            viewGroup = (ViewGroup) inflater.inflate(R.layout.dialog_link_account, null);
            mLinkAccountButton = (Button) viewGroup.findViewById(R.id.link_account_button);
            mEmailTextView = (EditText) viewGroup.findViewById(R.id.link_email);
            mPassword = (TextInputEditText) viewGroup.findViewById(R.id.link_password);

            mLinkAccountButton.setOnClickListener(this);
        } else {
            viewGroup = (ViewGroup) inflater.inflate(R.layout.dialog_link_account_google, null);

            mGoogleSignInButton = (SignInButton) viewGroup.findViewById(R.id.link_google_btn);
            mGoogleSignInButton.setOnClickListener(this);
        }
        return viewGroup;
    }

    private void linkGoogleAccount() {

        // We need to create GoogleSignInOption object to use from building googleApiClient
        GoogleApiClient googleApiClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                if (!connectionResult.isSuccess()) {
                    Log.d(LOG_TAG, " onConnectionFailed ");
                }

            }
        };

        // Now we can create GoogleApiClient
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), onConnectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // we can now use GoogleApiClient to create Intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

       getActivity().startActivityForResult(signInIntent, requestCode);

    }

    private void linkEmailAndPassword() {

        if (Utility.validateEmailAndPassword(mEmailTextView, mPassword)) {

            String email = mEmailTextView.getText().toString();
            String password = mPassword.getText().toString();

            // get credentials
            final AuthCredential credential = EmailAuthProvider.getCredential(email, password);

            final ProgressDialog progressDialog = Utility.showProgressDialog(getContext(), "Linking Account.. ");

            mFireBaseAuth.getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Utility.ToastMessage(getContext(), "Link is successful");

                            } else {
                                Utility.ToastMessage(getContext(), task.getException().getLocalizedMessage());
                                Log.d(LOG_TAG, task.getException().getLocalizedMessage());
                            }
                            progressDialog.dismiss();
                            dismiss();
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.link_google_btn:
                linkGoogleAccount();
                break;
            case R.id.link_account_button:
                linkEmailAndPassword();
                break;
        }

    }
}
