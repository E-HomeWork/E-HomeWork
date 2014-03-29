package etsy.homework.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by emir on 28/03/14.
 */
public class MainImage {

    @SerializedName(Keys.LISTING_ID)
    private final Long mListingId;
    @SerializedName(Keys.LISTING_IMAGE_ID)
    private final Long mListingImageId;
    @SerializedName(Keys.URL_75_X_75)
    private final String mUrl75X75;
    @SerializedName(Keys.URL_170_X_135)
    private final String mUrl170X135;
    @SerializedName(Keys.URL_570_X_N)
    private final String mUrl570XN;
    @SerializedName(Keys.URL_FULL_X_FULL)
    private final String mUrlFullXFull;

    public MainImage(final Long listingId, final Long listingImageId, final String url75X75, final String url170X135, final String url570XN, final String urlFullXFull) {
        mListingId = listingId;
        mListingImageId = listingImageId;
        mUrl75X75 = url75X75;
        mUrl170X135 = url170X135;
        mUrl570XN = url570XN;
        mUrlFullXFull = urlFullXFull;
    }

    public Long getListingId() {
        return mListingId;
    }

    public Long getListingImageId() {
        return mListingImageId;
    }

    public String getUrl75X75() {
        return mUrl75X75;
    }

    public String getUrl170X135() {
        return mUrl170X135;
    }

    public String getUrl570XN() {
        return mUrl570XN;
    }

    public String getUrlFullXFull() {
        return mUrlFullXFull;
    }

    public static final class Keys {
        public static final String LISTING_ID = "listing_id";
        public static final String LISTING_IMAGE_ID = "listing_image_id";
        public static final String URL_75_X_75 = "url_75x75";
        public static final String URL_170_X_135 = "url_170x135";
        public static final String URL_570_X_N = "url_570xN";
        public static final String URL_FULL_X_FULL = "url_fullxfull";

    }
}
