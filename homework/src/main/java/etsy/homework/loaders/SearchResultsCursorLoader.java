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

    public static final int LOADER_ID = 0;
    public static final String URI_PATH = SearchResultsView.URI_PATH;
    private static final Uri URI = SearchResultsView.URI;
    private String mKeyword;

    public SearchResultsCursorLoader(Context context, LoaderManager loaderManager, RestLoaderCallbacksListener restLoaderCallbacksListener) {
        super(context, loaderManager, restLoaderCallbacksListener);
    }

    @Override
    public String getSortOrder() {
        return SearchResultsView.Columns.TYPE + ", " + SearchResultsView.Columns.INDEX;
    }

    public void setKeyword(final Context context, final String keyword) {
        mKeyword = keyword;
        onStart(context);
    }

    @Override
    public Uri getUri() {
        final Uri uri = URI.buildUpon().appendQueryParameter(SearchResultsView.KEYWORD, mKeyword).build();
        return uri;
    }

    @Override
    public int getLoaderId() {
        return LOADER_ID;
    }

}
