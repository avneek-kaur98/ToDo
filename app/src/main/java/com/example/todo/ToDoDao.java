package com.example.todo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ToDoDao  {

    @Insert
    void addToDo(ToDoThing toDoThing);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateToDo(ToDoThing toDoThing);

    @Delete
    void deleteToDo(ToDoThing toDoThing);

    @Query("Select * from ToDoThing")
    List<ToDoThing> selectAll();

    @Query("select * from ToDoThing where task_id = :id")
    ToDoThing getTodo(int id);

    @Query("select * from ToDoThing where requestCode = :rq")
    ToDoThing getToDoRq(int rq);

    @Query("select * from ToDoThing where category = :category")
    List<ToDoThing> getCategoryToDo(String category);


}
