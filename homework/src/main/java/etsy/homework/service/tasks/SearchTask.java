package etsy.homework.service.tasks;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.Collection;

import etsy.homework.database.views.SearchResultsView;
import etsy.homework.providers.EtsyContentProvider;

/**
 * Created by emir on 28/03/14.
 */
public class SearchTask extends EtsyTask{

    public SearchTask(Context context, Uri taskId) {
        super(context, taskId);
    }

    @Override
    public void executeTask() throws Exception {
        final Collection<ContentValues> contentValueList = null;
        updateDatabase(contentValueList);
    }

    private void updateDatabase(final Collection<ContentValues> contentValueList) throws RemoteException, OperationApplicationException {
        final ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<ContentProviderOperation>();

        final ContentProviderOperation deleteContentProviderOperation = ContentProviderOperation.newDelete(getUri()).build();
        contentProviderOperations.add(deleteContentProviderOperation);

        for (final ContentValues contentValues : contentValueList) {
            final ContentProviderOperation contentProviderOperation = ContentProviderOperation.newInsert(getUri()).withValues(contentValues).build();
            contentProviderOperations.add(contentProviderOperation);
        }

        final ContentResolver contentResolver = getContext().getContentResolver();
        final ContentProviderResult[] contentProviderResults = contentResolver.applyBatch(EtsyContentProvider.AUTHORITY, contentProviderOperations);
        contentResolver.notifyChange(SearchResultsView.URI, null);
    }
}
