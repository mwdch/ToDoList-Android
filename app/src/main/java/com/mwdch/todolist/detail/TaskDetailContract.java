package com.mwdch.todolist.detail;

import com.mwdch.todolist.BasePresenter;
import com.mwdch.todolist.BaseView;
import com.mwdch.todolist.model.Task;

public interface TaskDetailContract {

    interface View extends BaseView {
        void showTask(Task task);

        void setDeleteBtnVisibility(boolean visible);

        void showError(String error);

        void returnResult(int resultCode, Task task);
    }

    interface Presenter extends BasePresenter<View> {
        void deleteTask();

        void saveChanges(int importance, String title);
    }
}
