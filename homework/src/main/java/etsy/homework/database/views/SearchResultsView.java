package etsy.homework.database.views;

import android.net.Uri;
import android.provider.BaseColumns;

import etsy.homework.database.tables.KeywordResultRelationshipTable;
import etsy.homework.database.tables.MainImageTable;
import etsy.homework.database.tables.PaginationTable;
import etsy.homework.database.tables.ResultsTable;
import etsy.homework.models.Pagination;
import etsy.homework.providers.EtsyContentProvider;

/**
 * Created by emir on 28/03/14.
 */
public class SearchResultsView {

    public enum SearchResultsViewType {
        item, pagination
    }

    public static final String KEYWORD = "keyword";
    public static final int CODE = 2;
    public static String VIEW_NAME = "searchResultsView";
    public static final String DROP = "DROP VIEW IF EXISTS " + VIEW_NAME;
    public static final String URI_PATH = VIEW_NAME;
    public static final Uri URI = Uri.parse(EtsyContentProvider.SCHEMA + EtsyContentProvider.AUTHORITY + "/" + VIEW_NAME);

    public static final String DELETE_KEYWORD_MATCH =
            " DELETE FROM " +
                ResultsTable.TABLE_NAME +
            " WHERE " +
                    ResultsTable.Columns.LISTING_ID + " IN " +
                        " ( " +
                            " SELECT " +
                                KeywordResultRelationshipTable.Columns.LISTING_ID +
                            " FROM " +
                                KeywordResultRelationshipTable.TABLE_NAME +
                            " WHERE " +
                                KeywordResultRelationshipTable.Columns.KEYWORD + "=? " +
                        " ) " +
                " AND " +
                    ResultsTable.Columns.LISTING_ID + " NOT IN " +
                        " ( " +
                            " SELECT " +
                                KeywordResultRelationshipTable.Columns.LISTING_ID +
                            " FROM " +
                                KeywordResultRelationshipTable.TABLE_NAME +
                            " WHERE " +
                                KeywordResultRelationshipTable.Columns.KEYWORD + "<>? " +
                        " ) " +
            " ; ";


    public static final String CREATE = "CREATE VIEW " + VIEW_NAME + " AS " +
        " SELECT " +
            ResultsTable.TABLE_NAME + "." + ResultsTable.Columns.LISTING_ID + " AS " + BaseColumns._ID + ", " +
            ResultsTable.TABLE_NAME + "." + ResultsTable.Columns.DESCRIPTION + " AS " + Columns.DESCRIPTION + ", " +
            ResultsTable.TABLE_NAME + "." + ResultsTable.Columns.TITLE + " AS " + Columns.TITLE + ", " +
            ResultsTable.TABLE_NAME + "." + ResultsTable.Columns.PRICE + " AS " + Columns.PRICE + ", " +
            ResultsTable.TABLE_NAME + "." + ResultsTable.Columns.CURRENCY_CODE + " AS " + Columns.CURRENCY_CODE + ", " +
            KeywordResultRelationshipTable.TABLE_NAME + "." + KeywordResultRelationshipTable.Columns.KEYWORD + " AS " + Columns.KEYWORD + ", " +
            KeywordResultRelationshipTable.TABLE_NAME + "." + KeywordResultRelationshipTable.Columns.INDEX + " AS " + Columns.INDEX + ", " +
            MainImageTable.TABLE_NAME + "." + MainImageTable.Columns.URL_75_X_75 + " AS " + Columns.URL_75_X_75 + ", " +
            MainImageTable.TABLE_NAME + "." + MainImageTable.Columns.URL_75_X_75 + " AS " + Columns.URL_75_X_75 + ", " +
            MainImageTable.TABLE_NAME + "." + MainImageTable.Columns.URL_170_X_135 + " AS " + Columns.URL_170_X_135 + ", " +
            MainImageTable.TABLE_NAME + "." + MainImageTable.Columns.URL_570_X_N + " AS " + Columns.URL_570_X_N + ", " +
            MainImageTable.TABLE_NAME + "." + MainImageTable.Columns.URL_FULL_X_FULL + " AS " + Columns.URL_FULL_X_FULL + ", " +
            "'" + SearchResultsViewType.item.ordinal() + "'" + " AS " + Columns.TYPE + ", " +
            " NULL AS " + Columns.PAGINATION_STATE + ", " +
            " NULL AS " + Columns.NEXT_PAGE + " " +
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
        " UNION " +
            " SELECT " +
                PaginationView.VIEW_NAME + "." + PaginationView.Columns.NEXT_PAGE + " AS " + BaseColumns._ID + ", " +
                " NULL AS " + Columns.DESCRIPTION + ", " +
                " NULL AS " + Columns.TITLE + ", " +
                " NULL AS " + Columns.PRICE + ", " +
                " NULL AS " + Columns.CURRENCY_CODE + ", " +
                PaginationView.VIEW_NAME + "." + PaginationView.Columns.KEYWORD + " AS " + Columns.KEYWORD + ", " +
                " NULL AS " + Columns.INDEX + ", " +
                " NULL AS " + Columns.URL_75_X_75 + ", " +
                " NULL AS " + Columns.URL_75_X_75 + ", " +
                " NULL AS " + Columns.URL_170_X_135 + ", " +
                " NULL AS " + Columns.URL_570_X_N + ", " +
                " NULL AS " + Columns.URL_FULL_X_FULL + ", " +
                "'" + SearchResultsViewType.pagination.ordinal() + "'" + " AS " + Columns.TYPE + ", " +
                PaginationView.VIEW_NAME + "." + PaginationView.Columns.STATE + " AS " + Columns.PAGINATION_STATE + ", " +
                PaginationView.VIEW_NAME + "." + PaginationView.Columns.NEXT_PAGE + " AS " + Columns.NEXT_PAGE + " " +
            " FROM " +
                PaginationView.VIEW_NAME +
            " WHERE " +
                PaginationView.VIEW_NAME + "." + PaginationView.Columns.NEXT_PAGE + " IS NOT NULL " +
        " ORDER BY " +
            Columns.TYPE + " ASC " +
        ";";

    public static final class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String DESCRIPTION = ResultsTable.Columns.DESCRIPTION;
        public static final String TITLE = ResultsTable.Columns.TITLE;
        public static final String PRICE = ResultsTable.Columns.PRICE;
        public static final String CURRENCY_CODE = ResultsTable.Columns.CURRENCY_CODE;
        public static final String URL_75_X_75 = MainImageTable.Columns.URL_75_X_75;
        public static final String URL_170_X_135 = MainImageTable.Columns.URL_170_X_135;
        public static final String URL_570_X_N = MainImageTable.Columns.URL_570_X_N;
        public static final String URL_FULL_X_FULL = MainImageTable.Columns.URL_FULL_X_FULL;
        public static final String KEYWORD = KeywordResultRelationshipTable.Columns.KEYWORD;
        public static final String INDEX = KeywordResultRelationshipTable.Columns.INDEX;
        public static final String NEXT_PAGE = PaginationView.Columns.NEXT_PAGE;
        public static final String TYPE = "type";
        public static final String PAGINATION_STATE = PaginationTable.Columns.STATE;
    }

}
