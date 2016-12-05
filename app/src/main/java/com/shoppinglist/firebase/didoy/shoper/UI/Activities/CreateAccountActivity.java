package com.shoppinglist.firebase.didoy.shoper.UI.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.shoppinglist.firebase.didoy.shoper.R;
import com.shoppinglist.firebase.didoy.shoper.model.User;
import com.shoppinglist.firebase.didoy.shoper.util.FirebaseUtility;
import com.shoppinglist.firebase.didoy.shoper.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateAccountActivity extends AppCompatActivity {


    @BindView(R.id.create_email_editext)
    EditText mEmailEditText;
    @BindView(R.id.create_name_editText)
    EditText mNameEditText;
    @BindView(R.id.create_password_editext)
    TextInputEditText mPasswrodEditext;

    private Bitmap bitmap;
    private View rootView;
    private FirebaseAuth mFireBaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = LayoutInflater.from(this).inflate(R.layout.activity_create_account, null);

        ButterKnife.bind(this, rootView);

        Utility.blurBackgroundActivity(this,
                rootView,
                R.drawable.login_activity_bg,
                R.drawable.login_activity_bg_land,
                this );

        // final DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constant.FIREBASE_URL);
        FirebaseUtility.getFireBaseRootReference();
        mFireBaseAuth = FirebaseAuth.getInstance();

    }


    @OnClick(R.id.create_account_btn)
    void createUser() {

        if (Utility.validateEmailAndPassword(mEmailEditText, mPasswrodEditext)) {
            registerUser();
        }

    }

    @OnClick(R.id.create_sigin_textview)
    void signIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // Signing up the user's information in firebase database
    private void registerUser() {

        final String password = mPasswrodEditext.getText().toString();
        final String email = mEmailEditText.getText().toString();

        final ProgressDialog dialog = ProgressDialog.show(this, "",
                "Creating.. Please wait...", true);

        mFireBaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String name = mNameEditText.getText().toString();
                            User user = new User(name, email);

                            // We still need to store the name of the user in /Users/<User Email>
                            FirebaseUtility.saveUsersInformation(user);

                            // We update the name so when the verification email is send the firebase can put the name of the recipient 
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            mFireBaseAuth.getCurrentUser().updateProfile(profileUpdates);

                            mFireBaseAuth.getCurrentUser().sendEmailVerification();

                            Utility.ToastMessage(CreateAccountActivity.this, "Please check your email for verification");

                        } else {
                            //   Pop some information why the registration is unsuccessful
                            Utility.ToastMessage(CreateAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG);
                        }

                        resetTextViews();
                        dialog.dismiss();
                    }
                });

    }

    private void resetTextViews() {
        mEmailEditText.setText("");
        mNameEditText.setText("");
        mPasswrodEditext.setText("");
    }
}