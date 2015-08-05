package com.example.test.todotask.views.adapter;/*                                                  
          @auther  ATHIRA K.R.  on 2/7/15.        
                                                   
          Project Name      TodoTask                                                          
          Package Name      com.example.test.todotask.views.adapter                                                        
          Time              12:40 PM                                                                 
          Month    07    Year  2015        
  */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.todotask.R;
import com.example.test.todotask.model.TaskDetails;
import com.example.test.todotask.views.view.ExpandableListview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskList extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater;
    private List<TaskDetails> mTaskList;


    public TaskList(Activity activity,List<TaskDetails> mTaskList) {
        this.activity=activity;
        this.mTaskList=mTaskList;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }



    @Override
    public int getCount() {
        return mTaskList.size();
    }



    @Override
    public TaskDetails getItem(int position) {
        return mTaskList.get(position);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(activity,
                    R.layout.customview, null);
            new ViewHolder(convertView);
        }
            ViewHolder holder = (ViewHolder) convertView.getTag();

            TaskDetails currentTaskDetails=mTaskList.get(position);

            holder.heading_textview.setText(currentTaskDetails.getTaskName());
            holder.content_textview.setText(currentTaskDetails.getTaskContent());
            holder.date_textview.setText(currentTaskDetails.getTaskDate());


        return convertView;
    }




    class ViewHolder {

        public TextView  heading_textview;
        public TextView  content_textview;
        public TextView  date_textview;

        public ViewHolder(View view) {

            heading_textview=(TextView)view.findViewById(R.id.place_info_textview);
            content_textview=(TextView)view.findViewById(R.id.address_info_textview);
            date_textview=(TextView)view.findViewById(R.id.pincode_textview);

           view.setTag(this);
       }
    }



}








