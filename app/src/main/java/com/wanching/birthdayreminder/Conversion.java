package com.wanching.birthdayreminder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by WanChing on 6/8/2017.
 */

public class Conversion {

    public void Conversion(){}

    public static String encodeToBase64(Bitmap image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return  imageEncoded;
    }

    public static Bitmap decodeToBase64 (String input){
        byte[] decodeByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
    }
}
