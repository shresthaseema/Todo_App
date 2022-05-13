package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class IndividualActivity extends AppCompatActivity {

    private Button delete_task_button;
    private Button save_task_button;
    private Button complete_task_button;
    private EditText task_title_editText;
    private EditText task_description_editText;
    private Spinner task_category_spinner;
    private EditText task_status_editText;
    private EditText task_date_editText;
    private EditText task_time_editText;
    private DatePickerDialog.OnDateSetListener set_listener;
    private int hour, minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual);

        Task currentTask = (Task) getIntent().getSerializableExtra("CURRENT_TASK");

        delete_task_button = findViewById(R.id.task_delete_button);
        delete_task_button.setOnClickListener(view -> {
           Intent intent = new Intent(IndividualActivity.this, MainActivity.class);
           TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
           taskViewModel.deleteTask(currentTask);
           startActivity(intent);
        });

        task_title_editText = findViewById(R.id.ind_task_title);
        task_description_editText = findViewById(R.id.ind_task_description);
        task_status_editText = findViewById(R.id.ind_task_status);
        task_time_editText = findViewById(R.id.ind_task_time);
        task_date_editText = findViewById(R.id.ind_task_date);
        complete_task_button = findViewById(R.id.task_complete_button);
        save_task_button = findViewById(R.id.task_save_button);
        task_category_spinner = findViewById(R.id.ind_category_spinner);
        ArrayList<String> category_list = new ArrayList<>(Arrays.asList("--Select a category--", "Household", "Study", "Workout", "Work", "Reminders"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.individual_spinner_category, category_list);
        task_category_spinner.setAdapter(adapter);

        task_title_editText.setText(currentTask.getTask_title());
        task_description_editText.setText(currentTask.getTask_description());
        task_status_editText.setText(currentTask.getTask_status());
        if(currentTask.getTask_status().equals("COMPLETED")) {
            task_status_editText.setTextColor(getResources().getColor(R.color.green_color));
            complete_task_button.setVisibility(View.INVISIBLE);
        }
        task_date_editText.setText(currentTask.getTask_date());
        task_time_editText.setText(currentTask.getTask_time());

        int spinnerPosition = 0;
        if(currentTask.getTask_category().equals("Household")) {
            spinnerPosition = 1;
        }
        else if (currentTask.getTask_category().equals("Study")) {
            spinnerPosition = 2;
        }
        else if (currentTask.getTask_category().equals("Workout")){
            spinnerPosition = 3;
        }
        else if (currentTask.getTask_category().equals("Work")){
            spinnerPosition = 4;
        }
        else {
            spinnerPosition = 5;
        }

        task_category_spinner.setSelection(spinnerPosition);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        task_date_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(IndividualActivity.this, R.style.DialogTheme, set_listener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.primary_color));
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.primary_color));
            }
        });

        set_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                String date = day + "/" + month + "/" +year;
                task_date_editText.setText(date);
            }
        };

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selected_hour, int selected_minute) {
                hour = selected_hour;
                minute = selected_minute;
                task_time_editText.setText(String.format(Locale.getDefault(), "%02d : %02d", hour, minute ));
            }
        };

        task_time_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(IndividualActivity.this, R.style.DialogTheme, onTimeSetListener, hour, minute, true);
                timePickerDialog.show();
                timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.primary_color));
                timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.primary_color));
            }
        });


        complete_task_button.setOnClickListener(view -> {
            Intent intent = new Intent(IndividualActivity.this, MainActivity.class);
            TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
            taskViewModel.updateTaskStatus(currentTask);
            startActivity(intent);
        });

        save_task_button.setOnClickListener(view -> {
            if (TextUtils.isEmpty(task_title_editText.getText()) || TextUtils.isEmpty(task_description_editText.getText()) || task_category_spinner.getSelectedItem().toString().equals("--Select a category--") || TextUtils.isEmpty(task_date_editText.getText()) || TextUtils.isEmpty(task_time_editText.getText())) {
                Toast.makeText(this, "All Fields Required", Toast.LENGTH_LONG).show();
            }
            else {
                Intent intent = new Intent(IndividualActivity.this, MainActivity.class);
                TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
                String taskTitle = task_title_editText.getText().toString();
                String taskDescription = task_description_editText.getText().toString();
                String taskCategory = task_category_spinner.getSelectedItem().toString();
                String taskDate = task_date_editText.getText().toString();
                String taskTime = task_time_editText.getText().toString();
                taskViewModel.updateTask(currentTask, taskTitle, taskDescription, taskCategory, taskDate, taskTime);
                startActivity(intent);
            }
        });

    }
}