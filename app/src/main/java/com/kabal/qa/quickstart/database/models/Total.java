package com.kabal.qa.quickstart.database.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by amgoth.naik on 11/19/2017.
 */

@IgnoreExtraProperties
public class Total {
    public long count;

    public Total(){

    }
    public Total(long count){
        this.count=count;
    }
}