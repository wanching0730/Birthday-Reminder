package com.wanching.birthdayreminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WanChing on 4/8/2017.
 */

public class BirthdayDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "birthday.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BirthdayContract.BirthdayEntry.TABLE_NAME + " (" +
                    BirthdayContract.BirthdayEntry._ID + " INTEGER PRIMARY KEY," +
                    BirthdayContract.BirthdayEntry.COLUMN_NAME_NAME + " TEXT," +
                    BirthdayContract.BirthdayEntry.COLUMN_NAME_EMAIL + " TEXT," +
                    BirthdayContract.BirthdayEntry.COLUMN_NAME_DATE + " INTEGER," +
                    BirthdayContract.BirthdayEntry.COLUMN_NAME_NOTIFY + " INTEGER)";

    public BirthdayDbHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
