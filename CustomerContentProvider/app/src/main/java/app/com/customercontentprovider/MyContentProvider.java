package app.com.customercontentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    //查询表1的所有数据
    private static final int BOOK_DIR = 0;
    //查询表1的一条数据
    private static final int BOOK_ITEM = 1;
    //权限
    public static final String AUTHORITY = "com.content.provider";
    //数据库
    private CustomerContentSql customerContentSql;
    private static UriMatcher uriMatcher;

    static {
        //获取uriMatcher的实例，并且不匹配任何uri
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
    }

    public MyContentProvider() {
    }

    /**
     * 根据uri删除selection指定的条件所匹配的全部记录
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = customerContentSql.getWritableDatabase();
        int deletedRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deletedRows=db.delete("book",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId=uri.getPathSegments().get(1);
                deletedRows=db.delete("book","id=?",new String[]{bookId});
                break;
            default:
                break;
        }
        return deletedRows;
    }

    /**
     * 返回uri的MIME类型
     * 如果URI对应的数据类型可能包括多条记录，那么MIME类型的字符串就是vnd.android.dir/开头
     * 如果URI对应的数据只有一条记录，那么MIME类型的字符串就是vnd.android.cursor.item/开头
     *
     * @param uri
     * @return
     */
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.content.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.content.provider.book";
        }
      return null;
    }

    /**
     * 根据uri插入values对应的数据
     *
     * @param uri
     * @param values
     * @return
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase = customerContentSql.getReadableDatabase();
        Uri uriReturn = null;
        Long newBookId = sqLiteDatabase.insert("book", null, values);
        uriReturn = Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
        return uriReturn;
    }

    /**
     * 在ContentProvider创建后调用
     *
     * @return
     */
    @Override
    public boolean onCreate() {
        //创建数据库
        customerContentSql = new CustomerContentSql(getContext(), "book_store.db", null, 1);

        return customerContentSql!=null?true:false;
    }

    /**
     * 根据uri查询selection指定的条件所匹配的全部记录，并且可以指定查询哪些列以什么方式(order)排序
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //读取数据库
        SQLiteDatabase sqLiteDatabase = customerContentSql.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                //查询出来的数据放入cursor
                cursor = sqLiteDatabase.query("book", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                //获取数据库bookId=1的数据
                String bookId = uri.getPathSegments().get(1);
                cursor = sqLiteDatabase.query("book", projection, "id=?", new String[]{bookId}, null, null, sortOrder);
                break;
            default:
                break;
        }

        return cursor;
    }

    /**
     * 根据uri修改selection指定的条件所匹配的全部记录
     *
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = customerContentSql.getReadableDatabase();
        int upDate = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                upDate = db.update("book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                upDate = db.update("book", values, "id=?", new String[]{bookId});
                break;
            default:
                break;
        }
        return upDate;
    }

    /**
     * 创建数据库
     */
    class CustomerContentSql extends SQLiteOpenHelper {

        String sql = "CREATE TABLE book(id integer primary key autoincrement,bookName varchar)";

        public CustomerContentSql(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
