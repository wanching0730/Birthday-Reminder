package com.wanching.birthdayreminder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateBirthdayActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE = 1;
    private Person person;
    private Date formattedDate;
    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etDate;
    private ImageView ivImage;
    private static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_birthday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        person = (Person) intent.getSerializableExtra(ViewBirthdayActivity.EXTRA_BIRTHDAY);

        bitmap = person.getImage();

        etName = (EditText) findViewById(R.id.add_name);
        etEmail = (EditText) findViewById(R.id.add_email);
        etPhone = (EditText) findViewById(R.id.add_phone);
        etDate = (EditText) findViewById(R.id.date_selection);
        ivImage = (ImageView) findViewById(R.id.person_image);

        etName.setText(person.getName());
        etEmail.setText(person.getEmail());
        etPhone.setText(person.getPhone());
        etDate.setText(new SimpleDateFormat("EEEE, MMMM d, yyyy").format(person.getDate()));
        ivImage.setImageBitmap(bitmap);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select an Image"), SELECT_IMAGE);
            }
        });

        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.fab_save);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = etDate.getText().toString();

                try{
                    formattedDate = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH).parse(date);
                }catch (ParseException ex){
                    ex.printStackTrace();
                }

                person.setName(etName.getText().toString());
                person.setEmail(etEmail.getText().toString());
                person.setPhone(etPhone.getText().toString());
                person.setDate(formattedDate);
                person.setImage(bitmap);

                BirthdayDbQueries dbq = new BirthdayDbQueries(new BirthdayDbHelper(getApplicationContext()));
                dbq.update(person);

                finish();
            }
        });
    }

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

//    public static String encodeToBase64(Bitmap image){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] b = baos.toByteArray();
//        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
//
//        Log.d("Image Log:", imageEncoded);
//        return  imageEncoded;
//    }
//
//    public static Bitmap decodeToBase64 (String input){
//        byte[] decodeByte = Base64.decode(input, 0);
//        return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
//    }

}
