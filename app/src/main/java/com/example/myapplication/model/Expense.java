package com.example.myapplication.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Expense implements Serializable {
    private String id;
    private String title;
    private double amount;
    private String category;
    private String date;
    private String note;

    public Expense(String id, String title, double amount, String category, String date, String note) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFormattedAmount() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        return decimalFormat.format((long) amount) + " đ";
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("title", title);
            jsonObject.put("amount", amount);
            jsonObject.put("category", category);
            jsonObject.put("date", date);
            jsonObject.put("note", note);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Expense fromJsonObject(JSONObject jsonObject) {
        try {
            String id = jsonObject.optString("id", String.valueOf(System.currentTimeMillis()));
            String title = jsonObject.optString("title", "");
            double amount = jsonObject.optDouble("amount", 0);
            String category = jsonObject.optString("category", "Khác");
            String date = jsonObject.optString("date", "");
            String note = jsonObject.optString("note", "");
            return new Expense(id, title, amount, category, date, note);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}