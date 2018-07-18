package com.example.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoOpenHelper extends SQLiteOpenHelper {
    private static final String db_name = "ToDoDB";
    private static final int version = 1;

    private static ToDoOpenHelper toDoOpenHelper;

    public static ToDoOpenHelper getInstance(Context context) {
        if (toDoOpenHelper == null) {
            toDoOpenHelper = new ToDoOpenHelper(context.getApplicationContext());
        }
        return toDoOpenHelper;
    }

    public ToDoOpenHelper(Context context) {
        super(context, db_name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_table = "Create table " + Contract.ToDo.Table_Name + " ( " +
                Contract.ToDo.Column_ID + " Integer primary key autoincrement, " +
                Contract.ToDo.Column_Description + " TEXT, " +
                Contract.ToDo.Column_Title + " Text, " +
                Contract.ToDo.Column_Date + " Text, " +
                Contract.ToDo.request_code + " Integer, " +
                Contract.ToDo.Column_Time + " Text )";

        db.execSQL(create_table);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
