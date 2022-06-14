package com.example.myapplication;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public interface SensorInterface extends SensorEventListener {

    void iniSensores();

     void pararSensores();

}
