package com.dut.moneytracker.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * Copyright@ AsianTech .Inc
 * Created by lyhv on 08/12/2016.
 */
public class ResourceUtils {
    private static ResourceUtils ourInstance = new ResourceUtils();

    public static ResourceUtils getInstance() {
        return ourInstance;
    }

    private ResourceUtils() {

    }
    public int getIdResStageBitmap(Resources resources, Context context, String path) {
        return resources.getIdentifier(path, "drawable", context.getPackageName());
    }
}
