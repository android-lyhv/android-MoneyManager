package com.dut.moneytracker.models.realms;

import android.text.TextUtils;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.constant.FilterType;
import com.dut.moneytracker.constant.PieChartType;
import com.dut.moneytracker.models.charts.ValueLineChart;
import com.dut.moneytracker.models.charts.ValuePieChart;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.objects.GroupCategory;
import com.dut.moneytracker.utils.DateTimeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 06/03/2017.
 */
public class ExchangeManger extends RealmHelper {
    private static ExchangeManger ourInstance;

    public static ExchangeManger getInstance() {
        if (ourInstance == null) {
            ourInstance = new ExchangeManger();
        }
        return ourInstance;
    }

    private ExchangeManger() {
    }


    public void deleteExchangeById(String id) {
        realm.beginTransaction();
        Exchange exchange = realm.where(Exchange.class).equalTo("id", id).findFirst();
        exchange.deleteFromRealm();
        realm.commitTransaction();
    }

    public List<Exchange> getFilterExchanges() {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAll();
        realmResults = realmResults.sort("created", Sort.DESCENDING);
        List<Exchange> exchanges = realmResults.subList(0, realmResults.size());
        realm.commitTransaction();
        return exchanges;
    }

    public List<Exchange> getExchangesByAccount(String accountID) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).equalTo("idAccount", accountID).findAllSorted("created", Sort.DESCENDING);
        List<Exchange> exchanges = realmResults.subList(0, realmResults.size());
        realm.commitTransaction();
        return exchanges;
    }

    public List<Exchange> getExchangesLimit(int limit) {
        realm.beginTransaction();
        List<Exchange> exchanges;
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAll();
        realmResults = realmResults.sort("created", Sort.DESCENDING);
        if (limit >= realmResults.size()) {
            exchanges = realmResults.subList(0, realmResults.size());
        } else {
            exchanges = realmResults.subList(0, limit);
        }
        realm.commitTransaction();
        return exchanges;
    }

    public List<Exchange> getExchangesLimitByAccount(String accountID, int limit) {
        realm.beginTransaction();
        List<Exchange> exchanges;
        RealmResults<Exchange> realmList = realm.where(Exchange.class).equalTo("idAccount", accountID).findAllSorted("created", Sort.DESCENDING);
        if (limit >= realmList.size()) {
            exchanges = realmList.subList(0, realmList.size());
        } else {
            exchanges = realmList.subList(0, limit);
        }
        realm.commitTransaction();
        return exchanges;
    }

    public List<ValueLineChart> getValueChartByDailyDay(int limitDay) {
        List<Date> dates = DateTimeUtils.getInstance().getListLastDay(limitDay);
        List<ValueLineChart> valueLineCharts = new ArrayList<>();
        int size = dates.size();
        for (int i = 0; i < size; i++) {
            String amount = AccountManager.getInstance().getAmountAvailableByDate(dates.get(i));
            valueLineCharts.add(new ValueLineChart(dates.get(i), amount, DateTimeUtils.getInstance().getStringDayMonthUs(dates.get(i))));
        }
        return valueLineCharts;
    }

    public List<ValueLineChart> getValueChartByDailyDay(String idAccount, int limitDay) {
        List<Date> dates = DateTimeUtils.getInstance().getListLastDay(limitDay);
        List<ValueLineChart> valueLineCharts = new ArrayList<>();
        int size = dates.size();
        for (int i = 0; i < size; i++) {
            String amount = AccountManager.getInstance().getAmountAvailableByDate(idAccount, dates.get(i));
            valueLineCharts.add(new ValueLineChart(dates.get(i), amount, DateTimeUtils.getInstance().getStringDayMonthUs(dates.get(i))));
        }
        return valueLineCharts;
    }

    /**
     * get list exchange for chart pie
     *
     * @param filter
     */
    public List<ValuePieChart> getFilterValuePieCharts(Filter filter, int type) {
        List<Exchange> exchanges = getExchangesFilter(filter);
        List<ValuePieChart> valuePieCharts = new ArrayList<>();
        List<GroupCategory> groupCategories = CategoryManager.getInstance().getGroupCategory();
        if (type == PieChartType.INCOME) {
            for (GroupCategory groupCategory : groupCategories) {
                ValuePieChart valuePieChart = getValePieChart(exchanges, groupCategory, ExchangeType.INCOME);
                if (valuePieChart != null) {
                    valuePieCharts.add(valuePieChart);
                }
            }
        }
        if (type == PieChartType.EXPENSES) {
            for (GroupCategory groupCategory : groupCategories) {
                ValuePieChart valuePieChart = getValePieChart(exchanges, groupCategory, ExchangeType.EXPENSES);
                if (valuePieChart != null) {
                    valuePieCharts.add(valuePieChart);
                }
            }
        }
        ValuePieChart valuePieChart = getValePieChartTransfer(exchanges, type);
        if (valuePieChart != null) {
            valuePieCharts.add(valuePieChart);
        }
        return valuePieCharts;
    }

    private ValuePieChart getValePieChart(List<Exchange> exchanges, GroupCategory groupCategory, int exchangeType) {
        ValuePieChart valuePieChart = new ValuePieChart();
        BigDecimal bigDecimal = new BigDecimal("0");
        List<String> idCategories = CategoryManager.getInstance().getListIdCategoryByGroupId(groupCategory.getId());
        for (String idCategory : idCategories) {
            for (Exchange exchange : exchanges) {
                if (TextUtils.equals(idCategory, exchange.getIdCategory()) && exchange.getTypeExchange() == exchangeType) {
                    bigDecimal = bigDecimal.add(new BigDecimal(exchange.getAmount()));
                }
            }
        }
        if (bigDecimal.floatValue() == 0f) {
            return null;
        }
        if (bigDecimal.toString().startsWith("-")) {
            bigDecimal = new BigDecimal(bigDecimal.toString().substring(1));
        }
        valuePieChart.setColorGroup(groupCategory.getColorCode());
        valuePieChart.setNameGroup(groupCategory.getName());
        valuePieChart.setAmount(bigDecimal.floatValue());
        valuePieChart.setAmountString(bigDecimal.toString());
        return valuePieChart;
    }

    private ValuePieChart getValePieChartTransfer(List<Exchange> exchanges, int type) {
        ValuePieChart valuePieChart = new ValuePieChart();
        BigDecimal bigDecimal = new BigDecimal("0");
        if (type == PieChartType.EXPENSES) {
            for (Exchange exchange : exchanges) {
                if (exchange.getTypeExchange() == ExchangeType.TRANSFER && exchange.getAmount().startsWith("-")) {
                    bigDecimal = bigDecimal.add(new BigDecimal(exchange.getAmount().substring(1)));
                }
            }
        }
        if (type == PieChartType.INCOME) {
            for (Exchange exchange : exchanges) {
                if (exchange.getTypeExchange() == ExchangeType.TRANSFER && !exchange.getAmount().startsWith("-")) {
                    bigDecimal = bigDecimal.add(new BigDecimal(exchange.getAmount()));
                }
            }
        }
        if (bigDecimal.floatValue() == 0f) {
            return null;
        }
        valuePieChart.setColorGroup(R.color.color_transfer);
        valuePieChart.setNameGroup("Chuyển khoản");
        valuePieChart.setAmount(bigDecimal.floatValue());
        valuePieChart.setAmountString(bigDecimal.toString());
        return valuePieChart;
    }

    public List<Exchange> getFilterExchanges(Filter filter) {
        boolean isRequestAccount = filter.isRequestByAccount();
        if (!isRequestAccount) {
            return getExchangesFilter(filter);
        } else {
            return getExchangesFilterByAccount(filter);
        }
    }

    public List<Exchange> getExchangesFilterByAccount(Filter filter) {
        int viewType = filter.getViewType();
        String accountID = filter.getAccountId();
        Date date = filter.getDateFilter();
        switch (viewType) {
            case FilterType.ALL:
                return getExchangesByAccount(accountID);
            case FilterType.DAY:
                return getExchangesSameDay(accountID, date);
            case FilterType.MONTH:
                return getExchangesSameMonth(accountID, date);
            case FilterType.YEAR:
                return getExchangesSameYear(accountID, date);
            case FilterType.CUSTOM:
                Date fromDate = filter.getFormDate();
                Date toDate = filter.getToDate();
                return getExchangesCustom(accountID, fromDate, toDate);
        }
        return new ArrayList<>();
    }

    public List<Exchange> getExchangesFilter(Filter filter) {
        int viewType = filter.getViewType();
        Date date = filter.getDateFilter();
        switch (viewType) {
            case FilterType.ALL:
                return getFilterExchanges();
            case FilterType.DAY:
                return getExchangesSameDay(date);
            case FilterType.MONTH:
                return getExchangesSameMonth(date);
            case FilterType.YEAR:
                return getExchangesSameYear(date);
            case FilterType.CUSTOM:
                Date fromDate = filter.getFormDate();
                Date toDate = filter.getToDate();
                return getExchangesCustom(fromDate, toDate);
        }
        return new ArrayList<>();
    }

    private List<Exchange> getExchangesCustom(String idAccount, Date fromDate, Date toDate) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).like("idAccount", idAccount).between("created", fromDate, toDate).findAllSorted("created", Sort.DESCENDING);
        List<Exchange> exchangesNew = realmResults.subList(0, realmResults.size());
        realm.commitTransaction();
        return exchangesNew;
    }

    private List<Exchange> getExchangesCustom(Date fromDate, Date toDate) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).between("created", fromDate, toDate).findAllSorted("created", Sort.DESCENDING);
        List<Exchange> exchangesNew = realmResults.subList(0, realmResults.size());
        realm.commitTransaction();
        return exchangesNew;
    }

    private List<Exchange> getExchangesSameDay(Date date) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAllSorted("created", Sort.DESCENDING);
        List<Exchange> exchangesNew = new ArrayList<>();
        for (Exchange exchange : realmResults) {
            if (DateTimeUtils.getInstance().isSameDate(exchange.getCreated(), date)) {
                exchangesNew.add(exchange);
            }
        }
        realm.commitTransaction();
        return exchangesNew;
    }

    private List<Exchange> getExchangesSameMonth(Date date) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAllSorted("created", Sort.DESCENDING);
        List<Exchange> exchangesNew = new ArrayList<>();
        for (Exchange exchange : realmResults) {
            if (DateTimeUtils.getInstance().isSameMonth(exchange.getCreated(), date)) {
                exchangesNew.add(exchange);
            }
        }
        realm.commitTransaction();
        return exchangesNew;
    }

    private List<Exchange> getExchangesSameYear(Date date) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAllSorted("created", Sort.DESCENDING);
        List<Exchange> exchangesNew = new ArrayList<>();
        for (Exchange exchange : realmResults) {
            if (DateTimeUtils.getInstance().isSameYear(exchange.getCreated(), date)) {
                exchangesNew.add(exchange);
            }
        }
        realm.commitTransaction();
        return exchangesNew;
    }

    private List<Exchange> getExchangesSameDay(String accountID, Date date) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).equalTo("idAccount", accountID).findAllSorted("created", Sort.DESCENDING);
        List<Exchange> result = new ArrayList<>();
        for (Exchange exchange : realmResults) {
            if (DateTimeUtils.getInstance().isSameDate(exchange.getCreated(), date)) {
                result.add(exchange);
            }
        }
        realm.commitTransaction();
        return result;
    }

    private List<Exchange> getExchangesSameMonth(String accountID, Date date) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).equalTo("idAccount", accountID).findAllSorted("created", Sort.DESCENDING);
        List<Exchange> result = new ArrayList<>();
        for (Exchange exchange : realmResults) {
            if (DateTimeUtils.getInstance().isSameMonth(exchange.getCreated(), date)) {
                result.add(exchange);
            }
        }
        realm.commitTransaction();
        return result;
    }

    private List<Exchange> getExchangesSameYear(String accountID, Date date) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).equalTo("idAccount", accountID).findAllSorted("created", Sort.DESCENDING);
        List<Exchange> result = new ArrayList<>();
        for (Exchange exchange : realmResults) {
            if (DateTimeUtils.getInstance().isSameYear(exchange.getCreated(), date)) {
                result.add(exchange);
            }
        }
        realm.commitTransaction();
        return result;
    }
}
