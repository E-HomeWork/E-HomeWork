package etsy.homework.database.tables;

import android.net.Uri;
import android.provider.BaseColumns;

import etsy.homework.models.Pagination;
import etsy.homework.providers.EtsyContentProvider;

/**
 * Created by emir on 28/03/14.
 */
public class PaginationTable {

    public static final int CODE = 5;
    public static String TABLE_NAME = "pagination";
    public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String URI_PATH = TABLE_NAME;
    public static final Uri URI = Uri.parse(EtsyContentProvider.SCHEMA + EtsyContentProvider.AUTHORITY + "/" + TABLE_NAME);

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Columns.NEXT_PAGE + " INTEGER, " +
            Columns.STATE + " INTEGER, " +
            Columns.KEYWORD + " varchar(255) " +
            ");";

    public static final class Columns {
        public static final String KEYWORD = Pagination.Keys.KEYWORD;
        public static final String NEXT_PAGE = Pagination.Keys.NEXT_PAGE;
        public static final String STATE = Pagination.Keys.STATE;
    }
}