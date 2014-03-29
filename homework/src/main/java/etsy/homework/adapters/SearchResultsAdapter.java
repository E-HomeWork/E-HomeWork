package etsy.homework.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import etsy.homework.R;
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

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.list_item_search_result, null);

        optimize(view, R.id.list_item_search_result_title);
        optimize(view, R.id.list_item_search_result_price);
        optimize(view, R.id.list_item_search_result_imageView);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        bindTextView(view, R.id.list_item_search_result_title, cursor, SearchResultsView.Columns.TITLE);
        bindTextView(view, R.id.list_item_search_result_price, cursor, SearchResultsView.Columns.PRICE);
        bindImageView(context, view, R.id.list_item_search_result_imageView, cursor, SearchResultsView.Columns.URL_170_X_135);

    }

    private static void optimize(final View view, final int resourceId) {
        view.setTag(resourceId, view.findViewById(resourceId));
    }

    private static void bindTextView(final View view, final int resourceId, final Cursor cursor, final String columnName){
        final int columnIndex = cursor.getColumnIndex(columnName);
        final String string = cursor.getString(columnIndex);
        final TextView textView = (TextView) view.getTag(resourceId);
        textView.setText(string);
    }

    private static void bindImageView(final Context context, final View view, final int resourceId, final Cursor cursor, final String columnName){
        final int columnIndex = cursor.getColumnIndex(columnName);
        final String string = cursor.getString(columnIndex);
        final ImageView imageView = (ImageView) view.getTag(resourceId);

        Picasso.with(context).load(string).into(imageView);
    }
}
