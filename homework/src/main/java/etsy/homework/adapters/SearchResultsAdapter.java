package etsy.homework.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import etsy.homework.R;
import etsy.homework.bindings.SearchResultBindings;
import etsy.homework.database.views.SearchResultsView;
import etsy.homework.database.views.SearchResultsView.SearchResultsViewType;
import etsy.homework.listeners.PaginationListener;

/**
 * Created by emir on 28/03/14.
 */
public class SearchResultsAdapter extends CursorAdapter {

    private final Context mContext;
    private PaginationListener mPaginationListener;

    public SearchResultsAdapter(final Context context, final Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    public SearchResultsAdapter(final Context context, final Cursor cursor, final boolean autoRequery) {
        super(context, cursor, autoRequery);
        mContext = context;
    }

    public SearchResultsAdapter(final Context context, final Cursor cursor, final int flags) {
        super(context, cursor, flags);
        mContext = context;
    }

    private static void optimize(final View view, final int resourceId) {
        view.setTag(resourceId, view.findViewById(resourceId));
    }

    private static void bindTextView(final View view, final int resourceId, final Cursor cursor, final String columnName) {
        final int columnIndex = cursor.getColumnIndex(columnName);
        final String string = cursor.getString(columnIndex);
        final TextView textView = (TextView) view.getTag(resourceId);
        textView.setText(string);
    }

    public SearchResultsViewType getSearchResultsViewType (final int position){
        final Cursor cursor = (Cursor) getItem(position);
        return getSearchResultsViewType(cursor);
    }

    private SearchResultsViewType getSearchResultsViewType (final Cursor cursor){
        final int type = getItemViewType(cursor);
        return SearchResultsViewType.values()[type];
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final SearchResultsViewType searchResultsViewType = getSearchResultsViewType(cursor);
        switch (searchResultsViewType){
            case item:
                final View view = layoutInflater.inflate(R.layout.list_item_search_result_item, parent, false);

                optimize(view, R.id.list_item_search_result_title);
                optimize(view, R.id.list_item_search_result_price);
                optimize(view, R.id.list_item_search_result_imageView);

                return view;
            case pagination:
                return layoutInflater.inflate(R.layout.list_item_search_result_pagination, parent, false);
        }

        return null;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final Cursor cursor = (Cursor) getItem(position);
        final View view = getView(convertView, parent, cursor);
        bindView(view, mContext, cursor);
        return view;
    }

    private View getView(final View convertView, final ViewGroup parent, final Cursor cursor) {
        if (convertView == null) {
            return newView(mContext, cursor, parent);
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return SearchResultsViewType.values().length;
    }

    private int getItemViewType(final Cursor cursor) {
        final int typeColumnIndex = cursor.getColumnIndex(SearchResultsView.Columns.TYPE);
        final int type = cursor.getInt(typeColumnIndex);
        return type;
    }

    @Override
    public int getItemViewType(final int position) {
        final Cursor cursor = (Cursor) getItem(position);
        return getItemViewType(cursor);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final SearchResultsViewType searchResultsViewType = getSearchResultsViewType(cursor);
        switch (searchResultsViewType){
            case item:
                SearchResultBindings.bindTextViewFromHtml(view, R.id.list_item_search_result_title, cursor, SearchResultsView.Columns.TITLE);
                SearchResultBindings.bindTextViewFromCurrency(view, R.id.list_item_search_result_price, cursor, SearchResultsView.Columns.PRICE, SearchResultsView.Columns.CURRENCY_CODE);
                SearchResultBindings.bindImageViewFromUrl(context, view, R.id.list_item_search_result_imageView, cursor, SearchResultsView.Columns.URL_570_X_N);
                SearchResultBindings.bindImageViewFromIndex(context, view, R.id.list_item_search_result_imageView, cursor, SearchResultsView.Columns.INDEX);
                break;
            case pagination:
                SearchResultBindings.bindPagination(context, mPaginationListener, cursor, SearchResultsView.Columns.NEXT_PAGE);
        }

    }

    public void setPaginationListener(final PaginationListener paginationListener){
        mPaginationListener = paginationListener;
    }
}
