package com.dut.moneytracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.base.BaseRecyclerAdapter;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.GroupCategory;

import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */

public class GroupCategoryAdapter extends BaseRecyclerAdapter {
    private int mType;

    public GroupCategoryAdapter(Context context, RealmResults objects, int type) {
        super(context, objects);
        mType = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryHolder categoryHolder = (CategoryHolder) holder;
        if (mType == ExchangeType.EXPENSES) {
            categoryHolder.onBind((GroupCategory) getItem(position));
        } else {
            categoryHolder.onBind((Category) getItem(position));
        }
    }

    private class CategoryHolder extends RecyclerView.ViewHolder {
        private ImageView imgCategory;
        private TextView mTvCategoryName;

        CategoryHolder(View itemView) {
            super(itemView);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            mTvCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
        }

        public void onBind(GroupCategory groupCategory) {
            mTvCategoryName.setText(groupCategory.getName());
            Glide.with(getContext()).load(groupCategory.getByteImage()).into(imgCategory);
        }

        public void onBind(Category category) {
            mTvCategoryName.setText(category.getName());
            Glide.with(getContext()).load(category.getByteImage()).into(imgCategory);
        }
    }
}
