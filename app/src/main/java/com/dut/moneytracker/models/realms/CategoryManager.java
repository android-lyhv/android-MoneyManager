package com.dut.moneytracker.models.realms;

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

    public List<GroupCategory> getGroupCategory() {
        List<GroupCategory> groupCategories = new ArrayList<>();
        realm.beginTransaction();
        RealmResults<GroupCategory> realmResults = realm.where(GroupCategory.class).findAll();
        for (GroupCategory groupCategory : realmResults) {
            groupCategories.add(groupCategory);
        }
        realm.commitTransaction();
        return groupCategories;
    }

    public byte[] getImageByte(String id) {
        byte[] bytes;
        realm.beginTransaction();
        Category category = realm.where(Category.class).equalTo("id", id).findFirst();
        bytes = category.getByteImage();
        realm.commitTransaction();
        return bytes;
    }

    public Category getCategoryById(String id) {
        realm.beginTransaction();
        Category category = realm.where(Category.class).equalTo("id", id).findFirst();
        realm.commitTransaction();
        return category;
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
