package com.dut.moneytracker.activities.interfaces;

public interface AddListener {

    void onAddExpensesOrIncome();

    void onAddTransfer();

    boolean isAvailableIncomeAndExpense();

    void onSaveDataBase();

    void onUpToServer();
}