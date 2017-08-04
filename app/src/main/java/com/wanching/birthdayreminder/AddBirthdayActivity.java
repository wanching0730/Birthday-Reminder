package com.wanching.birthdayreminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddBirthdayActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etDate;
    private EditText etTime;

    private static final int SELECT_IMAGE = 1;

    private Date newDate;

    private ImageView ivImage;

    private Bitmap bitmap;

    private boolean saved = false;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_birthday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("spSaveState", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        etName = (EditText) findViewById(R.id.add_name);
        etEmail = (EditText) findViewById(R.id.add_email);
        ivImage = (ImageView) findViewById(R.id.person_image);
        etDate = (EditText) findViewById(R.id.date_selection);
        etTime = (EditText) findViewById(R.id.time_selection);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select an Image"), SELECT_IMAGE);
            }
        });

        final FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.fab_save);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String date = etDate.getText().toString();
                    String time = etTime.getText().toString();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte [] photo = baos.toByteArray();

                    SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm", Locale.ENGLISH);

                    String combination = date + " " + time;
                    newDate = formatter.parse(combination);

                    BirthdayDbQueries dbq = new BirthdayDbQueries(new BirthdayDbHelper(getApplicationContext()));
                    Birthday birthday = new Birthday(0, name, email, photo, newDate, false);

                    if (dbq.insert(birthday) != 0) {
                        saved = true;
                        Toast.makeText(AddBirthdayActivity.this, "Birthday inserted successfully!", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                }catch (ParseException ex){
                    ex.printStackTrace();
                    Toast.makeText(AddBirthdayActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_IMAGE){
                bitmap = null;
                if(intent != null){
                    try{
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), intent.getData());
                    }catch (IOException ex){
                        Log.wtf("Ioexception", ex);
                    }
                }

                ivImage.setImageBitmap(bitmap);
            }
        }
    }

    public void SetDate(View view){
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");

    }

    public void SetTime(View view){
        DialogFragment fragment = new TImePickerFragment();
        fragment.show(getSupportFragmentManager(), "timePicker");

    }

    protected void onPause() {
        super.onPause();

        if(saved){
            editor.clear();
        }
        else{
            String title = etName.getText().toString();
            String desc = etEmail.getText().toString();
            String date = etDate.getText().toString();
            String time = etTime.getText().toString();
            Bitmap

            editor.putString("SAVE_STATE_TITLE", title);
            editor.putString("SAVE_STATE_DESC", desc);
            editor.putString("SAVE_STATE_DATE", date);
            editor.putString("SAVE_STATE_TIME", time);
        }

        editor.commit();
    }

}
