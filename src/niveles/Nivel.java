package niveles;

import entidades.PersonajeEnemigo1;
import entidades.PersonajeDio;
import main.Juego;
import entidades.EsqueletoHueso;
import objetos.ContenedorJuego;
import objetos.Pinchos;
import objetos.Pocion;
import utils.Miscelaneos;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Miscelaneos.*;

//clase que representa los datos estructurales de un nivel individual
public class Nivel {
    //matriz de indices que define que tile se coloca en cada posicion del mapa
    private int[][] datosNivel;

    //imagen del nivel de la que se extraen todos los datos mediante colores de pixel
    private BufferedImage imagen;

    //listas de entidades y objetos presentes en el nivel
    private ArrayList<PersonajeEnemigo1> zombie;
    private ArrayList<PersonajeDio> dio;
    private ArrayList<Pocion> pocion;
    private ArrayList<Pinchos> pinchos;
    private ArrayList<ContenedorJuego> contenedor;
    private ArrayList<EsqueletoHueso> esqueletoHueso;

    //ancho del nivel en tiles y offsets para limitar el desplazamiento de la camara
    private int tilesAnchoNivel;
    private int tilesMaximoOffset;
    private int tilesMaximosOffsetX;

    //posicion de aparicion del jugador en este nivel
    private Point spawnNivel;

    //constructor que carga todos los datos del nivel a partir de la imagen
    public Nivel(BufferedImage imagen) {
        this.imagen = imagen;
        cargarDatosNivel();
        cargarEnemigos();
        cargarDio();
        crearPociones();
        crearContenedores();
        crearPinchos();
        crearCañones();
        calcularOffsetsNivel();
        calcularSpawnJugador();
    }

    //extrae los esqueletos lanzadores de huesos de la imagen del nivel
    private void crearCañones() {
        esqueletoHueso = Miscelaneos.getEsqueletoHueso(imagen);
    }

    //extrae los pinchos de la imagen del nivel
    private void crearPinchos() {
        pinchos = Miscelaneos.GetPinchos(imagen);
    }

    //extrae los contenedores de la imagen del nivel
    private void crearContenedores() {
        contenedor = Miscelaneos.getContenedor(imagen);
    }

    //extrae las pociones de la imagen del nivel
    private void crearPociones() {
        pocion = Miscelaneos.getPociones(imagen);
    }

    //extrae el punto de spawn del jugador de la imagen del nivel
    private void calcularSpawnJugador() {
        spawnNivel = Miscelaneos.getSpawnJugador(imagen);
    }

    //calcula el offset maximo que puede alcanzar la camara antes de salir del nivel
    private void calcularOffsetsNivel() {
        tilesAnchoNivel = imagen.getWidth();
        tilesMaximoOffset = tilesAnchoNivel - Juego.TILES_IN_WIDTH;
        tilesMaximosOffsetX = Juego.TILES_SIZE * tilesMaximoOffset;
    }

    //extrae la lista de enemigos zombie de la imagen del nivel
    private void cargarEnemigos() {
        zombie = getPersonajeEnemigo1(imagen);
    }

    //extrae la lista de enemigos Dio de la imagen del nivel
    private void cargarDio() {
        dio = Miscelaneos.getPersonajeDio(imagen);
    }

    //extrae la matriz de indices de tiles de la imagen del nivel
    private void cargarDatosNivel() {
        datosNivel = conseguirDatosNivel(imagen);
    }

    //devuelve el offset maximo en pixeles que puede alcanzar la camara horizontalmente
    public int getOffsetNivel() {
        return tilesMaximosOffsetX;
    }

    //devuelve la lista de enemigos zombie del nivel
    public ArrayList<PersonajeEnemigo1> getEnemigos() {
        return zombie;
    }

    //devuelve la lista de enemigos Dio del nivel
    public ArrayList<PersonajeDio> getDio() {
        return dio;
    }

    //devuelve el punto de spawn del jugador en este nivel
    public Point getSpawnJugador() {
        return spawnNivel;
    }

    //obtiene el valor del tile en una posicion especifica de la cuadricula
    public int getIndiceSprite(int x, int y) {
        return datosNivel[y][x];
    }

    //devuelve la matriz completa con todos los datos del nivel
    public int[][] getDatosNivel() {
        return datosNivel;
    }

    //devuelve una copia de la lista de pociones para evitar modificar la original
    public ArrayList<Pocion> getPocion() {
        ArrayList<Pocion> copia = new ArrayList<>();
        for (Pocion p : pocion) {
            copia.add(new Pocion(p.getX(), p.getY(), p.getTipoObjeto()));
        }
        return copia;
    }

    //devuelve una copia de la lista de contenedores para evitar modificar la original
    public ArrayList<ContenedorJuego> getContenedor() {
        ArrayList<ContenedorJuego> copia = new ArrayList<>();
        for (ContenedorJuego c : contenedor) {
            copia.add(new ContenedorJuego(c.getX(), c.getY(), c.getTipoObjeto()));
        }
        return copia;
    }

    //devuelve la lista de pinchos del nivel
    public ArrayList<Pinchos> getPinchos() {
        return pinchos;
    }

    //devuelve la lista de esqueletos lanzadores de huesos del nivel
    public ArrayList<EsqueletoHueso> getCañon() {
        return esqueletoHueso;
    }
}