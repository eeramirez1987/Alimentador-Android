package com.example.myapplication;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorService  implements SensorEventListener, SensorInterface{

    private SensorManager sensorManager;
    private Activity2Interface _view;
    boolean stage1 = false;
    boolean stage2 = false;
    long timerInicio = 0;

    public SensorService (Activity2Interface view, SensorManager manager){
        sensorManager = manager;
        _view = view;
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
                    _view.setTxtAcc(txt);

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
                            _view.setTxtSensor("movimiento detectado");
                            _view.setBackgroundYellow();
                        }
                        stage2 = false;
                    }

                    break;

                default:
                    _view.setTxtSensor("No hay cambios en sensores");
                    break;
            }
        }
    }
    @Override
    public void iniSensores(){
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void pararSensores(){
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
