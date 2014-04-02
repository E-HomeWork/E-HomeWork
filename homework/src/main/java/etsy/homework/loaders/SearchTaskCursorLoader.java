package etsy.homework.loaders;

import android.app.LoaderManager;
import android.content.Context;
import android.net.Uri;

import etsy.homework.database.tables.TasksTable;
import etsy.homework.rest.callbacks.RestLoaderCallbacks;
import etsy.homework.rest.callbacks.RestLoaderCallbacksListener;
import etsy.homework.rest.tasks.RestTask;
import etsy.homework.tasks.SearchTask;

/**
 * Created by emir on 29/03/14.
 */
public class SearchTaskCursorLoader extends RestLoaderCallbacks {

    private static final Uri URI = TasksTable.URI;
    public static final int LOADER_ID = 1;
    public static final String URI_PATH = TasksTable.URI_PATH;
    private String mKeyword;
    private int mPage = 1;

    public SearchTaskCursorLoader(Context context, LoaderManager loaderManager, RestLoaderCallbacksListener restLoaderCallbacksListener) {
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
        return SearchTask.URI.buildUpon().appendQueryParameter(SearchTask.KEYWORD, mKeyword).appendQueryParameter(SearchTask.PAGE, Integer.toString(mPage)).build();
    }

    @Override
    public int getLoaderId() {
        return LOADER_ID;
    }

    public int getPage(){
        return mPage;
    }

    public void setValues(final Context context, final String keyword, final int page) {
        mKeyword = keyword;
        mPage = page;
    }

    public void setKeyword(final Context context, final String keyword) {
        mKeyword = keyword;
        mPage = 1;
        onStart(context);
    }

    public void setPage(final Context context, final int page) {
        mPage = page;
        onStart(context);
    }
}
