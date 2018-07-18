package com.example.todo;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();
    public static String senderNum;
    public static String message;

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

//        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {
                    //here you will get currentMsg body phoneNmber and senderNumber

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    senderNum = phoneNumber;
                    message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);


                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();

                    Date today = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    String dateToStr = format.format(today);
                    String[] str1 = dateToStr.split("\\s");


                    /* Time time = */

                    ToDoDatabase database = Room.databaseBuilder(context,ToDoDatabase.class,"db_1").allowMainThreadQueries().build();
                    ToDoDao toDoDao = database.getToDoDao();
                    ToDoThing toDoThing = new ToDoThing(senderNum,message,str1[0],str1[1],"Default");
                    Add_to_do.requestCode = Add_to_do.requestCode++;
                    toDoThing.requestCode = Add_to_do.requestCode;
                    toDoDao.addToDo(toDoThing);
                    Toast.makeText(context,"idsms"+toDoThing.task_id,Toast.LENGTH_LONG).show();




                } // end for loop
            } // bundle is null

//        } catch (Exception e) {
//            Log.e("SmsReceiver", "Exception smsReceiver" +e);
//
//        }
    }
}
