package etsy.homework;

import android.app.Application;

import etsy.homework.Utilities.Debug;

/**
 * Created by emir on 30/03/14.
 */
public class EtsyApplication extends Application {

    @Override
    public void onCreate() {
        Debug.setIsDebug(true);
        super.onCreate();
    }
}
