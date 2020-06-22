package pucp.telecom.moviles.lab3.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import static pucp.telecom.moviles.lab3.Util.isInternetAvailable;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DialogFragmentGuardarRemoto extends androidx.fragment.app.DialogFragment {


    private String apiKey = "lpc4qJWzB4WwelLmtRrfLkueV1L3wJ";

    private double latitud;
    private double longitud;
    private int tiempo;
    private double[] mediciones;


    public DialogFragmentGuardarRemoto(int tiempo, double[] mediciones, double latitud, double longitud) {
        this.tiempo=tiempo;
        this.mediciones=mediciones;
        this.latitud=latitud;
        this.longitud=longitud;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setMessage("¿Guardar información en la nube?")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (isInternetAvailable(DialogFragmentGuardarRemoto.this.getContext())) {
                            RequestQueue requestQueue = Volley.newRequestQueue(DialogFragmentGuardarRemoto.this.getContext());

                            String url = " http://ec2-34-234-229-191.compute-1.amazonaws.com:5000/saveData";
                            StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> headers = new HashMap<>();
                                    headers.put("api-key", apiKey);
                                    return headers;
                                }

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();

                                    params.put("tiempo", String.valueOf(tiempo));
                                    params.put("mediciones", Arrays.toString(mediciones));
                                    params.put("latitud", String.valueOf(latitud));
                                    params.put("longitud", String.valueOf(longitud));

                                    return params;
                                }
                            };
                            requestQueue.add(stringRequest);
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
