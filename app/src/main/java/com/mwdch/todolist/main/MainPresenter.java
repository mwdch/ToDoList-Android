package com.mwdch.todolist.main;

import com.mwdch.todolist.model.Task;
import com.mwdch.todolist.model.TaskDao;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {

    private TaskDao taskDao;
    private List<Task> tasks;
    private MainContract.View view;

    public MainPresenter(TaskDao taskDao) {
        this.taskDao = taskDao;
        this.tasks = taskDao.getAll();
    }

    @Override
    public void onDeleteAllBtnClick() {
        taskDao.deleteAll();
        view.clearTasks();
        view.setEmptyStateVisibility(true);
    }

    @Override
    public void onSearch(String query) {
        List<Task> tasks;
        if (!query.isEmpty()) {
            tasks = taskDao.search(query);
        } else {
            tasks = taskDao.getAll();
        }
        view.showTasks(tasks);
    }

    @Override
    public void onTaskItemClick(Task task) {
        task.setCompleted(!task.isCompleted());
        int result = taskDao.update(task);
        if (result > 0)
            view.updateTask(task);
    }

    @Override
    public void onAttach(MainContract.View view) {
        this.view = view;

        if (!tasks.isEmpty()) {
            view.showTasks(tasks);
            view.setEmptyStateVisibility(false);
        } else
            view.setEmptyStateVisibility(true);
    }

    @Override
    public void onDetach() {
        view = null;
    }
}
