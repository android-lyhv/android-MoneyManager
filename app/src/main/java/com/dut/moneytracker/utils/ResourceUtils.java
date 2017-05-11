package com.dut.moneytracker.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Copyright@ AsianTech .Inc
 * Created by lyhv on 08/12/2016.
 */
public class ResourceUtils {
    private static final int SIZE_IMAGE_CATEGORY = 100;
    private static ResourceUtils ourInstance;

    public static ResourceUtils getInstance() {
        if (ourInstance == null) {
            ourInstance = new ResourceUtils();
        }
        return ourInstance;
    }

    private BitmapFactory.Options options;

    private ResourceUtils() {
        options = getOptions();
    }

    private BitmapFactory.Options getOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 1;
        return options;
    }

    private byte[] convertBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, SIZE_IMAGE_CATEGORY, stream);
        return stream.toByteArray();
    }

    public byte[] convertBitmap(Resources resources, Context context, String path) {
        int id = resources.getIdentifier(path, "drawable", context.getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(resources, id, options);
        return convertBitmap(bitmap);
    }

    public Bitmap getBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }
}
