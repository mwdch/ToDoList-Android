package com.mwdch.todolist.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.mwdch.todolist.R;
import com.mwdch.todolist.main.MainActivity;
import com.mwdch.todolist.model.AppDatabase;
import com.mwdch.todolist.model.Task;

public class TaskDetailActivity extends AppCompatActivity implements TaskDetailContract.View {
    private int selectedImportance = Task.IMPORTANCE_NORMAL;
    private ImageView ivLastSelectedImportance;
    private TaskDetailContract.Presenter presenter;
    private EditText etTitle;
    private ImageView ivDelete, ivBack;
    private MaterialButton btnSaveChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        initViews();

        presenter = new TaskDetailPresenter(AppDatabase.getAppDatabase(this).getTaskDao(), getIntent().getParcelableExtra(MainActivity.EXTRA_KEY_TASK));

        ivBack.setOnClickListener(v -> finish());

        ivDelete.setOnClickListener(view -> presenter.deleteTask());

        btnSaveChange.setOnClickListener(view -> presenter.saveChanges(selectedImportance, etTitle.getText().toString().trim()));

        View btnNormalImportance = findViewById(R.id.btn_taskDetail_normalImportance);
        ivLastSelectedImportance = btnNormalImportance.findViewById(R.id.checkIv_taskDetail_normalImportance);

        View btnHighImportance = findViewById(R.id.btn_taskDetail_highImportance);
        btnHighImportance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImportance != Task.IMPORTANCE_HIGH) {
                    ivLastSelectedImportance.setImageResource(0);
                    ImageView imageView = v.findViewById(R.id.checkIv_taskDetail_highImportance);
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_HIGH;
                    ivLastSelectedImportance = imageView;
                }
            }
        });
        View btnLowImportance = findViewById(R.id.btn_taskDetail_lowImportance);
        btnLowImportance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImportance != Task.IMPORTANCE_LOW) {
                    ivLastSelectedImportance.setImageResource(0);
                    ImageView imageView = v.findViewById(R.id.checkIv_taskDetail_lowImportance);
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_LOW;
                    ivLastSelectedImportance = imageView;
                }
            }
        });

        btnNormalImportance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImportance != Task.IMPORTANCE_NORMAL) {
                    ivLastSelectedImportance.setImageResource(0);
                    ImageView imageView = v.findViewById(R.id.checkIv_taskDetail_normalImportance);
                    imageView.setImageResource(R.drawable.ic_check_white_24dp);
                    selectedImportance = Task.IMPORTANCE_NORMAL;
                    ivLastSelectedImportance = imageView;
                }
            }
        });

        presenter.onAttach(this);
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_taskDetail_back);
        etTitle = findViewById(R.id.et_taskDetail_title);
        ivDelete = findViewById(R.id.iv_taskDetail_delete);
        btnSaveChange = findViewById(R.id.btn_taskDetail_saveChanges);
    }

    @Override
    public void showTask(Task task) {
        etTitle.setText(task.getTitle());
        switch (task.getImportance()) {
            case Task.IMPORTANCE_HIGH:
                findViewById(R.id.btn_taskDetail_highImportance).performClick();
                break;
            case Task.IMPORTANCE_NORMAL:
                findViewById(R.id.btn_taskDetail_normalImportance).performClick();
                break;
            case Task.IMPORTANCE_LOW:
                findViewById(R.id.btn_taskDetail_lowImportance).performClick();
                break;
        }
    }

    @Override
    public void setDeleteBtnVisibility(boolean visible) {
        ivDelete.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(findViewById(R.id.rootTaskDetail), error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void returnResult(int resultCode, Task task) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.EXTRA_KEY_TASK, task);
        setResult(resultCode, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
