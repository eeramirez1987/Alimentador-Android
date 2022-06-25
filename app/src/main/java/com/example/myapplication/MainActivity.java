package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{

    private Button button2;
    private Button buttonBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button2 = (Button)findViewById(R.id.buttonActivity2);
        buttonBT=(Button)findViewById(R.id.buttonBT);

        button2.setOnClickListener(boton2Listener);
        buttonBT.setOnClickListener(botonBTListener);

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

    private View.OnClickListener boton2Listener = new View.OnClickListener(){
      public void onClick(View v){
          irActivity2();
      }
    };

    private View.OnClickListener botonBTListener = new View.OnClickListener(){
        public void onClick(View v){irActivityBT();}
    };

    private void irActivityBT(){
        Intent intent;
        intent = new Intent(MainActivity.this, PairedActivity.class);
        startActivity(intent);
    }

    private void irActivity2(){
        Intent intent;
        intent = new Intent(MainActivity.this, Activity2.class);
        startActivity(intent);
    }
}