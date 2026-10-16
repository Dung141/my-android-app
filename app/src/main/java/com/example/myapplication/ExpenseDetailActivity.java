package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Expense;
import com.example.myapplication.utils.ExpenseManager;

public class ExpenseDetailActivity extends AppCompatActivity {

    public static final String EXTRA_EXPENSE = "extra_expense";

    private TextView tvTitle, tvAmount, tvCategory, tvDate, tvNote;
    private Button btnBack, btnDelete;
    private ImageButton btnHeaderBack;

    private ExpenseManager expenseManager;
    private Expense currentExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        expenseManager = new ExpenseManager(this);

        initViews();
        loadExpenseData();
        setupListeners();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvAmount = findViewById(R.id.tvAmount);
        tvCategory = findViewById(R.id.tvCategory);
        tvDate = findViewById(R.id.tvDate);
        tvNote = findViewById(R.id.tvNote);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        btnHeaderBack = findViewById(R.id.btnHeaderBack);
    }

    private void loadExpenseData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_EXPENSE)) {
            currentExpense = (Expense) intent.getSerializableExtra(EXTRA_EXPENSE);
            if (currentExpense != null) {
                tvTitle.setText(currentExpense.getTitle());
                tvAmount.setText(currentExpense.getFormattedAmount());
                tvCategory.setText(currentExpense.getCategory());
                tvDate.setText(currentExpense.getDate());

                if (!TextUtils.isEmpty(currentExpense.getNote())) {
                    tvNote.setText(currentExpense.getNote());
                } else {
                    tvNote.setText("Không có ghi chú");
                }
            }
        }
    }

    private void setupListeners() {
        btnHeaderBack.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> {
            if (currentExpense == null) return;

            new AlertDialog.Builder(ExpenseDetailActivity.this)
                    .setTitle("Xóa khoản chi")
                    .setMessage("Bạn có chắc chắn muốn xóa khoản chi '" + currentExpense.getTitle() + "' không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        expenseManager.deleteExpense(currentExpense.getId());
                        Toast.makeText(ExpenseDetailActivity.this, "Đã xóa khoản chi thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }
}