package etsy.homework.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import etsy.homework.database.tables.KeywordResultRelationshipTable;
import etsy.homework.database.tables.MainImageTable;
import etsy.homework.database.tables.ResultsTable;
import etsy.homework.database.tables.TasksTable;
import etsy.homework.database.views.SearchResultsView;

/**
 * Created by emir on 28/03/14.
 */
public class EtsyDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EtsyDatabase";
    private static final int DATABASE_VERSION = 4;
    private static SQLiteDatabase sDatabase;

    public EtsyDatabase(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);
    }

    public static synchronized SQLiteDatabase getDatabase(final Context context) {
        if (sDatabase == null) {
            final EtsyDatabase database = new EtsyDatabase(context, DATABASE_NAME);
            sDatabase = database.getWritableDatabase();
        }
        return sDatabase;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(TasksTable.DROP);
        db.execSQL(TasksTable.CREATE);

        db.execSQL(ResultsTable.DROP);
        db.execSQL(ResultsTable.CREATE);

        db.execSQL(MainImageTable.DROP);
        db.execSQL(MainImageTable.CREATE);

        db.execSQL(KeywordResultRelationshipTable.DROP);
        db.execSQL(KeywordResultRelationshipTable.CREATE);

        db.execSQL(SearchResultsView.DROP);
        db.execSQL(SearchResultsView.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion)
            onCreate(db);
    }

}
