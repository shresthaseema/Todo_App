package com.example.todo_app;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {

    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;
    private final LiveData<List<Task>> pendingTasksCount;
    private final LiveData<List<Task>> completedTasksCount;

    TaskRepository(Application application) {
        TaskRoomDatabase taskRoomDatabase = TaskRoomDatabase.getDatabase(application);
        taskDao = taskRoomDatabase.taskDao();
        allTasks = taskDao.getAllTasks();
        pendingTasksCount = taskDao.pending_tasks_count();
        completedTasksCount = taskDao.completed_tasks_count();
    }

    LiveData<List<Task>> getAllTasks() {
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
        TaskRoomDatabase.databaseWriteExecutor.execute(taskDao::delete_completed_tasks);
    }

    void deleteAllTasks() {
        TaskRoomDatabase.databaseWriteExecutor.execute(taskDao::delete_all_tasks);
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

    public LiveData<List<Task>> getCompletedTasksCount() {
        return completedTasksCount;
    }

    public LiveData<List<Task>> getPendingTasksCount() {
        return pendingTasksCount;
    }
}
