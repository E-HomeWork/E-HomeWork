package etsy.homework.loaders;

import android.app.LoaderManager;
import android.content.Context;
import android.net.Uri;

import etsy.homework.database.tables.TasksTable;
import etsy.homework.providers.EtsyContentProvider;
import etsy.homework.rest.callbacks.RestLoaderCallbacks;
import etsy.homework.rest.callbacks.RestLoaderCallbacksListener;
import etsy.homework.rest.tasks.RestTask;
import etsy.homework.tasks.SearchTask;

/**
 * Created by emir on 29/03/14.
 */
public class ResultsCursorLoader extends RestLoaderCallbacks {

    private static final Uri URI = TasksTable.URI;
    public static final int LOADER_ID = 1;
    public static final String URI_PATH = TasksTable.URI_PATH;
    private String mKeyword;

    public ResultsCursorLoader(Context context, LoaderManager loaderManager, RestLoaderCallbacksListener restLoaderCallbacksListener) {
        super(context, loaderManager, restLoaderCallbacksListener);
    }

    @Override
    public Uri getUri() {

        final Uri innerUri = getInnerUri();
        final Uri uri = URI.buildUpon().appendQueryParameter(RestTask.TASK_URI, innerUri.toString()).build();
        return uri;
    }

    private Uri getInnerUri() {
        if (mKeyword == null || mKeyword.isEmpty()){
            return SearchTask.URI;
        }
        return SearchTask.URI.buildUpon().appendQueryParameter(SearchTask.KEYWORD, mKeyword).build();
    }

    @Override
    public int getLoaderId() {
        return LOADER_ID;
    }

    public void setKeyword(final Context context, final String keyword) {
        mKeyword = keyword;
        onStart(context);
    }

}
