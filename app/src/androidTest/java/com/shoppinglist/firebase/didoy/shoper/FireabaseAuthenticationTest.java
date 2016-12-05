package com.shoppinglist.firebase.didoy.shoper;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoppinglist.firebase.didoy.shoper.UI.Activities.CreateAccountActivity;
import com.shoppinglist.firebase.didoy.shoper.util.Constant;
import com.shoppinglist.firebase.didoy.shoper.util.Utility;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Didoy on 11/13/2016.
 */

@RunWith(AndroidJUnit4.class)
public class FireabaseAuthenticationTest extends Application {

    Context mMockContext = InstrumentationRegistry.getTargetContext();

    boolean isSuccess;
    String googleIdToken = "";

    @Test
    public void testCreateUser(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Constant.FIREBASE_URL);
        FirebaseAuth mFireBaseAuth = FirebaseAuth.getInstance();

        boolean isSuccess = true;
        String password = "123321";
        String email = "villerdex@gmail.com";

        isSuccess = mFireBaseAuth.createUserWithEmailAndPassword(email, password).isSuccessful();

        // if the account is already added to firebase, then we expect false result.
        assertFalse(isSuccess);
    }

}
