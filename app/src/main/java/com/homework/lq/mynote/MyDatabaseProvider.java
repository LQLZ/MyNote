package com.homework.lq.mynote;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Switch;

import java.net.URI;

public class MyDatabaseProvider extends ContentProvider {
    public static final int NOTE_DIR = 0;
    public static final int NOTE_ITEM = 1;
    public static final String AUTHORITY = "com.example.mynote.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"note",NOTE_DIR);
        uriMatcher.addURI(AUTHORITY,"note/#",NOTE_ITEM);
    }
    @Override
    public boolean onCreate(){
        dbHelper = new MyDatabaseHelper(getContext(),"MyNote.db",null,2);
        return true;
    }
    public MyDatabaseProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri)){
            case NOTE_DIR:
                deleteRows = db.delete("note",selection,selectionArgs);
                break;
            case NOTE_ITEM:
                String noteid = uri.getPathSegments().get(1);
                deleteRows = db.delete("note","id = ?",new String[]{noteid});
                break;
            default:
                break;
        }
        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case NOTE_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.mynote.paovider.note";
            case NOTE_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.mynote.paovider.note";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case NOTE_DIR:
            case  NOTE_ITEM:
                long newnoteid = db.insert("note",null,values);
                uriReturn = Uri.parse("content://"+ AUTHORITY +"/note/"+newnoteid);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case NOTE_DIR:
                cursor = db.query("note",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case NOTE_ITEM:
                String noteid = uri.getPathSegments().get(1);
                cursor = db.query("note",projection,"id=?",new String[]{noteid},null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)){
            case NOTE_DIR:
                updateRows = db.update("note",values,selection,selectionArgs);
                break;
            case NOTE_ITEM:
                String noteid = uri.getPathSegments().get(1);
                updateRows = db.update("note",values,"id = ?",new String[]{noteid});
                break;
            default:
                break;
        }
        return updateRows;
    }
}
