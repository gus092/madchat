package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.constraintlayout.solver.GoalRow;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Frag3 extends Fragment {
    public int imagePosition;
    private File tempFile;
    public Bitmap bitmap;
    public byte[] byteArray;
    public static List<Integer> photoRemove = new ArrayList<Integer>();
    public static int size =0;
    Button fullbtnLogin;
    public static String a;
    public View view;
    public static GridView gallery ;
    public static Context context1;



    /** The images. */
    public static ArrayList<String> images;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.fragment_frag3,container,false);


        Button dbLogin = (Button)view.findViewById(R.id.goDB_button);
        dbLogin.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getContext(),"DB갤러리로 이동합니다.",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), GodbGallery.class);
                i.putExtra("path", images.get(imagePosition));
                startActivity(i);

            }

        });

//
//        Button cameraLogin = (Button)view.findViewById(R.id.camera);
//        cameraLogin.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Toast.makeText(getContext(),"카메라로 이동합니다.",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,0);
//            }
//        });

//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, Intent data){
//            super.onActivityResult(requestCode,resultCode,data);
//            Bitmap bitmap2 = (Bitmap)data.getExtras().get(data);
//        }


//       final GridView gallery = (GridView) view.findViewById(R.id.galleryGridView);
        gallery = (GridView) view.findViewById(R.id.galleryGridView);

        gallery.setAdapter(new ImageAdapter(requireContext()));
        context1=requireContext();

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> arg0, final View arg1,
                                    final int position, long arg3) {
//                positionGlobal = position;
                if (null != images && !images.isEmpty()) {



                    fullbtnLogin = (Button) getActivity().findViewById(R.id.full_button);
                    //fullbtnLogin.setVisibility(gallery.VISIBLE);
                    fullbtnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), FullImageActivity.class);
                            i.putExtra("path", images.get(position));
                            startActivity(i);
                        }
                    });


//                    Toast.makeText(
//                            getActivity().getApplicationContext(),
//                            "position " + position + " " + images.get(position),
//                            300).show();

                    bitmap = BitmapFactory.decodeFile(images.get(position)); //bitmap으로 변환중


                    //bitmap = Bitmap.createScaledBitmap(bitmap, 320, 240, false); // 비트맵 사이즈 줄이기

                    bitmap = Bitmap.createScaledBitmap(bitmap, 640, 480, false); // 비트맵 사이즈 줄이기

                    ImageView imageView = getActivity().findViewById(R.id.galleryImageView);



                    ByteArrayOutputStream stream = new ByteArrayOutputStream();  //bitmap크기 줄이고 bytearray로 만드는중
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();

                    bitmap = rotateBitmap(bitmap,90);
                    a= getStringFromBitmap(bitmap);



//                   Bitmap b = getBitmapFromString(a);

                    //bitmap으로 만든 사진 회전해서띄우기
//                   Matrix matrix = new Matrix();
//                    matrix.postRotate(90);
//                    Bitmap rotated = Bitmap.createBitmap(byteArrayToBitmap(byteArray), 0, 0, byteArrayToBitmap(byteArray).getWidth(),byteArrayToBitmap(byteArray).getHeight(), matrix, true);



                    imageView.setImageBitmap(bitmap);
//                    GradientDrawable drawable=
//                            (GradientDrawable) getContext().getDrawable(R.drawable.background_rounding);
//                    imageView.setBackground(drawable);
//                    imageView.setClipToOutline(true);


                    //imageView.bringToFront();// 맨앞으로

//                    Toast.makeText(requireContext(),"DB 갤러리에 저장이 완료되었습니다.",Toast.LENGTH_SHORT).show();
//
//                    new JSONPOST().execute("http://143.248.36.19:8080/api/gallerys"); //사진을 누르면 DB로 간다.?

//              //modified



                    new AlertDialog.Builder(requireContext())
                            .setTitle("해당 사진 DB 저장 ")
                            .setMessage("DB 갤러리에 저장하시겠습니까?")
                            .setIcon(android.R.drawable.ic_menu_save)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 확인시 처리 로직
                                    Toast.makeText( getActivity().getApplicationContext(), "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();

                                    new JSONPOST().execute("http://13.125.248.159:8080/api/gallerys");
                                }})

                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 취소시 처리 로직
                                    Toast.makeText( getActivity().getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                                }})
                            .show();



