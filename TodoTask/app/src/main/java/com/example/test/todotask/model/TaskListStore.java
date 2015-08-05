package com.example.test.todotask.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.test.todotask.constants.ToDoTaskConstants;

/**
 * Created by test on 9/7/15.
 */
public class TaskListStore extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public TaskListStore(Context context) {
        super(context, ToDoTaskConstants.APP_NAME, null, 7);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String query =ToDoTaskConstants.CREATE_TASK_LIST_TABLE;
        Log.d("create", "" + query);
        db.execSQL(query);

    }

    public void openDatabase() {

        db = getWritableDatabase();
        Log.d("DB opened", "hai");

    }


    public void insertDataIntoDatabase(String url) {

        db.execSQL(url);
        Log.d("name", "" + url);


    }

    public void deleteDataFromDatabase(String url) {

        db.execSQL(url);
        Log.d("name", "" + url);

    }


    public void truncateTable() {

        db.execSQL("TRUNCATE TABLE taskList");

    }


    public Cursor getTaskCategorydetail(String Table,String user) {
        String select = "select taskName,taskContent,taskDate from " + Table+" where status='false' and userName='"+user+"'";
        Log.d("select  :", "" + select);
        Cursor cursor = db.rawQuery(select, null);
        return cursor;
    }



    public void taskCompleted(String date) {

        String update = "update taskList set status='true' where taskDate='"+date+"'";
        Log.d("update  :", "" + update);
        db.execSQL(update);
    }


    public void editTask(String title,String content) {
        String update = "update taskList set taskContent='"+content+"' where taskName='"+title+"'";
        db.execSQL(update);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}