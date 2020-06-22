package pucp.telecom.moviles.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pucp.telecom.moviles.lab3.Entity.Medicion;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import pucp.telecom.moviles.lab3.Fragments.DialogFragmentEjemplo;
import pucp.telecom.moviles.lab3.Fragments.DialogFragmentGuardarLocal;
import pucp.telecom.moviles.lab3.Fragments.DialogFragmentGuardarRemoto;


public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient client;
    private double currentLocationLatitud;
    private double currentLocationLongitud;
    private int tiempo;
    private double[] mediciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonGuardarLocal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Medicion medicion = new Medicion(); // todo aqui se pone la medicion
                DialogFragmentGuardarLocal dialogFragment = new DialogFragmentGuardarLocal(medicion); //todo enviar aqui la medicion
                dialogFragment.show(getSupportFragmentManager(), "guardarLocal");
            }
        });

        findViewById(R.id.buttonGuardarRemoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getGPSValues();

                tiempo = 10;
                mediciones = new double[]{60.5, 61.4, 64.3};

                DialogFragmentGuardarRemoto dialogFragment = new DialogFragmentGuardarRemoto(tiempo, mediciones, currentLocationLatitud, currentLocationLongitud);
                dialogFragment.show(getSupportFragmentManager(), "guardarRemoto");
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getCurrentLocation() {
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocationLatitud = location.getLatitude();
                    currentLocationLongitud = location.getLongitude();

                    Toast.makeText(MainActivity.this, "Ubicación actual: " +
                            currentLocationLatitud + ", " + currentLocationLongitud, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getGPSValues() {
        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else { //Request for permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

        }
    }



}