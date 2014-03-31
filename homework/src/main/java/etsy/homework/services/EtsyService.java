package etsy.homework.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import etsy.homework.Utilities.Debug;
import etsy.homework.rest.tasks.RestTask;
import etsy.homework.tasks.SearchTask;

/**
 * Created by emir on 28/03/14.
 */
public class EtsyService extends Service {
    private static final int THREAD_POOL_SIZE = 4;
    private static final ScheduledThreadPoolExecutor mSheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);
    private final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static void startTask(final Context context, final Uri uri) {
        final String uriString = uri.toString();

        final Intent intent = new Intent(context, EtsyService.class);
        intent.putExtra(EXTRAS.URI, uriString);
        context.startService(intent);
    }


    @Override
    public void onCreate() {
        mURIMatcher.addURI(RestTask.AUTHORITY, SearchTask.URI_PATH, SearchTask.CODE);
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleOnStart(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void handleOnStart(Intent intent) {
        if (intent == null)
            return;

        final String uriString = intent.getStringExtra(EXTRAS.URI);
        final Uri uri = Uri.parse(uriString);
        final int code = mURIMatcher.match(uri);

        Debug.log("uri: " + uri);
        switch (code) {
            case SearchTask.CODE:
                Debug.log("SearchTask.CODE");
                final SearchTask searchTask = new SearchTask(getApplicationContext(), uri);
                mSheduledThreadPoolExecutor.execute(searchTask);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private static final class EXTRAS {
        public static final String URI = "uri";
    }
}
