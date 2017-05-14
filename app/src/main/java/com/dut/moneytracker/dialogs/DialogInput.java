package com.dut.moneytracker.dialogs;

import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;

import com.dut.moneytracker.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 27/03/2017.
 */
@EFragment(R.layout.dialog_input_text)
public class DialogInput extends DialogFragment {

    public static DialogInput getInstance() {
        return DialogInput_.builder().build();
    }

    @ViewById(R.id.edtDescription)
    EditText editText;
    private String initContent = "";

    @AfterViews
    void init() {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        editText.setText(initContent);
    }

    public interface DescriptionListener {
        void onResult(String content);
    }

    public void register(DescriptionListener descriptionListener) {
        this.descriptionListener = descriptionListener;
    }

    public void initValue(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        initContent = content;
    }

    DescriptionListener descriptionListener;

    @Click(R.id.tvConfirm)
    void onClickConfirm() {
        descriptionListener.onResult(editText.getText().toString());
        dismiss();
    }

    @Click(R.id.tvCancel)
    void onClickCancel() {
        dismiss();
    }
}
