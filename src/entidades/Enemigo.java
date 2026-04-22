package entidades;
import main.Juego;
import utils.Constantes;
import utils.Miscelaneos;

import java.awt.geom.Rectangle2D;

import static utils.Constantes.constantesDelEnemigo.*;
import static utils.Miscelaneos.*;
import static utils.Constantes.Direcciones.*;

public abstract class Enemigo extends Entidad {
    //declaracion de variables
    //nos permite usarlas en otras clases que extienden de Enemigo
    protected int aniIndice, estadoEnemigo, tipoEnemigo;
    protected int aniTick, aniVel = 25;
    protected boolean primeraActualizacion = true;
    protected boolean enAire = false;
    protected float velocidadCaida;
    protected float gravedad = 0.004f * Juego.ESCALA;
    protected float velocidadAndar =  0.5f * Juego.ESCALA;
    protected int direccionAndar = IZQUIERDA;
    protected float xVelocidad = 0.00f;
    protected static final int SPRITE_W = (int)(64 * Juego.ESCALA);
    protected static final int SPRITE_H = (int)(40 * Juego.ESCALA);
    protected int tileY;
    protected float distanciAtaque = Juego.TILES_SIZE;
    //true si el enemigo mira hacia la derecha, se usa para voltear el sprite
    protected boolean mirandoDerecha = (direccionAndar == DERECHA);

    //constructor que va usar el enemigo, extrendido de la clase entidad
    public Enemigo(float x, float y, int width, int height, int tipoEnemigo) {
        super(x, y, width, height);
        this.tipoEnemigo = tipoEnemigo;
//        iniciarHitbox(x, y, (int)(20 * Juego.ESCALA), (int)(23 * Juego.ESCALA));
    }

    protected void checkPrimeraActualizacion(int [][] datosNivel) {
        primeraActualizacion = false;
        if (!solido(hitbox.x, hitbox.y + 1, datosNivel)) {
            enAire = true;
        } else {
            //si ya esta en el suelo desde el inicio calculamos tileY aqui
            tileY = (int) (hitbox.y / Juego.TILES_SIZE);
        }
    }
    //para saber cuando chocamos con el suelo
    protected void actualizarEnAire(int [][] datosNivel) {
        if (puedeMoverse(hitbox.x, hitbox.y + velocidadCaida, hitbox.width, hitbox.height, datosNivel)){
            hitbox.y += velocidadCaida;
            velocidadCaida += gravedad;
        }else{
            enAire = false;
            hitbox.y = GetYPosTechoOSuelo(hitbox, velocidadCaida,hitbox.y + velocidadCaida);
            tileY = (int) hitbox.y / Juego.TILES_SIZE;
        }
    }
    protected void movimiento(int [][] datosNivel) {

        if (direccionAndar == IZQUIERDA) {
            xVelocidad = -velocidadAndar;
            mirandoDerecha = false;
        } else {
            xVelocidad = velocidadAndar;
            mirandoDerecha = true;
        }

        if (puedeMoverse(hitbox.x + xVelocidad, hitbox.y, hitbox.width, hitbox.height, datosNivel)) {
            if (esSuelo(hitbox, xVelocidad, datosNivel)) {
                hitbox.x += xVelocidad;
                return;
            }
        }
        cambiarDireccionAndar();
    }
    protected void nuevoEstado(int estadoEnemigo) {
        this.estadoEnemigo = estadoEnemigo;
        aniTick = 0;
        aniIndice = 0;
    }
    protected boolean verJugador(int [][] datosNivel, Jugador jugador) {
        //lo primero que comprueba es si estan en la misma altura, es la comprobacion mas simple
        int tileYJugador =(int)(jugador.getHitbox().y / Juego.TILES_SIZE);
        if(tileYJugador == tileY){
            if(estaEnRango(jugador)) {
                if(vistaDespejada(datosNivel, hitbox, jugador.hitbox, tileY)) {
                    return true;
                }
            }
        }
        return false;

    }
    protected void dirigirseAJugador(Jugador jugador) {
        if (jugador.hitbox.x > hitbox.x) {
            direccionAndar = DERECHA;
            mirandoDerecha = true;
        } else {
            direccionAndar = IZQUIERDA;
            mirandoDerecha = false;
        }
    }



    protected boolean estaEnRango(Jugador jugador) {
        //tendremos un rango visual y otro para el ataque
        //el del ataque sera mas corto que el visual
        //el metodo abs calcula la distancia absoluta entre dos puntos, nunca sera negativo
        int valorAbsoluto = (int)Math.abs(jugador.hitbox.x - hitbox.x);
        return valorAbsoluto <= distanciAtaque * 5;
    }
    protected boolean cercaParaAtacar(Jugador jugador){
        int valorAbsoluto = (int)Math.abs(jugador.hitbox.x - hitbox.x);
        return valorAbsoluto <= distanciAtaque;
    }
    //metodo para poder estblecer la animacion y su velocidad
//    private void actualizarAnimacionTick(){
//        aniTick++;
//        if(aniTick >= aniVel){
//            aniTick = 0;
//            aniIndice++;
//            if (aniIndice >=getSpriteAmount(tipoEnemigo, estadoEnemigo)){
//                aniIndice = 0;
//            }
//        }
//    }

    protected void actualizarAnimacionTick(){
        aniTick++;
        if(aniTick >= aniVel){
            aniTick = 0;
            aniIndice++;
            if (aniIndice >= getSpriteAmount(tipoEnemigo, estadoEnemigo)){
                aniIndice = 0;
                if(estadoEnemigo == ATAQUE){
                    estadoEnemigo = IDLE;
                }
            }
        }

    }





    protected void cambiarDireccionAndar() {
        if (direccionAndar == IZQUIERDA){
            direccionAndar = DERECHA;
        }else {
            direccionAndar = IZQUIERDA;
        }
    }


    //metodo para recoger el indice de la animacion que va a usar el enemigo
    public int getAniIndice(){
        return aniIndice;
    }

    //metodo para coger el estado (ya sea inicial o en el que nos vayamos a entonctrar al enemigo)
    public int getEstadoEnemigo(){
        return estadoEnemigo;
    }

    protected void cambiarEstado(int nuevoEstado) {
        this.estadoEnemigo = nuevoEstado;
        this.aniIndice = 0;
        this.aniTick = 0;
    }
}
