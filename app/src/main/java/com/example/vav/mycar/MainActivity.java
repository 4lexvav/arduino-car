package com.example.vav.mycar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import android.widget.Button;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int BUTTON_STOP  = 0;
    public static final int BUTTON_LEFT  = 1;
    public static final int BUTTON_RIGHT = 2;
    public static final int BUTTON_UP    = 3;
    public static final int BUTTON_DOWN  = 4;

    // Class instances for our buttons
    Button buttonLeft;
    Button buttonRight;
    Button buttonUp;
    Button buttonDown;
    Button buttonStop;

    // Socket helps us to send data to Arduino
    BluetoothSocket clientSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Connect button's view with the implementation
        buttonLeft  = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);
        buttonUp    = (Button) findViewById(R.id.buttonUp);
        buttonDown  = (Button) findViewById(R.id.buttonDown);
        buttonStop  = (Button) findViewById(R.id.buttonStop);

        // Add listeners for buttons
        buttonLeft.setOnClickListener(this);
        buttonRight.setOnClickListener(this);
        buttonUp.setOnClickListener(this);
        buttonDown.setOnClickListener(this);
        buttonStop.setOnClickListener(this);

        // Enable bluetooth
        String enableBT = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        startActivityForResult(new Intent(enableBT), 0);

        // We want to use default bluetooth adapter
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

        try {
            // Our bluetooth
            BluetoothDevice device = bluetooth.getRemoteDevice("98:D3:31:70:42:98");

            // Initiate connection with device
            Method m = device.getClass().getMethod(
                    "createRfcommSocket", new Class[] {int.class});

            clientSocket = (BluetoothSocket) m.invoke(device, 1);
            clientSocket.connect();

        } catch (IOException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (SecurityException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalAccessException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (InvocationTargetException e) {
            Log.d("BLUETOOTH", e.getMessage());
        }

        Toast.makeText(getApplicationContext(), "CONNECTED", Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        try {
            // Get input stream for data transferring
            OutputStream outStream = clientSocket.getOutputStream();

            int value = 0;

            if (v == buttonLeft) {
                value = BUTTON_LEFT;
            } else if (v == buttonRight) {
                value = BUTTON_RIGHT;
            } else if (v == buttonUp) {
                value = BUTTON_UP;
            } else if (v == buttonDown) {
                value = BUTTON_DOWN;
            } else if (v == buttonStop) {
                value = BUTTON_STOP;
            }

            outStream.write(value);
        } catch (IOException e) {
            Log.d("BLUETOOTH", e.getMessage());
        }
    }
}
