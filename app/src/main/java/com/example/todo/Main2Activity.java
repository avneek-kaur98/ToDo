package com.example.todo;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list;
    List<ToDoThing> category_to_do;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        list = findViewById(R.id.list);
        list.setOnItemClickListener(this);
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        ToDoDatabase toDoDatabase = Room.databaseBuilder(getApplicationContext(),ToDoDatabase.class,"db_1").allowMainThreadQueries().build();
        ToDoDao toDoDao = toDoDatabase.getToDoDao();
       category_to_do = new ArrayList<>();

        CategoryAdapter toDoAdapter = new CategoryAdapter(this,category_to_do);
        list.setAdapter(toDoAdapter);
        List<ToDoThing> list = toDoDao.getCategoryToDo(category);
        category_to_do.addAll(list);
        toDoAdapter.notifyDataSetChanged();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,Task_Info.class);
        ToDoThing toDoThing = category_to_do.get(position);
        intent.putExtra(MainActivity.POSITION_KEY,position);
        intent.putExtra(Add_to_do.ID_KEY,toDoThing.task_id);
        startActivityForResult(intent,MainActivity.EDIT_TASK_REQUEST_CODE);
    }
}
