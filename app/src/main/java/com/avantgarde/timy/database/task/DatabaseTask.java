package com.avantgarde.timy.database.task;

import android.os.AsyncTask;
import android.support.annotation.CallSuper;

import com.avantgarde.timy.database.OfflineDatabase;
import com.avantgarde.timy.database.OnCompleteListener;
import com.avantgarde.timy.database.OnErrorListener;

public abstract class DatabaseTask<TParam, TResult> extends AsyncTask<TParam,Void,TResult> {

    private OnCompleteListener<TResult> onCompleteListener;
    private OnErrorListener onErrorListener;
    protected String error;

    protected OfflineDatabase database;

    public DatabaseTask(OfflineDatabase offlineDatabase) {
        database = offlineDatabase;
    }

    @CallSuper
    @Override
    protected void onPostExecute(TResult tResult) {
        super.onPostExecute(tResult);
//        if (tResult == null && onErrorListener != null)
//            onErrorListener.onError(error);
//        else
        if (onCompleteListener != null)
            onCompleteListener.onComplete(tResult);
    }

    public DatabaseTask<TParam, TResult> setOnCompleteListener(OnCompleteListener<TResult> onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
        return this;
    }

    public DatabaseTask<TParam, TResult> setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
        return this;
    }

}