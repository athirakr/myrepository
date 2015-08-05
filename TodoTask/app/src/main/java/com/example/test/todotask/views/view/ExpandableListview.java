package com.example.test.todotask.views.view;

import android.app.Activity;
import android.app.Dialog;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;


import com.example.test.todotask.R;

import com.example.test.todotask.constants.ToDoTaskConstants;

import com.example.test.todotask.model.TaskListStore;
import com.example.test.todotask.views.adapter.ExpandableTask;


public class ExpandableListview extends Activity {
    ExpandableTask listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> childDataHashMap;
    List<String> childList;
    List<String> childDate;
    String taskHead;
    String taskDescription;
    private ImageButton add_imageButton;
    private EditText content_editText,title_editText;;
    private Button edit_button, cancel_button;
    ArrayList taskArrayList;
    TaskListStore taskliststore;
    private Bundle bundle;
    private String name_bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_listview);
        bundle=getIntent().getExtras();
        name_bundle=bundle.getString("name");

        // preparing list data
        prepareListData();

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition),
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + childDataHashMap.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                popUp(listDataHeader.get(groupPosition), childDataHashMap.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition));


                return false;
            }
        });

    }


    public void popUp(final String title, final String content) {

        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_main);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        title_editText = (EditText) dialog.findViewById(R.id.editText_title);
        content_editText = (EditText) dialog.findViewById(R.id.editText_content);
        edit_button = (Button) dialog.findViewById(R.id.button_update);
        cancel_button = (Button) dialog.findViewById(R.id.button_cancel);


        title_editText.setText(title);

        content_editText.setText(content);


        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Editing already exisitng task

                String newTilte = title_editText.getText().toString();
                String newContent = content_editText.getText().toString();

                editTask(newTilte, newContent);
                //Closing the dialog
                dialog.dismiss();
                finish();

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        listAdapter.notifyDataSetChanged();

    }


    /*
    * Given Task is edited
    * */
    public void editTask(String title, String content) {
        if(title.equals("")||content.equals("")){
            Toast.makeText(getApplicationContext(), "Please enter something..", Toast.LENGTH_SHORT).show();
        }
        else {
            taskliststore = new TaskListStore(this);
            taskliststore.openDatabase();
            taskliststore.editTask(title, content);
            taskliststore.close();
            Toast.makeText(getApplicationContext(), " Task Edited..", Toast.LENGTH_SHORT).show();
            listAdapter.notifyDataSetChanged();
        }
    }


    /*
     * Preparing the list data
     */
    private void prepareListData() {


        listDataHeader = new ArrayList<String>();
        childDataHashMap = new HashMap<String, List<String>>();
        taskliststore = new TaskListStore(this);
        taskliststore.openDatabase();
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);

        Cursor cursor = taskliststore.getTaskCategorydetail(ToDoTaskConstants.TASK_LIST,name_bundle);

        //function to retrieve all values from a table-
        while (cursor.moveToNext()) {
            try {
                taskHead = cursor.getString(cursor.getColumnIndex(ToDoTaskConstants.TASK_NAME));
                taskDescription = cursor.getString(cursor.getColumnIndex(ToDoTaskConstants.TASK_CONTENT));
                listDataHeader.add(taskHead);

                childList = new ArrayList<String>();
                childList.add(taskDescription);




            } catch (Exception e) {
                Log.e("Database ", " Error " + e.toString());
            }

            childDataHashMap.put(taskHead, childList);

            listAdapter = new ExpandableTask(this, listDataHeader, childDataHashMap);
            expListView.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();


        }

        cursor.close();
    }


}


