package etsy.homework.database.views;

import android.net.Uri;
import android.provider.BaseColumns;

import etsy.homework.database.tables.KeywordResultRelationshipTable;
import etsy.homework.database.tables.MainImageTable;
import etsy.homework.database.tables.ResultsTable;
import etsy.homework.providers.EtsyContentProvider;

/**
 * Created by emir on 28/03/14.
 */
public class SearchResultsView {

    public static String VIEW_NAME = "searchResultsView";
    public static final String DROP = "DROP VIEW IF EXISTS " + VIEW_NAME;
    public static final String URI_PATH = VIEW_NAME;
    public static final Uri URI = Uri.parse(EtsyContentProvider.CONTENT + EtsyContentProvider.AUTHORITY + "/" + VIEW_NAME);
    public static final int CODE = 2;
    public static final String CREATE = "CREATE VIEW " + VIEW_NAME + " AS " +
        " SELECT " +
            ResultsTable.TABLE_NAME + "." + ResultsTable.Columns.LISTING_ID + " AS " + BaseColumns._ID + ", " +
            ResultsTable.TABLE_NAME + "." + ResultsTable.Columns.DESCRIPTION + " AS " + Columns.DESCRIPTION + ", " +
            ResultsTable.TABLE_NAME + "." + ResultsTable.Columns.TITLE + " AS " + Columns.TITLE + ", " +
            KeywordResultRelationshipTable.TABLE_NAME + "." + KeywordResultRelationshipTable.Columns.KEYWORD + " AS " + Columns.KEYWORD + ", " +
            MainImageTable.TABLE_NAME + "." + MainImageTable.Columns.URL_75_X_75 + " AS " + Columns.URL_75_X_75 + ", " +
            MainImageTable.TABLE_NAME + "." + MainImageTable.Columns.URL_75_X_75 + " AS " + Columns.URL_75_X_75 + ", " +
            MainImageTable.TABLE_NAME + "." + MainImageTable.Columns.URL_170_X_135 + " AS " + Columns.URL_170_X_135 + ", " +
            MainImageTable.TABLE_NAME + "." + MainImageTable.Columns.URL_570_X_N + " AS " + Columns.URL_570_X_N + ", " +
            MainImageTable.TABLE_NAME + "." + MainImageTable.Columns.URL_FULL_X_FULL + " AS " + Columns.URL_FULL_X_FULL + " " +
        " FROM " +
            ResultsTable.TABLE_NAME +
        " JOIN " +
            MainImageTable.TABLE_NAME  +
        " JOIN " +
            KeywordResultRelationshipTable.TABLE_NAME  +
        " ON " +
                ResultsTable.TABLE_NAME + "." + ResultsTable.Columns.LISTING_ID +
                    " = " +
                MainImageTable.TABLE_NAME + "." + ResultsTable.Columns.LISTING_ID +
            " AND " +
                ResultsTable.TABLE_NAME + "." + ResultsTable.Columns.LISTING_ID +
                    " = " +
                KeywordResultRelationshipTable.TABLE_NAME + "." + KeywordResultRelationshipTable.Columns.LISTING_ID +
         ";";

    public static final class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String DESCRIPTION = ResultsTable.Columns.DESCRIPTION;
        public static final String TITLE = ResultsTable.Columns.TITLE;
        public static final String PRICE = ResultsTable.Columns.PRICE;
        public static final String URL_75_X_75 = MainImageTable.Columns.URL_75_X_75;
        public static final String URL_170_X_135 = MainImageTable.Columns.URL_170_X_135;
        public static final String URL_570_X_N = MainImageTable.Columns.URL_570_X_N;
        public static final String URL_FULL_X_FULL = MainImageTable.Columns.URL_FULL_X_FULL;
        public static final String KEYWORD = KeywordResultRelationshipTable.Columns.KEYWORD;
    }

}
