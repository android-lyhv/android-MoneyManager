package com.dut.moneytracker.adapter.calculator;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dut.moneytracker.R;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 28/04/2017.
 */

public class CalculatorAdapter extends RecyclerView.Adapter<CalculatorAdapter.ItemHolderCalculator> {
    public interface ItemCalClickListener {
        void onCLick(String s);
    }

    public void registerClickCal(ItemCalClickListener itemCalClickListener) {
        mItemClick = itemCalClickListener;
    }

    private ItemCalClickListener mItemClick;
    private final Context mContext;
    private final String[] items = new String[]{"7", "8", "9", "4", "5", "6", "1", "2", "3", ".", "0", "="};

    public CalculatorAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ItemHolderCalculator onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_calculator, parent, false);
        return new ItemHolderCalculator(view);
    }

    @Override
    public void onBindViewHolder(ItemHolderCalculator holder, int position) {
        holder.onBind(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    class ItemHolderCalculator extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvCal;
        private final CardView cardViewCal;

        ItemHolderCalculator(View itemView) {
            super(itemView);
            tvCal = (TextView) itemView.findViewById(R.id.tvCal);
            cardViewCal = (CardView) itemView.findViewById(R.id.cardViewCal);
            cardViewCal.setOnClickListener(this);
        }

        public void onBind(String chart) {
            tvCal.setText(chart);
        }

        @Override
        public void onClick(View v) {
            mItemClick.onCLick(items[getAdapterPosition()]);
        }
    }
}
