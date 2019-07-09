package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ChatActivity2 extends AppCompatActivity {

    private RecyclerView myrecyclerview;
    private ArrayList<ContactItem> lstContact;
    private Button show_button;
    private ImageView send_button;

    private ArrayList<TextItem> arrayList;
    private JSONArray jsonArray;

    private String send_name, send_text,bitmap_string;
    private int new_len , old_len, flag ;
    private static int ON_CREATE =1111 ;
    private static int NOT_ON_CREATE = 2222;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_act1);
        send_button = findViewById(R.id.send_button3);

        final EditText chat_text = (EditText) findViewById(R.id.chating_text3);
        arrayList = new ArrayList<TextItem>();
        send_name = getIntent().getExtras().getString("name");
        flag = ON_CREATE;
        new_len = 1;
        old_len = 0;


        Intent resultIntent = new Intent();
        resultIntent.putExtra("name",send_name);
        setResult(2001,resultIntent);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        send_text = send_name + "님이 입장하셨습니다.";


        new JsonPostText().execute("http://54.180.100.32:8080/api/texts");


        bitmap_string = getIntent().getExtras().getString("stringofbitmap");

        new JsonGetAllTexts().execute("http://54.180.100.32:8080/api/texts");


        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try{
                    try{new JsonGetAllTexts().execute("http://54.180.100.32:8080/api/texts").get();
                    }catch(ExecutionException e){
                        e.printStackTrace();
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                send_button.setOnClickListener(new View.OnClickListener() {      //Send button 누를때 반응

                    @Override
                    public void onClick(View view) {
                        if( chat_text.getText().toString().length() == 0){
                            Toast.makeText(getApplicationContext(),"Please Enter Text",Toast.LENGTH_SHORT);
                        }
                        else{
                            send_text = chat_text.getText().toString();
                            new JsonPostText().execute("http://54.180.100.32:8080/api/texts");
                            chat_text.setText(null);
                        }
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 100);



    }


    @SuppressLint("StaticFieldLeak")
    public class JsonGetAllTexts extends AsyncTask<String, String, String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("text","imi");
//                    sObject.put("photoId",photo_id );
                jsonObject.put("name", "imi");
                jsonObject.put("bitmap","imi");


                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();    //con : 연결 객체


                    con.setRequestMethod("GET");//Get방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoInput(true);
                    con.connect();//연결 수행

                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();   //con이라는 연결 객체를 통해 입력 스트림 생성

                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳
                    StringBuffer buffer = new StringBuffer();

                    //line별 스트링을 받기 위한 temp 변수
                    String line = "";

                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까

                    return buffer.toString();

                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        //버퍼를 닫아준다.
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//finally 부분
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONArray tempar = new JSONArray(result);


                    if(tempar.length() > 40){
                        new JsonDeleteText().execute("http://54.180.100.32:8080/api/texts");

                    }
                    new_len = tempar.length();
                    if (new_len != old_len) {
                        // 새로 item이 들어온 경우에 만
                        arrayList = new ArrayList<TextItem>();
                        for (int i = 0; i < tempar.length(); i++) {
                            JSONObject obj = tempar.getJSONObject(i);
                            arrayList.add(new TextItem(obj.getString("name"), obj.getString("text"), obj.getString("bitmap")));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (new_len != old_len) {

                    myrecyclerview = (RecyclerView) findViewById(R.id.contact_recyclerview2);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setStackFromEnd(true);
                    myrecyclerview.setLayoutManager(linearLayoutManager);
                    myrecyclerview.setAdapter(new RecyclerViewAdapterText(getApplicationContext(), arrayList, send_name));
                    old_len = new_len;
                }
            }

        }


    }
    @SuppressLint("StaticFieldLeak")
    public class JsonPostText extends AsyncTask<String, String, String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("text", send_text );    // 바꿔야함.
//                    sObject.put("photoId",photo_id );
                sObject.put("name", send_name);  // 바꿔야함
                if(  flag == ON_CREATE ){
                    sObject.put("bitmap","not_yet");
                    flag = NOT_ON_CREATE;
                }
                else{sObject.put("bitmap",bitmap_string);}


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


    @SuppressLint("StaticFieldLeak")
    public class JsonDeleteText extends AsyncTask<String, String, String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("text", "imi" );    // 바꿔야함.
//                    sObject.put("photoId",photo_id );
                sObject.put("name", "imi");
                sObject.put("bitmap","imi");


                HttpURLConnection con = null;   //http client 객체 생성
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);//url을 가져온다.
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
