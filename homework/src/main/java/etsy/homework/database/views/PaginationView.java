package etsy.homework.database.views;

import android.net.Uri;
import android.provider.BaseColumns;

import etsy.homework.database.tables.KeywordResultRelationshipTable;
import etsy.homework.database.tables.MainImageTable;
import etsy.homework.database.tables.PaginationTable;
import etsy.homework.database.tables.ResultsTable;
import etsy.homework.providers.EtsyContentProvider;

/**
 * Created by emir on 28/03/14.
 */
public class PaginationView {

    public static String VIEW_NAME = "paginationView";
    public static final String DROP = "DROP VIEW IF EXISTS " + VIEW_NAME;
    public static final String CREATE = "CREATE VIEW " + VIEW_NAME + " AS " +
        " SELECT " +
            PaginationTable.TABLE_NAME + "." + PaginationTable.Columns.KEYWORD + " AS " + Columns.KEYWORD + ", " +
            " MAX ( " +
                PaginationTable.TABLE_NAME + "." + PaginationTable.Columns.NEXT_PAGE +
            " ) " +
                " AS " + Columns.NEXT_PAGE + " " +
        " FROM " +
            PaginationTable.TABLE_NAME +
        " GROUP BY " +
            Columns.KEYWORD +
         ";";

    public static final class Columns {
        public static final String KEYWORD = PaginationTable.Columns.KEYWORD;
        public static final String NEXT_PAGE = PaginationTable.Columns.NEXT_PAGE;
    }

}
