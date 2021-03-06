package com.ase.aat_android.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ase.aat_android.data.SessionData;
import com.ase.aat_android.util.Constants;
import com.google.appengine.repackaged.com.google.common.base.Flag;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.resource.ClientResource;

/**
 * Created by anahitik on 09.01.17.
 */

public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private ProgressDialog loadingDialog;
    private String loadingMessage;
    protected String failureMessage;
    Activity activity;

    public BaseAsyncTask(Activity activity) {
        loadingMessage = Constants.loading;
        this.activity = activity;
        initializeProgressBar();
    }

    public BaseAsyncTask(Activity activity, String message) {
        loadingMessage = message;
        this.activity = activity;
        initializeProgressBar();
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
        if (o == null || (o instanceof Boolean && ((Boolean) o) == false)) {
            if (failureMessage == null || failureMessage.isEmpty()) {
                failureMessage = "Failed";
            }
            Toast.makeText(activity.getApplicationContext(), failureMessage, Toast.LENGTH_LONG).show();
        }
    }

    protected static ClientResource createClientResource(Method method, String url, boolean auth) {
        ClientResource resourse = new ClientResource(method, url);
        resourse.setResponseEntityBuffering(true);
        resourse.setRequestEntityBuffering(true);
        resourse.accept(MediaType.APPLICATION_ALL_JSON);
        if (auth) {
            resourse.getRequest().getCookies().add(0, SessionData.getSessionToken());
        }
        return resourse;
    }

    private void initializeProgressBar() {
        loadingDialog = new ProgressDialog(activity);
        loadingDialog.setMessage(loadingMessage);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.setCancelable(false);
        loadingDialog.dismiss();
    }
}
