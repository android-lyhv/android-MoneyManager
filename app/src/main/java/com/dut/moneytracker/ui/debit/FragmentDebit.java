package com.dut.moneytracker.ui.debit;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.ClickItemListener;
import com.dut.moneytracker.adapter.ClickItemRecyclerView;
import com.dut.moneytracker.adapter.debit.DebitAdapter;
import com.dut.moneytracker.models.realms.DebitManager;
import com.dut.moneytracker.objects.Debit;
import com.dut.moneytracker.ui.MainActivity_;
import com.dut.moneytracker.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 13/04/2017.
 */
@EFragment(R.layout.fragment_debit)
public class FragmentDebit extends BaseFragment implements RealmChangeListener<RealmResults<Debit>> {
    @ViewById(R.id.recyclerDebit)
    RecyclerView mRecyclerViewDebit;
    private DebitAdapter mDebitAdapter;
    private RealmResults<Debit> mDebits;

    @AfterViews
    void init() {
        initRecyclerDebit();
        intLoadDebits();
    }

    @Click(R.id.fab)
    void onCLickAddDebit() {
        ActivityAddDebit_.intent(this).start();
    }

    private void initRecyclerDebit() {
        mDebitAdapter = new DebitAdapter(getContext(), mDebits);
        mRecyclerViewDebit.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewDebit.setAdapter(mDebitAdapter);
        mRecyclerViewDebit.addOnItemTouchListener(new ClickItemRecyclerView(getContext(), new ClickItemListener() {
            @Override
            public void onClick(View view, int position) {
                // TODO detail debit
            }
        }));
    }

    private void intLoadDebits() {
        mDebits = DebitManager.getInstance().onLoadDebitAsync();
        mDebits.addChangeListener(this);
    }

    @Override
    public void onChange(RealmResults<Debit> element) {
        mDebitAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity_) getActivity()).loadMenuItemFragmentDebit();
    }
}
