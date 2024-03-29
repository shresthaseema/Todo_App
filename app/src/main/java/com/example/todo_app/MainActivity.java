package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    public static final int NEW_TASK_ACTIVITY_REQUEST_CODE = 1;
    private final TaskListAdapter taskListAdapter = new TaskListAdapter(new TaskListAdapter.TaskDiff());
    private AlertDialog alertDialog;
    private TextView total_tasks, pending_tasks, completed_tasks;
    private Boolean isDarkMode;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private String selectedTask = "Total";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets recyclerView Adapter
        recyclerView = findViewById(R.id.textView_recyclerView);
        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Sets the total, pending and completed tasks
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        total_tasks = findViewById(R.id.total_task_textview);
        taskViewModel.getAllTasks().observe(this, tasks -> {
            String totalTasksCount = String.valueOf(taskViewModel.getAllTasks().getValue().size());
            total_tasks.setText("Total: " + totalTasksCount);
            total_tasks.setBackground(getResources().getDrawable(R.drawable.round_background2));

        });

        pending_tasks = findViewById(R.id.pending_task_textview);
        taskViewModel.getPendingTasksCount().observe(this, tasks -> {
            String pendingTaskCount = String.valueOf(taskViewModel.getPendingTasksCount().getValue().size());
            pending_tasks.setText("Pending: " + pendingTaskCount);
        });

        completed_tasks = findViewById(R.id.completed_task_textview);
        taskViewModel.getCompletedTasksCount().observe(this, tasks -> {
            String completedTaskCount = String.valueOf(taskViewModel.getCompletedTasksCount().getValue().size());
            completed_tasks.setText("Completed: " + completedTaskCount);
        });

        //Displays the selected list of task
        total_tasks.setOnClickListener(view -> {
            taskViewModel.getAllTasks().observe(this, tasks -> {
                taskListAdapter.submitList(tasks);
                selectedTask = "Total";
                total_tasks.setBackground(getResources().getDrawable(R.drawable.round_background2));
                pending_tasks.setBackground(getResources().getDrawable(R.drawable.round_background));
                completed_tasks.setBackground(getResources().getDrawable(R.drawable.round_background));

            });
        });

        pending_tasks.setOnClickListener(view -> {
            taskViewModel.getPendingTasksCount().observe(this, tasks -> {
                taskListAdapter.submitList(tasks);
                selectedTask = "Pending";
                pending_tasks.setBackground(getResources().getDrawable(R.drawable.round_background2));
                total_tasks.setBackground(getResources().getDrawable(R.drawable.round_background));
                completed_tasks.setBackground(getResources().getDrawable(R.drawable.round_background));

            });
        });

        completed_tasks.setOnClickListener(view -> {
            taskViewModel.getCompletedTasksCount().observe(this, tasks -> {
                taskListAdapter.submitList(tasks);
                selectedTask = "Completed";
                completed_tasks.setBackground(getResources().getDrawable(R.drawable.round_background2));
                pending_tasks.setBackground(getResources().getDrawable(R.drawable.round_background));
                total_tasks.setBackground(getResources().getDrawable(R.drawable.round_background));

            });

        });

        //Displays the task list category
        switch (selectedTask) {
            case "Total":
                taskViewModel.getAllTasks().observe(this, taskListAdapter::submitList);
                break;
            case "Pending":
                taskViewModel.getPendingTasksCount().observe(this, taskListAdapter::submitList);
                break;
            case "Completed":
                taskViewModel.getCompletedTasksCount().observe(this, taskListAdapter::submitList);
                break;
        }

        //To add a new task
        FloatingActionButton add_task_button = findViewById(R.id.add_task);
        add_task_button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivityForResult(intent, NEW_TASK_ACTIVITY_REQUEST_CODE);
        });

        //Menu bar to get dialog box
        FloatingActionButton menu_bar_button = findViewById(R.id.menu_bar_fab);
        menu_bar_button.setOnClickListener(view -> {
            getMenuDialog();
        });

        //Attached itemTouchHelper to recyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //Set the app theme mode
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettingPrefs", 0);
        isDarkMode = sharedPreferences.getBoolean("NightMode", false);
        sharedPreferencesEditor = sharedPreferences.edit();

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    //Adds a new task
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_TASK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();
            String taskTitle = bundle.getString("TASK_TITLE");
            String taskDescription = bundle.getString("TASK_DESCRIPTION");
            String taskCategory = bundle.getString("TASK_CATEGORY");
            String taskDate = bundle.getString("TASK_DATE");
            String taskTime = bundle.getString("TASK_TIME");

            Task task = new Task(taskTitle, taskDescription, taskCategory, "PENDING", taskTime, taskDate);
            taskViewModel.insert_task(task);
        }
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            //Get the list of selected task category
            LiveData<List<Task>> listLiveData = null;
            switch (selectedTask) {
                case "Total":
                    listLiveData = taskViewModel.getAllTasks();
                    break;
                case "Pending":
                    listLiveData = taskViewModel.getPendingTasksCount();
                    break;
                case "Completed":
                    listLiveData = taskViewModel.getCompletedTasksCount();
                    break;
            }

            //Delete and complete the task by swiping left and right respectively using Snackbar
            List<Task> taskList = listLiveData.getValue();
            Task task = taskList.get(viewHolder.getPosition());
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    Task deletedTask = task;
                    taskViewModel.deleteTask(task);
                    Snackbar.make(recyclerView, "Task Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            taskViewModel.insert_task(deletedTask);
                            Toast.makeText(MainActivity.this, "Action Undone", Toast.LENGTH_LONG).show();
                        }
                    }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    taskViewModel.updateTaskStatus(task);
                    Toast.makeText(MainActivity.this, "Task Completed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        //Decorates swipe action using RecyclerViewSwipeDecorator
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(getResources().getColor(R.color.red_color))
                    .addSwipeLeftLabel("DELETE")
                    .setSwipeLeftLabelColor(getResources().getColor(R.color.white))
                    .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 25)
                    .addSwipeRightBackgroundColor(getResources().getColor(R.color.green_color))
                    .addSwipeRightLabel("COMPLETED")
                    .setSwipeRightLabelColor(getResources().getColor(R.color.white))
                    .setSwipeRightLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 25)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    //Adds menu bar popup box containing actions
    public void getMenuDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final View menuDialogView = getLayoutInflater().inflate(R.layout.menu_dialogbox, null);

        Button button_cancel = menuDialogView.findViewById(R.id.cancel_button);

        ImageView delete_completed_imageView = menuDialogView.findViewById(R.id.delete_completed_icon);
        ImageView delete_all_imageView = menuDialogView.findViewById(R.id.delete_all_icon);

        alertDialogBuilder.setView(menuDialogView);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        //Closes the popup box
        button_cancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        //Deletes completed tasks
        delete_completed_imageView.setOnClickListener(view -> {
            taskViewModel.deleteCompletedTasks();
            alertDialog.dismiss();
            Toast.makeText(this, "Deleted Completed Tasks", Toast.LENGTH_LONG).show();
        });

        //Deletes all tasks
        delete_all_imageView.setOnClickListener(view -> {
            taskViewModel.deleteAllTasks();
            alertDialog.dismiss();
            Toast.makeText(this, "All Tasks Deleted", Toast.LENGTH_LONG).show();
        });

        //Sets the app theme mode
        ImageView dark_mode_imageView = menuDialogView.findViewById(R.id.dark_mode_icon);
        dark_mode_imageView.setOnClickListener(view -> {
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sharedPreferencesEditor.putBoolean("NightMode", false);
                Toast.makeText(this, "Dark Mode Disabled", Toast.LENGTH_LONG).show();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                sharedPreferencesEditor.putBoolean("NightMode", true);
                Toast.makeText(this, "Dark Mode Enabled ", Toast.LENGTH_LONG).show();
            }
            sharedPreferencesEditor.apply();
            alertDialog.dismiss();

        });

    }
}