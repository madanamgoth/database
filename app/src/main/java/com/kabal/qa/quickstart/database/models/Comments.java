package com.kabal.qa.quickstart.database.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START comment_class]
@IgnoreExtraProperties
public class Comments{

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
    public String postStatus;
    public Map<String,Long> userTags;
    //public Map<String, Boolean> stars = new HashMap<>();

    public Comments() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comments(String uid, String author, String text,Long date,String purl,int starCount,String postStatus,Map userTags) {
        this.uid = uid;
        this.author = author;
        this.text = text;
        this.date=date;
        //this.city=city;
        this.purl=purl;
        //this.postCheck=postCheck;
        //his.authoranonymous=authoranonymous;
        this.starCount=starCount;
        this.postStatus=postStatus;
        this.userTags=userTags;
    }

    public Comments(String uid, String author, String text, Long date, String purl,  int starCount, Map stars,String postStatus) {
        this.uid = uid;
        this.author = author;
        this.text = text;
        this.date=date;
        //this.city=city;
        this.purl=purl;
        //this.postCheck=postCheck;
        //this.authoranonymous=authoranonymous;
        this.starCount=starCount;
        this.stars=stars;
        this.postStatus=postStatus;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        //result.put("authoranonymous", authoranonymous);
        //result.put("city", city);
        result.put("date", date);
        //result.put("postCheck", postCheck);
        result.put("purl", purl);
        result.put("starCount", starCount);
        result.put("text", text);
        result.put("userTags",userTags);
        result.put("postStatus","Y");
        return result;
    }
    // [END post_to_map]
}