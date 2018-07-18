package com.example.todo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.test.suitebuilder.annotation.LargeTest;

import java.sql.Time;
import java.util.Date;

@Entity
public class ToDoThing implements itemInterface{
    String task_title;
    String task_description;
    String task_date;
    String task_time;
    @PrimaryKey(autoGenerate = true)
    int task_id;
    int requestCode;
    public int type = 1;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    String category;

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTask_time() {
        return task_time;
    }

    public void setTask_time(String task_time) {
        this.task_time = task_time;
    }

    public String getTask_date() {

        return task_date;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
    }

    public String getTask_description() {

        return task_description;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public String getTask_title() {

        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public ToDoThing(String task_title, String task_description, String task_date, String task_time,String category) {
        this.task_title = task_title;
        this.task_description = task_description;
        this.task_date = task_date;
        this.task_time = task_time;
        this.category = category;

    }

    @Override
    public int getItemType() {
        return this.type;
    }
}
