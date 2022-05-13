package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton add_task_button;
    private FloatingActionButton menu_bar_button;
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    public static final int NEW_TASK_ACTIVITY_REQUEST_CODE = 1;
    private final TaskListAdapter taskListAdapter = new TaskListAdapter(new TaskListAdapter.TaskDiff());
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private TextView total_tasks, pending_tasks, completed_tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.textView_recyclerView);
        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        total_tasks = findViewById(R.id.total_task_textview);
        taskViewModel.getAllTasks().observe(this, tasks -> {
            taskListAdapter.submitList(tasks);
            String totalTasksCount = String.valueOf(taskViewModel.getAllTasks().getValue().size());
            total_tasks.setText("Total: "+ totalTasksCount);
            total_tasks.setBackgroundColor(getResources().getColor(R.color.black));

        });

        pending_tasks = findViewById(R.id.pending_task_textview);
        taskViewModel.getPendingTasksCount().observe(this, tasks -> {
            String pendingTaskCount = String.valueOf(taskViewModel.getPendingTasksCount().getValue().size());
            pending_tasks.setText("Pending: " +pendingTaskCount);

        });

        completed_tasks = findViewById(R.id.completed_task_textview);
        taskViewModel.getCompletedTasksCount().observe(this, tasks -> {
            String completedTaskCount = String.valueOf(taskViewModel.getCompletedTasksCount().getValue().size());
            completed_tasks.setText("Completed: " +completedTaskCount);

        });

        total_tasks.setOnClickListener(view -> {
            taskViewModel.getAllTasks().observe(this, tasks -> {
                taskListAdapter.submitList(tasks);
                });
            total_tasks.setBackgroundColor(getResources().getColor(R.color.black));
            pending_tasks.setBackgroundColor(getResources().getColor(R.color.primary_color));
            completed_tasks.setBackgroundColor(getResources().getColor(R.color.primary_color));
        });

        pending_tasks.setOnClickListener(view -> {
            taskViewModel.getPendingTasksCount().observe(this, tasks -> {
                taskListAdapter.submitList(tasks);
            });
            pending_tasks.setBackgroundColor(getResources().getColor(R.color.black));
            total_tasks.setBackgroundColor(getResources().getColor(R.color.primary_color));
            completed_tasks.setBackgroundColor(getResources().getColor(R.color.primary_color));
        });

        completed_tasks.setOnClickListener(view -> {
            taskViewModel.getCompletedTasksCount().observe(this, tasks -> {
                taskListAdapter.submitList(tasks);
            });
            completed_tasks.setBackgroundColor(getResources().getColor(R.color.black));
            total_tasks.setBackgroundColor(getResources().getColor(R.color.primary_color));
            pending_tasks.setBackgroundColor(getResources().getColor(R.color.primary_color));
        });

        add_task_button = findViewById(R.id.add_task);
        add_task_button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivityForResult(intent, NEW_TASK_ACTIVITY_REQUEST_CODE );
        });

        menu_bar_button = findViewById(R.id.menu_bar_fab);
        menu_bar_button.setOnClickListener(view -> {
            getMenuDialog();
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    public void onActivityResult (int requestCode, int resultCode, Intent data){
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
            LiveData<List<Task>> listLiveData = taskViewModel.getAllTasks();
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

    public void getMenuDialog() {
        alertDialogBuilder = new AlertDialog.Builder(this);
        final View menuDialogView = getLayoutInflater().inflate(R.layout.menu_dialogbox, null);

        Button button_cancel = menuDialogView.findViewById(R.id.cancel_button);
        TextView delete_completed_textView = menuDialogView.findViewById(R.id.delete_completed);
        TextView delete_all_textView = menuDialogView.findViewById(R.id.delete_all);

        alertDialogBuilder.setView(menuDialogView);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        button_cancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        delete_completed_textView.setOnClickListener(view -> {
            taskViewModel.deleteCompletedTasks();
            alertDialog.dismiss();
            Toast.makeText(this, "Deleted Completed Tasks", Toast.LENGTH_LONG).show();
        });

        delete_all_textView.setOnClickListener(view -> {
            taskViewModel.deleteAllTasks();
            alertDialog.dismiss();
            Toast.makeText(this, "All Tasks Deleted", Toast.LENGTH_LONG).show();
        });

    }
}