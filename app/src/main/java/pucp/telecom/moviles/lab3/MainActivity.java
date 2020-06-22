package pucp.telecom.moviles.lab3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import pucp.telecom.moviles.lab3.Fragments.DialogFragmentEjemplo;
import pucp.telecom.moviles.lab3.Fragments.DialogFragmentGuardarLocal;
import pucp.telecom.moviles.lab3.Fragments.DialogFragmentGuardarRemoto;


public class MainActivity extends AppCompatActivity {

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
                DialogFragmentGuardarRemoto dialogFragment = new DialogFragmentGuardarRemoto();
                dialogFragment.show(getSupportFragmentManager(), "guardarRemoto");
            }
        });
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