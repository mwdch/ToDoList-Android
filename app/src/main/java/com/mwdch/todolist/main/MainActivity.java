package com.mwdch.todolist.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.mwdch.todolist.R;
import com.mwdch.todolist.detail.TaskDetailActivity;
import com.mwdch.todolist.model.AppDatabase;
import com.mwdch.todolist.model.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View, TaskAdapter.TaskItemEventListener {

    private static final int REQUEST_CODE = 430;
    public static final int RESULT_CODE_ADD_TASK = 1111;
    public static final int RESULT_CODE_DELETE_TASK = 2222;
    public static final int RESULT_CODE_UPDATE_TASK = 3333;
    public static final String EXTRA_KEY_TASK = "task";

    private MainContract.Presenter presenter;

    private TaskAdapter taskAdapter;
    private RecyclerView rvTasks;

    private MaterialButton btnDeleteAll, btnAddNewTask;
    private EditText etSearch;
    private View emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        presenter = new MainPresenter(AppDatabase.getAppDatabase(this).getTaskDao());
        taskAdapter = new TaskAdapter(this, this);

        btnDeleteAll.setOnClickListener(view -> presenter.onDeleteAllBtnClick());

        btnAddNewTask.setOnClickListener(view -> startActivityForResult(new Intent(MainActivity.this, TaskDetailActivity.class), REQUEST_CODE));

        rvTasks.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvTasks.setAdapter(taskAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onSearch(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        presenter.onAttach(this);
    }

    private void initViews() {
        emptyState = findViewById(R.id.main_emptyState);
        btnDeleteAll = findViewById(R.id.btn_main_deleteAll);
        btnAddNewTask = findViewById(R.id.btn_main_addNewTask);
        rvTasks = findViewById(R.id.rv_main_tasks);
        etSearch = findViewById(R.id.et_main_search);
    }

    @Override
    public void showTasks(List<Task> tasks) {
        taskAdapter.setTasks(tasks);
    }

    @Override
    public void clearTasks() {
        taskAdapter.clearTasks();
    }

    @Override
    public void updateTask(Task task) {
        taskAdapter.updateTask(task);
    }

    @Override
    public void setEmptyStateVisibility(boolean visible) {
        emptyState.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if ((resultCode == RESULT_CODE_ADD_TASK || resultCode == RESULT_CODE_UPDATE_TASK
                    || resultCode == RESULT_CODE_DELETE_TASK) && data != null) {
                Task task = data.getParcelableExtra(EXTRA_KEY_TASK);
                if (task != null) {
                    if (resultCode == RESULT_CODE_ADD_TASK) {
                        taskAdapter.addTask(task);
                    } else if (resultCode == RESULT_CODE_UPDATE_TASK) {
                        taskAdapter.updateTask(task);
                    } else {
                        taskAdapter.deleteTask(task);
                    }
                    setEmptyStateVisibility(taskAdapter.getItemCount() == 0);
                }
            }
        }
    }

    @Override
    public void onClick(Task task) {
        presenter.onTaskItemClick(task);
    }

    @Override
    public void onLongClick(Task task) {
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra(EXTRA_KEY_TASK, task);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}