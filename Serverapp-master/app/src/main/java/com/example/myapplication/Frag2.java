package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import com.facebook.share.model.ShareLinkContent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Frag2 extends Fragment {

    private View v;

//    private OnFragmentInteractionListener mListener;

    public Frag2() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    public static int PICK_FROM_ALBUM = 11111;

    public static int CHAT_END = 2000;
    public static int CHAT_END2 = 2001;
    public File tempFile;
    public String stringofbitmap = null;
    public byte[] byteArray;
    public String send_name;
    public Bitmap originalBm , saveBm;
    public ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_frag2, container, false);
        Button joinbutton = v.findViewById(R.id.joinbutton);
        Button joinbutton2 = v.findViewById(R.id.joinbutton2);
        Button choosebutoon = v.findViewById(R.id.choose_gallery);
        ImageView rotatebutton = v.findViewById(R.id.rotate_button);
        final EditText editText = v.findViewById(R.id.name_edit2);
        send_name = editText.getText().toString();

        joinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {            //준범 서버
                if(originalBm == null){
                    Toast.makeText(getApplicationContext(),"Please Choose Your Profile Picture",Toast.LENGTH_LONG);
                }
                else{
                saveBm = originalBm ;
                originalBm = Bitmap.createScaledBitmap(originalBm, 40, 40, false);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();  //bitmap크기 줄이고 bytearray로 만드는중
                originalBm.compress(Bitmap.CompressFormat.PNG, 10, stream);
                byteArray = stream.toByteArray();
                stringofbitmap= getStringFromBitmap(originalBm);
                }

                if(editText.getText().toString().length() == 0 && stringofbitmap == null){
                    Toast.makeText(getApplicationContext(),"Please Enter Your Name",Toast.LENGTH_LONG);
                }
                else if(stringofbitmap == null && editText.getText().toString().length() != 0){
                    Toast.makeText(getApplicationContext(),"Please Choose Your Profile Picture",Toast.LENGTH_LONG);
                }
                else if( stringofbitmap == null && editText.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(),"Please Enter Informations",Toast.LENGTH_LONG);
                }
                else{




                    Intent intent = new Intent(getActivity(),ChatActivity.class);
                    intent.putExtra("name",editText.getText().toString());
                    intent.putExtra("stringofbitmap",stringofbitmap);
                    startActivityForResult(intent,CHAT_END);
                }
            }
        });



        joinbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {   //현지 누나 서버
                if(originalBm == null){
                    Toast.makeText(getApplicationContext(),"Please Choose Your Profile Picture",Toast.LENGTH_LONG);
                }
                else{
                    saveBm = originalBm ;
                    originalBm = Bitmap.createScaledBitmap(originalBm, 40, 40, false);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();  //bitmap크기 줄이고 bytearray로 만드는중
                    originalBm.compress(Bitmap.CompressFormat.PNG, 10, stream);
                    byteArray = stream.toByteArray();
                    stringofbitmap= getStringFromBitmap(originalBm);
                }

                if(editText.getText().toString().length() == 0 && stringofbitmap == null){
                    Toast.makeText(getApplicationContext(),"Please Enter Your Name",Toast.LENGTH_LONG);
                }
                else if(stringofbitmap == null && editText.getText().toString().length() != 0){
                    Toast.makeText(getApplicationContext(),"Please Choose Your Profile Picture",Toast.LENGTH_LONG);
                }
                else if( stringofbitmap == null && editText.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(),"Please Enter Informations",Toast.LENGTH_LONG);
                }
                else{

                    Intent intent = new Intent(getActivity(),ChatActivity2.class);
                    intent.putExtra("name",editText.getText().toString());
                    intent.putExtra("stringofbitmap",stringofbitmap);
                    startActivityForResult(intent,CHAT_END2);
                }
            }
        });



        choosebutoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_FROM_ALBUM);

            }
        });


        rotatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(originalBm != null) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    originalBm = Bitmap.createBitmap(originalBm, 0, 0, originalBm.getWidth(), originalBm.getHeight(), matrix, true);
                    imageView.setImageBitmap(originalBm);
                }
            }
        });



        return v;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_ALBUM) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                Uri photoUri = data.getData();     // get data는 intent 가 작업하고 있는 데이타를 가져옴 이를 통해 갤러리에서 선택한 이미지의 uri 를 받아옴
                Cursor cursor = null;
                try {
                    /*
                     *  Uri 스키마를
                     *  content:/// 에서 file:/// 로  변경한다.
                     */
                    String[] proj = {MediaStore.Images.Media.DATA};
                    assert photoUri != null;
                    cursor = getActivity().getContentResolver().query(photoUri, proj, null, null, null);

                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    tempFile = new File(cursor.getString(column_index));  // 이미지를 임시적으로 저장할 파일

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }


                imageView = getActivity().findViewById(R.id.chosenpicture);
                BitmapFactory.Options options = new BitmapFactory.Options();
                originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options); //tempfile을 bitmap 파일로 변경함


                imageView.setImageBitmap(originalBm);  //imageview 에 사진을 추가 현재 originalBm에 사진 정보가 있음








            }
        }
        if(requestCode == CHAT_END){
            originalBm = saveBm;
            send_name = data.getStringExtra("name") ;
            new JsonPostText().execute("http://13.125.248.159:8080/api/texts");
        }

        if(requestCode == CHAT_END2){
            originalBm = saveBm;
            send_name = data.getStringExtra("name") ;
            new JsonPostText().execute("http://54.180.100.32:8080/api/texts"); // 누나
        }
    }
    private String getStringFromBitmap(Bitmap bitmap) {
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }


    @SuppressLint("StaticFieldLeak")
    public class JsonPostText extends AsyncTask<String, String, String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("text", send_name+"님이 퇴장하셨습니다." );    // 바꿔야함.
//                    sObject.put("photoId",photo_id );
                sObject.put("name", send_name);
                sObject.put("bitmap","not_yet");

                HttpURLConnection con = null;   //http client 객체 생성
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();    //con : 연결 객체

                    // 연결 객체를 POST 방식으로 설정,
                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);
                    con.connect();//연결 수행

                    //입력 스트림 생성
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(sObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


        }


    }

}