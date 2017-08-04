package com.wanching.birthdayreminder;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.text.format.DateFormat;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by WanChing on 4/8/2017.
 */

public class BirthdayCursorAdapter extends CursorAdapter {

    private LayoutInflater inflater;
    private Birthday birthday;

    public BirthdayCursorAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        birthday = new Birthday(
                cursor.getLong(cursor.getColumnIndex(BirthdayContract.BirthdayEntry._ID)),
                cursor.getString(cursor.getColumnIndex(BirthdayContract.BirthdayEntry.COLUMN_NAME_NAME)),
                cursor.getString(cursor.getColumnIndex(BirthdayContract.BirthdayEntry.COLUMN_NAME_EMAIL)),
                new Date(cursor.getLong(cursor.getColumnIndex(BirthdayContract.BirthdayEntry.COLUMN_NAME_DATE))),
                changeBoolean(cursor.getInt(cursor.getColumnIndex(BirthdayContract.BirthdayEntry.COLUMN_NAME_NOTIFY))));

        Birthday.Countdown countdown = birthday.getCountdown();

        TextView tvName = view.findViewById(R.id.name);
        TextView tvEmail =  view.findViewById(R.id.email);
        TextView tvMonth = view.findViewById(R.id.month);
        TextView tvDay = view.findViewById(R.id.day);
        TextView tvCountdown = view.findViewById(R.id.countdown);
        TextView tvAge = view.findViewById(R.id.new_age);

        String name = cursor.getString(cursor.getColumnIndex(BirthdayContract.BirthdayEntry.COLUMN_NAME_NAME));
        String email = cursor.getString(cursor.getColumnIndex(BirthdayContract.BirthdayEntry.COLUMN_NAME_EMAIL));
        Long date = cursor.getLong(cursor.getColumnIndex(BirthdayContract.BirthdayEntry.COLUMN_NAME_DATE));
        Date formattedDate = new Date(date);

        long duration = Calendar.getInstance().getTimeInMillis() - formattedDate.getTime();
        int newAge = Integer.parseInt(Long.toString(duration)) + 1;

        tvName.setText(name);
        tvEmail.setText(email);
        tvMonth.setText(DateFormat.format("MMM", formattedDate));
        tvDay.setText(DateFormat.format("dd", formattedDate));
        tvCountdown.setText("In " + Long.toString(countdown.getDays()) + " days");
        tvAge.setText("Turning " + newAge);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.list_item, parent, false);
    }

    public boolean changeBoolean(int notify){
        return notify > 0;
    }
}


