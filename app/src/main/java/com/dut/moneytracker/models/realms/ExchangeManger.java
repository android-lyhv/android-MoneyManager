package com.dut.moneytracker.models.realms;

import android.text.TextUtils;

import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.constant.FilterType;
import com.dut.moneytracker.constant.PieChartType;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.objects.GroupCategory;
import com.dut.moneytracker.ui.charts.objects.ValueCategoryChart;
import com.dut.moneytracker.ui.charts.objects.ValueLineChart;
import com.dut.moneytracker.ui.charts.objects.ValuePieChart;
import com.dut.moneytracker.utils.DateTimeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    /**
     * Sync with firebase
     */
    public void insertOrUpdate(Exchange object) {
        realm.beginTransaction();
        realm.insertOrUpdate(object);
        realm.commitTransaction();
    }

    void updateExchangeByDebit(int idDebit, String newIdAccount) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).equalTo("idDebit", idDebit).findAll();
        realm.commitTransaction();
        for (Exchange exchange : realmResults) {
            exchange.setIdAccount(newIdAccount);
            insertOrUpdate(exchange);
        }
    }

    public void deleteExchangeById(String id) {
        realm.beginTransaction();
        Exchange exchange = realm.where(Exchange.class).equalTo("id", id).findFirst();
        exchange.deleteFromRealm();
        realm.commitTransaction();
    }

    void deleteExchangeByDebit(int idDebit) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).equalTo("idDebit", idDebit).findAll();
        realmResults.deleteAllFromRealm();
        realm.commitTransaction();
    }

    void deleteExchangeByAccount(String idAccount) {
        realm.beginTransaction();
        Exchange exchange = realm.where(Exchange.class).equalTo("idAccount", idAccount).findFirst();
        if (exchange != null) {
            exchange.deleteFromRealm();
        }
        realm.commitTransaction();
    }

    /*****************************************/

    public String getAmountExchangeByDebit(int idDebit) {
        BigDecimal bigDecimal = new BigDecimal("0");
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).equalTo("idDebit", idDebit).notEqualTo("id", String.valueOf(idDebit)).findAll();
        for (Exchange exchange : realmResults) {
            bigDecimal = bigDecimal.add(new BigDecimal(exchange.getAmount()));
        }
        return bigDecimal.toString();
    }

    public RealmResults<Exchange> getExchanges() {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    public RealmResults<Exchange> onLoadExchangeByDebit(int idDebit) {
        return realm.where(Exchange.class).equalTo("idDebit", idDebit).findAllSortedAsync("created", Sort.DESCENDING);
    }

    private RealmResults<Exchange> getExchangesByAccount(String accountID) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).equalTo("idAccount", accountID).findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    public RealmResults<Exchange> onLoadExchangeAsync(int limit) {
        return realm.where(Exchange.class).findAllSortedAsync("created", Sort.DESCENDING);
    }


    public RealmResults<Exchange> onLoadExchangeAsyncByAccount(String accountID, int limit) {
        return realm.where(Exchange.class).equalTo("idAccount", accountID).findAllSortedAsync("created", Sort.DESCENDING);
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
    public List<ValuePieChart> getFilterValuePieCharts(Filter filter, int chartType) {
        List<Exchange> exchanges = getExchanges(filter);
        List<ValuePieChart> valuePieCharts = new ArrayList<>();
        List<GroupCategory> groupCategories = CategoryManager.getInstance().getGroupCategory();
        if (chartType == PieChartType.INCOME) {
            for (GroupCategory groupCategory : groupCategories) {
                ValuePieChart valuePieChart = getValePieChart(exchanges, groupCategory, ExchangeType.INCOME);
                if (valuePieChart != null) {
                    valuePieCharts.add(valuePieChart);
                }
            }
        }
        if (chartType == PieChartType.EXPENSES) {
            for (GroupCategory groupCategory : groupCategories) {
                ValuePieChart valuePieChart = getValePieChart(exchanges, groupCategory, ExchangeType.EXPENSES);
                if (valuePieChart != null) {
                    valuePieCharts.add(valuePieChart);
                }
            }
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

/*
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
*/

    public RealmResults<Exchange> getExchanges(Filter filter) {
        boolean isRequestAccount = filter.isRequestByAccount();
        if (!isRequestAccount) {
            return getExchangesFilter(filter);
        } else {
            return getExchangesFilterByAccount(filter);
        }
    }

    private RealmResults<Exchange> getExchangesFilterByAccount(Filter filter) {
        int filterType = filter.getTypeFilter();
        String accountID = filter.getAccountId();
        Date date = filter.getDateFilter();
        switch (filterType) {
            case FilterType.ALL:
                return getExchangesByAccount(accountID);
            case FilterType.DAY:
            case FilterType.MONTH:
            case FilterType.YEAR:
                return getExchangesSameDate(accountID, date, filterType);
            case FilterType.CUSTOM:
                Date fromDate = filter.getFormDate();
                Date toDate = filter.getToDate();
                return getExchangesCustom(accountID, fromDate, toDate);
        }
        return getExchanges();
    }

    private RealmResults<Exchange> getExchangesFilter(Filter filter) {
        int filterType = filter.getTypeFilter();
        Date date = filter.getDateFilter();
        switch (filterType) {
            case FilterType.ALL:
                return getExchanges();
            case FilterType.DAY:
            case FilterType.MONTH:
            case FilterType.YEAR:
                return getExchangesSameDate(date, filterType);
            case FilterType.CUSTOM:
                Date fromDate = filter.getFormDate();
                Date toDate = filter.getToDate();
                return getExchangesCustom(fromDate, toDate);
        }
        return getExchanges();
    }

    private RealmResults<Exchange> getExchangesCustom(String idAccount, Date fromDate, Date toDate) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).like("idAccount", idAccount).between("created", fromDate, toDate).findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    private RealmResults<Exchange> getExchangesCustom(Date fromDate, Date toDate) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).between("created", fromDate, toDate).findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    private RealmResults<Exchange> getExchangesSameDate(String accountID, Date date, int typeFilter) {
        Date[] dates = DateTimeUtils.getInstance().geStartDateAndEndDate(date, typeFilter);
        Date startDate = dates[0];
        Date endDate = dates[1];
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).equalTo("idAccount", accountID).between("created", startDate, endDate).findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    private RealmResults<Exchange> getExchangesSameDate(Date date, int typeFilter) {
        Date[] dates = DateTimeUtils.getInstance().geStartDateAndEndDate(date, typeFilter);
        Date startDate = dates[0];
        Date endDate = dates[1];
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).between("created", startDate, endDate).findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    // This for category
    public List<ValueCategoryChart> getValueCategoryCharts(Filter filter) {
        RealmResults<Category> categories = CategoryManager.getInstance().getCategories();
        List<ValueCategoryChart> valueCategoryCharts = new ArrayList<>();
        for (Category category : categories) {
            RealmResults<Exchange> exchanges = getExchanges(filter, category);
            if (exchanges.size() > 0) {
                ValueCategoryChart valueCategoryChart = new ValueCategoryChart();
                valueCategoryChart.setCategory(category);
                valueCategoryChart.setAmount(AccountManager.getInstance().getTotalAmountExchanges(exchanges));
                valueCategoryCharts.add(valueCategoryChart);
            }
        }
        return valueCategoryCharts;
    }

    public RealmResults<Exchange> getExchanges(Filter filter, Category category) {
        boolean isRequestAccount = filter.isRequestByAccount();
        if (!isRequestAccount) {
            return getExchangesFilter(filter, category);
        } else {
            return getExchangesFilterByAccount(filter, category);
        }
    }

    private RealmResults<Exchange> getExchangesFilterByAccount(Filter filter, Category category) {
        int filterType = filter.getTypeFilter();
        String accountID = filter.getAccountId();
        Date date = filter.getDateFilter();
        switch (filterType) {
            case FilterType.ALL:
                return getExchangesByAccount(accountID, category);
            case FilterType.DAY:
            case FilterType.MONTH:
            case FilterType.YEAR:
                return getExchangesSameDate(accountID, date, filterType, category);
            case FilterType.CUSTOM:
                Date fromDate = filter.getFormDate();
                Date toDate = filter.getToDate();
                return getExchangesCustom(accountID, fromDate, toDate, category);
        }
        return getExchanges();
    }

    private RealmResults<Exchange> getExchangesFilter(Filter filter, Category category) {
        int filterType = filter.getTypeFilter();
        Date date = filter.getDateFilter();
        switch (filterType) {
            case FilterType.ALL:
                return getExchanges(category);
            case FilterType.DAY:
            case FilterType.MONTH:
            case FilterType.YEAR:
                return getExchangesSameDate(date, filterType, category);
            case FilterType.CUSTOM:
                Date fromDate = filter.getFormDate();
                Date toDate = filter.getToDate();
                return getExchangesCustom(fromDate, toDate, category);
        }
        return getExchanges();
    }

    private RealmResults<Exchange> getExchangesCustom(String idAccount, Date fromDate, Date toDate, Category category) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class)
                .equalTo("idCategory", category.getId())
                .equalTo("typeExchange", ExchangeType.EXPENSES)
                .equalTo("idAccount", idAccount)
                .between("created", fromDate, toDate)
                .findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    private RealmResults<Exchange> getExchangesCustom(Date fromDate, Date toDate, Category category) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class)
                .equalTo("idCategory", category.getId())
                .equalTo("typeExchange", ExchangeType.EXPENSES)
                .between("created", fromDate, toDate)
                .findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    private RealmResults<Exchange> getExchangesSameDate(String accountID, Date date, int typeFilter, Category category) {
        Date[] dates = DateTimeUtils.getInstance().geStartDateAndEndDate(date, typeFilter);
        Date startDate = dates[0];
        Date endDate = dates[1];
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class)
                .equalTo("idCategory", category.getId())
                .equalTo("typeExchange", ExchangeType.EXPENSES)
                .equalTo("idAccount", accountID)
                .between("created", startDate, endDate)
                .findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    private RealmResults<Exchange> getExchangesSameDate(Date date, int typeFilter, Category category) {
        Date[] dates = DateTimeUtils.getInstance().geStartDateAndEndDate(date, typeFilter);
        Date startDate = dates[0];
        Date endDate = dates[1];
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class)
                .equalTo("idCategory", category.getId())
                .equalTo("typeExchange", ExchangeType.EXPENSES)
                .between("created", startDate, endDate)
                .findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    private RealmResults<Exchange> getExchangesByAccount(String accountID, Category category) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class)
                .equalTo("idCategory", category.getId())
                .equalTo("typeExchange", ExchangeType.EXPENSES)
                .equalTo("idAccount", accountID)
                .findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    private RealmResults<Exchange> getExchanges(Category category) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class)
                .equalTo("idCategory", category.getId())
                .equalTo("typeExchange", ExchangeType.EXPENSES)
                .findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    public void sortByAmountExchange(List<ValueCategoryChart> valueCategoryCharts) {
        Collections.sort(valueCategoryCharts, new Comparator<ValueCategoryChart>() {
            @Override
            public int compare(ValueCategoryChart v1, ValueCategoryChart v2) {
                String str1 = v1.getAmount();
                String str2 = v2.getAmount();
                if (str1.startsWith("-")) {
                    str1 = str1.substring(1);
                }
                if (str2.startsWith("-")) {
                    str2 = str2.substring(1);
                }
                float amount1 = CurrencyUtils.getInstance().getFloatMoney(str1);
                float amount2 = CurrencyUtils.getInstance().getFloatMoney(str2);
                if (amount1 > amount2) {
                    return -1;
                }
                if (amount1 == amount2) {
                    return 0;
                }
                return 1;
            }
        });
    }

    public Date getMinDate() {
        realm.beginTransaction();
        long time = realm.where(Exchange.class).minimumDate("created").getTime();
        realm.commitTransaction();
        return new Date(time);
    }

    public Date getMaxDate() {
        realm.beginTransaction();
        long time = realm.where(Exchange.class).minimumDate("created").getTime();
        realm.commitTransaction();
        return new Date(time);
    }
}
