package com.dut.moneytracker.ui.category;

import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.GroupCategoryAdapter;
import com.dut.moneytracker.adapter.LoadCategoryListener;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.models.realms.CategoryManager;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.GroupCategory;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */
@EFragment(R.layout.fragament_category)
public class FragmentGroupCategory extends Fragment implements ClickItemListener {
    private LoadCategoryListener mLoadCategoryListener;
    private RealmResults<GroupCategory> mGroupCategories;
    private RealmResults<Category> mCategories;
    GroupCategoryAdapter mAdapter;
    @ViewById(R.id.recyclerCategory)
    RecyclerView mRecyclerViewCategory;
    @FragmentArg
    int mType;

    public void registerLoadChildCategory(LoadCategoryListener loadCategoryListener) {
        mLoadCategoryListener = loadCategoryListener;
    }

    @AfterViews
    void init() {
        initView();
    }

    private void initView() {
        switch (mType) {
            case ExchangeType.EXPENSES:
                mGroupCategories = CategoryManager.getInstance().getGroupCategoryExpense();
                mAdapter = new GroupCategoryAdapter(getActivity(), mGroupCategories, ExchangeType.EXPENSES);
                break;
            case ExchangeType.INCOME:
                mCategories = CategoryManager.getInstance().getCategoriesInCome();
                mAdapter = new GroupCategoryAdapter(getActivity(), mCategories, ExchangeType.INCOME);
                break;
        }

        mRecyclerViewCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewCategory.setAdapter(mAdapter);
        mRecyclerViewCategory.addOnItemTouchListener(new ClickItemRecyclerView(getActivity(), this));
    }

    @Override
    public void onClick(View view, int position) {
        if (mType == ExchangeType.EXPENSES) {
            String idGroup = mGroupCategories.get(position).getId();
            mLoadCategoryListener.onLoadChildCategory(CategoryManager.getInstance().getCategoriesByGroupId(idGroup));
        } else {
            mLoadCategoryListener.onPickCategory((Category) mAdapter.getItem(position));
        }
    }

}
