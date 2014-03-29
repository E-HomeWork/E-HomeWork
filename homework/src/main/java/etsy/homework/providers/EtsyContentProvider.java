package etsy.homework.providers;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Locale;

import etsy.homework.database.EtsyDatabase;
import etsy.homework.database.tables.KeywordResultRelationshipTable;
import etsy.homework.database.tables.MainImageTable;
import etsy.homework.database.tables.ResultsTable;
import etsy.homework.database.tables.TasksTable;
import etsy.homework.database.views.SearchResultsView;

/**
 * Created by emir on 28/03/14.
 */
public class EtsyContentProvider extends ContentProvider {

    private static final String MIME_TYPE = "mime_type";
    private static final long STALE_DATA_THRESHOLD = 1000 * 30; // 30 seconds

    public static final String AUTHORITY = "etsy.homework.authority";
    public static final String CONTENT = "content://";
    public static final String TASK_URI = "taskUri";
    public static final String FORCE_REQUEST = "forceRequest";

    private final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private String getTableName(final Uri uri) {
        final int match = mURIMatcher.match(uri);
        switch (match) {
            case TasksTable.CODE:
                return TasksTable.TABLE_NAME;
            case ResultsTable.CODE:
                return ResultsTable.TABLE_NAME;
            case MainImageTable.CODE:
                return MainImageTable.TABLE_NAME;
            case KeywordResultRelationshipTable.CODE:
                return KeywordResultRelationshipTable.TABLE_NAME;
            case SearchResultsView.CODE:
                return SearchResultsView.VIEW_NAME;
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        mURIMatcher.addURI(AUTHORITY, TasksTable.URI_PATH, TasksTable.CODE);
        mURIMatcher.addURI(AUTHORITY, ResultsTable.URI_PATH, ResultsTable.CODE);
        mURIMatcher.addURI(AUTHORITY, MainImageTable.URI_PATH, MainImageTable.CODE);
        mURIMatcher.addURI(AUTHORITY, KeywordResultRelationshipTable.URI_PATH, KeywordResultRelationshipTable.CODE);
        mURIMatcher.addURI(AUTHORITY, SearchResultsView.URI_PATH, SearchResultsView.CODE);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        final String dataSetName = getTableName(uri);
        final String type = MIME_TYPE + "/" + AUTHORITY + "." + dataSetName;
        return type.toLowerCase(Locale.getDefault());
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final Context context = getContext();
        final SQLiteDatabase sqLiteDatabase = EtsyDatabase.getDatabase(context);
        final long id = sqLiteDatabase.insert(getTableName(uri), null, values);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final Context context = getContext();
        final SQLiteDatabase sqLiteDatabase = EtsyDatabase.getDatabase(context);
        final int numRowsDeleted = sqLiteDatabase.delete(getTableName(uri), selection, selectionArgs);
        return numRowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final Context context = getContext();
        final SQLiteDatabase sqLiteDatabase = EtsyDatabase.getDatabase(context);
        final int numRowsAffected = sqLiteDatabase.update(getTableName(uri), values, selection, selectionArgs);
        return numRowsAffected;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        final Context context = getContext();
        final SQLiteDatabase sqLiteDatabase = EtsyDatabase.getDatabase(context);
        sqLiteDatabase.beginTransaction();
        try {
            final ContentProviderResult[] contentProviderResults = super.applyBatch(operations);
            sqLiteDatabase.setTransactionSuccessful();
            return contentProviderResults;
        } finally {
            sqLiteDatabase.endTransaction();
        }

    }
}
