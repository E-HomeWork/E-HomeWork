package etsy.homework.models;

import android.content.ContentValues;

import etsy.homework.database.tables.KeywordResultRelationshipTable;

/**
 * Created by emir on 29/03/14.
 */
public class KeywordResultRelationship {

    private final Long mListingId;
    private final String mKeyword;

    public KeywordResultRelationship(Long listingId, String keyword) {
        mListingId = listingId;
        mKeyword = keyword;
    }

    public Long getListingId() {
        return mListingId;
    }

    public String getKeyword() {
        return mKeyword;
    }

    public ContentValues getContentValues() {
        final ContentValues value = new ContentValues();
        value.put(KeywordResultRelationshipTable.Columns.LISTING_ID, getListingId());
        value.put(KeywordResultRelationshipTable.Columns.KEYWORD, getKeyword());
        return value;
    }

    public static final class Keys {
        public static final String LISTING_ID = MainImage.Keys.LISTING_ID;
        public static final String KEYWORD = "keyword";
    }
}
