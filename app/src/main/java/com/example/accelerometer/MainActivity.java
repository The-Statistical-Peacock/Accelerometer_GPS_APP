package com.example.accelerometer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final int PERMISSIONS_FINE_LOCATION = 1;
    private SensorManager sensorManager;
    private DBHelper db;


    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallBack;


    Sensor accelerometer;

    TextView xValue, yValue, zValue;
    TextView xRot, yRot, zRot;

    TextView lat, lon;

    boolean recording = false;
    boolean recording1 = false;
    double latitude, longitude;
    float speed;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);

        xValue = findViewById(R.id.xValue);  /* Only implemented data display to make sure data was correct */
        yValue = findViewById(R.id.yValue);  /* Recording data at high rate into the DB and displaying simultaneously caused app to crash */
        zValue = findViewById(R.id.zValue);  /* Need to create Async Operation to prevent app from crashing if want data display and storage */

        xRot = findViewById(R.id.xRot);
        yRot = findViewById(R.id.yRot);
        zRot = findViewById(R.id.zRot);


        lat = findViewById(R.id.lat);
        lon = findViewById(R.id.lon);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //Can add additional sensors here ....

        /* Set GPS update intervals and Accuracy */
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1500);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallBack = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //save location
                updateTextView(locationResult.getLastLocation());
                latitude = locationResult.getLastLocation().getLatitude();
                longitude = locationResult.getLastLocation().getLongitude();
                speed = locationResult.getLastLocation().getSpeed();

            }
        };


        final Button button = (Button) findViewById(R.id.record_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recording) {
                    button.setText("Stop Recording Data");
                    Toast.makeText(getBaseContext(), "Data Recording Started", Toast.LENGTH_SHORT).show();
                    yValue.setText("Recording");

                } else {
                    Toast.makeText(getBaseContext(), "Data Recording Stopped", Toast.LENGTH_SHORT).show();
                    button.setText("Start Recording Data");
                    yValue.setText("Not Recording");

                }
                recording = !recording;
            }
        });


        final Button button2 = (Button) findViewById(R.id.record_button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recording1) {
                    button2.setText("Stop Recording Data");
                    Toast.makeText(getBaseContext(), "Data Recording Started", Toast.LENGTH_SHORT).show();
                    yRot.setText("Recording");

                } else {
                    Toast.makeText(getBaseContext(), "Data Recording Stopped", Toast.LENGTH_SHORT).show();
                    button2.setText("Start Recording Data");
                    yRot.setText("Not Recording");

                }
                recording1 = !recording1;
            }
        });

        updateGPS();
        startLocationUpdates();


    }//End onCreate method




    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER && recording) {
                db.addDataRoad1(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2], latitude, longitude, speed);

        } else if (sensor.getType() == Sensor.TYPE_ACCELEROMETER && recording1 ) {
                db.addDataRoad2(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2], latitude, longitude, speed);

        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        // Can add multiple sensors here....

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        /* Free up sensorManger on pause, to reduce phone battery usage */
    }
    

    /* Requesting permission from android to utilize GPS data. Android Likes security */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateGPS();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateGPS(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateTextView(location);

                }
            });

        } else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }

    }

    private void updateTextView(Location location) {
        //Update TextView with new location
        lat.setText(String.valueOf(location.getLatitude()));
        lon.setText(String.valueOf(location.getLongitude()));

    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();

    }



}//End of Activity