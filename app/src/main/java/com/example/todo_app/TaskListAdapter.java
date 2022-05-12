package com.example.todo_app;

import android.view.ViewGroup;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;


public class TaskListAdapter extends ListAdapter<Task, TaskViewHolder> {

    private Task currentTask;

    public TaskListAdapter (@NonNull DiffUtil.ItemCallback<Task> diffCallback) {
        super(diffCallback);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TaskViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        currentTask = getItem(position);
        holder.title(currentTask.getTask_title());
        holder.description(currentTask.getTask_description());
        holder.category(currentTask.getTask_category());
        holder.status(currentTask.getTask_status());
        holder.time(currentTask.getTask_time());
        holder.date(currentTask.getTask_date());
        holder.setCurrentTask(currentTask);
    }

    static class TaskDiff extends DiffUtil.ItemCallback<Task> {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getTask_title().equals(newItem.getTask_title());
        }
    }

    public Task getCurrentTask(){
        return currentTask;
    }
}
