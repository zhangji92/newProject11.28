package com.yoka.mrskin.track;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

/**
 * 
 * @author qianda
 * 
 */
public class YokaTrackProvider extends ContentProvider
{
    private final static boolean isDebug = false;
    private static final UriMatcher sUriMatcher;

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.yoka.mrskin.track_debug.provider";
    public static final String DATABASE_NAME = "mrskin_track_debug.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_TRACK = "mrskin_track";
    public static final String TRACK_NOTIFICATION = "mrskin_track_notification";

    private static final String TRACK_PATH = "mrskin_track";
    private static final int TRACK = 0;
    private static final int TRACK_ID = 1;

    /**
     * track表相关属性定义
     * 
     * @author qianda
     * 
     */
    public interface TrackColumns extends BaseColumns
    {
        String SOURCE_URL = "_url";
        String SOURCE_TIME = "_time";

        /**
         * Content URI
         */
        Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + "/" + TRACK_PATH);

        /**
         * Content URI for notification
         */
        Uri NOTIFICATION_URI = Uri.parse(SCHEME + AUTHORITY + "/"
                + TRACK_NOTIFICATION);

        /**
         * MIME type of {@link #CONTENT_URI}
         */
        String CONTENT_TYPE = "vnd.android.cursor.dir/" + TRACK_PATH;

        String DEFAULT_SORT_ORDER = SOURCE_TIME + " ASC";

    }

    /**
     * db helper
     * 
     * @author qianda
     * 
     */
    public static class DatabaseHelper extends SQLiteOpenHelper
    {

        public DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            if (isDebug) {
                System.out.println("DatabaseHelper oncreate start");
            }
            try {
                db.execSQL("CREATE TABLE " + TABLE_TRACK + " ("
                        + TrackColumns._ID + " INTEGER PRIMARY KEY,"
                        + TrackColumns.SOURCE_URL + " TEXT,"
                        + TrackColumns.SOURCE_TIME + " LONG" + ");");
            } catch (Exception e) {
            }

            if (isDebug) {
                System.out.println("DatabaseHelper oncreate end");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (isDebug) {
                System.out.println("DatabaseHelper onUpgrade");
            }
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACK);
                onCreate(db);
            } catch (Exception e) {
            }
        }

    }

    public static DatabaseHelper getDataBaseHelper(Context context) {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context);
        }
        return dbHelper;
    }

    private static DatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        getDataBaseHelper(context);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
        case TRACK:
        case TRACK_ID:
            return TrackColumns.CONTENT_TYPE;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (isDebug) {
            System.out.println(YokaTrackProvider.class.getName() + "insert  "
                    + uri.toString());
        }
        if (values == null) {
            if (isDebug) {
                System.out.println(YokaTrackProvider.class.getName()
                        + "insert  values == null return");
            }
            return null;
        }

        Uri[] notificationUris = null;
        long rowId;
        Uri retUri = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
        case TRACK:
            // long time = System.currentTimeMillis();
            // values.put(TrackColumns.SOURCE_TIME, time);
            try {
                notificationUris = new Uri[] { TrackColumns.NOTIFICATION_URI };
                rowId = db.insert(TABLE_TRACK, null, values);

                retUri = ContentUris.withAppendedId(TrackColumns.CONTENT_URI,
                        rowId);
            } catch (Exception e) {
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI" + uri);
        }

        ContentResolver cr = getContext().getContentResolver();
        cr.notifyChange(uri, null);
        if (notificationUris != null) {
            for (Uri toNotify : notificationUris) {
                cr.notifyChange(toNotify, null);
            }
        }
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (isDebug) {
            System.out.println(YokaTrackProvider.class.getName() + " delete  "
                    + uri.toString());
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;
        String url;
        Uri[] notificationUris = null;
        switch (sUriMatcher.match(uri)) {
        case TRACK:
            try {
                count = db.delete(TABLE_TRACK, selection, selectionArgs);
                notificationUris = new Uri[] { TrackColumns.NOTIFICATION_URI };
            } catch (Exception e) {
            }
            break;
        case TRACK_ID:
            try {
                url = uri.getLastPathSegment();
                count = db.delete(TABLE_TRACK, TrackColumns.SOURCE_URL
                        + "="
                        + url
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                + ')' : ""), selectionArgs);
                notificationUris = new Uri[] { TrackColumns.NOTIFICATION_URI };
            } catch (Exception e) {
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentResolver cr = getContext().getContentResolver();
        cr.notifyChange(uri, null);
        if (notificationUris != null) {
            for (Uri toNotify : notificationUris) {
                cr.notifyChange(toNotify, null);
            }
        }

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        if (isDebug) {
            System.out.println(YokaTrackProvider.class.getName() + "update "
                    + uri.toString());
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;
        String url;
        Uri[] notificationUris = null;
        switch (sUriMatcher.match(uri)) {
        case TRACK:
            try {
                count = db
                        .update(TABLE_TRACK, values, selection, selectionArgs);
                notificationUris = new Uri[] { TrackColumns.NOTIFICATION_URI };
            } catch (Exception e) {
            }
            break;
        case TRACK_ID:
            try {
                url = uri.getLastPathSegment();
                count = db.update(TABLE_TRACK, values, TrackColumns.SOURCE_URL
                        + "="
                        + url
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                                + ")" : ""), selectionArgs);
                notificationUris = new Uri[] { TrackColumns.NOTIFICATION_URI };
            } catch (Exception e) {
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentResolver cr = getContext().getContentResolver();
        cr.notifyChange(uri, null);
        if (notificationUris != null) {
            for (Uri toNotify : notificationUris) {
                cr.notifyChange(toNotify, null);
            }
        }

        return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        if (isDebug) {
            System.out.println(YokaTrackProvider.class.getName() + "query  "
                    + uri.toString());
        }
        String limit = uri.getQueryParameter("limit");
        String offset = uri.getQueryParameter("offset");

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuffer rawSql = new StringBuffer();
        Cursor cur = null;
        String orderBy;
        switch (sUriMatcher.match(uri)) {
        case TRACK:
            try {
                orderBy = TextUtils.isEmpty(sortOrder) ? TrackColumns.DEFAULT_SORT_ORDER
                        : sortOrder;
                rawSql.append(SQLiteQueryBuilder.buildQueryString(false,
                        TABLE_TRACK, projection, selection, null, null,
                        orderBy, limit));
                if (offset != null) {
                    rawSql.append(" OFFSET " + offset);
                }
                cur = db.rawQuery(rawSql.toString(), selectionArgs);
            } catch (Exception e) {
            }
            break;
        case TRACK_ID:
            try {
                qb.setTables(TABLE_TRACK);
                qb.appendWhere(TrackColumns.SOURCE_URL + "="
                        + uri.getLastPathSegment());
                cur = qb.query(db, projection, selection, selectionArgs, null,
                        null, null);
            } catch (Exception e) {
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (cur != null) {
            cur.setNotificationUri(getContext().getContentResolver(), uri);
        }
        if (isDebug) {
            System.out.println(YokaTrackProvider.class.getName()
                    + "query  count" + cur.getCount());
        }
        return cur;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TRACK_PATH, TRACK);
        sUriMatcher.addURI(AUTHORITY, TRACK_PATH + "/#", TRACK_ID);
    }
}
