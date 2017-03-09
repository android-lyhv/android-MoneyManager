package com.dut.moneytracker.utils;

import android.animation.ArgbEvaluator;

import java.util.ArrayList;
import java.util.List;

public class ArgbEvaluatorColor {
    private static ArgbEvaluatorColor ourInstance = new ArgbEvaluatorColor();
    private final ArgbEvaluator mArgbEvaluator;
    private List<Integer> mColors;


    public static ArgbEvaluatorColor getInstance() {
        return ourInstance;
    }

    private ArgbEvaluatorColor() {
        mArgbEvaluator = new ArgbEvaluator();
        mColors = new ArrayList<>();
    }

    public void addColorCode(int colorCode) {
        mColors.add(colorCode);
    }

    public int getHeaderColor(float positionOffset, int startColor, int endColor) {
        return (int) mArgbEvaluator.evaluate(positionOffset, startColor, endColor);
    }

    public int getHeaderColor(int position, float positionOffset) {
        if (position == mColors.size() - 1) {
            return mColors.get(position);
        }
        int startColor = mColors.get(position);
        int endColor = mColors.get(position+1);
        return ArgbEvaluatorColor.getInstance().getHeaderColor(positionOffset, startColor, endColor);
    }

}