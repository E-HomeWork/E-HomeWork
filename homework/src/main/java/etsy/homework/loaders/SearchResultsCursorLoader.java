package etsy.homework.loaders;

import android.app.LoaderManager;
import android.content.Context;
import android.net.Uri;

import etsy.homework.database.views.SearchResultsView;
import etsy.homework.rest.callbacks.RestLoaderCallbacks;
import etsy.homework.rest.callbacks.RestLoaderCallbacksListener;

/**
 * Created by emir on 29/03/14.
 */
public class SearchResultsCursorLoader extends RestLoaderCallbacks {

    private static final Uri URI = SearchResultsView.URI;
    private static final int LOADER_ID = 0;

    public SearchResultsCursorLoader(Context context, LoaderManager loaderManager, RestLoaderCallbacksListener restLoaderCallbacksListener) {
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
