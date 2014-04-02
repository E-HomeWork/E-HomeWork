package etsy.homework.models;

import android.content.ContentValues;

import etsy.homework.database.tables.KeywordResultRelationshipTable;

/**
 * Created by emir on 29/03/14.
 */
public class KeywordResultRelationship {

    private final Long mListingId;
    private final String mKeyword;
    private final Integer mIndex;

    public KeywordResultRelationship(final Long listingId, final String keyword, final int index) {
        mListingId = listingId;
        mKeyword = keyword;
        mIndex = index;
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
        value.put(KeywordResultRelationshipTable.Columns.INDEX, getIndex());
        return value;
    }

    private Integer getIndex() {
        return mIndex;
    }

    public static final class Keys {
        public static final String LISTING_ID = MainImage.Keys.LISTING_ID;
        public static final String KEYWORD = "keyword";
        public static final String INDEX = "indexOrder";

    }
}

