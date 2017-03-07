package com.dut.moneytracker.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright@ AsianTech .Inc
 * Created by lyhv on 08/12/2016.
 */
public class ResourceUtils {
    private static final int SIZE_IMAGE_CATEGORY = 100;
    private static ResourceUtils ourInstance = new ResourceUtils();

    public static ResourceUtils getInstance() {
        return ourInstance;
    }

    private ResourceUtils() {

    }

    public int getIdResStageBitmap(Resources resources, Context context, String path) {
        return resources.getIdentifier(path, "drawable", context.getPackageName());
    }

    public byte[] convertBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, SIZE_IMAGE_CATEGORY, stream);
        return stream.toByteArray();
    }

    public byte[] convertBitmap(Resources resources, Context context, String path) {
        int id = resources.getIdentifier(path, "drawable", context.getPackageName());
        Log.d("convertBitmap ", path + "   " + String.valueOf(id));
        Bitmap bitmap = BitmapFactory.decodeResource(resources, id);
        return convertBitmap(bitmap);
    }

    public Bitmap getBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }

    public String getStringJsonCurrencyAssets(Context context, String fileName) {
        String json = "";
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            json = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
