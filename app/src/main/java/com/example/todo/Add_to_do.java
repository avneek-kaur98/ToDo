package com.example.todo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Add_to_do extends AppCompatActivity {

    EditText add_task_title;
    EditText add_task_description;
    EditText add_task_date;
    EditText add_task_time;
    Button Done;


    public static int ADD_ITEM_RESULT_CODE = 2;
    public static final String ID_KEY = "id";
    public static int requestCode = 0;




    Calendar myCalendar = Calendar.getInstance();

    Spinner spinner;
    String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        add_task_title = findViewById(R.id.add_task_title);
        add_task_description = findViewById(R.id.add_task_description);
        add_task_date = findViewById(R.id.add_task_date);
        add_task_time = findViewById(R.id.add_task_time);
        Done = findViewById(R.id.Done);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = "Default";
            }
        });

    }

    public void add_todo(View view) {

        ToDoDatabase database = Room.databaseBuilder(getApplicationContext(),ToDoDatabase.class,"db_1").allowMainThreadQueries().build();
        ToDoDao toDoDao = database.getToDoDao();
        String Task_title = add_task_title.getText().toString();
        String task_date = add_task_date.getText().toString();
        String task_description = add_task_description.getText().toString();
        String task_time = add_task_time.getText().toString();

        if(Task_title.equals("") || task_date.equals("") || task_description.equals("") || task_time.equals("") ) {
        }
        else{


            requestCode++;
            ToDoThing toDoThing = new ToDoThing(Task_title, task_description, task_date, task_time, category);
            toDoThing.requestCode = requestCode;
            toDoDao.addToDo(toDoThing);


            ToDoThing toDoThing1 = toDoDao.getToDoRq(toDoThing.requestCode);

            Intent data = new Intent();
            int id = toDoThing1.getTask_id();
            setAlarm(task_date, task_time, id);
            data.putExtra(ID_KEY, id);
            setResult(ADD_ITEM_RESULT_CODE, data);
            finish();

        }


    }



    public void Open_Date_Dialog(View view) {

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        new DatePickerDialog(this,date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        add_task_date.setText(sdf.format(myCalendar.getTime()));
    }

    public void Open_Time_Dialog(View view) {

        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                add_task_time.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }
    public  void setAlarm(String task_date,String task_time,long id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        String date = task_date;
        date = date.substring(0,6)+"20"+date.substring(6,date.length());
        String dateFinal = date.substring(3,5)+"/"+date.substring(0,2)+"/"+
                date.substring(6,10);

        String time =task_time;
        if(time.substring(1,2).equals(":")){
            time = "0"+time;
        }
        if(time.length()==4){
            time = time.substring(0,3)+"0"+time.substring(3,4);
        }
        time = time+":00";

        try {
            Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
            intent.putExtra("task_id",id);
            PendingIntent pendingIntent =PendingIntent.getBroadcast(getApplicationContext(),requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP,new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(dateFinal+ " " +time).getTime(),pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }
}

