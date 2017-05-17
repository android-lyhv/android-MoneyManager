package com.dut.moneytracker.adapter.loop;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.base.BaseRecyclerAdapter;
import com.dut.moneytracker.constant.ExchangeType;
import com.dut.moneytracker.constant.FilterLoop;
import com.dut.moneytracker.constant.LoopType;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.models.realms.CategoryManager;
import com.dut.moneytracker.objects.Category;
import com.dut.moneytracker.objects.ExchangeLooper;
import com.dut.moneytracker.utils.DateTimeUtils;

import io.realm.RealmResults;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 20/03/2017.
 */

public class LoopExchangeAdapter extends BaseRecyclerAdapter {
    private ClickItemListener clickItemListener;
    private int mIdFilter = 0;

    public interface ClickItemListener {
        void onClickItem(int position);
    }

    public void registerItemClick(ClickItemListener clickItemListener) {
        this.clickItemListener = clickItemListener;
    }

    public LoopExchangeAdapter(Context context, RealmResults objects) {
        super(context, objects);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_loop_exhange, parent, false);
        return new ItemLoopExchange(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExchangeLooper exchangeLooper = (ExchangeLooper) getItem(position);
        ((ItemLoopExchange) holder).onBind(exchangeLooper);

        Log.d(TAG, "onBindViewHolder: " + mIdFilter);
        switch (mIdFilter) {
            case FilterLoop.ALL:
                break;
            case FilterLoop.DAY:
                if (exchangeLooper.getTypeLoop() != LoopType.DAY) {
                    holder.itemView.setVisibility(View.GONE);
                }
                break;
            case FilterLoop.WEAK:
                if (exchangeLooper.getTypeLoop() != LoopType.WEAK) {
                    holder.itemView.setVisibility(View.GONE);
                }
                break;
            case FilterLoop.MONTH:
                if (exchangeLooper.getTypeLoop() != LoopType.MONTH) {
                    holder.itemView.setVisibility(View.GONE);
                }
                break;
            case FilterLoop.YEAR:
                if (exchangeLooper.getTypeLoop() != LoopType.YEAR) {
                    holder.itemView.setVisibility(View.GONE);
                }
                break;
            case FilterLoop.INCOME:
                if (exchangeLooper.getTypeExchange() != ExchangeType.INCOME) {
                    holder.itemView.setVisibility(View.GONE);
                }
                break;
            case FilterLoop.EXPENSES:
                if (exchangeLooper.getTypeExchange() != ExchangeType.EXPENSES) {
                    holder.itemView.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void setIdFilter(int idFilter) {
        mIdFilter = idFilter;
        notifyDataSetChanged();
    }

    public class ItemLoopExchange extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout rlContent;
        ImageView imgCategory;
        TextView tvCategoryName;
        TextView tvDescription;
        TextView tvTypeLoop;
        TextView tvAmount;
        TextView tvLastCreated;
        TextView tvStatus;

        public ItemLoopExchange(View itemView) {
            super(itemView);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            tvCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvAccountName);
            tvTypeLoop = (TextView) itemView.findViewById(R.id.tvTypeLoop);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            tvLastCreated = (TextView) itemView.findViewById(R.id.tvLastCreated);
            rlContent = (RelativeLayout) itemView.findViewById(R.id.content);
            rlContent.setOnClickListener(this);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
        }

        public void onBind(ExchangeLooper exchangeLooper) {
            Category category = CategoryManager.getInstance().getCategoryById(exchangeLooper.getIdCategory());
            Glide.with(getContext()).load(category.getByteImage()).into(imgCategory);
            tvCategoryName.setText(category.getName());
            String description = exchangeLooper.getDescription();
            if (TextUtils.isEmpty(description)) {
                tvDescription.setVisibility(View.GONE);
            } else {
                tvDescription.setVisibility(View.VISIBLE);
                tvDescription.setText(description);
            }
            switch (exchangeLooper.getTypeLoop()) {
                case LoopType.DAY:
                    tvTypeLoop.setText(R.string.loop_day);
                    break;
                case LoopType.WEAK:
                    tvTypeLoop.setText(R.string.loop_week);
                    break;
                case LoopType.MONTH:
                    tvTypeLoop.setText(R.string.loop_month);
                    break;
                case LoopType.YEAR:
                    tvTypeLoop.setText(R.string.loop_year);
            }
            tvLastCreated.setText(DateTimeUtils.getInstance().getStringDateUs(exchangeLooper.getCreated()));
            tvAmount.setText(CurrencyUtils.getInstance().getStringMoneyFormat(exchangeLooper.getAmount(), "VND"));
            if (exchangeLooper.getTypeExchange() == ExchangeType.INCOME) {
                tvAmount.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            } else {
                tvAmount.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
            }
            if (exchangeLooper.isLoop()) {
                tvStatus.setText(R.string.loop_status_ok);
                tvStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            } else {
                tvStatus.setText(R.string.loop_status_close);
                tvStatus.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
            }
        }

        @Override
        public void onClick(View v) {
            clickItemListener.onClickItem(getAdapterPosition());
        }
    }
}
