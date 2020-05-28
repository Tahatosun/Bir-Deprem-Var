package com.example.birdepremvar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPreferences = this.getSharedPreferences("com.example.birdepremvar",MODE_PRIVATE);
        //Toast.makeText(this, "Güven durumu: "+sharedPreferences.getBoolean("isGuvende",true), Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityDegis(sharedPreferences.getBoolean("isGuvende",true));
            }
        },3000);

        OnesignalInit();

    }


    private void ActivityDegis(boolean result){

        if (result==false) {

            Intent intent = new Intent(getApplicationContext(), Guvendeyim.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (result==true) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void OnesignalInit(){
        OneSignal.setLocationShared(true);
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationReceivedHandler(new OneSignal.NotificationReceivedHandler() {
                    @Override
                    public void notificationReceived(OSNotification notification) {
                        Intent intent = new Intent(getApplicationContext(), Guvendeyim.class);
                        startActivity(intent);
                    }
                })
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }


}
