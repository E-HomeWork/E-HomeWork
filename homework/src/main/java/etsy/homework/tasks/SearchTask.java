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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import etsy.homework.Utilities.Debug;
import etsy.homework.database.tables.KeywordResultRelationshipTable;
import etsy.homework.database.tables.MainImageTable;
import etsy.homework.database.tables.PaginationTable;
import etsy.homework.database.tables.ResultsTable;
import etsy.homework.database.views.SearchResultsView;
import etsy.homework.models.KeywordResultRelationship;
import etsy.homework.models.MainImage;
import etsy.homework.models.Pagination;
import etsy.homework.models.Result;
import etsy.homework.models.SearchResponse;
import etsy.homework.providers.EtsyContentProvider;
import etsy.homework.rest.tasks.RestTask;

/**
 * Created by emir on 28/03/14.
 */
public class SearchTask extends RestTask {

    public static final String URI_PATH = "searchTask";
    public static final Uri URI = Uri.parse(RestTask.SCHEMA + RestTask.AUTHORITY + "/" + URI_PATH);
    public static final String KEYWORD = "keyword";
    public static final String PAGE = "page";
    public static final int CODE = 0;
    public static final int FIRST_PAGE = 1;
    private static final int CONNECTION_TIMEOUT = 1000 * 3;
    private static final int SOCKET_TIMEOUT = 1000 * 5;
    private static final String URL = "https://api.etsy.com/v2/listings/active?api_key=liwecjs0c3ssk6let4p1wqt9&includes=MainImage&keywords=%s&page=%d";
    private final String mKeyword;
    private final int mPage;

    public SearchTask(Context context, Uri uri) {
        super(context, uri);
        mKeyword = uri.getQueryParameter(KEYWORD);
        final String pageString = uri.getQueryParameter(PAGE);
        if (pageString == null || pageString.isEmpty()) {
            mPage = FIRST_PAGE;
        } else {
            mPage = Integer.parseInt(pageString);
        }
    }

