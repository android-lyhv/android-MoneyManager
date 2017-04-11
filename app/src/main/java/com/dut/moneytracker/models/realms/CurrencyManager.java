package com.dut.moneytracker.models.realms;


import android.content.Context;
import android.util.Log;

import com.dut.moneytracker.objects.Currency;
import com.dut.moneytracker.utils.ResourceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 05/03/2017.
 */

public class CurrencyManager extends RealmHelper {
    private static CurrencyManager currencyManager;
    private static final String FILE_NAME_JSON = "default_currency_map.json";
    private static final String TAG = CurrencyManager.class.getSimpleName();

    public static CurrencyManager getInstance() {
        if (currencyManager==null){
            currencyManager = new CurrencyManager();
        }
        return currencyManager;
    }

    private CurrencyManager() {

    }
    public void insertOrUpdate(Currency object) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
    }
    public void createDefaultCurrency(Context context) {
        String json = ResourceUtils.getInstance().getStringJsonCurrencyAssets(context, FILE_NAME_JSON);
        try {
            JSONArray jsonArray = new JSONArray(json);
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                createNewCurrency(jsonObject.getString("CurrencyCode"), jsonObject.getString("CurrencyName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public float convetMoney(String fromCode, String toCode, float amount) {
        //TODO
        return amount;
    }

    public void createNewCurrency(String code, String name) {
        Currency currency = new Currency(code, name);
        insertOrUpdate(currency);
    }

    public String getCurrentCodeCurrencyDefault() {
        Locale locale = Locale.getDefault();
        java.util.Currency currency = java.util.Currency.getInstance(locale);
        return currency.getCurrencyCode();
    }

    public List<Currency> getCurrencyCodes() {
        realm.beginTransaction();
        RealmResults<Currency> realmResults = realm.where(Currency.class).findAll();
        List<Currency> currencies = realmResults.subList(0, realmResults.size());
        Log.d(TAG, "getCurrencyCodes: "+ currencies.size());
        realm.commitTransaction();
        return currencies;
    }
}
