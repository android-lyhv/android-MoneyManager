package com.dut.moneytracker.ui.interfaces;

public interface AddListener {

    void onAddExpensesOrIncome();

    void onAddTransfer();

    boolean isAvailableIncomeAndExpense();

    void onSaveDataBase();

    void onUpToServer();
}