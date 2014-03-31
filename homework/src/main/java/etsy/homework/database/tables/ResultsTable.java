package etsy.homework.database.tables;

import android.net.Uri;

import etsy.homework.models.Result;
import etsy.homework.providers.EtsyContentProvider;

/**
 * Created by emir on 28/03/14.
 */
public class ResultsTable {

    public static final int CODE = 1;
    public static String TABLE_NAME = "results";
    public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String URI_PATH = TABLE_NAME;
    public static final Uri URI = Uri.parse(EtsyContentProvider.SCHEMA + EtsyContentProvider.AUTHORITY + "/" + TABLE_NAME);

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Columns.LISTING_ID + " INTEGER, " +
            Columns.TITLE + " varchar(255), " +
            Columns.DESCRIPTION + " varchar(255), " +
            Columns.PRICE + " varchar(255), " +
            Columns.QUANTITY + " INTEGER " +
            ");";

    public static final class Columns {
        public static final String LISTING_ID = Result.Keys.LISTING_ID;
        public static final String TITLE = Result.Keys.TITLE;
        public static final String DESCRIPTION = Result.Keys.DESCRIPTION;
        public static final String PRICE = Result.Keys.PRICE;
        public static final String QUANTITY = Result.Keys.QUANTITY;
    }
}