    @Override
    public void executeTask() throws Exception {
        Debug.log("mKeyword: " + mKeyword);

        if (mKeyword == null || mKeyword.isEmpty()) {
            return;
        }

        final String url = String.format(URL, mKeyword, mPage);
        final HttpGet httpget = new HttpGet(url);
        final HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
        final HttpClient httpclient = new DefaultHttpClient(httpParameters);

        Debug.log("url: " + url);


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


        final Pagination pagination = searchResponse.getPagination();
        pagination.setKeyword(mKeyword);
        final int effectiveOffset = pagination.getEffectiveOffset();
        final boolean isFirstPage = mPage == FIRST_PAGE;
        if (isFirstPage) {
            // Pagination
            final String deletePaginationSelection = PaginationTable.Columns.KEYWORD + "=?";
            final String[] deletePaginationSelectionArguments = new String[]{mKeyword};
            final ContentProviderOperation deletePaginationContentProviderOperation = ContentProviderOperation.newDelete(PaginationTable.URI).withSelection(deletePaginationSelection, deletePaginationSelectionArguments).build();
            contentProviderOperations.add(deletePaginationContentProviderOperation);

            // SearchResultsView
            final String deletSearchResultsSelection = SearchResultsView.Columns.KEYWORD + "=?";
            final String[] deleteSearchResultsSelectionArguments = new String[]{mKeyword};
            final ContentProviderOperation deleteSearchResultsContentProviderOperation = ContentProviderOperation.newDelete(SearchResultsView.URI).withSelection(deletSearchResultsSelection, deleteSearchResultsSelectionArguments).build();
            contentProviderOperations.add(deleteSearchResultsContentProviderOperation);

            // MainImage

            // KeywordRelationship
            final String deleteKeywordRelationshipSelection = KeywordResultRelationshipTable.Columns.KEYWORD + "=?";
            final String[] deleteKeywordRelationshipSelectionArguments = new String[]{mKeyword};
            final ContentProviderOperation deleteKeywordRelationshipContentProviderOperation = ContentProviderOperation.newDelete(KeywordResultRelationshipTable.URI).withSelection(deleteKeywordRelationshipSelection, deleteKeywordRelationshipSelectionArguments).build();
            contentProviderOperations.add(deleteKeywordRelationshipContentProviderOperation);
        }

        // Pagination
        final ContentValues paginationContentValues = pagination.getContentValues();
        final ContentProviderOperation paginationContentProviderOperation = ContentProviderOperation.newInsert(PaginationTable.URI).withValues(paginationContentValues).build();
        contentProviderOperations.add(paginationContentProviderOperation);

        final ArrayList<Result> resutls = searchResponse.getResutls();
        for (int index = 0; index < resutls.size(); index++) {

            final Result result = resutls.get(index);

            // Reults
            final Long listingId = result.getListingId();
            final String deleteResultSelection = ResultsTable.Columns.LISTING_ID + "=?";
            final String[] deleteResultSelectionArguments = new String[]{Long.toString(listingId)};
            final ContentProviderOperation deleteResultContentProviderOperation = ContentProviderOperation.newDelete(ResultsTable.URI).withSelection(deleteResultSelection, deleteResultSelectionArguments).build();
            contentProviderOperations.add(deleteResultContentProviderOperation);

            final ContentValues resultContentValues = result.getContentValues();
            final ContentProviderOperation resultContentProviderOperation = ContentProviderOperation.newInsert(ResultsTable.URI).withValues(resultContentValues).build();
            contentProviderOperations.add(resultContentProviderOperation);

            // Main Image
            final String deleteMainImageSelection = MainImageTable.Columns.LISTING_ID + "=?";
            final String[] deleteMainImageSelectionArguments = new String[]{Long.toString(listingId)};
            final ContentProviderOperation deleteMainImageContentProviderOperation = ContentProviderOperation.newDelete(MainImageTable.URI).withSelection(deleteMainImageSelection, deleteMainImageSelectionArguments).build();
            contentProviderOperations.add(deleteMainImageContentProviderOperation);

            final MainImage mainImage = result.getMainImage();
            final ContentValues mainImageContentValues = mainImage.getContentValues();
            final ContentProviderOperation mainImageContentProviderOperation = ContentProviderOperation.newInsert(MainImageTable.URI).withValues(mainImageContentValues).build();
            contentProviderOperations.add(mainImageContentProviderOperation);

            // Keyword Relationship
            final String deleteKeywordRelationshipSelection = KeywordResultRelationshipTable.Columns.LISTING_ID + "=? AND " + KeywordResultRelationshipTable.Columns.KEYWORD + "=?";
            final String[] deleteKeywordRelationshipSelectionArguments = new String[]{Long.toString(listingId), mKeyword};
            final ContentProviderOperation deleteKeywordRelationshipContentProviderOperation = ContentProviderOperation.newDelete(KeywordResultRelationshipTable.URI).withSelection(deleteKeywordRelationshipSelection, deleteKeywordRelationshipSelectionArguments).build();
            contentProviderOperations.add(deleteKeywordRelationshipContentProviderOperation);

            final int keywordIndex = index + effectiveOffset;
            final KeywordResultRelationship keywordResultRelationship = new KeywordResultRelationship(listingId, mKeyword, keywordIndex);
            final ContentValues keywordRelationshipContentValues = keywordResultRelationship.getContentValues();
            final ContentProviderOperation keywordRelationshipContentProviderOperation = ContentProviderOperation.newInsert(KeywordResultRelationshipTable.URI).withValues(keywordRelationshipContentValues).build();
            contentProviderOperations.add(keywordRelationshipContentProviderOperation);
        }

        final ContentResolver contentResolver = getContext().getContentResolver();
        final ContentProviderResult[] contentProviderResults = contentResolver.applyBatch(EtsyContentProvider.AUTHORITY, contentProviderOperations);
        contentResolver.notifyChange(SearchResultsView.URI, null);
    }

}


