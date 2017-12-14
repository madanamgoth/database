package com.kabal.qa.quickstart.database.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amgoth.naik on 10/15/2017.
 */

public class ParcelablePost implements Parcelable {

    public String uid;
    public String author;
    public String title;
    public String body;
    public double starCount = 0;
    public long date;
    public double answerCount=0;
    public String city;
    public Map<String, String> stars = new HashMap<>();
    public String purl;
    public int postCheck=0;
    public String authoranonymous;
   // public int postStatus=0;




    public ParcelablePost(Post post){
        this.uid=post.uid;
        this.author=post.author;
        //this.title=post.title;
        this.body=post.body;
        this.starCount=post.starCount;
        this.date=post.date;
        this.answerCount=post.answerCount;
        //this.city=post.city;
        this.purl=post.purl;
        //this.authoranonymous=post.authoranonymous;
        //this.postStatus=post.postStatus;
        this.stars=post.stars;
        //this.postCheck=post.postCheck;
    }

    protected ParcelablePost(Parcel in) {
        uid = in.readString();
        author = in.readString();
        title = in.readString();
        body = in.readString();
        starCount = in.readInt();
        date = in.readLong();
        answerCount = in.readInt();
       // city = in.readString();
        purl = in.readString();
        //postCheck = in.readInt();
        //authoranonymous = in.readString();
       // postStatus = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeDouble(starCount);
        dest.writeLong(date);
        dest.writeDouble(answerCount);
        //dest.writeString(city);
        dest.writeString(purl);
        //dest.writeInt(postCheck);
        //dest.writeString(authoranonymous);
        //dest.writeInt(postStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelablePost> CREATOR = new Creator<ParcelablePost>() {
        @Override
        public ParcelablePost createFromParcel(Parcel in) {
            return new ParcelablePost(in);
        }

        @Override
        public ParcelablePost[] newArray(int size) {
            return new ParcelablePost[size];
        }
    };
}
