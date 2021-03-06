package etsy.homework.rest.callbacks;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.Loader.ForceLoadContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import etsy.homework.Utilities.Debug;
import etsy.homework.rest.loaders.RestCursorLoader;

public abstract class RestLoaderCallbacks implements LoaderCallbacks<Cursor> {

    private final RestLoaderCallbacksListener mRestLoaderCallbacksListener;
    private final Context mContext;
    private LoaderManager mLoaderManager;
    private ForceLoadContentObserver mForceLoadContentObserver;

    public RestLoaderCallbacks(final Context context, final LoaderManager loaderManager, final RestLoaderCallbacksListener restLoaderCallbacksListener) {
        mLoaderManager = loaderManager;
        mRestLoaderCallbacksListener = restLoaderCallbacksListener;
        mContext = context;
    }

    public abstract Uri getUri();

    public abstract int getLoaderId();

    public String[] getProjection() {
        return null;
    }

    public String getSelection() {
        return null;
    }

    public String[] getSelectionArguments(){
        return null;
    }

    public String getSortOrder(){
        return null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final Uri uri = getUri();
        final RestCursorLoader restCursorLoader = new RestCursorLoader(mContext, uri, getProjection(), getSelection(), getSelectionArguments(),getSortOrder());
        restCursorLoader.setUri(uri);
        mForceLoadContentObserver = (ForceLoadContentObserver) restCursorLoader.getForceLoadContentObserver();
        mContext.getContentResolver().registerContentObserver(uri, false, mForceLoadContentObserver);
        return restCursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        mRestLoaderCallbacksListener.onLoadFinished(getUri(), cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRestLoaderCallbacksListener.onLoaderReset(loader);
    }

    public void onStart(Context context) {
        final Loader<?> loader = mLoaderManager.getLoader(getLoaderId());
        if (loader == null)
            mLoaderManager.initLoader(getLoaderId(), null, this);
        else
            mLoaderManager.restartLoader(getLoaderId(), null, this);
    }

    public void onStop(final Context context) {
        if (mForceLoadContentObserver != null)
            mContext.getContentResolver().unregisterContentObserver(mForceLoadContentObserver);
    }

}