package com.dut.moneytracker.activities.interfaces;

public interface AddListener {
    void onAddIncome();

    void onAddExpenses();

    void onAddTransfer();

    boolean onCheckFieldAvailable();

    void onSaveDataBase();

    void onUpToServer();
}