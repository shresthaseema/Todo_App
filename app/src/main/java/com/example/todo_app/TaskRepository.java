package com.example.todo_app;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    TaskRepository(Application application) {
        TaskRoomDatabase taskRoomDatabase = TaskRoomDatabase.getDatabase(application);
        taskDao = taskRoomDatabase.taskDao();
        allTasks = taskDao.getAllTasks();
    }
    LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }

    void insert_task(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insert_task(task);
        });
    }
}
