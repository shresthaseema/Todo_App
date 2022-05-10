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

    @Delete
    void delete_task(Task task);

    @Update
    void update_task(Task task);

    @Query("SELECT * FROM todo_task_table ORDER BY task_id")
    LiveData<List<Task>> getAllTasks();

    @Query("DELETE FROM TODO_TASK_TABLE")
    void delete_all_tasks();
}
