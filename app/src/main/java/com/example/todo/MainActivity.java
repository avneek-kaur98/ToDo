package com.example.todo;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, checkBoxListener {

    List<itemInterface> todoList;
    ToDoAdapter toDoAdapter;
    ListView list;
    public final static int add_todo_RequestCode = 1;
    public final static int EDIT_TASK_REQUEST_CODE = 3;

    public static final String POSITION_KEY = "position";
    int requestCode = 0;

    ToDoDao toDoDao;
    LinkedHashMap<String, ArrayList<ToDoThing>> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ToDoDatabase database = Room.databaseBuilder(getApplicationContext(),ToDoDatabase.class,"db_1").allowMainThreadQueries().build();
        toDoDao = database.getToDoDao();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        initialise();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,Add_to_do.class);
                startActivityForResult(intent,add_todo_RequestCode);

            }
        });


    }

    private void initialise() {
        list = findViewById(R.id.list);


        todoList = new ArrayList<>();

        List<ToDoThing> dao_todolist = toDoDao.selectAll();
        todoList.addAll(dao_todolist);
        toDoAdapter = new ToDoAdapter(this,todoList, MainActivity.this);

        list.setAdapter(toDoAdapter);
        list.setOnItemLongClickListener(this);
        list.setOnItemClickListener(this);



//         map = new LinkedHashMap<>();
//
//        if(dao_todolist != null) {
//            for (int i = 0; i < dao_todolist.size(); i++) {
//
//                ToDoThing item = dao_todolist.get(i);
//
//                if (!map.containsKey(item.getTask_date())) {
//
//                    ArrayList<ToDoThing> expenseItems1 = new ArrayList<>();
//                    expenseItems1.add(item);
//                    map.put(item.getTask_date(), expenseItems1);
//                } else {
//
//                    ArrayList<ToDoThing> expenseItems1 = map.get(item.getTask_date());
//                    expenseItems1.add(item);
//                    map.put(item.getTask_date(), expenseItems1);
//
//                }
//            }
//        }
//
//        else
//
//        {
//
//            Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();
//
//        }
//
//        setArrayList(map);
//





    }

