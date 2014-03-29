package etsy.homework.loaders;

import android.app.LoaderManager;
import android.content.Context;
import android.net.Uri;

import etsy.homework.database.tables.TasksTable;
import etsy.homework.providers.EtsyContentProvider;
import etsy.homework.rest.callbacks.RestLoaderCallbacks;
import etsy.homework.rest.callbacks.RestLoaderCallbacksListener;
import etsy.homework.tasks.SearchTask;

/**
 * Created by emir on 29/03/14.
 */
public class ResultsCursorLoader extends RestLoaderCallbacks {

    private static final Uri URI = TasksTable.URI.buildUpon().appendQueryParameter(EtsyContentProvider.TASK_URI, SearchTask.URI.toString()).build();
    private static final int LOADER_ID = 1;

    public ResultsCursorLoader(Context context, LoaderManager loaderManager, RestLoaderCallbacksListener restLoaderCallbacksListener) {
        super(context, loaderManager, restLoaderCallbacksListener);
    }

    @Override
    public Uri getUri() {
        return URI;
    }

    @Override
    public int getLoaderId() {
        return LOADER_ID;
    }
}
