package com.example.test.todotask.model;

/**
 * Created by test on 9/7/15.
 */
public class TaskDetails  {

    //Used to store task name
    private String taskName;

    //Used to store task count
    private String taskContent;

    //Used to store taskid
    private int id;

    //Used to store task date
    private String taskDate;


    //Used to store task date
    private String status;


    //Used to store username
    private String userName;


    public String getTaskName() {
        return taskName;
    }


    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate (String taskDate) {
        this.taskDate = taskDate;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }
}