//    private void setArrayList(LinkedHashMap<String, ArrayList<ToDoThing>> map) {
//        ArrayList<itemInterface> itemsList = new ArrayList<>();
//        Set<String> keySet = map.keySet();
//
//        ArrayList<String> sortedKeys = new ArrayList<>(keySet);
//
//        for(String s: sortedKeys){
//
//            headerItem headerItem = new headerItem();
//            headerItem.header_date = s;
//            itemsList.add(headerItem);
//            ArrayList<ToDoThing> expenseItems = map.get(s);
//            itemsList.addAll(expenseItems);
//        }
//        todoList.addAll(itemsList);
//        toDoAdapter.notifyDataSetChanged();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == add_todo_RequestCode) {
            if (resultCode == Add_to_do.ADD_ITEM_RESULT_CODE) {
                int id = data.getIntExtra(Add_to_do.ID_KEY, 0);
                ToDoThing toDoThing = toDoDao.getTodo(id);
                if(toDoThing!=null) {
                    todoList.add(toDoThing);
//                    setArrayList(map);
                    toDoAdapter.notifyDataSetChanged();
                    setAlarm(toDoThing.task_date,toDoThing.task_time,toDoThing.task_id,toDoThing.requestCode);
                }

            }
        }

            if(requestCode==EDIT_TASK_REQUEST_CODE){
                if(resultCode==Task_Info.EDIT_TASK_RESULT_CODE){
                    int id = data.getIntExtra(Add_to_do.ID_KEY,0);
                    int position = data.getIntExtra(POSITION_KEY,0);


                        ToDoThing toDoThing1 = (ToDoThing) todoList.get(position);
                        cancelAlarm(toDoThing1);
                        todoList.remove(position);

                        ToDoThing toDoThing = toDoDao.getTodo(id);

                        todoList.add(position,toDoThing);
//                        setArrayList(map);
                        toDoAdapter.notifyDataSetChanged();
                        setAlarm(toDoThing.task_date,toDoThing.task_time,toDoThing.task_id,toDoThing.requestCode);

                }
            }

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.message_permission) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                String permissions[] = {Manifest.permission.RECEIVE_SMS};
                ActivityCompat.requestPermissions(this,permissions,1);
            }
        }
        else if(id == R.id.feedback){

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                Uri uri = Uri.parse("mailto:avneek1612@gmail.com");
                intent.setData(uri);
                if(intent.resolveActivity(getPackageManager())!=null)
                    startActivity(intent);

        }
        else if(id==R.id.Date){
            Collections.sort(todoList, new Comparator<itemInterface>() {
                @Override
                public int compare(itemInterface o1, itemInterface o2) {
                    if (o1 instanceof ToDoThing && o2 instanceof ToDoThing) {
                        ToDoThing O1 = (ToDoThing)o1;
                        ToDoThing O2 = (ToDoThing)o2;
                        DateFormat f = new SimpleDateFormat("dd/MM/yy");
                        try {
                            return f.parse(O1.task_date).compareTo(f.parse(O2.task_date));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                    return 0;
                }

            });
            toDoAdapter.notifyDataSetChanged();

        }
        else if(id==R.id.Title) {
            Collections.sort(todoList, new Comparator<itemInterface>() {
                @Override
                public int compare(itemInterface o1, itemInterface o2) {
                    if (o1 instanceof ToDoThing && o2 instanceof ToDoThing) {
                        ToDoThing O1 = (ToDoThing) o1;
                        ToDoThing O2 = (ToDoThing) o2;
                        return O1.task_title.compareTo(O2.task_title);
                    }
                    return 0;
                }
            });
            toDoAdapter.notifyDataSetChanged();
        }
        else if(id == R.id.Time){
            Collections.sort(todoList, new Comparator<itemInterface>() {
                @Override
                public int compare(itemInterface o1, itemInterface o2) {
                    if (o1 instanceof ToDoThing && o2 instanceof ToDoThing) {
                        DateFormat f = new SimpleDateFormat("h:mm");
                        ToDoThing O1 = (ToDoThing) o1;
                        ToDoThing O2 = (ToDoThing) o2;
                        try {
                            return f.parse(O1.task_time).compareTo(f.parse(O2.task_time));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                    return 0;
                }

            });
            toDoAdapter.notifyDataSetChanged();
        }
        else if(id==R.id.default1){
            Intent intent = new Intent(this,Main2Activity.class);
            intent.putExtra("category","Default");
            startActivity(intent);
        }
        else if(id==R.id.Personal){
            Intent intent = new Intent(this,Main2Activity.class);
            intent.putExtra("category","Personal");
            startActivity(intent);
        }
        else if(id==R.id.Shopping){
            Intent intent = new Intent(this,Main2Activity.class);
            intent.putExtra("category","Shopping");
            startActivity(intent);
        }
        else if(id==R.id.Wishlist){
            Intent intent = new Intent(this,Main2Activity.class);
            intent.putExtra("category","Wishlist");
            startActivity(intent);
        }
        else if(id==R.id.Work){
            Intent intent = new Intent(this,Main2Activity.class);
            intent.putExtra("category","Work");
            startActivity(intent);
        }
        else if(id==R.id.aboutUs){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("https://codingninjas.in");
            intent.setData(uri);
            if(intent.resolveActivity(getPackageManager())!=null)
                startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"ok",Toast.LENGTH_LONG).show();
        }
    }


    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final int i = position;
        final itemInterface toDoThing1 = todoList.get(i);
        if(toDoThing1 instanceof ToDoThing) {

            ToDoThing toDoThing = (ToDoThing)toDoThing1;
            if (todoList.size() > 0) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Confirm Delete");
                dialog.setMessage("Do you really want to delete this task?");
                dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final itemInterface toDoThing1 = todoList.get(i);
                        ToDoThing toDoThing = (ToDoThing) toDoThing1;

                        toDoDao.deleteToDo(toDoThing);
                        cancelAlarm(toDoThing);
                        todoList.remove(toDoThing);
//                        setArrayList(map);
                        toDoAdapter.notifyDataSetChanged();
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        }
        return true;
    }

    private void cancelAlarm(ToDoThing toDoThing1) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), toDoThing1.requestCode, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,Task_Info.class);
        itemInterface toDoThing1 = todoList.get(position);
        if(toDoThing1 instanceof ToDoThing) {
            ToDoThing  toDoThing = (ToDoThing) toDoThing1;
            intent.putExtra(POSITION_KEY, position);
            intent.putExtra(Add_to_do.ID_KEY, toDoThing.task_id);
            startActivityForResult(intent, EDIT_TASK_REQUEST_CODE);
        }
    }

    public  void setAlarm(String task_date,String task_time,int id,int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
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

    @Override
    public void checkBoxClicked(final View view, final int position) {
        final itemInterface toDoThing = todoList.get(position);
        if (toDoThing instanceof ToDoThing) {


            if (todoList.size() > 0) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Task Accomplished");
                dialog.setMessage("Remove ToDo from list?");
                dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final itemInterface toDoThing1 = todoList.get(position);
                        ToDoThing toDoThing = (ToDoThing) toDoThing1;

                        toDoDao.deleteToDo(toDoThing);
                        cancelAlarm(toDoThing);
                        todoList.remove(toDoThing);
//                        setArrayList(map);
                        toDoAdapter.notifyDataSetChanged();
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckBox checkBox = (CheckBox) view;
                        checkBox.setChecked(false);
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }


        }
    }
}
