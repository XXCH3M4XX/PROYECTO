package niveles;

import entidades.PersonajeEnemigo1;
import main.Juego;
import objetos.ContenedorJuego;
import objetos.Pocion;
import utils.Miscelaneos;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Miscelaneos.*;

//clase que representa los datos estructurales de un nivel individual
public class Nivel {
    private int[][] datosNivel;
    private BufferedImage imagen;
    private ArrayList<PersonajeEnemigo1> zombie;
    private ArrayList<Pocion> pocion;
    private ArrayList<ContenedorJuego> contenedor;
    private int tilesAnchoNivel;
    private int tilesMaximoOffset;
    private int tilesMaximosOffsetX;
    private Point spawnNivel;

    //constructor que asigna la matriz de indices para la construccion del mapa
    public Nivel(BufferedImage imagen) {

        this.imagen = imagen;
        cargarDatosNivel();
        cargarEnemigos();
        crearPociones();
        crearContenedores();
        calcularOffsetsNivel();
        calcularSpawnJugador();

    }

    private void crearContenedores() {
        contenedor = Miscelaneos.getContenedor(imagen);
    }

    private void crearPociones() {
        pocion = Miscelaneos.getPociones(imagen);
    }

    private void calcularSpawnJugador() {
        spawnNivel = Miscelaneos.getSpawnJugador(imagen);
    }

    private void calcularOffsetsNivel() {
        tilesAnchoNivel = imagen.getWidth();
        tilesMaximoOffset = tilesAnchoNivel - Juego.TILES_IN_WIDTH;
        tilesMaximosOffsetX = Juego.TILES_SIZE * tilesMaximoOffset;
    }

    private void cargarEnemigos() {
        zombie =  getPersonajeEnemigo1(imagen);
    }

    private void cargarDatosNivel() {
        datosNivel = conseguirDatosNivel(imagen);
    }
    public int getOffsetNivel() {
        return tilesMaximosOffsetX;
    }
    public ArrayList<PersonajeEnemigo1> getEnemigos() {
        return zombie;
    }
    public Point getSpawnJugador() {
        return spawnNivel;
    }

    //obtiene el valor del tile en una posicion especifica de la cuadricula
    public int getIndiceSprite(int x, int y) {
        return datosNivel[y][x];
    }

    //devuelve la matriz completa con todos los datos del nivel
    public int[][]  getDatosNivel() {
        return datosNivel;
    }

    public ArrayList<Pocion> getPocion() {
        ArrayList<Pocion> copia = new ArrayList<>();
        for (Pocion p : pocion) {
            copia.add(new Pocion(p.getX(), p.getY(), p.getTipoObjeto()));
        }
        return copia;
    }

    public ArrayList<ContenedorJuego> getContenedor() {
        ArrayList<ContenedorJuego> copia = new ArrayList<>();
        for (ContenedorJuego c : contenedor) {
            copia.add(new ContenedorJuego(c.getX(), c.getY(), c.getTipoObjeto()));
        }
        return copia;
    }

}