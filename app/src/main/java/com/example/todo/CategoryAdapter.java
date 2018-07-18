package com.example.todo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends ArrayAdapter<ToDoThing> {
    private List<ToDoThing> toDoList;
    private LayoutInflater inflater;
    public CategoryAdapter(@NonNull Context context, @NonNull List<ToDoThing> toDoList) {
        super(context, 0, toDoList);
        this.toDoList = toDoList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View output = convertView;
        if(output==null){
            output= inflater.inflate(R.layout.category_layout,parent,false);

            TextView task_title = output.findViewById(R.id.task_title);
            TextView tak_date = output.findViewById(R.id.task_date);
            TextView task_time = output.findViewById(R.id.task_time);

            CategoryViewHolder categoryViewHolder = new CategoryViewHolder();
            categoryViewHolder.task_title = task_title;
            categoryViewHolder.task_date = tak_date;
            categoryViewHolder.task_time = task_time;


            output.setTag(categoryViewHolder);
        }

        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) output.getTag();

        ToDoThing toDoThing = toDoList.get(position);
        categoryViewHolder.task_title.setText(toDoThing.task_title);
        categoryViewHolder.task_date.setText(toDoThing.task_date);
        categoryViewHolder.task_time.setText(toDoThing.task_time);


        return output;

    }

    @Override
    public int getCount() {
        return toDoList.size();
    }
}
