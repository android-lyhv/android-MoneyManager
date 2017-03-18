package com.dut.moneytracker.maps;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 17/03/2017.
 */
public class DownloadImage {
    public interface DownloadListener {
        void onDownLoadCompleted(Bitmap bitmap);
    }

    private DownloadListener downloadListener;

    public void registerDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    private static DownloadImage ourInstance = new DownloadImage();

    public static DownloadImage getInstance() {
        return ourInstance;
    }

    private DownloadImage() {
    }

    public void DowloadImage(Uri uri) {
        new Downloader().execute(uri);
    }

    class Downloader extends AsyncTask<Uri, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Uri... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            downloadListener.onDownLoadCompleted(bitmap);
        }
    }
}
