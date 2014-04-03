package etsy.homework.database.tables;

import android.net.Uri;

import etsy.homework.models.MainImage;
import etsy.homework.providers.EtsyContentProvider;

/**
 * Created by emir on 28/03/14.
 */
public class MainImageTable {

    public static final int CODE = 0;
    public static String TABLE_NAME = "mainImage";
    public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String URI_PATH = TABLE_NAME;
    public static final Uri URI = Uri.parse(EtsyContentProvider.SCHEMA + EtsyContentProvider.AUTHORITY + "/" + TABLE_NAME);

    public static final String DELETE_KEYWORD_MATCH =
            " DELETE FROM " +
                TABLE_NAME +
            " WHERE " +
                    Columns.LISTING_ID + " IN " +
                        " ( " +
                            " SELECT " +
                                KeywordResultRelationshipTable.Columns.LISTING_ID +
                            " FROM " +
                                KeywordResultRelationshipTable.TABLE_NAME +
                            " WHERE " +
                                KeywordResultRelationshipTable.Columns.KEYWORD + "=? " +
                        " ) " +
                " AND " +
                    Columns.LISTING_ID + " NOT IN " +
                        " ( " +
                            " SELECT " +
                                KeywordResultRelationshipTable.Columns.LISTING_ID +
                            " FROM " +
                                KeywordResultRelationshipTable.TABLE_NAME +
                            " WHERE " +
                                KeywordResultRelationshipTable.Columns.KEYWORD + "<>? " +
                        " ) " +
            " ; ";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            Columns.LISTING_ID + " INTEGER, " +
            Columns.LISTING_IMAGE_ID + " INTEGER, " +
            Columns.URL_75_X_75 + " varchar(255), " +
            Columns.URL_170_X_135 + "	varchar(255), " +
            Columns.URL_570_X_N + " varchar(255), " +
            Columns.URL_FULL_X_FULL + " varchar(255) " +
            ");";

    public static final class Columns {
        public static final String LISTING_ID = MainImage.Keys.LISTING_ID;
        public static final String LISTING_IMAGE_ID = MainImage.Keys.LISTING_IMAGE_ID;
        public static final String URL_75_X_75 = MainImage.Keys.URL_75_X_75;
        public static final String URL_170_X_135 = MainImage.Keys.URL_170_X_135;
        public static final String URL_570_X_N = MainImage.Keys.URL_570_X_N;
        public static final String URL_FULL_X_FULL = MainImage.Keys.URL_FULL_X_FULL;
    }
}