package com.example.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Edit_Task extends AppCompatActivity {

    EditText et_title;
    EditText et_desc;
    EditText et_date;
    EditText et_time;
    Spinner spinner;

    Calendar myCalendar = Calendar.getInstance();

    public static final int CHANGE_TASK_RESULT = 6;

    int id;

    ToDoDao toDoDao;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__task);

        ToDoDatabase database = Room.databaseBuilder(getApplicationContext(),ToDoDatabase.class,"db_1").allowMainThreadQueries().build();
        toDoDao = database.getToDoDao();

        et_title = findViewById(R.id.et_title);
        et_desc = findViewById(R.id.et_desc);
        et_date = findViewById(R.id.et_date);
        et_time = findViewById(R.id.et_time);
        spinner = findViewById(R.id.spinner1);
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

        Intent intent = getIntent();
         id = intent.getIntExtra(Add_to_do.ID_KEY,0);
        setEditText(id);
    }

    private void setEditText(int id) {
        ToDoThing toDoThing  =toDoDao.getTodo(id);
            et_title.setText(toDoThing.task_title);
            et_desc.setText(toDoThing.task_description);
            et_date.setText(toDoThing.task_date);
            et_time.setText(toDoThing.task_time);

    }

    public void open_edit_Date(View view) {

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

        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    public void open_edit_time(View view) {

        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                et_time.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    public void Edit_task(View view) {

//        ToDoOpenHelper toDoOpenHelper = ToDoOpenHelper.getInstance(getApplicationContext());
//        SQLiteDatabase database = toDoOpenHelper.getWritableDatabase();




        String Task_title = et_title.getText().toString();
        String task_date = et_date.getText().toString();
        String task_description = et_desc.getText().toString();
        String task_time = et_time.getText().toString();
        if(Task_title.equals("") || task_date.equals("") || task_description.equals("") || task_time.equals("") ) {
        }else{
            Add_to_do.requestCode++;

            ToDoThing toDoThing = new ToDoThing(Task_title, task_description, task_date, task_time, category);
            toDoThing.requestCode = Add_to_do.requestCode;
            toDoThing.task_id = id;

            toDoDao.updateToDo(toDoThing);

            // toDoDao
            if (id != -1L) {
                Intent data = new Intent();
                data.putExtra(Add_to_do.ID_KEY, id);
                setResult(CHANGE_TASK_RESULT, data);
                finish();
            }
        }
    }
}
