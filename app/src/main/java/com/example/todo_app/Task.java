package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Entity(tableName = "todo_task_table")
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int task_id;

    @NonNull
    @ColumnInfo(name = "task_title")
    private String task_title;

    @NonNull
    @ColumnInfo(name = "task_description")
    private String task_description;

    @NonNull
    @ColumnInfo(name = "task_category")
    private String task_category;

    @NonNull
    @ColumnInfo(name = "task_status")
    private final String task_status;

    @NonNull
    @ColumnInfo(name = "task_time")
    private String task_time;

    @NonNull
    @ColumnInfo(name = "task_date")
    private String task_date;


    public Task(@NonNull String task_title, @NonNull String task_description, @NonNull String task_category, @NonNull String task_status, @NonNull String task_time, @NonNull String task_date) {
        this.task_title = task_title;
        this.task_description = task_description;
        this.task_category = task_category;
        this.task_status = task_status;
        this.task_date = task_date;
        this.task_time = task_time;
    }

    public int getTask_id() {
        return this.task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public void setTaskTitle(String task_title) {
        this.task_title = task_title;
    }

    public void setTaskDescription(String task_description) {
        this.task_description = task_description;
    }

    public void setTaskCategory(String task_category) {
        this.task_category = task_category;
    }

    public void setTaskDate(String task_date) {
        this.task_date = task_date;
    }

    public void setTaskTime(String task_time) {
        this.task_time = task_time;
    }

    @NonNull
    public String getTask_title() {
        return this.task_title;
    }

    @NonNull
    public String getTask_description() {
        return this.task_description;
    }

    @NonNull
    public String getTask_category() {
        return this.task_category;
    }

    @NonNull
    public String getTask_status() {
        return this.task_status;
    }

    @NonNull
    public String getTask_time() {
        return this.task_time;
    }

    @NonNull
    public String getTask_date() {
        return this.task_date;
    }

}
