package com.shoppinglist.firebase.didoy.shoper.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;

/**
 * Created by Didoy on 11/7/2016.
 */

public class Utility {


    public static String formatDate(long date) {
        String dateFormat = "yyyy-MM-dd hh:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }

    public static void ToastMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context context, String msg, int toastLength) {
        Toast.makeText(context, msg, toastLength).show();
    }

    public static Bitmap blur(Context ctx, Bitmap image) {
        final float BITMAP_SCALE = 0.4f;
        final float BLUR_RADIUS = 10f;

        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(ctx);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public static boolean isLandScape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    // checks weather the phone is connected to wifi or mobile data
    public static boolean haveConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    // Since we store the user's email address
    // we need to alter the email by replacing the dots
    // on with comma "." -> ",", because firebase dont accept period in URL reference
    public static String formatEmailToFbChild(String email) {
        email = email.replace(".", ",");
        return email;
    }

    public static boolean validateEmailAndPassword(EditText emailTextView, TextInputEditText passwordTextView) {

        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextView.setError("Invalid Email address");
            return false;
        } else if (password.length() < 6) {
            emailTextView.setError("Password must contain at least 6 characters");
            return false;
        } else {
            return true;
        }
    }

    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = ProgressDialog.show(context, "", message, true);
        return progressDialog;
    }

    // to avoid boiler plate code, this method will blur the background of an activity
    public static void blurBackgroundActivity(Context context, View rootView, int portraitImg, int landImg, Activity activity) {
        Bitmap bitmap;
        if (isLandScape(context)) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), landImg);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), portraitImg);
        }
        //   Blur the image then set it as background for the activity
        bitmap = Utility.blur(context, bitmap);
        Drawable d = new BitmapDrawable(context.getResources(), bitmap);
        rootView.setBackground(d); //or whatever your image is
        activity.setContentView(rootView); //you might be forgetting this

    }
}
