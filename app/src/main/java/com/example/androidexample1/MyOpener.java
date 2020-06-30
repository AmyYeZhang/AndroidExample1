package com.example.androidexample1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "ChatMessageDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "CHATMSG";
    public final static String COL_ID = "_id";
    public final static String COL_SEND = "SEND"; //1: Send, 0: Receive
    public final static String COL_MSG = "MESSAGE";

    public MyOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_SEND + " INTEGER,"
                + COL_MSG  + " text);");  // add or remove columns

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
