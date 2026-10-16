package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.model.Expense;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExpenseManager {
    private static final String PREF_NAME = "ExpenseManagerPrefs";
    private static final String KEY_EXPENSES = "expenses_data";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public ExpenseManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();

        if (!pref.contains(KEY_EXPENSES)) {
            initSampleData();
        }
    }

    public List<Expense> getExpenses() {
        List<Expense> expenseList = new ArrayList<>();
        String jsonStr = pref.getString(KEY_EXPENSES, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Expense expense = Expense.fromJsonObject(obj);
                if (expense != null) {
                    expenseList.add(expense);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return expenseList;
    }

    public void saveExpenses(List<Expense> expenses) {
        JSONArray jsonArray = new JSONArray();
        for (Expense expense : expenses) {
            jsonArray.put(expense.toJsonObject());
        }
        editor.putString(KEY_EXPENSES, jsonArray.toString());
        editor.apply();
    }

    public void addExpense(Expense expense) {
        List<Expense> expenses = getExpenses();
        expenses.add(expense);
        saveExpenses(expenses);
    }

    public void deleteExpense(String expenseId) {
        List<Expense> expenses = getExpenses();
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId().equals(expenseId)) {
                expenses.remove(i);
                break;
            }
        }
        saveExpenses(expenses);
    }

    public double getTotalAmount() {
        List<Expense> expenses = getExpenses();
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    public String getFormattedTotalAmount() {
        double total = getTotalAmount();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        return decimalFormat.format((long) total) + " đ";
    }

    private void initSampleData() {
        List<Expense> sampleList = new ArrayList<>();
        sampleList.add(new Expense("1", "Ăn sáng", 30000, "Ăn uống", "12/10/2026", "Phở bò sáng"));
        sampleList.add(new Expense("2", "Đổ xăng", 50000, "Đi lại", "12/10/2026", "Xăng A95"));
        sampleList.add(new Expense("3", "Mua sách", 120000, "Học tập", "13/10/2026", "Sách Lập trình Android"));
        saveExpenses(sampleList);
    }
}