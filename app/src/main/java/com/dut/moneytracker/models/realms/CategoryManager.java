package com.dut.moneytracker.models.realms;

import com.dut.moneytracker.constant.GroupTag;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.GroupCategory;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 02/03/2017.
 */

public class CategoryManager extends RealmHelper {
    private static CategoryManager categoryManager = new CategoryManager();

    public static CategoryManager getInstance() {
        return categoryManager;
    }

    private CategoryManager() {

    }

    public void insertOrUpdate(Category object) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
    }

    public void insertOrUpdate(GroupCategory object) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
    }


    public RealmResults<GroupCategory> getGroupCategoryExpense() {
        realm.beginTransaction();
        RealmResults<GroupCategory> realmResults = realm.where(GroupCategory.class).notEqualTo("id", String.valueOf(GroupTag.INCOME)).findAll();
        realm.commitTransaction();
        return realmResults;
    }

    public Category getCategoryById(String id) {
        realm.beginTransaction();
        Category category = realm.where(Category.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return category;
    }

    public RealmResults<Category> getCategoriesByGroupId(String idGroup) {
        realm.beginTransaction();
        RealmResults<Category> realmResults = realm.where(Category.class).equalTo("idGroup", idGroup).findAll();
        realm.commitTransaction();
        return realmResults;
    }

    public RealmResults<Category> getCategories() {
        realm.beginTransaction();
        RealmResults<Category> realmResults = realm.where(Category.class).findAll();
        realm.commitTransaction();
        return realmResults;
    }

    public RealmResults<Category> getCategoriesInCome() {
        realm.beginTransaction();
        RealmResults<Category> realmResults = realm.where(Category.class).equalTo("idGroup", String.valueOf(GroupTag.INCOME)).findAll();
        realm.commitTransaction();
        return realmResults;
    }

    public List<String> getListIdCategoryByGroupId(String idGroup) {
        List<String> idCategories = new ArrayList<>();
        realm.beginTransaction();
        RealmResults<Category> realmResults = realm.where(Category.class).equalTo("idGroup", idGroup).findAll();
        for (Category category : realmResults) {
            idCategories.add(category.getId());
        }
        realm.commitTransaction();
        return idCategories;
    }
}
