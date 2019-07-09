package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


public class Frag1 extends Fragment {

    private View v;
    private RecyclerView myrecyclerview;
    private ArrayList<ContactItem> lstContact;
    private Button show_button,share_button ;
    private ImageView add_button , delete_button,refresh_button;
    private TextView result_text;
    private ArrayList<ContactItem> arrayList;
    private JSONArray jsonArray;
    private String jsonstring;
    RecyclerViewAdapter mAdapter;
//    private OnFragmentInteractionListener mListener;

    public Frag1() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        arrayList = new ArrayList<ContactItem>();
        send_name = new String();
        send_num = new String();



//        new  JsonGetAllContacts().execute("http://143.248.36.6:8080/api/contacts");   // jsonstring 에 DB 정보를 가져와 넣음


    }



    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_frag1, container, false);




        myrecyclerview = (RecyclerView) v.findViewById(R.id.contact_recyclerview);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        add_button = v.findViewById(R.id.add_button);
        delete_button = v.findViewById(R.id.trash);
        refresh_button = v.findViewById(R.id.refresh);


        mAdapter =  new RecyclerViewAdapter(getActivity(), arrayList, new RecyclerViewAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(ContactItem item) {       // recycler view 개별 항목을 선택.
                Intent intent = new Intent(v.getContext(), ContactViewAdapter.class);   //contactViewAdapter을 실행하기위해 intent 정의
                intent.putExtra("name", item.getUser_Name());
                intent.putExtra("num", item.getUser_phNumber());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent,11112);
            }
        });
        // recycler view 컨텐츠를 화면에 표시
        myrecyclerview.setAdapter(mAdapter);


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),AddContactActivity.class);
                startActivityForResult(i,2000);
            }
        });

        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                new  JsonGetAllContacts().execute("http://13.125.248.159:8080/api/contacts");
            }
        });


        arrayList.clear();
        new  JsonGetAllContacts().execute("http://13.125.248.159:8080/api/contacts");

//
//
//
//            }
//        });
        return v;
    }


    public String send_name;
    public String send_num;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 2000) {
            send_name = data.getStringExtra("name");
            send_num = data.getStringExtra("num");

            new JsonPost().execute("http://13.125.248.159:8080/api/contacts");


        }

            arrayList.clear();
            new JsonGetAllContacts().execute("http://13.125.248.159:8080/api/contacts");

    }





    private void gotoContactView(ContactItem item) {          //recycle view 의 컨텐츠를 누르면 실행되는 함수.
        Intent intent = new Intent(v.getContext(), ContactViewAdapter.class);   //contactViewAdapter을 실행하기위해 intent 정의
        intent.putExtra("name", item.getUser_Name());
        intent.putExtra("num", item.getUser_phNumber());
//        intent.putExtra("photo_id",item.getPhoto_id());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent,11112);
    }

    public ArrayList<ContactItem> getContractList() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };

        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);

        LinkedHashSet<ContactItem> hashlist = new LinkedHashSet<>();

        if (cursor.moveToFirst()) {        //hashlist 작성문
            do {
                ContactItem contactItem = new ContactItem(cursor.getString(0), cursor.getString(1));
                contactItem.setUser_phNumber(cursor.getString(0));
                contactItem.setUser_Name(cursor.getString(1));
                hashlist.add(contactItem);
            } while (cursor.moveToNext());
        }

        ArrayList<ContactItem> contactItems = new ArrayList<>(hashlist);

        return contactItems;
    }     // Arraylist 만들어냄







    @SuppressLint("StaticFieldLeak")
    public class JsonGetAllContacts extends AsyncTask<String, String, String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("phNumber","imi");
//                    sObject.put("photoId",photo_id );
                jsonObject.put("name", "imi");


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

                    if(buffer.toString() != null){
                        Log.i("D","+++++"+buffer.toString()+"+++++++++++++++++++++");
                    }
                    else if(buffer.toString() == null){

                        Log.i("E","------ERROR---------");
                    }
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
            try {
                JSONArray tempar = new JSONArray(result);
                for (int i = 0; i < tempar.length(); i++) {
                    JSONObject obj = tempar.getJSONObject(i);

                    arrayList.add(new ContactItem(obj.getString("name"),obj.getString("phNumber") ));
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            myrecyclerview.setAdapter(new RecyclerViewAdapter(getActivity(), arrayList, new RecyclerViewAdapter.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override

                public void onItemClick(ContactItem item) {       // recycler view 개별 항목을 선택.
                    gotoContactView(item);
//                ContactViewAdapter contactViewAdapter = new ContactViewAdapter();
                }
            }));

        }

    }
    @SuppressLint("StaticFieldLeak")
    public class JsonPost extends AsyncTask<String, String, String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("phNumber", send_num );
//                    sObject.put("photoId",photo_id );
                sObject.put("name", send_name);


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




