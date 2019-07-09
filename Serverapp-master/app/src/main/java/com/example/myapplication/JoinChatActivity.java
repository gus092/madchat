package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class JoinChatActivity extends AppCompatActivity {}

//
//    @Override
//    public void onCreate( Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.joinchat_activity);
//        final EditText nameEdit = (EditText)findViewById(R.id.name_edit2);
//
//        Button addbutton = (Button)findViewById(R.id.addbutton);
//
//        addbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(nameEdit.getText().toString() == null){
//                    Toast.makeText(getApplicationContext(),"Please Enter All Information",Toast.LENGTH_LONG);
//                }
//                else{
//
//                    Intent resultIntent = new Intent();
//                    resultIntent.putExtra("name",nameEdit.getText().toString());
//                    resultIntent.putExtra("result","Joined");
//                    setResult(2000,resultIntent);
//                    finish();
//
//                }
//
//            }
//
//        });
//
//
//    }
//}
