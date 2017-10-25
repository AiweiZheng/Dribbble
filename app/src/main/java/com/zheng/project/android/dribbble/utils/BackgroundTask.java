package com.zheng.project.android.dribbble.utils;

import android.os.AsyncTask;

public abstract class BackgroundTask<T> extends AsyncTask<Void, Void, T> {

    @Override
    protected T doInBackground(Void... voids) {
        return executeInBackGround();
    }

    @Override
    protected void onPostExecute(T data) {
        onPost(data);
    }

    protected abstract T executeInBackGround();
    protected void onPost(T data){}
}
