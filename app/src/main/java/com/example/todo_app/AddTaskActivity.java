package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

public class AddTaskActivity extends AppCompatActivity {

    private Spinner task_category_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        task_category_spinner = findViewById(R.id.task_category);
        ArrayList<String> category_list = new ArrayList<>(Arrays.asList("--Select a category--", "Household", "Study", "Workout"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category_list);
        task_category_spinner.setAdapter(adapter);
    }
}