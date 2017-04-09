package com.dut.moneytracker.dialogs;

import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.account.ColorAccountAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 27/03/2017.
 */
@EFragment(R.layout.dialog_pick_colors)
public class DialogPickColor extends DialogFragment {
    @ViewById(R.id.recyclerPickColor)
    RecyclerView mRecyclerView;
    private ColorAccountAdapter mColorAccountAdapter;

    @AfterViews
    void init() {
        mColorAccountAdapter = new ColorAccountAdapter(getContext());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecyclerView.setAdapter(mColorAccountAdapter);
        mRecyclerView.addOnItemTouchListener(new ClickItemRecyclerView(getContext(), new ClickItemListener() {
            @Override
            public void onClick(View view, int position) {
                pickColorListener.onResult(mColorAccountAdapter.getColorHex(position));
                dismiss();
            }
        }));
    }

    PickColorListener pickColorListener;

    public interface PickColorListener {
        void onResult(String colorHex);

    }

    public void register(PickColorListener descriptionListener) {
        this.pickColorListener = descriptionListener;
    }

    @Click(R.id.tvCancel)
    void onClickCancel() {
        dismiss();
    }
}
