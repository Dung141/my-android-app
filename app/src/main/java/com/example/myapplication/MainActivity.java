package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ExpenseAdapter;
import com.example.myapplication.model.Expense;
import com.example.myapplication.utils.ExpenseManager;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ExpenseAdapter.OnExpenseClickListener {

    private TextView tvTotalExpense, tvEmpty;
    private RecyclerView rvExpenses;
    private ExtendedFloatingActionButton btnAddExpense;

    private ExpenseAdapter expenseAdapter;
    private ExpenseManager expenseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expenseManager = new ExpenseManager(this);

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpenseData();
    }

    private void initViews() {
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvEmpty = findViewById(R.id.tvEmpty);
        rvExpenses = findViewById(R.id.rvExpenses);
        btnAddExpense = findViewById(R.id.btnAddExpense);
    }

    private void setupRecyclerView() {
        rvExpenses.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter(this, new ArrayList<>(), this);
        rvExpenses.setAdapter(expenseAdapter);
    }

    private void setupListeners() {
        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });
    }

    private void loadExpenseData() {
        List<Expense> expenses = expenseManager.getExpenses();
        tvTotalExpense.setText(expenseManager.getFormattedTotalAmount());

        if (expenses.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvExpenses.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvExpenses.setVisibility(View.VISIBLE);
            expenseAdapter.updateData(expenses);
        }
    }

    @Override
    public void onExpenseClick(Expense expense) {
        Intent intent = new Intent(MainActivity.this, ExpenseDetailActivity.class);
        intent.putExtra(ExpenseDetailActivity.EXTRA_EXPENSE, expense);
        startActivity(intent);
    }
}