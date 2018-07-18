package com.example.todo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {ToDoThing.class},version = 1)
public abstract class ToDoDatabase extends RoomDatabase{

    abstract ToDoDao getToDoDao();

}
