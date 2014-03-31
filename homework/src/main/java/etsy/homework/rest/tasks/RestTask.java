package etsy.homework.rest.tasks;

import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.google.gson.Gson;

import etsy.homework.database.EtsyDatabase;
import etsy.homework.database.tables.TasksTable;

/**
 * Created by emir on 28/03/14.
 */
public abstract class RestTask implements Runnable {
    public static final String TASK_URI = "taskUri";
    public static final String SCHEMA = "task://";
    public static final String AUTHORITY = "etsy.task";
    public static final Gson GSON = new Gson();
    private final Context mContext;
    private final Uri mTaskId;

    public RestTask(final Context context, final Uri taskId) {
        mContext = context;
        mTaskId = taskId;
    }

    public Context getContext() {
        return mContext;
    }

    public Uri getUri() {
        return mTaskId;
    }

    @Override
    public void run() {
        try {
            notifyRunning();
            executeTask();
            onSuccess();
        } catch (final Exception exception) {
            onFailure();
        }
    }

    private void notifyRunning() {

        final String whereClause = TasksTable.Columns.TASK_ID + "=? AND " + TasksTable.Columns.STATE + "<>?";
        final String[] whereArguments = new String[]{mTaskId.toString(), TasksTable.State.RUNNING};
        final ContentValues contentValues = new ContentValues();
        contentValues.put(TasksTable.Columns.STATE, TasksTable.State.RUNNING);
        contentValues.put(TasksTable.Columns.TASK_ID, mTaskId.toString());
        contentValues.put(TasksTable.Columns.TIME, System.currentTimeMillis());


        final Context context = getContext();
        final SQLiteDatabase sqLiteDatabase = EtsyDatabase.getDatabase(context);
        sqLiteDatabase.beginTransaction();
        try {
            final int rows = sqLiteDatabase.update(TasksTable.TABLE_NAME, contentValues, whereClause, whereArguments);
            if (rows == 0) {
                final String queryWhereClause = TasksTable.Columns.TASK_ID + "=? AND " + TasksTable.Columns.STATE + "=?";
                final String[] queryWhereArguments = new String[]{mTaskId.toString(), TasksTable.State.RUNNING};
                final Cursor cursor = sqLiteDatabase.query(TasksTable.TABLE_NAME, null, queryWhereClause, queryWhereArguments, null, null, null);
                try {
                    if (cursor.getCount() != 0)
                        return;
                } finally {
                    cursor.close();
                }
                sqLiteDatabase.insert(TasksTable.TABLE_NAME, null, contentValues);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

        final ContentResolver contentResolver = getContext().getContentResolver();
        contentResolver.notifyChange(TasksTable.URI, null);
    }

    private void onFailure() {
        notifyState(TasksTable.State.FAIL);
    }

    private void onSuccess() {
        notifyState(TasksTable.State.SUCCESS);
    }

    private void notifyState(final String state) {
        final ContentResolver contentResolver = getContext().getContentResolver();
        final String whereClause = TasksTable.Columns.TASK_ID + "=?";
        final String[] whereArguments = new String[]{mTaskId.toString()};
        final ContentValues contentValues = new ContentValues();
        contentValues.put(TasksTable.Columns.STATE, state);
        contentValues.put(TasksTable.Columns.TASK_ID, mTaskId.toString());
        contentValues.put(TasksTable.Columns.TIME, System.currentTimeMillis());
        contentResolver.update(TasksTable.URI, contentValues, whereClause, whereArguments);
        contentResolver.notifyChange(TasksTable.URI, null);
    }

    public abstract void executeTask() throws Exception;

}
