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

/**
 * Created by emir on 28/03/14.
 */
public class SearchResultsAdapter extends CursorAdapter {

    public SearchResultsAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public SearchResultsAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public SearchResultsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
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

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.list_item_search_result, parent, false);

        optimize(view, R.id.list_item_search_result_title);
        optimize(view, R.id.list_item_search_result_price);
        optimize(view, R.id.list_item_search_result_imageView);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        SearchResultBindings.bindTextViewFromHtml(view, R.id.list_item_search_result_title, cursor, SearchResultsView.Columns.TITLE);
        SearchResultBindings.bindTextViewFromCurrency(view, R.id.list_item_search_result_price, cursor, SearchResultsView.Columns.PRICE, SearchResultsView.Columns.CURRENCY_CODE);
        SearchResultBindings.bindImageViewFromUrl(context, view, R.id.list_item_search_result_imageView, cursor, SearchResultsView.Columns.URL_570_X_N);
        SearchResultBindings.bindImageViewFromIndex(context, view, R.id.list_item_search_result_imageView, cursor, SearchResultsView.Columns.INDEX);
    }
}
