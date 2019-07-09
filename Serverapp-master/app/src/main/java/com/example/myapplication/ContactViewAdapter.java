package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ContactViewAdapter extends AppCompatActivity {


    public ContactViewAdapter() {

    }

    private Toolbar toolbar1;
    private ImageView imageView4, trashpress;
    private TextView textView2, textView3, editText, editText3;

    private String num;
    private String name;
    private String photo_id;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail);
        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        num = intent.getExtras().getString("num");
//        photo_id = intent.getExtras().getString("photo");
        initContactViews();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    public void initContactViews() {
        toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        editText = (TextView) findViewById(R.id.editText);
        editText3 = (TextView) findViewById(R.id.editText3);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        trashpress = (ImageView) findViewById(R.id.trash);

        ImageView phone = (ImageView) findViewById(R.id.phone2);

        editText.setText(name);
        editText3.setText(num);
        imageView4.setImageResource(R.drawable.user3);
        GradientDrawable drawable= (GradientDrawable) getApplicationContext().getDrawable(R.drawable.background_rounding);
        imageView4.setBackground(drawable);
        imageView4.setClipToOutline(true);


        toolbar1.setTitle("Contacts");
        toolbar1.setTitleTextColor(getResources().getColor(android.R.color.black));
        //setHasOptionsMenu(true);
        toolbar1.inflateMenu(R.menu.contact_detail_menu);
        toolbar1.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar1);

        /** enabling back button ***/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar1.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {     // toolbar를 눌렀을 때

                if (item.getItemId() == R.id.item_list) {
                    // do something
                    gotoContactListActivity();
                } else {
                    // do something
                }

                return false;
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {  // 휴대폰 버튼 을 눌렀을 때
                try {
                    String mNum = num;
                    String tel = "tel:" + num;
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(tel));
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);}
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


        trashpress.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override

            public void onClick(View view) {
                new JsonDelete().execute("http://13.125.248.159:8080/api/contacts");
                finish();
            }
        });


    }


    @SuppressLint("StaticFieldLeak")
    public class JsonDelete extends AsyncTask<String, String, String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
//                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
//                sObject.put("phNumber", "imi" );
//                    sObject.put("photoId",photo_id );
//                sObject.put("name","imi");


                HttpURLConnection con = null;   //http client 객체 생성
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]+"/name/"+name);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();    //con : 연결 객체

                    // 연결 객체를 POST 방식으로 설정,
                    con.setRequestMethod("DELETE");//POST방식으로 보냄
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);
                    con.connect();//연결 수행

                    //입력 스트림 생성
//                    OutputStream outStream = con.getOutputStream();
//                    //버퍼를 생성하고 넣음
//                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
//                    writer.write(sObject.toString());
//                    writer.flush();
//                    writer.close();//버퍼를 받아줌

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

    private void gotoContactListActivity() {
        Intent intent = new Intent(this, Frag1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

}


