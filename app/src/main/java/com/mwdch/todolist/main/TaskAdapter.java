package com.mwdch.todolist.main;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mwdch.todolist.R;
import com.mwdch.todolist.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks = new ArrayList<>();
    private TaskItemEventListener eventListener;
    private Drawable highImportantDrawable;
    private Drawable normalImportantDrawable;
    private Drawable lowImportantDrawable;

    public TaskAdapter(Context context, TaskItemEventListener eventListener) {
        this.eventListener = eventListener;
        highImportantDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.shape_importance_high_rect, null);
        normalImportantDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.shape_importance_normal_rect, null);
        lowImportantDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.shape_importance_low_rect, null);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bindTask(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void addTask(Task task) {
        tasks.add(0, task);
        notifyItemInserted(0);
    }

    public void updateTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (task.getId() == tasks.get(i).getId()) {
                tasks.set(i, task);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void deleteTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void clearTasks() {
        tasks.clear();
        notifyDataSetChanged();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivCheckBox;
        private View viewImportance;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_task_title);
            ivCheckBox = itemView.findViewById(R.id.iv_task_checkBox);
            viewImportance = itemView.findViewById(R.id.view_task_importance);
        }

        public void bindTask(final Task task) {
            tvTitle.setText(task.getTitle());
            if (task.isCompleted()) {
                ivCheckBox.setBackgroundResource(R.drawable.shape_checkbox_checked);
                ivCheckBox.setImageResource(R.drawable.ic_check_white_24dp);
                tvTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                ivCheckBox.setImageResource(0);
                ivCheckBox.setBackgroundResource(R.drawable.shape_checkbox_default);
                tvTitle.setPaintFlags(0);

            }

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    eventListener.onLongClick(task);
                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onClick(task);
                }
            });


            switch (task.getImportance()) {
                case Task.IMPORTANCE_HIGH:
                    viewImportance.setBackground(highImportantDrawable);
                    break;
                case Task.IMPORTANCE_LOW:
                    viewImportance.setBackground(lowImportantDrawable);
                    break;
                case Task.IMPORTANCE_NORMAL:
                    viewImportance.setBackground(normalImportantDrawable);
                    break;
            }


        }
    }

    public interface TaskItemEventListener {
        void onClick(Task task);

        void onLongClick(Task task);
    }
}
