package com.example.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class  FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_view);

        Intent i = getIntent();

        String path1 = i.getStringExtra("path");

        ImageView GalleryPreviewImg = (ImageView) findViewById(R.id.full_image_view2);
        GalleryPreviewImg.bringToFront();

        //Bitmap bitmap = BitmapFactory.decodeFile(path);

        // String a = getStringFromBitmap(bitmap);


        Glide.with(this).asBitmap().load(new File(path1)).into(GalleryPreviewImg);
//       Bitmap bitmap = ((BitmapDrawable)GalleryPreviewImg.getDrawable()).getBitmap();
//        ImageView imageView2 = findViewById(R.id.full_image_view2);
//        imageView2.setImageBitmap( bitmap);


        View.OnClickListener closebtn_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        Button closeLogin = (Button) findViewById(R.id.close_button);
        closeLogin.setOnClickListener(closebtn_listener);

//
//        // 3. 연산한 결과 값을 resultIntent 에 담아서 MainActivity 로 전달하고 현재 Activity 는 종료.
//        Intent resultIntent = new Intent();
//        resultIntent.putExtra("result","연산 결과는 "+result+" 입니다.");
//        setResult(RESULT_OK,resultIntent);
//        finish();

    }
//    private String getStringFromBitmap(Bitmap bitmapPicture) { // 이미지를 바이트 배열로 변환 후 이 데이터를 Base64 문자열로 인코딩 한 다음 JSON의 value로 전달
//        String encodedImage;
//        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
//        bitmapPicture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
//        byte[] b = byteArrayBitmapStream.toByteArray();
//        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//        return encodedImage;
//    }


}