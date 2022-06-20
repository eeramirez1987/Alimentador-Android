package com.example.myapplication;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*********************************************************************************************************
 * Activity que muestra el listado de los dispositivos bluethoot encontrados
 **********************************************************************************************************/

public class PairedActivity extends Activity {
    private ListView mListView;
    private DeviceListAdapter mAdapter;
    private ArrayList<BluetoothDevice> mDeviceList;
    private int posicionListBluethoot;
    private Button btnRefresh;
    private Button btnVolver;
    private ArrayList<BluetoothDevice> mDeviceList2 = new ArrayList<BluetoothDevice>();
    private ArrayList<BluetoothDevice> list = new ArrayList<BluetoothDevice>();

    private BluetoothAdapter mBluetoothAdapter;

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    //se crea un array de String con los permisos a solicitar en tiempo de ejecucion
    //Esto se debe realizar a partir de Android 6.0, ya que con verdiones anteriores
    //con solo solicitarlos en el Manifest es suficiente
    String[] permissions = new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired);

        //defino los componentes de layout
        mListView = (ListView) findViewById(R.id.lv_paired);

        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        btnVolver = (Button) findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(botonVolverListener);
        btnRefresh.setOnClickListener(botonRefreshListener);
        checkPermissions();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //obtengo por medio de un Bundle del intent la lista de dispositivos encontrados
        mDeviceList2 = getIntent().getExtras().getParcelableArrayList("device.list");
       // mDeviceList2 = cargarLista();

        //defino un adaptador para el ListView donde se van mostrar en la activity los dispositovs encontrados
        mAdapter = new DeviceListAdapter(this);

        //asocio el listado de los dispositovos pasado en el bundle al adaptador del Listview
        mAdapter.setData(mDeviceList2);

        //defino un listener en el boton emparejar del listview
        mAdapter.setListener(listenerBotonEmparejar);
        mListView.setAdapter(mAdapter);

        //se definen un broadcastReceiver que captura el broadcast del SO cuando captura los siguientes eventos:
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); //Cuando se empareja o desempareja el bluethoot

        //se define (registra) el handler que captura los broadcast anterirmente mencionados.
        registerReceiver(mPairReceiver, filter);

    }

    private View.OnClickListener botonRefreshListener = new View.OnClickListener() {
        public void onClick(View v) {


        }
    };
    private View.OnClickListener botonVolverListener = new View.OnClickListener() {
        public void onClick(View v) {
            unregisterReceiver(mPairReceiver);
            finish();
        }
    };


    private ArrayList<BluetoothDevice> cargarLista() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            showToast("No hay permisos BT en PairedActivity cargarLista()");
        }
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices == null || pairedDevices.size() == 0)
        {
            showToast("No se encontraron dispositivos emparejados");
            list = null;
        }
        else
        {
            showToast("Se carga la lista");
            list.addAll(pairedDevices);
        }
        return list;
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(mPairReceiver);

        super.onDestroy();
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Metodo que actua como Listener de los eventos que ocurren en los componentes graficos de la activty
    private DeviceListAdapter.OnPairButtonClickListener listenerBotonEmparejar = new DeviceListAdapter.OnPairButtonClickListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPairButtonClick(int position) {

            //Obtengo los datos del dispostivo seleccionado del listview por el usuario
            BluetoothDevice device = mDeviceList.get(position);

            //Se checkea si el sipositivo ya esta emparejado
            if (device.getBondState() == BluetoothDevice.BOND_BONDED)
            {
                //Si esta emparejado,quiere decir que se selecciono desemparjar y entonces se le desempareja
                unpairDevice(device);
            }
            else
            {

                //Si no esta emparejado,quiere decir que se selecciono emparjar y entonces se le empareja
                showToast("Emparejando");
                posicionListBluethoot = position;
                pairDevice(device);

            }
        }
    };

    //Handler que captura los brodacast que emite el SO al ocurrir los eventos del bluethoot
    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            //Atraves del Intent obtengo el evento de Bluethoot que informo el broadcast del SO
            String action = intent.getAction();

            //si el SO detecto un emparejamiento o desemparjamiento de bulethoot
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
            {
                //Obtengo los parametro, aplicando un Bundle, que me indica el estado del Bluethoot
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                //se analiza si se puedo emparejar o no
                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING)
                {
                    //Si se detecto que se puedo emparejar el bluethoot
                    showToast("Emparejado");
                    BluetoothDevice dispositivo = (BluetoothDevice) mAdapter.getItem(posicionListBluethoot);

                    //se inicia el Activity de comunicacion con el bluethoot, para transferir los datos.
                    //Para eso se le envia como parametro la direccion(MAC) del bluethoot Arduino
                    String direccionBluethoot = dispositivo.getAddress();
                    Intent i = new Intent(PairedActivity.this, activity_comunicacion.class);
                    i.putExtra("Direccion_Bluethoot", direccionBluethoot);

                    startActivity(i);

                }  //si se detrecto un desaemparejamiento
                else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                    showToast("No emparejado");
                }

                mAdapter.notifyDataSetChanged();
            }
        }
    };

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        //Se chequea si la version de Android es menor a la 6
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }


        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                   // Now you call here what ever you want :)
                } else {
                    String perStr = "";
                    for (String per : permissions) {
                        perStr += "\n" + per;
                    }
                    // permissions list of don't granted permission
                    Toast.makeText(this,"ATENCION: La aplicacion no funcionara " +
                            "correctamente debido a la falta de Permisos", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}


