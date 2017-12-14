package com.kabal.qa.quickstart.database.Util;

/**
 * Created by amgoth.naik on 10/28/2017.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kabal.qa.quickstart.database.models.User;
import com.kabal.qa.quickstart.database.models.UserNotification;

public class HandlerBackGround extends Activity {
    private String FMCurl = "https://fcm.googleapis.com/fcm/send";
    private String authKey="AAAApR2IiFI:APA91bGe6hW1KYyMpr_CVpc3QxtauhZj3LVGbrlLmUc45TAZ0mGxZ_Mdyiy2d_rP6o-NyuKcaC9UUcBgJ_EsMwhloeZWTdxmU3EQaH3sG8K_L92Soi9CZStehQrewyp7-tfUfjEbfWSg";
    public String title;
    public String body;
    public String to;
   // public String fcmIdto;
    public String from;
    public String city;
    private DatabaseReference mDatabase;


   /* private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };*/

    public HandlerBackGround() {
    }

    private void sendNotification(String fcmID,String title,String body)  {

        try {
            URL url = new URL(FMCurl);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + authKey);
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();
            json.put("to", fcmID.trim());
            JSONObject info = new JSONObject();
            info.put("title", title); // Notification title
            info.put("body", body); // Notification
            info.put("click_action",".DisplayNotification");
            // body
            json.put("notification", info);
            try {
                OutputStreamWriter wr = new OutputStreamWriter(
                        conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                }
                // result = CommonConstants.SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                // result = CommonConstants.FAILURE;
            }
            System.out.println("GCM Notification is sent successfully");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
        e.printStackTrace();
    }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        //return result;
    }

    public void writeNotification(UserNotification userNotification,String to){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("/user-notifications/" + to + "/");
        Map<String, Object> postValues = userNotification.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        String notificationId = mDatabase.push().getKey();
        //.push().setValue(comment);
        //with key start
        childUpdates.put( "/user-notifications/" + to + "/"+notificationId, postValues);
        //with key end
        mDatabase.updateChildren(childUpdates);
    }


    public void prepareNotification( String title,  String body,  String from, String to,  String city, String commentId,String postId){
        final String  title1 =title;
        final String body1=body;
        final String from1=from;
        final String city1=city;
        final String to1=to;
        final String commentId1=commentId;
        final String mPostKey1=postId;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(to).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                UserNotification userNotification= new UserNotification(title1,body1,"deeplink",user.fcmId,System.currentTimeMillis(),"Y",from1,city1,commentId1,to1,mPostKey1,"Y");
                writeNotification(userNotification,to1);
                final String fcmId=user.fcmId;
                new Thread() {
                    public void run() {
                        sendNotification(fcmId,title1,body1);
                    }
                }.start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}