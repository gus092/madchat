package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;



public class AddContactActivity extends AppCompatActivity {


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        setResult(1999);
        final EditText nameEdit = (EditText)findViewById(R.id.name_edit);
        final EditText numEdit = (EditText)findViewById(R.id.num_edit);

        Button addbutton = (Button)findViewById(R.id.addbutton);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nameEdit.getText().toString().length() != 0 &&
                        numEdit.getText().toString().length() != 0){

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name",nameEdit.getText().toString());
                    resultIntent.putExtra("num",numEdit.getText().toString());
                    setResult(2000,resultIntent);
                    finish();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Enter All Information",Toast.LENGTH_LONG).show();

                }

            }

        });


    }
}
