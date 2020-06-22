package pucp.telecom.moviles.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        try (FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

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