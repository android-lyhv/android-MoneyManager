package com.dut.moneytracker.ui.debit;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.dut.moneytracker.R;
import com.dut.moneytracker.adapter.debit.DebitAdapter;
import com.dut.moneytracker.currency.CurrencyUtils;
import com.dut.moneytracker.dialogs.DialogCalculator;
import com.dut.moneytracker.dialogs.DialogPayDebit;
import com.dut.moneytracker.dialogs.DialogPayDebit_;
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
public class
FragmentDebit extends BaseFragment implements RealmChangeListener<RealmResults<Debit>>, DebitAdapter.ClickDebitListener {
    @ViewById(R.id.recyclerDebit)
    RecyclerView mRecyclerViewDebit;
    private DebitAdapter mDebitAdapter;
    private DialogPayDebit mDialogPayDebit;
    private DialogCalculator mDialogCalculator;

    @AfterViews
    void init() {
        initDialog();
        initRecyclerDebit();
    }

    private void initDialog() {
        mDialogPayDebit = DialogPayDebit_.builder().build();
        mDialogCalculator = DialogCalculator.getInstance();
    }

    @Click(R.id.fab)
    void onCLickAddDebit() {
        ActivityAddDebit_.intent(this).start();
    }

    private void initRecyclerDebit() {
        RealmResults<Debit> mDebits = DebitManager.getInstance().onLoadDebitAsync();
        mDebitAdapter = new DebitAdapter(getContext(), mDebits);
        mDebitAdapter.registerClickDebit(this);
        mRecyclerViewDebit.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewDebit.setAdapter(mDebitAdapter);
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

    @Override
    public void onClickDetail(Debit debit) {
        ActivityDetailDebit_.intent(FragmentDebit.this).mDebit(debit).start();
    }

    @Override
    public void onClickViewExchange(Debit debit) {
        ActivityExchangeDebits_.intent(this).mDebit(debit).start();
    }

    @Override
    public void onClickCheckDebit(final Debit debit) {
        mDialogPayDebit.show(getFragmentManager(), null);
        mDialogPayDebit.register(new DialogPayDebit.ClickListener() {
            @Override
            public void onClickPartial() {
                mDialogCalculator.show(getActivity().getFragmentManager(), null);
                mDialogCalculator.setAmount("0");
                mDialogCalculator.registerResultListener(new DialogCalculator.ResultListener() {
                    @Override
                    public void onResult(String amount) {
                        String remindAmount = DebitManager.getInstance().getRemindAmountDebit(debit);
                        if (CurrencyUtils.getInstance().compareAmount(remindAmount, amount) == -1) {
                            Toast.makeText(getContext(), R.string.messger_remind_amount, Toast.LENGTH_LONG).show();
                            return;
                        }
                        DebitManager.getInstance().genExchangeFromDebit(debit, amount);
                        mDebitAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onClickFinishDebit() {
                DebitManager.getInstance().setStatusDebit(debit.getId(), true);
                String remindAmount = DebitManager.getInstance().getRemindAmountDebit(debit);
                if (remindAmount.startsWith("-")) {
                    remindAmount = remindAmount.substring(1);
                }
                DebitManager.getInstance().genExchangeFromDebit(debit, remindAmount);
                mDebitAdapter.notifyDataSetChanged();
            }
        });
    }
}
