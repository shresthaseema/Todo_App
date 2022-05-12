package com.example.todo_app;

import android.view.ViewGroup;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;


public class TaskListAdapter extends ListAdapter<Task, TaskViewHolder> {
    public TaskListAdapter (@NonNull DiffUtil.ItemCallback<Task> diffCallback) {
        super(diffCallback);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TaskViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task current = getItem(position);
        holder.title(current.getTask_title());
        holder.description(current.getTask_description());
        holder.category(current.getTask_category());
        holder.status(current.getTask_status());
        holder.time(current.getTask_time());
        holder.date(current.getTask_date());
        holder.setCurrentTask(current);
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
}
