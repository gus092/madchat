package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class GodbGallery extends AppCompatActivity {
    public static ArrayList<Bitmap> listdata = new ArrayList<Bitmap>();

    public int [] array1 = new int[500];
    public String[] arrayname;
    public int check;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //     setContentView(R.layout.full_image_view);//

        Intent i = getIntent();

        String path = i.getStringExtra("path");

        new JSONGET().execute("http://13.125.248.159:8080/api/gallerys");

        //modified
//       setContentView(R.layout.full_image_view);
//      GridView dbgallery = (GridView) findViewById(R.id.dbGridView);
////
////
////
//       dbgallery.setAdapter(new GridViewAdapter(this,listdata));
        //modified

//        GridView dbgallery = (GridView) findViewById(R.id.dbGridView);     ///
//        GridViewAdapter gridAdapter=new GridViewAdapter(this,listdata);
//        dbgallery.setAdapter(gridAdapter);


//        ImageView GalleryPreviewImg = (ImageView) findViewById(R.id.full_image_view);
//

        //닫기버튼
//        View.OnClickListener closebtn_listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        };
//        Button closeLogin = (Button) findViewById(R.id.close_button);
//        closeLogin.setOnClickListener(closebtn_listener);
        //닫기버튼

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){ // 카메라로가서 사진 찍고 DB에 저장
        super.onActivityResult(requestCode,resultCode,data);
        Bitmap dbCambitmap = (Bitmap)data.getExtras().get("data");

        Frag3.a= getStringFromBitmap(dbCambitmap);

        new Frag3.JSONPOST().execute("http://13.125.248.159:8080/api/gallerys");
        //수정

        new JSONGET().execute("http://13.125.248.159:8080/api/gallerys");

    }
    private String getStringFromBitmap(Bitmap bitmap) {
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }


    public class JSONGET extends AsyncTask<String, String, String> {


        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... urls) {
            try {
                //  JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("bitmap", "333");
                // jsonObject.accumulate("author", "ssssss");
                //jsonObject.accumulate("name", "1990-01-01.jpg");
                jsonObject.accumulate("date", "1990-01-01");

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

            listdata.clear();
            arrayname = new String[500];


//            public int [] array1 = new int[500];
//            public String [] arrayname = new String[500];

////            TextView textView1 = (TextView) findViewById(R.id.result_text) ;
////            textView1.setText(result);

            try {
                JSONArray tempar = new JSONArray(result);

                for (int i = 0; i < tempar.length(); i++) {
                    JSONObject obj = tempar.getJSONObject(i);

                    // array1[i]=i;
                    listdata.add(getBitmapFromString(obj.getString("bitmap"))); //listdata에는 bitmap들이 들어와있음

                    obj.getString("bitmap");
                    arrayname[i] = obj.getString("date");
                    obj.getString("date");
                }

                setContentView(R.layout.go_dbgallery);

                final GridView dbgallery = (GridView) findViewById(R.id.dbGridView);


                dbgallery.setAdapter(new GridViewAdapter(getBaseContext(),listdata));

                Toast toast= Toast.makeText(getApplicationContext(), "사진을 삭제하려면 DELETE 버튼을 누르세요", Toast.LENGTH_LONG); toast.show();

                /////

                Button cameraLogin2 = (Button)findViewById(R.id.dbCamera);
                cameraLogin2.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Toast.makeText(getApplicationContext(),"카메라로 이동합니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,0);
                    }
                });



//Origin
                dbgallery.setOnItemClickListener(new AdapterView.OnItemClickListener() { //갤러리 선택할때


                    @Override
                    public void onItemClick(AdapterView<?> arg0, final View arg1,
                                            final int position, long arg3) {
                        check = position;

                        View.OnClickListener delete_listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                new JSONDELETE().execute("http://13.125.248.159:8080/api/gallerys/date/"+ arrayname[check]);

                                Toast.makeText(v.getContext(),"DB갤러리에서 사진이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                                new JSONGET().execute("http://13.125.248.159:8080/api/gallerys");

                            }
                        };

                        Button deleteLogin = (Button) findViewById(R.id.db_delete);
                        deleteLogin.setOnClickListener(delete_listener);

                    }
                });

                //닫기버튼
                View.OnClickListener closebtn_listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                };
                Button closeLogin = (Button) findViewById(R.id.close_button);
                closeLogin.setOnClickListener(closebtn_listener);
                // 닫기버튼

                //삭제버튼
//        View.OnClickListener delete_listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                new JSONGET().execute("http://143.248.36.19:8080/api/gallerys");
//            }
//        };
//        Button deleteLogin = (Button) findViewById(R.id.db_delete);
//        deleteLogin.setOnClickListener(delete_listener);
                //삭제버튼




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }




        public class JSONDELETE extends AsyncTask<String, String, String>{


            @Override
            protected String doInBackground(String... urls) {
                try {

                    //modified
                    //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                    JSONObject jsonObject = new JSONObject();

                    SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
                    Date time = new Date();
                    String time1 = format1.format(time);


                    jsonObject.accumulate("bitmap","a");
                    // jsonObject.accumulate("author", "ssssss");
//                    jsonObject.accumulate("name", time1+".jpg");
                    jsonObject.accumulate("date", time1);


                    //modified
                    HttpURLConnection con = null;   //http client 객체 생성
                    BufferedReader reader = null;

                    try {
                        //URL url = new URL("http://192.168.25.16:3000/users");
                        URL url = new URL(urls[0]);
                        //URL url = new URL("http://143.248.36.19:8080/api/gallery");//url을 가져온다.
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
                        writer.write(jsonObject.toString());
                        writer.flush();
                        writer.close();//버퍼를 받아줌

                        //서버로 부터 데이터를 받음
                        InputStream stream = con.getInputStream();

                        reader = new BufferedReader(new InputStreamReader(stream));

                        StringBuffer buffer = new StringBuffer();

                        String line = "";
                        while((line = reader.readLine()) != null){
                            buffer.append(line);
                        }


                        return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                    } catch (MalformedURLException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(con != null){
                            con.disconnect();
                        }
                        try {
                            if(reader != null){
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

                arrayname = new String[500];
                final GridView dbgallery = (GridView) findViewById(R.id.dbGridView);
                listdata.remove(check);
//                    dbgallery.setAdapter(new GridViewAdapter(getBaseContext(), listdata));



            }
        }



        public Bitmap getBitmapFromString(String stringPicture) {
            byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
        }

    }



}