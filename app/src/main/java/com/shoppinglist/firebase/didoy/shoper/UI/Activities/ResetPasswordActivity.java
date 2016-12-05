package com.shoppinglist.firebase.didoy.shoper.UI.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.shoppinglist.firebase.didoy.shoper.R;
import com.shoppinglist.firebase.didoy.shoper.util.FirebaseUtility;
import com.shoppinglist.firebase.didoy.shoper.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends AppCompatActivity {


    @BindView(R.id.reset_email) EditText reset_email_edittext;
    private View rootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = LayoutInflater.from(this).inflate(R.layout.activity_reset_password, null);

        ButterKnife.bind(this, rootView);

        Utility.blurBackgroundActivity(this,
                rootView,
                R.drawable.login_activity_bg,
                R.drawable.login_activity_bg_land,
                this );
    }

    @OnClick(R.id.reset_btn) void sendPasswordResetEmail(){

        String email = reset_email_edittext.getText().toString();

         FirebaseUtility.getFireBaseRootReference();
         FirebaseAuth.getInstance().sendPasswordResetEmail(email);

         Utility.ToastMessage(this, "The instruction are send to your email");

    }
}
