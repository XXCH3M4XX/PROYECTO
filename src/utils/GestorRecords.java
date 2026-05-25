package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

//gestiona la lectura y escritura de las estadisticas de partidas en disco
public class GestorRecords {

    //ruta del archivo donde se guardan las estadisticas
    private static final String RUTA = "records.dat";

    //numero maximo de records que se guardan
    private static final int MAX_RECORDS = 3;

    //carga la lista de records desde el archivo, si no existe devuelve una lista vacia
    public static ArrayList<RegistroPartida> cargarRecords() {
        File archivo = new File(RUTA);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (ArrayList<RegistroPartida>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //guarda la lista de records en el archivo
    private static void guardarRecords(ArrayList<RegistroPartida> records) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA))) {
            oos.writeObject(records);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //borra el record en la posicion indicada y guarda la lista actualizada
    public static void borrarRecord(int indice) {
        ArrayList<RegistroPartida> records = cargarRecords();
        if (indice >= 0 && indice < records.size()) {
            records.remove(indice);
            guardarRecords(records);
        }
    }
    //borra todos los records guardados
    public static void borrarTodosLosRecords() {
        guardarRecords(new ArrayList<>());
    }

    //añade una nueva partida, ordena por tiempo y guarda solo el top 3
    public static void añadirRecord(RegistroPartida nuevo) {
        ArrayList<RegistroPartida> records = cargarRecords();
        records.add(nuevo);
        records.sort(Comparator.comparingLong(RegistroPartida::getTiempoSegundos));
        guardarRecords(records);
    }


}