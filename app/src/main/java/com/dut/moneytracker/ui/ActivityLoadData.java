package com.dut.moneytracker.ui;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.dut.moneytracker.R;
import com.dut.moneytracker.constant.GroupTag;
import com.dut.moneytracker.models.AppConfig;
import com.dut.moneytracker.models.firebase.FireBaseSync;
import com.dut.moneytracker.models.firebase.LoadDataListener;
import com.dut.moneytracker.models.realms.AccountManager;
import com.dut.moneytracker.models.realms.CategoryManager;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.GroupCategory;
import com.dut.moneytracker.utils.ResourceUtils;
import com.google.firebase.auth.FirebaseAuth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 04/03/2017.
 */
@EActivity(R.layout.acitivity_splash)
public class ActivityLoadData extends AppCompatActivity implements LoadDataListener {
    @ViewById(R.id.progressBar)
    ProgressBar mProgressBar;
    private int idCategory;
    private Handler mHandler = new Handler();
    private static final long DELAY = 1000L;

    @AfterViews
    void init() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FireBaseSync.getInstance().onLoadDataServer(getApplicationContext(), ActivityLoadData.this);
            }
        }, DELAY);
    }

    @Override
    public void onFinishLoadDataServer() {
        AppConfig.getInstance().setCurrentUserId(this, FirebaseAuth.getInstance().getCurrentUser().getUid());
        onCreateCategories();
        onCreateDefaultAccount();
        MainActivity_.intent(this).start();
        finish();
    }

    private void onCreateDefaultAccount() {
        if (AccountManager.getInstance().getAccountsNotOutside().isEmpty()) {
            AccountManager.getInstance().createDefaultAccount(this);
            AccountManager.getInstance().createOutSideAccount(this);
        }
    }

    private void onCreateCategories() {
        if (!AppConfig.getInstance().isInitCategory(this)) {
            createGroupCategory();
            AppConfig.getInstance().setInitCategory(this, true);
        }
    }

    private void createGroupCategory() {
        CategoryManager mCategoryManager = CategoryManager.getInstance();
        String[] nameGroups = getResources().getStringArray(R.array.group_name);
        String[] pathGroups = getResources().getStringArray(R.array.group_path);
        int size = nameGroups.length;
        for (int i = 0; i < size; i++) {
            GroupCategory groupCategory = new GroupCategory();
            groupCategory.setId(String.valueOf(i));
            groupCategory.setName(nameGroups[i]);
            groupCategory.setByteImage(loadByteBitmap(pathGroups[i]));
            setListCategory(groupCategory, i);
            mCategoryManager.insertOrUpdate(groupCategory);
        }
    }

    private void setListCategory(GroupCategory groupCategory, int index) {
        switch (index) {
            case GroupTag.INCOME:
                groupCategory.setColorCode(R.color.color_income);
                setListChildCategory(groupCategory, R.array.income_name, R.array.income_path);
                break;
            case GroupTag.FOOD:
                groupCategory.setColorCode(R.color.color_food);
                setListChildCategory(groupCategory, R.array.food_drink_name, R.array.food_drink_path);
                break;
            case GroupTag.TRANSPORTATION:
                groupCategory.setColorCode(R.color.color_transportation);
                setListChildCategory(groupCategory, R.array.transportation_name, R.array.transportation_path);
                break;
            case GroupTag.ENTERTAINMENT:
                groupCategory.setColorCode(R.color.color_entertainment);
                setListChildCategory(groupCategory, R.array.entertainment_name, R.array.entertainment_path);
                break;
            case GroupTag.HEALTH:
                groupCategory.setColorCode(R.color.color_health);
                setListChildCategory(groupCategory, R.array.health_name, R.array.health_path);
                break;
            case GroupTag.FAMILY:
                groupCategory.setColorCode(R.color.color_family);
                setListChildCategory(groupCategory, R.array.family_name, R.array.family_path);
                break;
            case GroupTag.SHOPPING:
                groupCategory.setColorCode(R.color.color_shopping);
                setListChildCategory(groupCategory, R.array.shopping_name, R.array.shopping_path);
                break;
            case GroupTag.EDUCATION:
                groupCategory.setColorCode(R.color.color_education);
                setListChildCategory(groupCategory, R.array.education_name, R.array.education_path);
                break;
            case GroupTag.LOVE:
                groupCategory.setColorCode(R.color.color_love);
                setListChildCategory(groupCategory, R.array.love_name, R.array.love_path);
                break;
            case GroupTag.OTHER:
                groupCategory.setColorCode(R.color.color_other);
                setListChildCategory(groupCategory, R.array.other_name, R.array.other_path);
                break;
        }
    }

    private void setListChildCategory(GroupCategory groupCategory, int idListName, int idListPath) {
        String[] name = getResources().getStringArray(idListName);
        String[] path = getResources().getStringArray(idListPath);
        int size = name.length;
        for (int i = 0; i < size; i++) {
            idCategory += 1;
            Category category = new Category();
            category.setIdGroup(groupCategory.getId());
            category.setId(String.valueOf(idCategory));
            category.setName(name[i]);
            category.setByteImage(loadByteBitmap(path[i]));
            CategoryManager.getInstance().insertOrUpdate(category);
        }
    }

    private byte[] loadByteBitmap(String path) {
        return ResourceUtils.getInstance().convertBitmap(getResources(), this, path);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
