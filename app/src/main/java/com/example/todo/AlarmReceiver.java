package com.example.todo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if(intent.getExtras() != null){

            NotificationManager manager = (NotificationManager) context.getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel("mychannelid","To do Channel",NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);

            }

            int id = intent.getIntExtra("task_id",-1);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(),"mychannelid");
            builder.setContentTitle("To Do Alarm");
            builder.setContentText("Alarm Received");
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);
            builder.setPriority(NotificationManager.IMPORTANCE_HIGH);

            Intent intent1 = new Intent(context,Task_Info.class);
            intent1.putExtra("id",id);
            PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(),2,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            notification.flags  = Notification.FLAG_AUTO_CANCEL;
            manager.notify(1,notification);
        }


    }
}
