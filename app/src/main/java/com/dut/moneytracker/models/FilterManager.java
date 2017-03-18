package com.dut.moneytracker.models;

import com.dut.moneytracker.constant.TypeView;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.utils.DateTimeUtils;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 15/03/2017.
 */
public class FilterManager {
    private static FilterManager ourInstance = new FilterManager();

    public static FilterManager getInstance() {
        return ourInstance;
    }

    private FilterManager() {
    }

    public Filter getFilterDefault() {
        Filter filter = new Filter();
        filter.setRequestByAccount(false);
        filter.setDateFilter(new Date());
        filter.setViewType(TypeView.DAY);
        return filter;
    }

    public Filter getFilterDefaultAccount(String idAccount) {
        Filter filter = new Filter();
        filter.setRequestByAccount(true);
        filter.setAccountId(idAccount);
        filter.setDateFilter(new Date());
        filter.setViewType(TypeView.DAY);
        return filter;
    }

    public String getLabel(Filter filter, List<Exchange> exchanges) {
        String amount = AccountManager.getInstance().getTotalAmountByListExchange(exchanges);
        String convert = CurrencyUtils.getInstance().getStringMoneyType(amount, "VND");
        String date = "";
        switch (filter.getViewType()) {
            case TypeView.ALL:
                date = "ALL";
                break;
            case TypeView.DAY:
                date = DateTimeUtils.getInstance().getStringFullDate(filter.getDateFilter());
                break;
            case TypeView.MONTH:
                date = DateTimeUtils.getInstance().getStringMonthYear(filter.getDateFilter());
                break;
            case TypeView.YEAR:
                date = DateTimeUtils.getInstance().getStringYear(filter.getDateFilter());
                break;
        }
        String label = String.format(Locale.US, "%s\n%s", date, convert);
        return label;
    }

    /**
     * @param filter
     * @param
     * @return
     */

    public Filter changeFilter(Filter filter, int  steps) {
        Date newDate = DateTimeUtils.getInstance().changeStepMonth(filter.getDateFilter(), filter.getViewType(), steps);
        filter.setDateFilter(newDate);
        return filter;
    }
}
