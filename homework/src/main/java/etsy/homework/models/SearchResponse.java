package etsy.homework.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by emir on 28/03/14.
 */
public class SearchResponse {

    @SerializedName(Keys.COUNT)
    private final Long mCount;
    @SerializedName(Keys.RESULTS)
    private final ArrayList<Result> mResutls;
    @SerializedName(Keys.TYPE)
    private final String mType;
    @SerializedName(Keys.PAGINATION)
    private final Pagination mPagination;

    public SearchResponse(final Long count, final ArrayList<Result> results, final String type, final Pagination pagination) {
        mCount = count;
        mResutls = results;
        mType = type;
        mPagination = pagination;
    }

    public Long getCount() {
        return mCount;
    }

    public ArrayList<Result> getResutls() {
        return mResutls;
    }

    public String getType() {
        return mType;
    }

    public Pagination getPagination() {
        return mPagination;
    }

    public static boolean isValid() {
        return true;
    }

    public static final class Keys {
        public static final String COUNT = "count";
        public static final String RESULTS = "results";
        public static final String PARAMS = "params";
        public static final String TYPE = "type";
        public static final String PAGINATION = "pagination";
    }

}
