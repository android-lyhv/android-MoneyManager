package com.dut.moneytracker;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.dut.moneytracker.constant.GroupTag;
import com.dut.moneytracker.models.CategoryManager;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.GroupCategory;
import com.dut.moneytracker.utils.AppPreferences;
import com.facebook.FacebookSdk;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 20/02/2017.
 */

public class MoneyTrackerApplication extends Application {
    private static final String TAG = MoneyTrackerApplication.class.getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();
        configRealm(getApplicationContext());
        configFacebookSdk();
        configFireBase();
        if (!AppPreferences.getInstance().isFirstInstall(this)) {
            initInstallApp();
            AppPreferences.getInstance().setFirstInstall(this, true);
        }

    }

    private void configFacebookSdk() {
        FacebookSdk.sdkInitialize(this);
    }

    private void configRealm(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }

    private void configFireBase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private void initInstallApp() {
       // initCategory();
    }

    private void initCategory() {
        createGroupCategory();
    }

    private void createGroupCategory() {
        CategoryManager mCategoryManager = new CategoryManager();
        String[] nameGroups = getResources().getStringArray(R.array.group_name);
        String[] pathGroups = getResources().getStringArray(R.array.group_path);
        int size = nameGroups.length;
        for (int i = 0; i < size; i++) {
            GroupCategory groupCategory = new GroupCategory();
            groupCategory.setId(UUID.randomUUID().toString());
            groupCategory.setName(nameGroups[i]);
            groupCategory.setUrlImage(pathGroups[i]);
            setListCategory(groupCategory, i);
            mCategoryManager.insertOrUpdate(groupCategory);
        }
    }

    private void setListCategory(GroupCategory groupCategory, int index) {
        switch (index) {
            case GroupTag.INCOME:
                setListChildCategory(groupCategory, R.array.income_name, R.array.income_path);
                break;
            case GroupTag.FOOD:
                setListChildCategory(groupCategory, R.array.food_drink_name, R.array.food_drink_path);
                break;
            case GroupTag.TRANSPORTATION:
                setListChildCategory(groupCategory, R.array.transportation_name, R.array.transportation_path);
                break;
            case GroupTag.ENTERTAINMENT:
                setListChildCategory(groupCategory, R.array.entertainment_name, R.array.entertainment_path);
                break;
            case GroupTag.HEALTH:
                setListChildCategory(groupCategory, R.array.health_name, R.array.health_path);
                break;
            case GroupTag.FAMILY:
                setListChildCategory(groupCategory, R.array.family_name, R.array.family_path);
                break;
            case GroupTag.SHOPPING:
                setListChildCategory(groupCategory, R.array.shopping_name, R.array.shopping_path);
                break;
            case GroupTag.EDUCATION:
                setListChildCategory(groupCategory, R.array.education_name, R.array.education_path);
                break;
            case GroupTag.LOVE:
                setListChildCategory(groupCategory, R.array.love_name, R.array.love_path);
                break;
            case GroupTag.OTHER:
                setListChildCategory(groupCategory, R.array.other_name, R.array.other_path);
                break;
        }
    }

    private void setListChildCategory(GroupCategory groupCategory, int idListName, int idListPath) {
        String[] name = getResources().getStringArray(idListName);
        String[] path = getResources().getStringArray(idListPath);
        Log.d(TAG, "setListChildCategory: " + Arrays.toString(name) + "\n" + Arrays.toString(path));
        RealmList<Category> realmList = new RealmList<>();
        int size = name.length;
        for (int i = 0; i < size; i++) {
            Category category = new Category();
            category.setId(UUID.randomUUID().toString());
            category.setName(name[i]);
            category.setUrlImage(path[i]);
            realmList.add(category);
        }
        groupCategory.setCategories(realmList);
    }
}
