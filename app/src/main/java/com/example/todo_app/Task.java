package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int task_id;

    @NonNull
    @ColumnInfo(name = "todo_task")

    private final String todo_task;
    public Task(@NonNull String todo_task) {
        this.todo_task = todo_task;
    }

    @NonNull
    public String getTodo_task() {
        return this.todo_task;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getTask_id() {
        return this.task_id;
    }

}
