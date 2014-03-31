package etsy.homework.database.tables;

import android.net.Uri;

import etsy.homework.models.KeywordResultRelationship;
import etsy.homework.providers.EtsyContentProvider;

/**
 * Created by emir on 28/03/14.
 */
public class KeywordResultRelationshipTable {

    public static final int CODE = 4;
    public static String TABLE_NAME = "keywordResultRelationship";
    public static final String DROP = " DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String URI_PATH = TABLE_NAME;
    public static final Uri URI = Uri.parse(EtsyContentProvider.SCHEMA + EtsyContentProvider.AUTHORITY + "/" + TABLE_NAME);

    public static final String CREATE = " CREATE TABLE " + TABLE_NAME + " ( " +
            Columns.LISTING_ID + " INTEGER, " +
            Columns.KEYWORD + " varchar(255) " +
            ");";

    public static final class Columns {
        public static final String LISTING_ID = KeywordResultRelationship.Keys.LISTING_ID;
        public static final String KEYWORD = KeywordResultRelationship.Keys.KEYWORD;
    }

}
