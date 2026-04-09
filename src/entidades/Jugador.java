package entidades;

import main.Juego;
import utils.Constantes;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constantes.ConstantesJugador.*;
import static utils.Miscelaneos.puedeMoverse;

//clase que controla la logica, animaciones y colisiones del personaje principal
public class Jugador extends Entidad {

    private BufferedImage[][] animaciones;
    private int tickAnim, indiceAnim, velocidadAnim = 45;

    private int accionJugador = PREDETERMINADO;

    private boolean izquierda, derecha, arriba, abajo;
    private boolean movimiento = false;

    private float velocidadJugador = 2.0f;
    private boolean ataque = false;

    private int[][] datosNivel;

    //ajustes para el tamaño de la caja de colision escalada
    public static final int HITBOX_W = (int)(19 * Juego.ESCALA);
    public static final int HITBOX_H = (int)(28 * Juego.ESCALA);

    //dimensiones originales del sprite para el renderizado
    public static final int SPRITE_W = (int)(64 * Juego.ESCALA);
    public static final int SPRITE_H = (int)(40 * Juego.ESCALA);
    private int offsetX = 15;
    private int offsetY = 55;

    //constructor que inicializa animaciones y la hitbox del jugador
    public Jugador(float x, float y, int width, int height) {
        super(x, y, width, height);
        cargarAnimaciones();

        iniciarHitbox(x, y, HITBOX_W, HITBOX_H);
    }

    //actualiza el estado completo del jugador en cada frame
    public void update(){
        actualizarPosicion();
        actualizarAnimacion();
        setAnimacion();
    }

    //recibe la matriz de colisiones del nivel actual
    public void cargarDatosNivel(int[][] datosNivel){
        this.datosNivel = datosNivel;
    }

    //dibuja el sprite del jugador aplicando los offsets respecto a la hitbox
    public void render(Graphics g){

        g.drawImage(
                animaciones[accionJugador][indiceAnim],
                (int)(hitbox.x + offsetX - width / 2),
                (int)(hitbox.y + offsetY - height),
                width,
                height,
                null
        );

        pintarHitbox(g);
    }

    //determina que animacion debe reproducirse segun el estado del jugador
    private void setAnimacion() {
        int startAni = accionJugador;

        if(movimiento) {
            accionJugador = CORRIENDO;
        } else {
            accionJugador = PREDETERMINADO;
        }

        if(ataque){
            accionJugador = PATADA;
        }

        //si cambia la accion reseteamos el contador para empezar la animacion de cero
        if(startAni != accionJugador){
            resetAniTick();
        }
    }

    //pone a cero los indices de control de animacion
    private void resetAniTick(){
        tickAnim = 0;
        indiceAnim = 0;
    }

    //calcula el desplazamiento y verifica colisiones antes de mover al jugador
    private void actualizarPosicion() {

        movimiento = false;

        if(!izquierda && !derecha && !arriba && !abajo) {
            return;
        }

        float xVelocidad = 0, yVelocidad = 0;

        //calculo de direccion en el eje x
        if(izquierda && !derecha){
            xVelocidad = -velocidadJugador;
        } else if(derecha && !izquierda){
            xVelocidad = velocidadJugador;
        }

        //calculo de direccion en el eje y
        if(arriba && !abajo){
            yVelocidad = -velocidadJugador;
        } else if(abajo && !arriba){
            yVelocidad = velocidadJugador;
        }

        //comprobacion de colision en la nueva posicion tentativa
        if(puedeMoverse(
                hitbox.x + xVelocidad,
                hitbox.y + yVelocidad,
                hitbox.width,
                hitbox.height,
                datosNivel
        )) {
            hitbox.x += xVelocidad;
            hitbox.y += yVelocidad;

            x = hitbox.x;
            y = hitbox.y;

            movimiento = true;
        }
    }

    //gestiona el tiempo entre frames de la animacion actual
    private void actualizarAnimacion() {

        tickAnim++;

        if(tickAnim >= velocidadAnim) {
            tickAnim = 0;
            indiceAnim++;

            //vuelve al inicio de la animacion si llega al final
            if(indiceAnim >= Constantes.ConstantesJugador.GetCantidadSprite(accionJugador)) {
                indiceAnim = 0;
                ataque = false;
            }
        }
    }

    //extrae los subframes del atlas segun las constantes definidas
    private void cargarAnimaciones() {

        BufferedImage imagen = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animaciones = new BufferedImage[7][4];

        for (int j = 0; j < animaciones.length; j++) {
            int cantidad = Constantes.ConstantesJugador.GetCantidadSprite(j);

            for (int i = 0; i < cantidad; i++) {
                animaciones[j][i] = imagen.getSubimage(i * 64, j * 40, 64, 40);
            }
        }

        System.out.println("Imagen cargada correctamente");
    }

    //detiene todas las direcciones de movimiento
    public void resetDirBooleans(){
        izquierda = derecha = arriba = abajo = false;
    }

    //metodos de acceso y modificacion para el estado de movimiento y ataque
    public void setAtaque(boolean ataque){
        this.ataque = ataque;
    }

    public boolean isAbajo() { return abajo; }
    public boolean isArriba() { return arriba; }
    public boolean isDerecha() { return derecha; }
    public boolean isIzquierda() { return izquierda; }

    public void setAbajo(boolean abajo) { this.abajo = abajo; }
    public void setArriba(boolean arriba) { this.arriba = arriba; }
    public void setDerecha(boolean derecha) { this.derecha = derecha; }
    public void setIzquierda(boolean izquierda) { this.izquierda = izquierda; }
}