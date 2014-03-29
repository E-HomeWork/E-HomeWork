package etsy.homework.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by emir on 28/03/14.
 */
public class Pagination {
    @SerializedName(Keys.NEXT_PAGE)
    private final Integer mNextPage;

    public Pagination(final Integer nextPage) {
        mNextPage = nextPage;
    }

    public Integer getNextPage() {
        return mNextPage;
    }

    public static final class Keys {
        public static final String NEXT_PAGE = "next_page";
    }

}
