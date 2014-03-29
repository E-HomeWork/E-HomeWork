package etsy.homework.service.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

import etsy.homework.database.tables.TasksTable;

/**
 * Created by emir on 28/03/14.
 */
public abstract class EtsyTask implements Runnable{
    private final Context mContext;
    private final Uri mTaskId;

    public static final Gson GSON = new Gson();

    public EtsyTask(final Context context, final Uri taskId) {
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
        } finally {
        }
    }

    private void notifyRunning() {
        final ContentResolver contentResolver = getContext().getContentResolver();
        final String whereClause = TasksTable.Columns.TASK_ID + "=? AND " + TasksTable.Columns.STATE + "<>?";
        final String[] whereArguments = new String[]{mTaskId.toString(), TasksTable.State.RUNNING};
        final ContentValues contentValues = new ContentValues();
        contentValues.put(TasksTable.Columns.STATE, TasksTable.State.RUNNING);
        contentValues.put(TasksTable.Columns.TASK_ID, mTaskId.toString());
        contentValues.put(TasksTable.Columns.TIME, System.currentTimeMillis());

        // THE FOLLOWING NEEDS TO BE ATOMIC
        final int rows = contentResolver.update(TasksTable.URI, contentValues, whereClause, whereArguments);
        if (rows == 0) {
            final String queryWhereClause = TasksTable.Columns.TASK_ID + "=? AND " + TasksTable.Columns.STATE + "=?";
            final String[] queryWhereArguments = new String[]{mTaskId.toString(), TasksTable.State.RUNNING};

            final Cursor cursor = contentResolver.query(TasksTable.URI, null, queryWhereClause, queryWhereArguments, null);
            try {
                if (cursor.getCount() != 0)
                    return;
            } finally {
                cursor.close();
            }
            contentResolver.insert(TasksTable.URI, contentValues);

        }
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
