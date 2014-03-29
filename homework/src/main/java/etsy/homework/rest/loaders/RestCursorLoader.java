package etsy.homework.rest.loaders;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;

public class RestCursorLoader extends CursorLoader {

	private final ForceLoadContentObserver mForceLoadContentObserver;

	public RestCursorLoader(Context context) {
		this(context, null, null, null, null, null);
	}

	public RestCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		super(context, uri, projection, selection, selectionArgs, sortOrder);
		mForceLoadContentObserver = new ForceLoadContentObserver();
	}

	public ForceLoadContentObserver getForceLoadContentObserver() {
		return mForceLoadContentObserver;
	}

}
