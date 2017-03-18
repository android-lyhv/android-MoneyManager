package com.dut.moneytracker.objects;

import java.util.Date;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 15/03/2017.
 */

public class Filter {
    private Date dateFilter;
    private String accountId;
    private int viewType;
    private boolean isRequestByAccount;

    public boolean isRequestByAccount() {
        return isRequestByAccount;
    }

    public void setRequestByAccount(boolean requestByAccount) {
        isRequestByAccount = requestByAccount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Date getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(Date dateFilter) {
        this.dateFilter = dateFilter;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
