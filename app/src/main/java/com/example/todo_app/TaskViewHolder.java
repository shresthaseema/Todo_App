package com.example.todo_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    private final TextView task_title_textview;
    private final TextView task_description_textview;
    private final TextView task_category_textview;
    private final TextView task_status_textview;
    private final TextView task_time_textview;
    private final TextView task_date_textview;
    private final TextView task_day_textview;
    private final TextView task_month_textview;


    private TaskViewHolder(View itemView) {
        super(itemView);
        task_title_textview = itemView.findViewById(R.id.task_item_title);
        task_description_textview = itemView.findViewById(R.id.task_item_description);
        task_category_textview = itemView.findViewById(R.id.task_item_category);
        task_status_textview = itemView.findViewById(R.id.task_item_status);
        task_time_textview = itemView.findViewById(R.id.task_item_time);
        task_date_textview = itemView.findViewById(R.id.task_item_date);
        task_day_textview = itemView.findViewById(R.id.task_item_day);
        task_month_textview = itemView.findViewById(R.id.task_item_month);

    }
    public void title (String text){
        task_title_textview.setText(text);
    }

    public void description (String text) {
        task_description_textview.setText(text);
    }

    public void category (String text) {
        task_category_textview.setText(text);
    }
    public void status (String text) {
        task_status_textview.setText(text);
    }

    public void time (String text) {
        task_time_textview.setText(text);
    }

    public void date (String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MM yyyy", Locale.US);
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            Date itemDate = inputDateFormat.parse(date);
            String outputDateString = dateFormat.format(itemDate);
            String[] splitDate = outputDateString.split(" ");
            task_day_textview.setText(splitDate[0]);
            task_date_textview.setText(splitDate[1]);
            task_month_textview.setText(splitDate[2]);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    static TaskViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_task_item, parent, false);
        return new TaskViewHolder(view);
    }
}
