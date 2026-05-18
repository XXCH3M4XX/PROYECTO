package utils;

import java.io.Serializable;

//representa los datos de una partida completada, se serializa para guardarse en disco
public class RegistroPartida implements Serializable {

    //version de serializacion, importante para compatibilidad entre versiones
    private static final long serialVersionUID = 1L;

    //datos de la partida
    private String nombre;
    private long tiempoSegundos;
    private int muertes;
    private int dañoRecibido;
    private int porcionesRecogidas;

    public RegistroPartida(String nombre, long tiempoSegundos, int muertes, int dañoRecibido, int porcionesRecogidas) {
        this.nombre = nombre;
        this.tiempoSegundos = tiempoSegundos;
        this.muertes = muertes;
        this.dañoRecibido = dañoRecibido;
        this.porcionesRecogidas = porcionesRecogidas;
    }

    //formatea el tiempo en minutos y segundos para mostrarlo en pantalla
    public String getTiempoFormateado() {
        long minutos = tiempoSegundos / 60;
        long segundos = tiempoSegundos % 60;
        return minutos + ":" + String.format("%02d", segundos);
    }

    public String getNombre() { return nombre; }
    public long getTiempoSegundos() { return tiempoSegundos; }
    public int getMuertes() { return muertes; }
    public int getDañoRecibido() { return dañoRecibido; }
    public int getPorcionesRecogidas() { return porcionesRecogidas; }
}