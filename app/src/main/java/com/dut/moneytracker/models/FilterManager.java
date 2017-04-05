package com.dut.moneytracker.models;

import com.dut.moneytracker.constant.FilterType;
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
    private static final String TAG = FilterManager.class.getSimpleName();
    private static FilterManager ourInstance;

    public static FilterManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new FilterManager();
        }
        return ourInstance;
    }

    private FilterManager() {
    }

    public Filter getFilterDefault() {
        Filter filter = new Filter();
        filter.setRequestByAccount(false);
        filter.setDateFilter(new Date());
        filter.setViewType(FilterType.DAY);
        return filter;
    }

    public Filter getFilterDefaultAccount(String idAccount) {
        Filter filter = new Filter();
        filter.setRequestByAccount(true);
        filter.setAccountId(idAccount);
        filter.setDateFilter(new Date());
        filter.setViewType(FilterType.DAY);
        return filter;
    }

    public String getLabel(Filter filter, List<Exchange> exchanges) {
        String amount = AccountManager.getInstance().getTotalAmountByListExchange(exchanges);
        String convert = CurrencyUtils.getInstance().getStringMoneyType(amount, "VND");
        String date = "";
        switch (filter.getViewType()) {
            case FilterType.ALL:
                date = "Tất cả";
                break;
            case FilterType.DAY:
                date = DateTimeUtils.getInstance().getStringFullDate(filter.getDateFilter());
                break;
            case FilterType.MONTH:
                date = DateTimeUtils.getInstance().getStringMonthYear(filter.getDateFilter());
                break;
            case FilterType.YEAR:
                date = DateTimeUtils.getInstance().getStringYear(filter.getDateFilter());
                break;
            case FilterType.CUSTOM:
                String fromDate = DateTimeUtils.getInstance().getStringDateUs(filter.getFormDate());
                String toDate = DateTimeUtils.getInstance().getStringDateUs(filter.getToDate());
                date = String.format(Locale.US, "%s đến %s", fromDate, toDate);
                break;
        }
        return String.format(Locale.US, "%s\n%s", date, convert);
    }


    public Filter changeFilter(final Filter currentFilter, int steps) {
        Filter filter = copyFilter(currentFilter);
        int type = filter.getViewType();
        if (type == FilterType.ALL || type == FilterType.CUSTOM) {
            return filter;
        }
        Date newDate = DateTimeUtils.getInstance().changeDateStep(filter.getDateFilter(), filter.getViewType(), steps);
        filter.setDateFilter(newDate);
        return filter;
    }

    private Filter copyFilter(Filter currentFilter) {
        Filter filter = new Filter();
        filter.setAccountId(currentFilter.getAccountId());
        filter.setRequestByAccount(currentFilter.isRequestByAccount());
        filter.setViewType(currentFilter.getViewType());
        filter.setDateFilter(currentFilter.getDateFilter());
        filter.setToDate(currentFilter.getToDate());
        filter.setFormDate(currentFilter.getFormDate());
        return filter;
    }
}
