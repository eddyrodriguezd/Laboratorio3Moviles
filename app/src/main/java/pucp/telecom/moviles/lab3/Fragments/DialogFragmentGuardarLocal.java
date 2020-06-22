package pucp.telecom.moviles.lab3.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pucp.telecom.moviles.lab3.Entity.Medicion;

public class DialogFragmentGuardarLocal extends androidx.fragment.app.DialogFragment {

    private Medicion medicion;

    public DialogFragmentGuardarLocal(Medicion medicion) {
        this.medicion=medicion;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setMessage("¿Guardar información en el sistema de archivos local?")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }


}
