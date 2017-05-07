package com.dut.moneytracker.adapter.account;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dut.moneytracker.R;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 07/04/2017.
 */

public class ColorAccountAdapter extends RecyclerView.Adapter<ColorAccountAdapter.ItemHolder> {
    private Context mContext;
    private String[] mColors;

    public ColorAccountAdapter(Context context) {
        mContext = context;
        mColors = mContext.getResources().getStringArray(R.array.colors_account);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_view_color, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bindColor(mColors[position]);
    }

    @Override
    public int getItemCount() {
        return mColors.length;
    }

    public String getColorHex(int position) {
        return mColors[position];
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        ItemHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageColor);
        }

        void bindColor(String colorHex) {
            GradientDrawable shapeDrawable = (GradientDrawable) imageView.getBackground();
            shapeDrawable.setColor(Color.parseColor(colorHex));
            imageView.setBackground(shapeDrawable);
        }
    }
}
