package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity implements SensorEventListener {

    private Button botonVolver;
    private Button botonBorrar;
    private TextView txtSensor;
    private TextView txtAcc;
    private SensorManager sensorManager;
    private TextView txtSensor2;
    private TextView titulo1;
    private TextView titulo2;

    boolean stage1 = false;
    boolean stage2 = false;
    long timerInicio = 0;
    private View root;

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
    }

    @Override
    protected void onStart(){
        super.onStart();
        iniSensores();
    }

    @Override
    protected void onRestart(){
        iniSensores();
        super.onRestart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        iniSensores();
    }

    @Override
    protected void onPause(){
        pararSensores();
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

    protected void iniSensores(){
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void pararSensores(){
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String txt = "";
        float xActual;
        long tiempoActual;

        synchronized (this){
            Log.d("sensor", event.sensor.getName());

            switch(event.sensor.getType()){
                case Sensor.TYPE_ACCELEROMETER:
                    txt += "X: " + event.values[0]+"\n";
                    txt += "Y: " + event.values[1]+"\n";
                    txt += "Z: " + event.values[2]+"\n";
                    txtAcc.setText(txt);

                    tiempoActual =System.currentTimeMillis();
                    xActual=event.values[0];

                    if(xActual>-1 && xActual<1 && stage2==false) {
                        stage1 = true;
                    }

                    if(xActual>-9 && xActual<-5 && stage1) {
                        stage2 = true;
                        stage1 = false;
                        timerInicio = System.currentTimeMillis();
                    }

                    if(xActual>-1 && xActual<1 && stage2 ) {
                        if( tiempoActual - 1000 < timerInicio) {
                            txtSensor.setText("movimiento detectado");
                            root.setBackgroundColor(Color.YELLOW);
                        }
                        stage2 = false;
                    }

                    break;

                default:
                    txtSensor.setText("No hay cambio en sensores");
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}