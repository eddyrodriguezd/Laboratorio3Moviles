package pucp.telecom.moviles.lab3.Entity;

import java.io.Serializable;

public class Medicion implements Serializable {
    private int Tiempo;
    private double[] Mediciones;

    public int getTiempo() {
        return Tiempo;
    }

    public void setTiempo(int tiempo) {
        Tiempo = tiempo;
    }

    public double[] getMediciones() {
        return Mediciones;
    }

    public void setMediciones(double[] mediciones) {
        Mediciones = mediciones;
    }
}
