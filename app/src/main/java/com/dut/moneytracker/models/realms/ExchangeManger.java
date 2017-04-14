package com.dut.moneytracker.models.realms;

import android.text.TextUtils;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.constant.FilterType;
import com.dut.moneytracker.constant.PieChartType;
import com.dut.moneytracker.objects.Exchange;
import com.dut.moneytracker.objects.Filter;
import com.dut.moneytracker.objects.GroupCategory;
import com.dut.moneytracker.ui.charts.objects.ValueLineChart;
import com.dut.moneytracker.ui.charts.objects.ValuePieChart;
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

    public void insertOrUpdate(Exchange object) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
    }

    public void deleteExchangeByAccount(String idAccount) {
        realm.beginTransaction();
        Exchange exchange = realm.where(Exchange.class).equalTo("idAccount", idAccount).findFirst();
        if (exchange != null) {
            exchange.deleteFromRealm();
        }
        realm.commitTransaction();
    }

    public String getAmountExchangeByDebit(String idDebit) {
        BigDecimal bigDecimal = new BigDecimal("0");
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).equalTo("idDebit", idDebit).notEqualTo("id", idDebit).findAll();
        for (Exchange exchange : realmResults) {
            bigDecimal = bigDecimal.add(new BigDecimal(exchange.getAmount()));
        }
        return bigDecimal.toString();
    }

    public void deleteExchangeById(String id) {
        realm.beginTransaction();
        Exchange exchange = realm.where(Exchange.class).equalTo("id", id).findFirst();
        exchange.deleteFromRealm();
        realm.commitTransaction();
    }

    public void deleteExchangeByDebitId(String idDebit) {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).equalTo("idDebit", idDebit).findAll();
        realmResults.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public RealmResults<Exchange> getExchanges() {
        realm.beginTransaction();
        RealmResults<Exchange> realmResults = realm.where(Exchange.class).findAllSorted("created", Sort.DESCENDING);
        realm.commitTransaction();
        return realmResults;
    }

    public RealmResults<Exchange> getExchangesByAccount(String accountID) {
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
    public List<ValuePieChart> getFilterValuePieCharts(Filter filter, int type) {
        List<Exchange> exchanges = getExchanges(filter);
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

    public RealmResults<Exchange> getExchanges(Filter filter) {
        boolean isRequestAccount = filter.isRequestByAccount();
        if (!isRequestAccount) {
            return getExchangesFilter(filter);
        } else {
            return getExchangesFilterByAccount(filter);
        }
    }

    public RealmResults<Exchange> getExchangesFilterByAccount(Filter filter) {
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

    public RealmResults<Exchange> getExchangesFilter(Filter filter) {
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
}
