package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Time;
import java.util.Date;

@Entity(tableName = "todo_task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int task_id;

    @NonNull
    @ColumnInfo(name = "task_title")
    private final String task_title;

    @NonNull
    @ColumnInfo(name = "task_description")
    private final String task_description;

    @NonNull
    @ColumnInfo(name = "task_category")
    private final String task_category;

    @NonNull
    @ColumnInfo(name = "task_status")
    private final String task_status;

    @NonNull
    @ColumnInfo(name = "task_time")
    private final String task_time;

    @NonNull
    @ColumnInfo(name = "task_date")
    private final String task_date;


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

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }



}
