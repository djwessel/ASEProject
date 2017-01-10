package com.ase.aat_android.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by anahitik on 09.01.17.
 */

public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private ProgressDialog loadingDialog;
    private String loadingMessage;

    public BaseAsyncTask(Activity activity) {
        loadingMessage = "Loading";
        initializeProgressBar(activity);
    }

    public BaseAsyncTask(Activity activity, String message) {
        loadingMessage = message;
        initializeProgressBar(activity);
    }

    @Override
    protected void onPreExecute() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    protected void onPostExecute(Result o) {
        loadingDialog.dismiss();
    }

    private void initializeProgressBar(Activity activity) {

        loadingDialog = new ProgressDialog(activity);
        loadingDialog.setMessage(loadingMessage);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setCancelable(false);
        loadingDialog.dismiss();
    }
}
