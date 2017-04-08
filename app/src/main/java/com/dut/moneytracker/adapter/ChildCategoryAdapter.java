package com.dut.moneytracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.base.BaseRecyclerAdapter;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.utils.ResourceUtils;

import java.util.List;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 09/03/2017.
 */

public class ChildCategoryAdapter extends BaseRecyclerAdapter {

    public ChildCategoryAdapter(Context context, List objects) {
        super(context, objects);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryHolder categoryHolder = (CategoryHolder) holder;
        categoryHolder.onBind((Category) getItem(position));
    }

    private class CategoryHolder extends RecyclerView.ViewHolder {
        private ImageView imgCategory;
        private TextView mTvCategoryName;
        private ImageView imgMore;

        public CategoryHolder(View itemView) {
            super(itemView);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            mTvCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
            imgMore = (ImageView) itemView.findViewById(R.id.imgMore);
            imgMore.setVisibility(View.INVISIBLE);
        }

        public void onBind(Category category) {
            mTvCategoryName.setText(category.getName());
            imgCategory.setImageBitmap(ResourceUtils.getInstance().getBitmap(category.getByteImage()));
        }
    }
}
