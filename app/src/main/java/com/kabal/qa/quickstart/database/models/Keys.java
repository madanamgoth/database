package com.kabal.qa.quickstart.database.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amgoth.naik on 11/19/2017.
 */

// [START key_class]
@IgnoreExtraProperties
public class Keys {
    public Map<String, String> district = new HashMap<>();

    public Keys(){
    }
}