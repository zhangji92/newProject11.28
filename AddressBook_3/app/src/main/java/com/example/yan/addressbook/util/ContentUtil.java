package com.example.yan.addressbook.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.yan.addressbook.mode.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/16.
 */
public class ContentUtil {


    private static ContentUtil contentUtil;

    private ContentUtil(){
    }

    public static ContentUtil getContentUtil(){
        if(contentUtil==null){
            contentUtil = new ContentUtil();
        }
        return contentUtil;
    }

    /***
     * 查询所有联系人
     */
    public List<Friend> selectAllPhone(Context context){
        List<Friend> list = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();
       Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        //编号
        //int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int raw_idIndex = cursor.getColumnIndex(ContactsContract.RawContacts.Data.RAW_CONTACT_ID);
        //名字下标
        int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        int nubIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        while (cursor.moveToNext()){
           // String id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);
            String nub = cursor.getString(nubIndex);
            String raw_id = cursor.getString(raw_idIndex);
            Log.e("TAG" ,"Name is : "+name +"电话:"+nub+"  raw:"+raw_id);
            Friend friend = new Friend(raw_id,name,nub);
            list.add(friend);
        }
        return list;
    }

    /**
     * 添加联系人到本地
     * @param context
     * @param friend
     */
    public void addFriendToLocal(Context context,Friend friend){
        ContentResolver contentResolver= context.getContentResolver();
        ContentValues contentValues=new ContentValues();
        Uri rawUri= contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        long rawContactId = ContentUris.parseId(rawUri);

        //往data表入姓名数据
        contentValues.clear();
        contentValues.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
        contentValues.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, friend.getFriendName());
        context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, contentValues);


        //往data表入电话数据
        contentValues.clear();
        contentValues.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        contentValues.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, friend.getFriendNub());
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, contentValues);

        if(friend.getFriendEmail()!=null){
            //往data表入Email数据
            contentValues.clear();
            contentValues.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
            contentValues.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
            contentValues.put(ContactsContract.CommonDataKinds.Email.DATA, friend.getFriendEmail());
            contentValues.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
            context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, contentValues);
        }

    }

    /**
     * 修改联系人
     * @param context
     * @param friend
     */
    public void updateFriend(Context context,Friend friend){
        Log.e( "updateFriend","--------------"+ friend.getFriendNub() );
        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, friend.getFriendNub());
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);

        String where = ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?";
        String[] selectionArgs = new String[] { friend.getFriendId(), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE };

        int res = context.getContentResolver().update(ContactsContract.Data.CONTENT_URI, values, where, selectionArgs);

        values.clear();
        String[] selectionArgs1 = new String[]{friend.getFriendId(), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE  };
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, friend.getFriendName());
        values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        context.getContentResolver().update(ContactsContract.Data.CONTENT_URI, values, where, selectionArgs1);
        Log.e("updateFriend: ", "----------------------"+res);

    }

    /***
     * 删除联系人
     * @param context 上下文对象
     * @param friend 删除对象
     */
    public int deleteFriend(Context context,Friend friend){
        //根据姓名求id
        ContentResolver resolver = context.getContentResolver();
        //resolver.delete(Uri.parse("content://com.android.contacts/raw_contacts"), "display_name=?", new String[]{friend.getFriendName()});
        Uri  uri = Uri.parse("content://com.android.contacts/data");
        int res =  resolver.delete(uri, "raw_contact_id=?", new String[]{friend.getFriendId()});
        return res;
    }



}
