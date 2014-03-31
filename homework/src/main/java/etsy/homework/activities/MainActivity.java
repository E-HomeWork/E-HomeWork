package etsy.homework.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import etsy.homework.Utilities.Debug;
import etsy.homework.R;
import etsy.homework.adapters.SearchResultsAdapter;
import etsy.homework.database.tables.TasksTable;
import etsy.homework.loaders.ResultsCursorLoader;
import etsy.homework.loaders.SearchResultsCursorLoader;
import etsy.homework.providers.EtsyContentProvider;
import etsy.homework.rest.callbacks.RestLoaderCallbacksListener;


public class MainActivity extends Activity implements View.OnClickListener, RestLoaderCallbacksListener {

    private AdapterView<SearchResultsAdapter> mAdapterView;
    private Button mButton;
    private EditText mEditText;
    private ResultsCursorLoader mResultsCursorLoader;
    private SearchResultsCursorLoader mSearchResultsCursorLoader;
    private SearchResultsAdapter mSearchResultsAdapter;
    private final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        mAdapterView = (AdapterView<SearchResultsAdapter>) findViewById(R.id.activity_main_list_view_search);
        mEditText = (EditText) findViewById(R.id.activity_main_edit_text_search);
        mButton = (Button) findViewById(R.id.activity_main_button_search);
        mButton.setOnClickListener(this);

        initializeLoaders();

    }

    private void initializeLoaders() {
        final Context context = getApplicationContext();
        final LoaderManager loaderManager = getLoaderManager();
        mResultsCursorLoader = new ResultsCursorLoader(context, loaderManager, this);
        mSearchResultsCursorLoader = new SearchResultsCursorLoader(context, loaderManager, this);
        mURIMatcher.addURI(EtsyContentProvider.AUTHORITY, ResultsCursorLoader.URI_PATH, ResultsCursorLoader.LOADER_ID);
        mURIMatcher.addURI(EtsyContentProvider.AUTHORITY, SearchResultsCursorLoader.URI_PATH, SearchResultsCursorLoader.LOADER_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mButton.setOnClickListener(null);
        mButton = null;
        mEditText = null;
        mAdapterView = null;
        mResultsCursorLoader = null;
        mSearchResultsCursorLoader = null;
    }

    @Override
    protected void onStart() {
        final Context context = getApplicationContext();
        mResultsCursorLoader.onStart(context);
        mSearchResultsCursorLoader.onStart(context);
        super.onStart();
    }

    @Override
    protected void onStop() {
        final Context context = getApplicationContext();
        mResultsCursorLoader.onStop(context);
        mSearchResultsCursorLoader.onStop(context);
        super.onStop();
    }

    @Override
    public void onClick(final View view) {
        final Context context = getApplicationContext();
        final String keyword = mEditText.getText().toString();
        mSearchResultsCursorLoader.setKeyword(context, keyword);
        mResultsCursorLoader.setKeyword(context, keyword);
    }

    @Override
    public void onLoadFinished(Uri uri, Cursor cursor) {
        Debug.log("uri: " + uri + " cursor.getCount(): " + cursor.getCount());

        final Context context = getApplicationContext();
        final int code = mURIMatcher.match(uri);
        switch (code){
            case SearchResultsCursorLoader.LOADER_ID:
                if (mSearchResultsAdapter == null) {
                    mSearchResultsAdapter = new SearchResultsAdapter(context, cursor);
                    mAdapterView.setAdapter(mSearchResultsAdapter);
                } else {
                    mSearchResultsAdapter.swapCursor(cursor);
                }
                break;
            default:
                final boolean isCursorEmpty = cursor.getCount() == 0;
                Debug.log("uri: " + uri + " isCursorEmpty: " + isCursorEmpty);
                if (isCursorEmpty) {
                    setProgressBarIndeterminateVisibility(true);
                    return;
                }

                final int stateColumnIndex = cursor.getColumnIndex(TasksTable.Columns.STATE);
                final String state = cursor.getString(stateColumnIndex);
                Debug.log("uri: " + uri + " state: " + state);

                if (TasksTable.State.SUCCESS.equals(state)) {
                    setProgressBarIndeterminateVisibility(false);
                } else if (TasksTable.State.FAIL.equals(state)) {
                    setProgressBarIndeterminateVisibility(false);
                } else if (TasksTable.State.RUNNING.equals(state)) {
                    setProgressBarIndeterminateVisibility(true);
                }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapterView != null) {
            mAdapterView.setAdapter(null);
        }
    }
}
