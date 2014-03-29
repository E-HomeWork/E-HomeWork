package etsy.homework.tasks;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import etsy.homework.database.tables.KeywordResultRelationshipTable;
import etsy.homework.database.tables.MainImageTable;
import etsy.homework.database.tables.ResultsTable;
import etsy.homework.database.views.SearchResultsView;
import etsy.homework.models.KeywordResultRelationship;
import etsy.homework.models.MainImage;
import etsy.homework.models.Result;
import etsy.homework.models.SearchResponse;
import etsy.homework.providers.EtsyContentProvider;
import etsy.homework.rest.tasks.RestTask;

/**
 * Created by emir on 28/03/14.
 */
public class SearchTask extends RestTask {

    public static final String NAME = "searchTask";
    public static final Uri URI = Uri.parse(EtsyContentProvider.CONTENT + EtsyContentProvider.AUTHORITY + "/" + NAME);
    public static final String URL = "https://api.etsy.com/v2/listings/active?api_key=liwecjs0c3ssk6let4p1wqt9&includes=MainImage&keywords=%s";

    public SearchTask(Context context, Uri taskId) {
        super(context, taskId);
    }

    @Override
    public void executeTask() throws Exception {

        final String keyword = "cover";
        final HttpClient httpclient = new DefaultHttpClient();
        final String url = String.format(URL, keyword);
        final HttpGet httpget = new HttpGet(url);


        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        try {
            final HttpResponse response = httpclient.execute(httpget);
            final HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            inputStreamReader = new InputStreamReader(inputStream);
            final SearchResponse searchResponse = GSON.fromJson(inputStreamReader, SearchResponse.class);
            writeToDatabase(searchResponse);

        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (final IOException ioException) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException ioException) {
                }
            }
        }
    }

    private void writeToDatabase(final SearchResponse searchResponse) throws RemoteException, OperationApplicationException {
        final ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<ContentProviderOperation>();

//        final ContentProviderOperation deleteContentProviderOperation = ContentProviderOperation.newDelete(getUri()).build();
//        contentProviderOperations.add(deleteContentProviderOperation);

        final ArrayList<Result> resutls = searchResponse.getResutls();
        for (final Result result : resutls) {
            final String keyword = "cover";
            final Long listingId = result.getListingId();
            final MainImage mainImage = result.getMainImage();
            final KeywordResultRelationship keywordResultRelationship = new KeywordResultRelationship(listingId, keyword);

            final String deleteResultSelection = ResultsTable.Columns.LISTING_ID + "=?";
            final String[] deleteResultSelectionArguments = new String[]{Long.toString(listingId)};
            final ContentProviderOperation deleteResultContentProviderOperation = ContentProviderOperation.newDelete(ResultsTable.URI).withSelection(deleteResultSelection, deleteResultSelectionArguments).build();
            contentProviderOperations.add(deleteResultContentProviderOperation);

            final ContentValues resutContentValues = result.getContentValues();
            final ContentProviderOperation resultContentProviderOperation = ContentProviderOperation.newInsert(ResultsTable.URI).withValues(resutContentValues).build();
            contentProviderOperations.add(resultContentProviderOperation);

            final String deleteMainImageSelection = MainImageTable.Columns.LISTING_ID + "=?";
            final String[] deleteMainImageSelectionArguments = new String[]{Long.toString(listingId)};
            final ContentProviderOperation deleteMainImageContentProviderOperation = ContentProviderOperation.newDelete(MainImageTable.URI).withSelection(deleteMainImageSelection, deleteMainImageSelectionArguments).build();
            contentProviderOperations.add(deleteMainImageContentProviderOperation);

            final ContentValues mainImageContentValues = mainImage.getContentValues();
            final ContentProviderOperation mainImageContentProviderOperation = ContentProviderOperation.newInsert(MainImageTable.URI).withValues(mainImageContentValues).build();
            contentProviderOperations.add(mainImageContentProviderOperation);

            final String deleteKeywordRelationshipSelection = KeywordResultRelationshipTable.Columns.LISTING_ID + "=?";
            final String[] deleteKeywordRelationshipSelectionArguments = new String[]{Long.toString(listingId)};
            final ContentProviderOperation deleteKeywordRelationshipContentProviderOperation = ContentProviderOperation.newDelete(KeywordResultRelationshipTable.URI).withSelection(deleteKeywordRelationshipSelection, deleteKeywordRelationshipSelectionArguments).build();
            contentProviderOperations.add(deleteKeywordRelationshipContentProviderOperation);

            final ContentValues keywordRelationshipContentValues = keywordResultRelationship.getContentValues();
            final ContentProviderOperation keywordRelationshipContentProviderOperation = ContentProviderOperation.newInsert(KeywordResultRelationshipTable.URI).withValues(keywordRelationshipContentValues).build();
            contentProviderOperations.add(keywordRelationshipContentProviderOperation);
        }

        final ContentResolver contentResolver = getContext().getContentResolver();
        final ContentProviderResult[] contentProviderResults = contentResolver.applyBatch(EtsyContentProvider.AUTHORITY, contentProviderOperations);
        contentResolver.notifyChange(SearchResultsView.URI, null);
    }

}

