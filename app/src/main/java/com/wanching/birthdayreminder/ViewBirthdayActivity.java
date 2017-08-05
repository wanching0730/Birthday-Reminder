package com.wanching.birthdayreminder;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewBirthdayActivity extends AppCompatActivity {

    public static final String EXTRA_BIRTHDAY = "com.wanching.birthdayreminder.BIRTHDAY";
    private Person person;
    private ImageButton btnEdit;
    private ImageButton btnMessage;
    private ImageButton btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_birthday);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        long id = intent.getLongExtra(MainActivity.EXTRA_ID, 0);

        BirthdayDbQueries dbq = new BirthdayDbQueries((new BirthdayDbHelper(getApplicationContext())));

        final String[] columns = {
                BirthdayContract.BirthdayEntry._ID,
                BirthdayContract.BirthdayEntry.COLUMN_NAME_NAME,
                BirthdayContract.BirthdayEntry.COLUMN_NAME_EMAIL,
                BirthdayContract.BirthdayEntry.COLUMN_NAME_PHONE,
                BirthdayContract.BirthdayEntry.COLUMN_NAME_IMAGE,
                BirthdayContract.BirthdayEntry.COLUMN_NAME_DATE,
                BirthdayContract.BirthdayEntry.COLUMN_NAME_NOTIFY};

        String selection = BirthdayContract.BirthdayEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};

        Cursor cursor = dbq.read(columns, selection, selectionArgs, null, null, null);

        if(cursor.moveToNext()){
            person = new Person(
                    cursor.getLong(cursor.getColumnIndex(BirthdayContract.BirthdayEntry._ID)),
                    cursor.getString(cursor.getColumnIndex(BirthdayContract.BirthdayEntry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(BirthdayContract.BirthdayEntry.COLUMN_NAME_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(BirthdayContract.BirthdayEntry.COLUMN_NAME_PHONE)),
                    cursor.getBlob(cursor.getColumnIndex(BirthdayContract.BirthdayEntry.COLUMN_NAME_IMAGE)),
                    new Date(cursor.getLong(cursor.getColumnIndex(BirthdayContract.BirthdayEntry.COLUMN_NAME_DATE))),
                    changeBoolean(cursor.getInt(cursor.getColumnIndex(BirthdayContract.BirthdayEntry.COLUMN_NAME_NOTIFY)))
            );

            setTitle(person.getName());

            TextView tvName = (TextView) findViewById(R.id.show_name);
            TextView tvEmail = (TextView) findViewById(R.id.show_email);
            TextView tvPhone = (TextView) findViewById(R.id.show_phone);
            TextView tvDate = (TextView) findViewById(R.id.date);
            TextView tvDay = (TextView) findViewById(R.id.day);
            TextView tvHour = (TextView) findViewById(R.id.hour);
            TextView tvMinute = (TextView) findViewById(R.id.minute);
            TextView tvSecond = (TextView) findViewById(R.id.second);
            TextView tvLeft = (TextView) findViewById(R.id.left);

            Person.Countdown countdown = person.getCountdown();

            tvName.setText(person.getName());
            tvEmail.setText(person.getEmail());
            tvPhone.setText(person.getPhone());
            tvDate.setText(new SimpleDateFormat("EEEE, d MMMM, yyyy").format(person.getDate()));
            tvDay.setText(Long.toString(countdown.getDays()));
            tvHour.setText(Long.toString(countdown.getHours()));
            tvMinute.setText(Long.toString(countdown.getMinutes()));
            tvSecond.setText(Long.toString(countdown.getSeconds()));

            if(Calendar.getInstance().get(Calendar.MONTH) < person.getDateAsCalendar().get(Calendar.MONTH))
                tvLeft.setText("Left");
            else
                tvLeft.setText("Ago");

            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else{
            Log.e("id not found", Long.toString(cursor.getLong(cursor.getColumnIndex(BirthdayContract.BirthdayEntry._ID))));
            finish();
        }
    }

    public boolean changeBoolean(int notify){
        return notify > 0;
    }


}
