package com.example.myapplication;

import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Activity2 extends AppCompatActivity implements Activity2Interface {

    private Button botonVolver;
    private Button botonBorrar;
    private TextView txtSensor;
    private TextView txtAcc;
    private SensorManager sensorManager;
    private TextView txtSensor2;
    private TextView titulo1;
    private TextView titulo2;


    private View root;
    private SensorService sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        root = ((View) findViewById(android.R.id.content));

        botonVolver = (Button)findViewById(R.id.buttonVolver);
        botonVolver.setOnClickListener(botonVolverListener);
        botonBorrar = (Button)findViewById(R.id.buttonBorrar);
        botonBorrar.setOnClickListener(botonBorrarListener);

        txtSensor = (TextView) findViewById(R.id.textViewSensor);
        txtAcc = (TextView) findViewById(R.id.textViewAcc);
        titulo1 = (TextView) findViewById(R.id.tituloAcelerometro);
        titulo1.setEnabled(false);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = new SensorService(this, sensorManager);
    }

    @Override
    protected void onStart(){
        super.onStart();
        sensor.iniSensores();
    }

    @Override
    protected void onRestart(){
        sensor.iniSensores();
        super.onRestart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensor.iniSensores();
    }

    @Override
    protected void onPause(){
        sensor.pararSensores();
        super.onPause();
    }

    private View.OnClickListener botonVolverListener = new View.OnClickListener(){
        public void onClick(View v){
            irActivity1();
        }
    };

    private View.OnClickListener botonBorrarListener = new View.OnClickListener(){
        public void onClick(View v){
            txtSensor.setText("");
            root.setBackgroundColor(Color.WHITE);
        }
    };

    private void irActivity1(){
        finish();
    }

    @Override
    public void setTxtAcc(String text){
        this.txtAcc.setText(text);
    }
    @Override
    public void setTxtSensor(String text){
        this.txtSensor.setText(text);
    }
    @Override
    public void setBackgroundYellow(){
        root.setBackgroundColor(Color.YELLOW);
    }

}