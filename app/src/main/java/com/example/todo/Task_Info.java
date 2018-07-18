package com.example.todo;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Task_Info extends AppCompatActivity {

    TextView tv_title;
    TextView tv_desc;
    TextView tv_date;
    TextView tv_time;
    TextView category_task_info;

    int id;
    int position;
    int requestcode;

    public static final int CHANGE_TASK = 5;
    public static final int EDIT_TASK_RESULT_CODE = 9;
    ToDoDao toDoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task__info);

        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        category_task_info = findViewById(R.id.category_task_info);

        Intent intent = getIntent();
        if(intent!=null) {
            id = intent.getIntExtra(Add_to_do.ID_KEY, -1);
            ToDoDatabase toDoDatabase = Room.databaseBuilder(getApplicationContext(),ToDoDatabase.class,"db_1").allowMainThreadQueries().build();
            toDoDao = toDoDatabase.getToDoDao();
            ToDoThing toDoThing = toDoDao.getTodo(id);
            if (toDoThing!=null) {
                position = intent.getIntExtra(MainActivity.POSITION_KEY, 0);
                setTextViews(id);
            }
            else{
                Toast.makeText(this,"To Do Deleted",Toast.LENGTH_LONG).show();
                finish();
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id1 = item.getItemId();
        if(id1==R.id.edit){
            edit_task();
        }
        return super.onOptionsItemSelected(item);
    }

    private void edit_task() {
        Intent intent = new Intent(this,Edit_Task.class);
        intent.putExtra(Add_to_do.ID_KEY,id);
        startActivityForResult(intent,CHANGE_TASK);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==CHANGE_TASK){
            if(resultCode== Edit_Task.CHANGE_TASK_RESULT){
                id = data.getIntExtra(Add_to_do.ID_KEY,0);
                setTextViews(id);
                Intent intent = new Intent();
                intent.putExtra(MainActivity.POSITION_KEY,position);
                intent.putExtra(Add_to_do.ID_KEY,id);
                setResult(EDIT_TASK_RESULT_CODE,intent);
            }
        }
    }

    private void setTextViews(int id) {
            ToDoDatabase toDoDatabase = Room.databaseBuilder(getApplicationContext(),ToDoDatabase.class,"db_1").allowMainThreadQueries().build();
             toDoDao = toDoDatabase.getToDoDao();
            ToDoThing toDoThing = toDoDao.getTodo(id);
            tv_title.setText(toDoThing.task_title);
            tv_desc.setText(toDoThing.task_description);
            tv_date.setText(toDoThing.task_date);
            tv_time.setText(toDoThing.task_time);
            category_task_info.setText(toDoThing.category);
    }
}
