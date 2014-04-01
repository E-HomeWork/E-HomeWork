package etsy.homework.adapters;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Currency;
import java.util.Locale;
import java.util.Random;

import etsy.homework.R;
import etsy.homework.database.views.SearchResultsView;

/**
 * Created by emir on 28/03/14.
 */
public class SearchResultsAdapter extends CursorAdapter {

    private static final Random RANDOM = new Random();
    private static final String CURRENCY_FORMAT = "%s %s %s";

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

    private static void bindTextViewFromHtml(final View view, final int resourceId, final Cursor cursor, final String columnName) {
        final int columnIndex = cursor.getColumnIndex(columnName);
        final String string = cursor.getString(columnIndex);
        final TextView textView = (TextView) view.getTag(resourceId);

        final Spanned spanned = Html.fromHtml(string);
        textView.setText(spanned);
    }

    private static void bindTextViewFromCurrency(final View view, final int resourceId, final Cursor cursor, final String priceColumnName, final String currencyCodeColumnName) {
        final int priceColumnIndex = cursor.getColumnIndex(priceColumnName);
        final String price = cursor.getString(priceColumnIndex);

        final int currencyCodeColumnIndex = cursor.getColumnIndex(currencyCodeColumnName);
        final String currencyCode = cursor.getString(currencyCodeColumnIndex);

        final Currency currency = Currency.getInstance(currencyCode);
        final String currencySymbol = currency.getSymbol(Locale.getDefault());
        final String fullPriceString = String.format(CURRENCY_FORMAT, currencySymbol, price, currencyCode);

        final TextView textView = (TextView) view.getTag(resourceId);
        textView.setText(fullPriceString);
    }

    private static void bindImageView(final Context context, final View view, final int resourceId, final Cursor cursor, final String columnName) {
        final int columnIndex = cursor.getColumnIndex(columnName);
        final String string = cursor.getString(columnIndex);
        final ImageView imageView = (ImageView) view.getTag(resourceId);

        final int backgroundColorResourceId = getBackgroundColorResourceId();
        imageView.setBackgroundResource(backgroundColorResourceId);

        Picasso.with(context).load(string).into(imageView);
    }

    private static int getBackgroundColorResourceId() {
        final int backgroundChoice = RANDOM.nextInt() % 6;
        switch (backgroundChoice) {
            case 0:
                return R.color.list_item_search_result_background_color_1;
            case 1:
                return R.color.list_item_search_result_background_color_2;
            case 2:
                return R.color.list_item_search_result_background_color_3;
            case 3:
                return R.color.list_item_search_result_background_color_4;
            case 4:
                return R.color.list_item_search_result_background_color_5;
            default:
                return R.color.list_item_search_result_background_color_6;
        }
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
        bindTextViewFromHtml(view, R.id.list_item_search_result_title, cursor, SearchResultsView.Columns.TITLE);
        bindTextViewFromCurrency(view, R.id.list_item_search_result_price, cursor, SearchResultsView.Columns.PRICE, SearchResultsView.Columns.CURRENCY_CODE);
        bindImageView(context, view, R.id.list_item_search_result_imageView, cursor, SearchResultsView.Columns.URL_570_X_N);

    }
}
