package com.example.birdepremvar.Services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

public class BackgroundNotificationServices extends NotificationExtenderService {


    JobScheduler jobScheduler;
    JobInfo jobInfo;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected boolean onNotificationProcessing(final OSNotificationReceivedResult notification) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.birdepremvar", MODE_PRIVATE);
        String title = notification.payload.title;

        if(title.equals("Lütfen güvende olduğunuzu bildirin!")){
            Log.i("xxx","Deprem Oldu");
            sharedPreferences.edit().putBoolean("isGuvende", false).commit();
            Log.i("xxx",""+sharedPreferences.getBoolean("isGuvende",true));
            initSchedulerService();
        }


        return false;
    }

    private void initSchedulerService(){
        Log.i("xxx","init scheduler");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            ComponentName componentName = new ComponentName(getApplicationContext(),SmsJobService.class);
            JobInfo.Builder builder =new JobInfo.Builder(10,componentName);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setPersisted(true);

            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(builder.build());
        }
    }

}
