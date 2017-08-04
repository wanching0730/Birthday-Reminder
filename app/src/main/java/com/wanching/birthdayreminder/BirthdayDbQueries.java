package com.wanching.birthdayreminder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by WanChing on 4/8/2017.
 */

public class BirthdayDbQueries {

    private BirthdayDbHelper helper;

    public BirthdayDbQueries (BirthdayDbHelper helper){
        this.helper = helper;
    }

    public Cursor read (String[] columns, String selection, String[] selectionArgs, String groupby, String having, String orderBy){
        SQLiteDatabase db = helper.getReadableDatabase();

        return db.query(
                BirthdayContract.BirthdayEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupby,
                having,
                orderBy
                );
    }

    public long insert (Birthday birthday){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_NAME, birthday.getName());
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_EMAIL, birthday.getEmail());
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_IMAGE, birthday.getImage());
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_DATE, birthday.getDateAsCalendar().getTimeInMillis());
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_NOTIFY, birthday.isNotify());

        long id = db.insert(BirthdayContract.BirthdayEntry.TABLE_NAME, null, values);
        birthday.setId(id);

        return id;
    }

}