//저장버튼 내에 추가된 내용

//                    bitmap = BitmapFactory.decodeFile(images.get(position)); //bitmap으로 변환중
//
//                    ImageView imageView = getActivity().findViewById(R.id.galleryImageView)
//
//
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();  //bitmap크기 줄이고 bytearray로 만드는중
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byteArray = stream.toByteArray();
//
//
////                    a= getStringFromBitmap(bitmap);
////                    Bitmap b = getBitmapFromString(a);
//                    imageView.setImageBitmap(byteArrayToBitmap(byteArray)); //bitmap으로 만든 사진 띄우기
//                    imageView.bringToFront();// 맨앞으로
//                    //modified
//
//
//                    new JSONPOST().execute("http://143.248.36.19:8080/api/gallerys");


                    // new JSONGET().execute("http://143.248.36.19:8080/api/gallerys/id/5d1ec0df500fbe1b9483e083");

                    //modified

//                    Toast.makeText(
//                            getActivity().getApplicationContext(),
//                            "Choose Action",
//                            Toast.LENGTH_SHORT).show();
//저장버튼 내에 추가된 내용


//                    Button delLogin = (Button) getActivity().findViewById(R.id.delbutton);
//
//                    delLogin.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            gallery.setAdapter(new ImageAdapter(requireContext(),position));
//             }
//                    });

//                    Button dbLogin = (Button) getActivity().findViewById(R.id.goDB_button);
//                   dbLogin.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent i = new Intent(getActivity(), Godbgallery.class);
//                            i.putExtra("path", images.get(imagePosition));
//                            startActivity(i);
//                        }
//                    });


//                    Button fullbtnLogin = (Button) getActivity().findViewById(R.id.full_button);
//                    fullbtnLogin.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent i = new Intent(getActivity(), FullImageActivity.class);
//                            i.putExtra("path", images.get(imagePosition));
//                            startActivity(i);
//                        }
//                    });



//                    Button dbgalleryLogin = (Button)getActivity().findViewById(R.id.dbgallery);
//                    dbgalleryLogin.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent i = new Intent(getActivity(), DBgallery.class);
//                            i.putExtra("check", "okay");
//                            startActivity(i);
//                            Toast.makeText(v.getContext(),"DB갤러리로 이동합니다.",Toast.LENGTH_SHORT).show();
//                            // new JSONGET().execute("http://143.248.36.19:8080/api/gallerys/id/:id");
//
//                        }
//                    });
                }


            }

//            private Bitmap getBitmapFromString(String stringPicture) {
//                byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
//                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                return decodedByte;
//            }

            private String getStringFromBitmap(Bitmap bitmap) {
                String encodedImage;
                ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
                byte[] b = byteArrayBitmapStream.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                return encodedImage;
            }
        });
        return view;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);


        final GridView gallery = (GridView) view.findViewById(R.id.galleryGridView);
        gallery.setAdapter(new ImageAdapter(requireContext()));

    }




    public Bitmap rotateBitmap(Bitmap original, float degrees) {
        int width = original.getWidth();
        int height = original.getHeight();
        if(original.getWidth() > original.getHeight()){
            Matrix matrix = new Matrix();
            matrix.preRotate(degrees);

            Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);

            return rotatedBitmap;}
        return original;

    }





    //modified get action added


