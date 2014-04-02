package etsy.homework.bindings;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Currency;
import java.util.Locale;

import etsy.homework.R;

/**
 * Created by emir on 01/04/14.
 */
public class SearchResultBindings {

    private static final String CURRENCY_FORMAT = "%s %s %s";

    public static void bindTextViewFromHtml(final View view, final int resourceId, final Cursor cursor, final String columnName) {
        final int columnIndex = cursor.getColumnIndex(columnName);
        final String string = cursor.getString(columnIndex);
        final TextView textView = (TextView) view.getTag(resourceId);

        final Spanned spanned = Html.fromHtml(string);
        textView.setText(spanned);
    }

    public static void bindTextViewFromCurrency(final View view, final int resourceId, final Cursor cursor, final String priceColumnName, final String currencyCodeColumnName) {
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

    public static void bindImageViewFromUrl(final Context context, final View view, final int resourceId, final Cursor cursor, final String columnName) {
        final int columnIndex = cursor.getColumnIndex(columnName);
        final String string = cursor.getString(columnIndex);
        final ImageView imageView = (ImageView) view.getTag(resourceId);

        Picasso.with(context).load(string).into(imageView);
    }

    public static void bindImageViewFromIndex(final Context context, final View view, final int resourceId, final Cursor cursor, final String columnName) {
        final int columnIndex = cursor.getColumnIndex(columnName);
        final int index = cursor.getInt(columnIndex);
        final ImageView imageView = (ImageView) view.getTag(resourceId);

        final int backgroundColorResourceId = getBackgroundColorResourceId(index);
        imageView.setBackgroundResource(backgroundColorResourceId);
    }

    private static int getBackgroundColorResourceId(final int index) {
        final int backgroundChoice = index % 6;
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
}
