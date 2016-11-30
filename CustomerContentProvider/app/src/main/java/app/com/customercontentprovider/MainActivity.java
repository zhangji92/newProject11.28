package app.com.customercontentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bnt_add;
    private Button bnt_query;
    private Button bnt_queryOne;
    private Button bnt_upData;
    private Button bnt_delete;
    private ListView listView;
    private String newId;
    BaseAdapter baseAdapter;
    List<Book> bookList = new ArrayList<>();
    Book book=new Book();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //添加
        bnt_add = (Button) findViewById(R.id.add_data);
        bnt_add.setOnClickListener(this);
        //查询
        bnt_query = (Button) findViewById(R.id.query_data);
        bnt_query.setOnClickListener(this);
        //修改
        bnt_upData = (Button) findViewById(R.id.upData_data);
        bnt_upData.setOnClickListener(this);
        //删除
        bnt_delete = (Button) findViewById(R.id.detele_data);
        bnt_delete.setOnClickListener(this);
        //查询一条数据
        bnt_queryOne = (Button) findViewById(R.id.select_one_data);
        bnt_queryOne.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.list_view);

    }

    private BaseAdapter getMessage() {

        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return bookList.size() == 0 ? 0 : bookList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = new TextView(MainActivity.this);
                textView.setText(bookList.get(position).getBookName());
//                String bookename=bookList.get(position).getBookName();
//                Log.e("============","====bookename====="+bookename);
                return textView;
            }
        };

        return baseAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_data:
                //添加数据
                Uri uriAdd = Uri.parse("content://com.content.provider/book");
                ContentValues contentValues = new ContentValues();
                contentValues.put("bookName", "java基础");
                Uri newUri = getContentResolver().insert(uriAdd, contentValues);

                List<String> list = newUri.getPathSegments();
                newId = list.get(1);
                Log.e("=======", "id" + newId);
                break;
            case R.id.query_data:
                Toast.makeText(MainActivity.this, "查询", Toast.LENGTH_LONG).show();
                //查询数据
                Uri uriSelect = Uri.parse("content://com.content.provider/book");
                Cursor cursor = getContentResolver().query(uriSelect, null, null, null, null);
                if (cursor != null) {
                    bookList.clear();
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("bookName"));
                        Log.e("============","====name====="+name);
                        book.setBookName(name);
                        bookList.add(book);

                    }
                    Log.e("============","====bookList====="+bookList.get(4).getBookName());
                    cursor.close();
                }
                break;
            case R.id.upData_data:
                Toast.makeText(MainActivity.this, "更新", Toast.LENGTH_LONG).show();
                //更新数据
                Uri uriUpDate = Uri.parse("content://com.content.provider/book");
                ContentValues values = new ContentValues();
                values.put("bookName", "java疯狂讲义");


                getContentResolver().update(uriUpDate, values, "id=?", new String[]{"5"});
                break;
            case R.id.detele_data:
                Toast.makeText(MainActivity.this, "删除", Toast.LENGTH_LONG).show();
                //删除数据
                Uri uriDelete = Uri.parse("content://com.content.provider/book");
                getContentResolver().delete(uriDelete, "id=?", new String[]{"1"});
                break;
            case R.id.select_one_data:
                //查询数据
                Uri uriSelectOne = Uri.parse("content://com.content.provider/book");
                Cursor cursorOne = getContentResolver().query(uriSelectOne, null, "id=?", new String[]{"5"}, null);
                if (cursorOne != null) {
                    bookList.clear();
                    while (cursorOne.moveToNext()) {
                        String name = cursorOne.getString(cursorOne.getColumnIndex("bookName"));
                        book.setBookName(name);
                        bookList.add(book);
                    }

                    cursorOne.close();
                }
                break;
        }

        listView.setAdapter(getMessage());
        baseAdapter.notifyDataSetChanged();

    }
}
