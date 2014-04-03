package etsy.homework.models;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import etsy.homework.database.tables.PaginationTable;

/**
 * Created by emir on 28/03/14.
 */
public class Pagination {

    public enum State {
        active, inactive
    }

    @SerializedName(Keys.NEXT_PAGE)
    private final Integer mNextPage;
    @SerializedName(Keys.EFFECTIVE_OFFSET)
    private final Integer mEffectiveOffset;
    private String mKeyword;
    private State mState = State.active;

    public Pagination(final Integer nextPage, final Integer effectiveOffset) {
        mNextPage = nextPage;
        mEffectiveOffset = effectiveOffset;
    }

    public Integer getNextPage() {
        return mNextPage;
    }

    public String getKeyword() {
        return mKeyword;
    }

    public State getState() {
        if (mState == null){
            return State.active;
        }
        return mState;
    }

    public void setKeyword(final String keyword) {
        mKeyword = keyword;
    }

    public void setState(final State state) {
        mState = state;
    }

    public Integer getEffectiveOffset() {
        return mEffectiveOffset;
    }

    public ContentValues getContentValues() {
        final ContentValues value = new ContentValues();
        value.put(PaginationTable.Columns.NEXT_PAGE, getNextPage());
        final State state = getState();
        value.put(PaginationTable.Columns.STATE, state.ordinal());
        final String keyword = getKeyword();
        if (keyword != null) {
            value.put(PaginationTable.Columns.KEYWORD, keyword);
        }

        return value;
    }

    public static final class Keys {
        public static final String NEXT_PAGE = "next_page";
        public static final String KEYWORD = KeywordResultRelationship.Keys.KEYWORD;
        public static final String STATE = "state";
        public static final String EFFECTIVE_OFFSET = "effective_offset";
    }

}

