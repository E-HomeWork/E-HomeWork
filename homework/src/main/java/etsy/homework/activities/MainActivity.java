package etsy.homework.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import etsy.homework.R;
import etsy.homework.Utilities.Debug;
import etsy.homework.adapters.SearchResultsAdapter;
import etsy.homework.database.tables.TasksTable;
import etsy.homework.loaders.ResultsCursorLoader;
import etsy.homework.loaders.SearchResultsCursorLoader;
import etsy.homework.providers.EtsyContentProvider;
import etsy.homework.rest.callbacks.RestLoaderCallbacksListener;


public class MainActivity extends Activity implements View.OnClickListener, RestLoaderCallbacksListener, TextView.OnEditorActionListener {

    private static final String IS_SEARCH_EDIT_TEXT_VISIBLE = "isSearchEditTextVisible";
    private static final String KEYWORD = "keyword";
    private final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private AdapterView<SearchResultsAdapter> mAdapterView;
    private ResultsCursorLoader mResultsCursorLoader;
    private SearchResultsCursorLoader mSearchResultsCursorLoader;
    private SearchResultsAdapter mSearchResultsAdapter;
    private View mSearchButton;
    private EditText mSearchEditText;
    private ProgressBar mProgressBar;
    private String mKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        mAdapterView = (AdapterView<SearchResultsAdapter>) findViewById(R.id.activity_main_list_view_search);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_main_progress_bar);

        initializeLoaders(savedInstanceState);
        setUpActionBar(savedInstanceState);

    }

    private void setUpActionBar(final Bundle savedInstanceState) {
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setCustomView(R.layout.action_bar_activity_main);
        actionBar.setIcon(R.drawable.ic_launcher);
        mSearchButton = actionBar.getCustomView().findViewById(R.id.action_bar_button_search);
        mSearchEditText = (EditText) actionBar.getCustomView().findViewById(R.id.action_bar_edit_text_search);
        mSearchButton.setOnClickListener(this);
        mSearchEditText.setOnEditorActionListener(this);

        if (savedInstanceState != null){
            final boolean isSearchEditTextVisible = savedInstanceState.getBoolean(IS_SEARCH_EDIT_TEXT_VISIBLE);
            if (isSearchEditTextVisible){
                enableSearch();
            }
        }
    }

    private void tearDownActionBar() {
        mSearchButton.setOnClickListener(null);
        mSearchButton = null;
    }

    private void initializeLoaders(final Bundle savedInstanceState) {
        final Context context = getApplicationContext();
        final LoaderManager loaderManager = getLoaderManager();
        mResultsCursorLoader = new ResultsCursorLoader(context, loaderManager, this);
        mSearchResultsCursorLoader = new SearchResultsCursorLoader(context, loaderManager, this);
        mURIMatcher.addURI(EtsyContentProvider.AUTHORITY, ResultsCursorLoader.URI_PATH, ResultsCursorLoader.LOADER_ID);
        mURIMatcher.addURI(EtsyContentProvider.AUTHORITY, SearchResultsCursorLoader.URI_PATH, SearchResultsCursorLoader.LOADER_ID);

        if (savedInstanceState != null){
            mKeyword = savedInstanceState.getString(KEYWORD);
            if (mKeyword != null){
                mSearchResultsCursorLoader.setKeyword(getApplicationContext(), mKeyword);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapterView = null;
        mResultsCursorLoader = null;
        mSearchResultsCursorLoader = null;
        mProgressBar = null;
        tearDownActionBar();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_SEARCH_EDIT_TEXT_VISIBLE, isSearchEditTextVisible());
        outState.putString(KEYWORD, mKeyword);
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

    private boolean isSearchEditTextVisible(){
        final int visibility = mSearchEditText.getVisibility();
        final boolean isVisible = visibility == View.VISIBLE;
        return isVisible;
    }

    @Override
    public void onClick(final View view) {
        if (!isSearchEditTextVisible()) {
            enableSearch();
            final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_open_animation);
            mSearchEditText.startAnimation(animation);
        }
    }

    @Override
    public void onLoadFinished(final Uri uri, final Cursor cursor) {
        Debug.log("uri: " + uri + " cursor.getCount(): " + cursor.getCount());

        final Context context = getApplicationContext();
        final int code = mURIMatcher.match(uri);
        switch (code) {
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
                    setProgrssBarState(true);
                    return;
                }

                final int stateColumnIndex = cursor.getColumnIndex(TasksTable.Columns.STATE);
                final String state = cursor.getString(stateColumnIndex);
                Debug.log("uri: " + uri + " state: " + state);

                if (TasksTable.State.SUCCESS.equals(state)) {
                    setProgrssBarState(false);
                } else if (TasksTable.State.FAIL.equals(state)) {
                    setProgrssBarState(false);
                } else if (TasksTable.State.RUNNING.equals(state)) {
                    setProgrssBarState(true);
                }
        }
    }

    private void setProgrssBarState(final boolean isVisible){
        if (isVisible) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        if (mAdapterView != null) {
            mAdapterView.setAdapter(null);
        }
    }

    @Override
    public boolean onEditorAction(final TextView textView, final int actionId, final KeyEvent event) {
        final Context context = getApplicationContext();
        mKeyword = textView.getText().toString();
        mSearchResultsCursorLoader.setKeyword(context, mKeyword);
        mResultsCursorLoader.setKeyword(context, mKeyword);

        disableSearch(textView);

        return true;
    }

    private void disableSearch(final TextView textView) {
        textView.setVisibility(View.GONE);
        textView.setText(null);
        mSearchButton.setVisibility(View.VISIBLE);
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }

    private void enableSearch() {
        mSearchEditText.setVisibility(View.VISIBLE);
        mSearchButton.setVisibility(View.GONE);
        mSearchEditText.requestFocus();
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mSearchEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onBackPressed() {
        if (isSearchEditTextVisible()){
            disableSearch(mSearchEditText);
            return;
        }
        super.onBackPressed();
    }
}

