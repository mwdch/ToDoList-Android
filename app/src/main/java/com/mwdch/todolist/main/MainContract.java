package com.mwdch.todolist.main;

import com.mwdch.todolist.BasePresenter;
import com.mwdch.todolist.BaseView;
import com.mwdch.todolist.model.Task;

import java.util.List;

public interface MainContract {

    interface View extends BaseView {
        void showTasks(List<Task> tasks);

        void clearTasks();

        void updateTask(Task task);

        void setEmptyStateVisibility(boolean visible);
    }

    interface Presenter extends BasePresenter<View> {
        void onDeleteAllBtnClick();

        void onSearch(String query);

        void onTaskItemClick(Task task);

    }
}
