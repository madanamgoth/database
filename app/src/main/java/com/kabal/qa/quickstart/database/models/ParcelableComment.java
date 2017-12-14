package com.kabal.qa.quickstart.database.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amgoth.naik on 10/15/2017.
 */

public class ParcelableComment implements Parcelable {



    public String uid;
    public String author;
    public String text;
    public long date;
    //public String city;
    public String purl;
    //public int postCheck=0;
    //public String authoranonymous;
    public int starCount = 0;
    public Map<String, String> stars = new HashMap<>();
    //public Map<String, Boolean> stars = new HashMap<>();

    public ParcelableComment(Comments comment){
        this.uid=comment.uid;
        this.author=comment.author;
        this.text=comment.text;
        this.date=comment.date;
        //this.city=comment.city;
        this.purl=comment.purl;
        //this.postCheck=comment.postCheck;
        //this.authoranonymous=comment.authoranonymous;
        this.starCount=comment.starCount;
        this.stars=comment.stars;
    }

    protected ParcelableComment(Parcel in) {
        uid = in.readString();
        author = in.readString();
        text = in.readString();
        date = in.readLong();
      //  city = in.readString();
        purl = in.readString();
       // postCheck = in.readInt();
        //authoranonymous = in.readString();
        starCount = in.readInt();
    }

    public static final Creator<ParcelableComment> CREATOR = new Creator<ParcelableComment>() {
        @Override
        public ParcelableComment createFromParcel(Parcel in) {
            return new ParcelableComment(in);
        }

        @Override
        public ParcelableComment[] newArray(int size) {
            return new ParcelableComment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(author);
        parcel.writeString(text);
        parcel.writeLong(date);
        //parcel.writeString(city);
        parcel.writeString(purl);
        //parcel.writeInt(postCheck);
        //parcel.writeString(authoranonymous);
        parcel.writeInt(starCount);
    }
}
