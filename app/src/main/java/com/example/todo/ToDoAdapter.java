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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToDoAdapter extends ArrayAdapter{
    private List<itemInterface> toDoList;
    private LayoutInflater inflater;
    checkBoxListener checkBoxListener;

    public ToDoAdapter(@NonNull Context context, @NonNull List<itemInterface> toDoList, checkBoxListener listener) {
        super(context, 0, toDoList);
        this.toDoList = toDoList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.checkBoxListener = listener;
    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View output = convertView;
        if(toDoList.get(position).getItemType()==1) {
            if (output == null) {
                output = inflater.inflate(R.layout.todo_thing_layout, parent, false);

                TextView task_title = output.findViewById(R.id.task_title);
                TextView tak_date = output.findViewById(R.id.task_date);
                TextView task_time = output.findViewById(R.id.task_time);
                CheckBox checkBox = output.findViewById(R.id.checkbox);
                TextView tv_category = output.findViewById(R.id.tv_category);

                ToDoViewHolder toDoViewHolder = new ToDoViewHolder();
                toDoViewHolder.task_title = task_title;
                toDoViewHolder.task_date = tak_date;
                toDoViewHolder.task_time = task_time;
                toDoViewHolder.checkBox = checkBox;
                checkBox.setChecked(false);
                toDoViewHolder.tv_category = tv_category;

                output.setTag(toDoViewHolder);
            }

            ToDoViewHolder toDoViewHolder = (ToDoViewHolder) output.getTag();

            ToDoThing toDoThing = (ToDoThing) toDoList.get(position);
            toDoViewHolder.task_title.setText(toDoThing.task_title);
            toDoViewHolder.task_date.setText(toDoThing.task_date);
            toDoViewHolder.task_time.setText(toDoThing.task_time);
            toDoViewHolder.tv_category.setText(toDoThing.category);
            toDoViewHolder.checkBox.setChecked(false);
            toDoViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) v;
                    if (checkBox.isChecked()) {
                        checkBoxListener.checkBoxClicked(v, position);
                    }
                }
            });

            return output;
        }
        else{
            if(output==null){
                output = inflater.inflate(R.layout.header_layout,parent,false);
                headerTextViewHolder textViewHolder = new headerTextViewHolder();
                textViewHolder.date = output.findViewById(R.id.date_header);
                output.setTag(textViewHolder);
            }
            headerTextViewHolder textViewHolder = (headerTextViewHolder) output.getTag();
            headerItem headerItem = (headerItem) toDoList.get(position);
            textViewHolder.date.setText(headerItem.header_date);
            return output;
        }

    }

    @Override
    public int getCount() {
        return toDoList.size();
    }




    @Override
    public int getViewTypeCount(){
        return 2;
    }

    // This function returns the type of item(in our case header or list item) that adapter wants to know in getView function.
    @Override
    public int getItemViewType(int position) {
        return toDoList.get(position).getItemType();
    }

    @Override
    public Object getItem(int position) {
        return toDoList.get(position);


    }

}
