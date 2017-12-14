package com.kabal.qa.quickstart.database.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post {

    public String uid;
    public String author;
    public String body;
    public double starCount = 0;
    public long date;
    public double answerCount=0;
    public Map<String, String> stars = new HashMap<>();
    public String purl;
    public String postStatus;
    public double trendCount=0;
    public Map<String,Long> userTags;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String body, long date, double  answerCount, double starCount, String purl, double trendCount,Map userTags,String postStatus) {
        this.uid = uid;
        this.author = author;
        this.body = body;
        this.date=date;
        this.answerCount=answerCount;
        this.purl=purl;
        this.starCount=starCount;
        this.trendCount=trendCount;
        this.userTags=userTags;
        this.postStatus=postStatus;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("stars", stars);
        result.put("date",date);
        result.put("answerCount" ,answerCount);
        result.put("purl",purl);
        result.put("trendCount",trendCount);
        result.put("userTags",userTags);
        result.put("postStatus",postStatus);
        return result;
    }
    // [END post_to_map]

}
// [END post_class]