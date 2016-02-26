package com.example.intel.bluethoo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    BluetoothAdapter adaptador;
    Button encender, Visible,Buscar;
    BroadcastReceiver MyreReceiver;
    ListView lis;
    ArrayList<BluetoothDevice> lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adaptador = BluetoothAdapter.getDefaultAdapter();

        encender = (Button) findViewById(R.id.button);
        Visible = (Button) findViewById(R.id.button2);
        Buscar=(Button)findViewById(R.id.button3);
        lis=(ListView)findViewById(R.id.listView);
        registrarEventosBluetooth();

        MyreReceiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                    String Accion= intent.getAction();
                    Log.d("Broadcast","conectado ");

                    if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(Accion)){

                        Log.d("servicio","inicio busqueda");

                    }

                    if(BluetoothDevice.ACTION_FOUND.equals(Accion)){

                        if (lista==null){
                            lista=new ArrayList<BluetoothDevice>();
                        }
                        BluetoothDevice dispositivo=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        lista.add(dispositivo);
                        String nombre=dispositivo.getName()+"[ "+dispositivo.getAddress()+" ]";
                        Log.d("nuevo",nombre);

                        //

                    }

                if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(Accion))
                {
                    // Instanciamos un nuevo adapter para el ListView mediante la clase que acabamos de crear
                    ArrayAdapter arrayAdapter = new ArrayAdaptador(getBaseContext(), android.R.layout.simple_list_item_2,  lista);
                    Log.d("finalizo",Integer.toString(arrayAdapter.getCount()));
                    lis.setAdapter(arrayAdapter);
                    Toast.makeText(getBaseContext(), "Fin de la búsqueda", Toast.LENGTH_SHORT).show();
                }


            }
        };
        encender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                encender();
            }
        });

        Visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visible();
            }
        });


        Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscar();
               
            }
        });
    }


    private void registrarEventosBluetooth()
    {
        // Registramos el BroadcastReceiver que instanciamos previamente para
        // detectar las distintos acciones que queremos recibir
        IntentFilter filtro = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filtro.addAction(BluetoothDevice.ACTION_FOUND);
        filtro.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);

        this.registerReceiver(MyreReceiver, filtro);
    }

    public void buscar() {

        if (lista != null)
            lista.clear();

        // Comprobamos si existe un descubrimiento en curso. En caso afirmativo, se cancela.
         if(adaptador.startDiscovery())

            Toast.makeText(this, "Iniciando búsqueda de dispositivos bluetooth", Toast.LENGTH_SHORT).show();
        } ;




    public void visible() {

        if (adaptador.isDiscovering()) {



        } else {



            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
            startActivity(discoverableIntent);

            Visible.setText("Ocultar");
        }
    }

    public void encender() {

        if (adaptador.isEnabled()) {

            adaptador.disable();
            encender.setText("Encender Bluethoo");

        } else {

            adaptador.enable();
            encender.setText("Apagar Bluethoo");

        }


    }
}
