package etsy.homework.models;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import etsy.homework.database.tables.ResultsTable;

/**
 * Created by emir on 28/03/14.
 */
public class Result {

    private static final String PRICE = "$ %s";

    @SerializedName(Keys.LISTING_ID)
    private final Long mListingId;
    @SerializedName(Keys.TITLE)
    private final String mTitle;
    @SerializedName(Keys.DESCRIPTION)
    private final String mDescription;
    @SerializedName(Keys.PRICE)
    private final String mPrice;
    @SerializedName(Keys.QUANTITY)
    private final Integer mQuantity;
    @SerializedName(Keys.MAIN_IMAGE)
    private final MainImage mMainImage;

    public Result(final Long listingId, final String title, final String description, final String price, final Integer quantity, final MainImage mainImage) {
        mListingId = listingId;
        mTitle = title;
        mDescription = description;
        mPrice = price;
        mQuantity = quantity;
        mMainImage = mainImage;
    }

    public ContentValues getContentValues(){
        final ContentValues value = new ContentValues();
        value.put(ResultsTable.Columns.LISTING_ID, getListingId());
        value.put(ResultsTable.Columns.TITLE, getTitle());
        value.put(ResultsTable.Columns.DESCRIPTION, getDescription());
        value.put(ResultsTable.Columns.PRICE, String.format(PRICE, getPrice()));
        value.put(ResultsTable.Columns.QUANTITY, getQuantity());
        return value;
    }

    public Long getListingId() {
        return mListingId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getPrice() {
        return mPrice;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public MainImage getMainImage() {
        return mMainImage;
    }

    public static final class Keys {
        public static final String LISTING_ID = "listing_id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String PRICE = "price";
        public static final String QUANTITY = "quantity";
        public static final String MAIN_IMAGE = "MainImage";

    }
}
