package com.example.todo_app;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;
    private final LiveData<List<Task>> allTasks;

    public TaskViewModel (Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        allTasks = taskRepository.getAllTasks();
    }

    LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert_task(Task task) {
        taskRepository.insertTask(task);
    }

    public void deleteTask(Task task) {
        taskRepository.deleteTask(task);
    }

    public void updateTaskStatus(Task task) {
        taskRepository.updateTaskStatus(task);
    }

    public void updateTask(Task task, String taskTitle, String taskDescription, String taskCategory, String taskDate, String taskTime) {
        taskRepository.updateTask(task, taskTitle,taskDescription, taskCategory, taskDate, taskTime);
    }
}
