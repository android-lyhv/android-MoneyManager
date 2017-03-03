package com.dut.moneytracker.models;

import com.dut.moneytracker.objects.GroupCategory;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 02/03/2017.
 */

public class CategoryManager extends RealmHelper {
    public List<GroupCategory> getList() {
        List<GroupCategory> groupCategories = new ArrayList<>();
        realm.beginTransaction();
        RealmResults<GroupCategory> realmResults = realm.where(GroupCategory.class).findAll();
        for (GroupCategory groupCategory: realmResults){
            groupCategories.add(groupCategory);
        }
        realm.commitTransaction();
        return groupCategories;
    }
}
