package com.wanching.birthdayreminder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

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

    public long insert (Person person){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_NAME, person.getName());
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_EMAIL, person.getEmail());
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_PHONE, person.getPhone());
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_IMAGE, convertToByteArray(person));
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_DATE, person.getDateAsCalendar().getTimeInMillis());
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_NOTIFY, person.isNotify());

        long id = db.insert(BirthdayContract.BirthdayEntry.TABLE_NAME, null, values);
        person.setId(id);

        return id;
    }

    public int update(Person person){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_NAME, person.getName());
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_EMAIL, person.getEmail());
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_PHONE, person.getPhone());
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_IMAGE, convertToByteArray(person));
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_DATE, person.getDateAsCalendar().getTimeInMillis());
        values.put(BirthdayContract.BirthdayEntry.COLUMN_NAME_NOTIFY, person.isNotify());

        String selection = BirthdayContract.BirthdayEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(person.getId())};

        return db.update(
                BirthdayContract.BirthdayEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    public void delete(long id){
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = BirthdayContract.BirthdayEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};

        db.delete(BirthdayContract.BirthdayEntry.TABLE_NAME, selection, selectionArgs);
    }

    public byte[] convertToByteArray(Person person){
        Bitmap imageBitmap = person.getImage();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }

}
