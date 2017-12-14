package com.kabal.qa.quickstart.database;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

/**
 * Created by amgoth.naik on 11/9/2017.
 */

public class DisplayNotification extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            Intent i = new Intent(DisplayNotification.this, GoogleSignInActivity.class);
            i.putExtra("notification","Y");
            startActivity(i);
            finish();
        }

    }