//    public class JSONGET extends AsyncTask<String, String, String> {
//
//        SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
//        Date time = new Date();
//        String time2 = format2.format(time);
//
//        @SuppressLint("WrongThread")
//        @Override
//        protected String doInBackground(String... urls) {
//            try {
//                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.accumulate("bitmap",byteArray);
//                // jsonObject.accumulate("author", "ssssss");
//                jsonObject.accumulate("date", time2);
//
//                HttpURLConnection con = null;
//                BufferedReader reader = null;
//
//                try {
//                    //URL url = new URL("http://192.168.25.16:3000/users");
//                    URL url = new URL(urls[0]);//url을 가져온다.
//                    con = (HttpURLConnection) url.openConnection();    //con : 연결 객체
//
//
//                    con.setRequestMethod("GET");//Get방식으로 보냄
//                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
//                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
//                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
//                    con.setDoInput(true);
//                    con.connect();//연결 수행
//
//                    //입력 스트림 생성
//                    InputStream stream = con.getInputStream();   //con이라는 연결 객체를 통해 입력 스트림 생성
//
//                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
//                    reader = new BufferedReader(new InputStreamReader(stream));
//
//                    //실제 데이터를 받는곳
//                    StringBuffer buffer = new StringBuffer();
//
//                    //line별 스트링을 받기 위한 temp 변수
//                    String line = "";
//
//                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
//                    while ((line = reader.readLine()) != null) {
//                        buffer.append(line);
//                    }
//
//                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까
//                    return buffer.toString();
//
//                    //아래는 예외처리 부분이다.
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    //종료가 되면 disconnect메소드를 호출한다.
//                    if (con != null) {
//                        con.disconnect();
//                    }
//                    try {
//                        //버퍼를 닫아준다.
//                        if (reader != null) {
//                            reader.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }//finally 부분
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            System.out.println("===================================================================================================="+result);
//            //JSONObject jsonObject = null;
//            //jsonObject = new JSONObject(result);
//
//
//
//
//            //////////////////////////////////////////////////////////////////////////////////////////////////////
//
//        }
//
//
//    }



    //modifed get action added



    //modified
    public Bitmap byteArrayToBitmap( byte[] byteArray ) {
        Bitmap bitmap = BitmapFactory.decodeByteArray( byteArray, 0, byteArray.length ) ;
        return bitmap ;
    }


    public static class JSONPOST extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {

                //modified
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();

                SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
                Date time = new Date();
                String time1 = format1.format(time);


                jsonObject.accumulate("bitmap",a);
                // jsonObject.accumulate("author", "ssssss");
                //jsonObject.accumulate("name", time1+".jpg");
                jsonObject.accumulate("date", time1);

                //modified
                HttpURLConnection con = null;   //http client 객체 생성
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
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

        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            gallery.setAdapter(new ImageAdapter(context1));

        }



    }



    //modified


    /**
     * The Class ImageAdapter.
     */
    public static class ImageAdapter extends BaseAdapter {

        /** The context. */
        public static Context context;

//       public static List photoRemove = new ArrayList();

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext
         *            the local context
         */
        public ImageAdapter(Context localContext) {
            context = localContext;
            images = getAllShownImagesPath(context);
        }

        public ImageAdapter(Context localContext,int i) {
            context = localContext;
            images = getAllShownImagesPath(context);
            images.remove(i);
            photoRemove.add(i);

        }


        public int getCount() {
            return images.size();
        }


        public String getImage (int position) {
            return images.get(position);
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(700, 700));

            } else {
                picturesView = (ImageView) convertView;
            }

            Glide.with(context).load(images.get(position))
                    //.placeholder(R.drawable.ic_launcher).centerCrop()
                    .into(picturesView);

            return picturesView;
        }

        /**
         * Getting All Images Path.
         *
         * @param activity
         *            the activity
         * @return ArrayList with images Path
         */
        private ArrayList<String> getAllShownImagesPath(Context activity) {
            Uri uri;
            Cursor cursor;
            int data,album;
            int check=0;


            int column_index_data, column_index_folder_name;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

            cursor = activity.getContentResolver().query(uri, projection, null, null, null);


            ///////<< 앨범으로 만들기
//            cursor = activity.getContentResolver().query(uri, projection, "GROUP BY (BUCKET_DISPLAY_NAME",
//                    null, null);
//            while(cursor.moveToNext()){
//
//            }
//                data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                album = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME);
//
//                viewholder{
//                    image view = set(data);
//                    image view onclik{
//
//                    }
//                    text view = set(album);
//
//                }
//
//
//
//             cursor=   activity.getContentResolver().query(uri, projection, "BUCKET_DISTPLAY_NAME ==" +"'" +album+"'" ,
//                     null, null);
            //<<---- 2번째 view
            //////<< 앨범으로 만들기


            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {

                absolutePathOfImage = cursor.getString(column_index_data);
                listOfAllImages.add(absolutePathOfImage);

            }
            return listOfAllImages;
        }
    }
}
