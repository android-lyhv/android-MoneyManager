package com.dut.moneytracker.models.realms;

import android.text.TextUtils;

import com.dut.moneytracker.constant.FilterType;
import com.dut.moneytracker.currency.CurrencyUtils;
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
        filter.setTypeFilter(FilterType.DAY);
        return filter;
    }

    public Filter getFilterDefaultAccount(String idAccount) {
        Filter filter = new Filter();
        filter.setRequestByAccount(true);
        filter.setAccountId(idAccount);
        filter.setDateFilter(new Date());
        filter.setTypeFilter(FilterType.DAY);
        return filter;
    }

    /**
     * @param filter
     * @param exchanges
     * @return if exchanges == null not append amount value
     */
    public String getLabel(Filter filter, List<Exchange> exchanges) {
        String amountTitle = "";
        String timeTitle = "";
        if (exchanges != null) {
            String amount = AccountManager.getInstance().getTotalAmountExchanges(exchanges);
            amountTitle = CurrencyUtils.getInstance().getStringMoneyFormat(amount, CurrencyUtils.DEFAULT_CURRENCY_CODE);
        }
        switch (filter.getTypeFilter()) {
            case FilterType.ALL:
                Date minDate = ExchangeManger.getInstance().getMinDate(filter.getAccountId());
                Date maxDate = ExchangeManger.getInstance().getMaxDate(filter.getAccountId());
                String fromDate = DateTimeUtils.getInstance().getStringDateUs(minDate);
                String toDate = DateTimeUtils.getInstance().getStringDateUs(maxDate);
                if (DateTimeUtils.getInstance().isSameDate(new Date(), maxDate)) {
                    toDate = "Hôm nay";
                }
                if (DateTimeUtils.getInstance().isSameDate(minDate, maxDate)) {
                    timeTitle = toDate;
                } else {
                    timeTitle = String.format(Locale.US, "%s đến %s", fromDate, toDate);
                }
                break;
            case FilterType.DAY:
                timeTitle = DateTimeUtils.getInstance().getStringFullDateVn(filter.getDateFilter());
                break;
            case FilterType.MONTH:
                timeTitle = DateTimeUtils.getInstance().getStringMonthYear(filter.getDateFilter());
                break;
            case FilterType.YEAR:
                timeTitle = DateTimeUtils.getInstance().getStringYear(filter.getDateFilter());
                break;
            case FilterType.CUSTOM:
                String fromDateCustom = DateTimeUtils.getInstance().getStringDateUs(filter.getFormDate());
                String toDateCustom = DateTimeUtils.getInstance().getStringDateUs(filter.getToDate());
                if (DateTimeUtils.getInstance().isSameDate(filter.getFormDate(), filter.getToDate())) {
                    timeTitle = fromDateCustom;
                } else {
                    timeTitle = String.format(Locale.US, "%s đến %s", fromDateCustom, toDateCustom);
                }
                break;
        }
        if (TextUtils.isEmpty(amountTitle)) {
            return timeTitle;
        }
        return String.format(Locale.US, "%s\n%s", timeTitle, amountTitle);
    }


    public Filter changeFilter(final Filter currentFilter, int steps) {
        Filter filter = copyFilter(currentFilter);
        int type = filter.getTypeFilter();
        if (type == FilterType.ALL || type == FilterType.CUSTOM) {
            return filter;
        }
        Date newDate = DateTimeUtils.getInstance().changeDateStep(filter.getDateFilter(), filter.getTypeFilter(), steps);
        filter.setDateFilter(newDate);
        return filter;
    }

    private Filter copyFilter(Filter currentFilter) {
        Filter filter = new Filter();
        filter.setAccountId(currentFilter.getAccountId());
        filter.setRequestByAccount(currentFilter.isRequestByAccount());
        filter.setTypeFilter(currentFilter.getTypeFilter());
        filter.setDateFilter(currentFilter.getDateFilter());
        filter.setToDate(currentFilter.getToDate());
        filter.setFormDate(currentFilter.getFormDate());
        return filter;
    }
}
