package com.example.todo_app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert_task(Task task);

    @Query("SELECT task_id FROM todo_task_table WHERE task_title = :task_item_title AND task_description = :task_item_description AND task_category = :task_item_category AND task_status = :task_item_status AND task_date = :task_item_date AND task_time = :task_item_time")
    int getTaskId(String task_item_title, String task_item_description, String task_item_category, String task_item_status, String task_item_date, String task_item_time);

    @Query("DELETE FROM todo_task_table WHERE task_id = :task_id")
    void delete_task(int task_id);

    @Query("SELECT * FROM todo_task_table ORDER BY task_status DESC")
    LiveData<List<Task>> getAllTasks();

    @Query("DELETE FROM TODO_TASK_TABLE")
    void delete_all_tasks();

    @Query("DELETE FROM todo_task_table WHERE task_status = 'COMPLETED'")
    void delete_completed_tasks();

    @Query("UPDATE todo_task_table SET task_status = :task_item_status WHERE task_id = :task_id")
    void update_task_status(String task_item_status, int task_id);

    @Query("UPDATE todo_task_table SET task_title = :task_item_title, task_description = :task_item_description, task_category = :task_item_category, task_date = :task_item_date, task_time = :task_item_time WHERE task_id = :task_id")
    void update_task(int task_id, String task_item_title, String task_item_description, String task_item_category, String task_item_date, String task_item_time);

    @Query("SELECT * FROM todo_task_table WHERE task_status = 'PENDING'")
    LiveData<List<Task>> pending_tasks_count();

    @Query("SELECT * FROM todo_task_table WHERE task_status = 'COMPLETED'")
    LiveData<List<Task>> completed_tasks_count();
}
