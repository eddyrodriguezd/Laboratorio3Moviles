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

    private boolean isRecording= false;

    private static final int REQUEST_CODE_GPS = 44;
    private static final int REQUEST_CODE_MIC = 55;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonGuardarLocal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragmentGuardarLocal dialogFragment = new DialogFragmentGuardarLocal();
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

        findViewById(R.id.buttonIniciar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMic();
            }
        });

        findViewById(R.id.buttonDetener).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isRecording){
                    stopMic();
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GPS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }

        if (requestCode == REQUEST_CODE_MIC) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDecibels();
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
        } else { //Solicitar permiso GPS
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_GPS);
        }
    }

    private void startMic() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            getDecibels();
        } else { //Solicitar permiso micro
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_MIC);
        }
    }

    private void getDecibels() {
        Toast.makeText(this, "La medición de ruido ha iniciado", Toast.LENGTH_SHORT).show();
        isRecording = true;
    }

    private void stopMic(){
        Toast.makeText(this, "La medición de ruido ha concluido", Toast.LENGTH_SHORT).show();
        isRecording = false;
    }

    public void guardarComoTexto(Medicion medicion) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String dia = formatter.format(date).substring(0,2);
        String mes = formatter.format(date).substring(3,5);
        String anho = formatter.format(date).substring(6,10);
        String hora = formatter.format(date).substring(11,13);
        String minuto = formatter.format(date).substring(14,16);
        String fileName = "medicion_"+dia+mes+anho+"_"+hora+minuto;

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);


        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD());) {
            Gson gson = new Gson();
            String listaComoJson = gson.toJson(medicion);
            fileWriter.write(listaComoJson);
            Log.d("infoApp", "Guardado exitoso");
        } catch (IOException e) {
            Log.d("infoApp", "Error al guardar");
            e.printStackTrace();
        }
    }

}