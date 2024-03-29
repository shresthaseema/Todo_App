package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

public class AddTaskActivity extends AppCompatActivity {

    private EditText task_title_editText;
    private EditText task_description_editText;
    private Spinner task_category_spinner;
    private EditText task_date_editText;
    private EditText task_time_editText;
    private DatePickerDialog.OnDateSetListener set_listener;
    private int hour, minute;
    public static final String EXTRA_REPLY = "com.example.todo_app.REPLY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //Implements spinner to add items to category
        task_category_spinner = findViewById(R.id.task_category);
        ArrayList<String> category_list = new ArrayList<>(Arrays.asList("--Select a category--", "Household", "Study", "Workout & Fitness", "Work & Travel", "Reminders"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_category_list, category_list);
        task_category_spinner.setAdapter(adapter);

        //Creates a calender
        task_date_editText = findViewById(R.id.task_date);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        //To display a calender
        task_date_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this, R.style.DialogTheme, set_listener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.primary_color));
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.primary_color));
            }
        });

        //Sets the date on the given format
        set_listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                task_date_editText.setText(date);
            }
        };

        //Sets a time in given format
        task_time_editText = findViewById(R.id.task_time);

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selected_hour, int selected_minute) {
                hour = selected_hour;
                minute = selected_minute;
                task_time_editText.setText(String.format(Locale.getDefault(), "%02d : %02d", hour, minute));
            }
        };

        //To display a time clock
        task_time_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this, R.style.DialogTheme, onTimeSetListener, hour, minute, true);
                timePickerDialog.show();
                timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.primary_color));
                timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.primary_color));
            }
        });

        //To add a new task
        task_title_editText = findViewById(R.id.task_title);
        task_description_editText = findViewById(R.id.task_description);
        final Button add_task_btn = findViewById(R.id.add_task_button);
        add_task_btn.setOnClickListener(view -> {

            //Validates all the task fields using intent bundle
            if (TextUtils.isEmpty(task_title_editText.getText()) || TextUtils.isEmpty(task_description_editText.getText()) || task_category_spinner.getSelectedItem().toString().equals("--Select a category--") || TextUtils.isEmpty(task_date_editText.getText()) || TextUtils.isEmpty(task_time_editText.getText())) {
                Toast.makeText(this, "All Fields Required", Toast.LENGTH_LONG).show();
            } else {
                Intent replyIntent = new Intent();
                Bundle replyBundle = new Bundle();
                String task_title = task_title_editText.getText().toString();
                replyBundle.putString("TASK_TITLE", task_title);

                String task_description = task_description_editText.getText().toString();
                replyBundle.putString("TASK_DESCRIPTION", task_description);

                String task_category = task_category_spinner.getSelectedItem().toString();
                replyBundle.putString("TASK_CATEGORY", task_category);

                String task_date = task_date_editText.getText().toString();
                replyBundle.putString("TASK_DATE", task_date);

                String task_time = task_time_editText.getText().toString();
                replyBundle.putString("TASK_TIME", task_time);

                replyIntent.putExtras(replyBundle);
                setResult(RESULT_OK, replyIntent);
                finish();
            }

        });

    }
}