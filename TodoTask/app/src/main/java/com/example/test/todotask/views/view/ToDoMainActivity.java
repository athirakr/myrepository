package com.example.test.todotask.views.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.test.todotask.R;
import com.example.test.todotask.constants.ToDoTaskConstants;
import com.example.test.todotask.model.TaskDetails;
import com.example.test.todotask.model.TaskListStore;
import com.example.test.todotask.views.adapter.TaskList;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by test on 7/7/15.
 */
public class ToDoMainActivity extends Activity {


//delete code currect-date


    private String currentDate;
    List<TaskDetails> taskArrayList;
    private SwipeMenuListView mListView;
    private TaskList mAdapter;
    private ImageButton add_imageButton;
    private EditText title_editText, content_editText;
    private Button add_button, cancel_button;
    private String title_string, content_string;
    private SwipeMenuItem deleteItem;
    private TaskListStore taskliststore;
    private TaskDetails taskdetails;
    private Bundle bundle;
    private String name_bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_list);

        //Layout Controlls casting
        initializeView();


        bundle=getIntent().getExtras();
        name_bundle=bundle.getString("name");


        //getAll Task
//        mAdapter.notifyDataSetChanged();
        getTaskData(name_bundle);

        //Call function for baseAdapter class

        popUpCall();


        add_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp();
            }
        });


        //TaskList controlls registration
        swipeMenuCreation();

        //listener item click event

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                deleteItem.setIcon(R.drawable.checkimg);

                switch (index) {
                    case 0:

                        TaskDetails currentTaskDetails = taskArrayList.get(position);
                        currentTaskDetails.getTaskName();

                        String date = currentTaskDetails.getTaskDate();

                        delete(date);
                        getTaskData(name_bundle);
                        mAdapter.notifyDataSetChanged();
                        break;

                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {

            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });


        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                TaskDetails currentTaskDetails = taskArrayList.get(position);

                Intent intent = new Intent(getApplicationContext(), ExpandableListview.class);

                Bundle bundle=new Bundle();
                bundle.putString("name",name_bundle);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
        mAdapter.notifyDataSetChanged();
    }


    public void initializeView() {

        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        add_imageButton = (ImageButton) findViewById(R.id.fab_image_button);
        add_imageButton.setBackgroundResource(R.drawable.edit_icon_green);

    }




    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTaskData(name_bundle);
        Log.d("onresume", "onresume");
        mAdapter.notifyDataSetChanged();
        popUpCall();
    }

    public void popUp() {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_list);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        title_editText = (EditText) dialog.findViewById(R.id.editText_title);
        content_editText = (EditText) dialog.findViewById(R.id.editText_content);
        add_button = (Button) dialog.findViewById(R.id.button_add);
        cancel_button = (Button) dialog.findViewById(R.id.button_cancel);


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adding a new task
                addTask();
                //Closing the dialog
                dialog.dismiss();

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                getTaskData(name_bundle);

            }
        });

    }


    public void addTask() {

        title_string = title_editText.getText().toString();
        content_string = content_editText.getText().toString();
        if(title_string.equals("")||content_string.equals("")){

            Toast.makeText(getApplicationContext(), "Please enter something..", Toast.LENGTH_SHORT).show();

        }
        else{
            //add task details to TaskDetails
            addDataToTaskDetails();

            //insert the task details to database
            insertDataToDataBase();
        }


    }


    public void insertDataToDataBase() {

        taskliststore = new TaskListStore(this);
        taskliststore.openDatabase();
        String query = ToDoTaskConstants.INSERT + ToDoTaskConstants.TASK_LIST + "(" + ToDoTaskConstants.TASK_NAME +
                "," + ToDoTaskConstants.TASK_CONTENT + "," + ToDoTaskConstants.TASK_DATE + "," +
                ToDoTaskConstants.STATUS + ","+ToDoTaskConstants.USERNAME+")" + ToDoTaskConstants.VALUE + "('" + title_string + "', " +
                "'" + content_string + "', '" + currentDate + "', '" + ToDoTaskConstants.FALSE + "','"+name_bundle+"')";

        taskliststore.insertDataIntoDatabase(query);
        taskliststore.close();
        Toast.makeText(getApplicationContext(), "Inserted Successfully..", Toast.LENGTH_SHORT).show();
        getTaskData(name_bundle);
        popUpCall();


    }


    public void addDataToTaskDetails() {


        taskdetails = new TaskDetails();

        // Getting the current Date
        currentDate = getCurrentDate();
        taskdetails.setTaskName(title_string);
        taskdetails.setTaskContent(content_string);
        taskdetails.setTaskDate(currentDate);
        taskdetails.setStatus(ToDoTaskConstants.FALSE);
        taskdetails.setUserName(name_bundle);
    }


    public String getCurrentDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(ToDoTaskConstants.SIMPLE_DATE_FORMAT);
        String currentDate = df.format(c.getTime());

        // Long formattedDate =c.getTimeInMillis();
        // return  formattedDate.toString();

        return currentDate;
    }


    private void swipeMenuCreation() {
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item width
                deleteItem.setWidth(dp2px(90));

                // set a icon
                deleteItem.setIcon(R.drawable.checkimg);
                // add to menu
                menu.addMenuItem(deleteItem);

            }
        };
        // set creator
        mListView.setMenuCreator(creator);

    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    public void getTaskData(String name) {
        taskArrayList = new ArrayList<>();

        taskliststore = new TaskListStore(this);
        taskliststore.openDatabase();


        Cursor cursor = taskliststore.getTaskCategorydetail(ToDoTaskConstants.TASK_LIST,name);

        //function to retrieve all values from a table-
        while (cursor.moveToNext()) {
            try {
                String taskHead = cursor.getString(cursor.getColumnIndex(ToDoTaskConstants.TASK_NAME));
                String taskDescription = cursor.getString(cursor.getColumnIndex(ToDoTaskConstants.TASK_CONTENT));
                String taskPerformDate = cursor.getString(cursor.getColumnIndex(ToDoTaskConstants.TASK_DATE));


                TaskDetails task = new TaskDetails();
                task.setTaskName(taskHead);
                task.setTaskContent(taskDescription);
                task.setTaskDate(taskPerformDate);
                taskArrayList.add(task);


            } catch (Exception e) {
                Log.e("Database ", " Error " + e.toString());

            }
        }





    }


    public void popUpCall() {
        mAdapter = new TaskList(this, taskArrayList);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    private void delete(String date) {
        // delete app
        try {
            taskliststore = new TaskListStore(this);
            taskliststore.openDatabase();
            taskliststore.taskCompleted(date);
            taskliststore.close();

            Toast.makeText(getApplicationContext(), " Task Deleted..", Toast.LENGTH_SHORT).show();
            getTaskData(name_bundle);
            popUpCall();

        } catch (Exception e) {
        }
    }
}