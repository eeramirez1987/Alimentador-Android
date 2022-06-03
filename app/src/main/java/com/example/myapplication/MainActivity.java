package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

public class MainActivity extends AppCompatActivity{

    private Button button1;
    private Button button2;
    private EditText editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button)findViewById(R.id.button1);
        editText1 = (EditText)findViewById(R.id.editText1);
        button2 = (Button)findViewById(R.id.buttonActivity2);

        button1.setOnClickListener(boton1Listener);
        button2.setOnClickListener(boton2Listener);

        Log.i("Ejecuto","Ejecuto onCreate");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i("Ejecuto", "Ejecuto onStart");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i("Ejecuto", "Ejecuto onDestroy");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i("Ejecuto", "Ejecuto onResume");
    }

    private View.OnClickListener boton1Listener = new View.OnClickListener(){
        public void onClick(View v){
        }
    };

    private View.OnClickListener boton2Listener = new View.OnClickListener(){
      public void onClick(View v){
          irActivity2();
      }
    };

    private void irActivity2(){
        Intent intent;
        intent = new Intent(MainActivity.this, Activity2.class);
        startActivity(intent);
    }
}