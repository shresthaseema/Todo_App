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

    void insertTask(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insert_task(task);
        });
    }

    void deleteTask(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            int task_id = taskDao.getTaskId(task.getTask_title(), task.getTask_description(), task.getTask_category(), task.getTask_status(), task.getTask_date(), task.getTask_time());
            taskDao.delete_task(task_id);
        });
    }

    void deleteCompletedTasks() {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.delete_completed_tasks();
        });
    }

    void deleteAllTasks() {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.delete_all_tasks();
        });
    }

    void updateTaskStatus(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            int task_id = taskDao.getTaskId(task.getTask_title(), task.getTask_description(), task.getTask_category(), task.getTask_status(), task.getTask_date(), task.getTask_time());
            taskDao.update_task_status("COMPLETED", task_id);
        });
    }

    void updateTask(Task task, String taskTitle, String taskDescription, String taskCategory, String taskDate, String taskTime) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            int task_id = taskDao.getTaskId(task.getTask_title(), task.getTask_description(), task.getTask_category(), task.getTask_status(), task.getTask_date(), task.getTask_time());
            taskDao.update_task(task_id, taskTitle, taskDescription, taskCategory, taskDate, taskTime);
        });
    }
}
