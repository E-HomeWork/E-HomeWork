package etsy.homework.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by emir on 28/03/14.
 */
public class Result {

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
