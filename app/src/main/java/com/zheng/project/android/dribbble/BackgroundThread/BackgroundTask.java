package com.zheng.project.android.dribbble.BackgroundThread;

import android.os.AsyncTask;

import com.zheng.project.android.dribbble.dribbble.auth.DribbbleException;

public abstract class BackgroundTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    DribbbleException exception;
    @Override
    protected Result doInBackground(Params... paramses) {
        try {
            return doJob();

        } catch (DribbbleException e) {
            e.printStackTrace();
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Result data) {
        if (exception != null) {
            onFailed(exception);
        }
        else {
            onSuccess(data);
        }
    }

    protected abstract void onSuccess(Result result);
    protected abstract void onFailed(DribbbleException e);

    protected abstract Result doJob() throws DribbbleException;

}
