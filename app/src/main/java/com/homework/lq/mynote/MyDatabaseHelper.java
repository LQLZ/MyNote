package com.homework.lq.mynote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/6/6.
 */

public class MyDatabaseHelper  extends SQLiteOpenHelper {
    public static final String CREATE_TABLE = "create table note("
    +"id integer primary key autoincrement,"
    +"degree integer,"
    +"date text,"
    +"time text,"
    +"content text)";
    private Context mcontext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mcontext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE);
        Toast.makeText(mcontext,"creat succeed",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldversion,int newversion){
        db.execSQL("drop table if exists note");
        onCreate(db);
    }
}
