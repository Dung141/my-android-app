package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Expense;
import com.example.myapplication.utils.ExpenseManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etTitle, etAmount, etDate, etNote;
    private Spinner spCategory;
    private Button btnSave;
    private ImageButton btnBack;

    private ExpenseManager expenseManager;
    private final Calendar selectedCalendar = Calendar.getInstance();

    private final String[] categories = {"Ăn uống", "Đi lại", "Học tập", "Giải trí", "Khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        expenseManager = new ExpenseManager(this);

        initViews();
        setupSpinner();
        setupDatePicker();
        setupListeners();
    }

    private void initViews() {
        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        etNote = findViewById(R.id.etNote);
        spCategory = findViewById(R.id.spCategory);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        // Mặc định điền ngày hiện tại
        updateDateEditText();
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    private void setupDatePicker() {
        etDate.setOnClickListener(v -> {
            int year = selectedCalendar.get(Calendar.YEAR);
            int month = selectedCalendar.get(Calendar.MONTH);
            int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, yearSelected, monthSelected, dayOfMonth) -> {
                selectedCalendar.set(Calendar.YEAR, yearSelected);
                selectedCalendar.set(Calendar.MONTH, monthSelected);
                selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateEditText();
            }, year, month, day);

            datePickerDialog.show();
        });
    }

    private void updateDateEditText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        etDate.setText(sdf.format(selectedCalendar.getTime()));
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        String title = etTitle.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String category = spCategory.getSelectedItem() != null ? spCategory.getSelectedItem().toString() : "Khác";
        String date = etDate.getText().toString().trim();
        String note = etNote.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Vui lòng nhập tên khoản chi!");
            etTitle.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(amountStr)) {
            etAmount.setError("Vui lòng nhập số tiền!");
            etAmount.requestFocus();
            return;
        }

        double amount = 0;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            etAmount.setError("Số tiền không hợp lệ!");
            etAmount.requestFocus();
            return;
        }

        if (amount <= 0) {
            etAmount.setError("Số tiền phải lớn hơn 0!");
            etAmount.requestFocus();
            return;
        }

        String id = String.valueOf(System.currentTimeMillis());
        Expense expense = new Expense(id, title, amount, category, date, note);
        expenseManager.addExpense(expense);

        Toast.makeText(this, "Đã thêm khoản chi mới!", Toast.LENGTH_SHORT).show();
        finish();
    }
}