package com.dut.moneytracker.fragment.categroy;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.GroupCategoryAdapter;
import com.dut.moneytracker.adapter.LoadCategoryListener;
import com.dut.moneytracker.models.realms.CategoryManager;
import com.dut.moneytracker.objects.GroupCategory;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */

public class FragmentGroupCategory extends Fragment implements ClickItemListener {
    private LoadCategoryListener mLoadCategoryListener;
    private RecyclerView mRecyclerCategory;
    private GroupCategoryAdapter childCategoryRecyclerAdapter;
    private List<GroupCategory> mGroupCategories = CategoryManager.getInstance().getGroupCategory();

    public void registerLoadChildCategory(LoadCategoryListener loadCategoryListener) {
        mLoadCategoryListener = loadCategoryListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragament_category, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        childCategoryRecyclerAdapter = new GroupCategoryAdapter(getActivity(), mGroupCategories);
        mRecyclerCategory = (RecyclerView) view.findViewById(R.id.recyclerCategory);
        mRecyclerCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerCategory.setAdapter(childCategoryRecyclerAdapter);
        mRecyclerCategory.addOnItemTouchListener(new ClickItemRecyclerView(getActivity(), this));
    }

    @Override
    public void onClick(View view, int position) {
        mLoadCategoryListener.onLoadChildCategory(mGroupCategories.get(position).getCategories());
    }

}
