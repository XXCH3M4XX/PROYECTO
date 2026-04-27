package entidades;

import main.Juego;

import java.awt.geom.Rectangle2D;

import static utils.Constantes.constantesDelEnemigo.*;
import static utils.Miscelaneos.*;
import static utils.Constantes.Direcciones.*;

//clase base de la que heredan todos los tipos de enemigo
public abstract class Enemigo extends Entidad {

    //indice del frame actual y estado de la maquina de estados del enemigo
    protected int aniIndice, estadoEnemigo, tipoEnemigo;

    //contador de ticks y velocidad de animacion en ticks por frame
    protected int aniTick, aniVel = 25;

    //flag para ejecutar logica de inicializacion solo en el primer update
    protected boolean primeraActualizacion = true;

    //fisica de caida
    protected boolean enAire = false;
    protected float velocidadCaida;
    protected float gravedad = 0.004f * Juego.ESCALA;

    //velocidad de desplazamiento horizontal y direccion actual
    protected float velocidadAndar = 0.5f * Juego.ESCALA;
    protected int direccionAndar = IZQUIERDA;
    protected float xVelocidad = 0.00f;

    //tamaño del sprite en pixeles escalados, usado para calcular offsets de dibujo
    protected static final int SPRITE_W = (int)(64 * Juego.ESCALA);
    protected static final int SPRITE_H = (int)(40 * Juego.ESCALA);

    //fila de tile en la que esta el enemigo, se usa para detectar si ve al jugador
    protected int tileY;

    //distancia maxima a la que el enemigo puede atacar
    protected float distanciAtaque = Juego.TILES_SIZE;

    //true si el enemigo mira hacia la derecha, se usa para voltear el sprite
    protected boolean mirandoDerecha = (direccionAndar == DERECHA);

    //vida maxima y actual del enemigo
    protected int vidaMax;
    protected int vidaActual;

    //false cuando el enemigo ha muerto y debe dejar de actualizarse y dibujarse
    protected boolean activo = true;

    //evita que el golpe se aplique mas de una vez por animacion de ataque
    protected boolean ataqueRealizado;

    //constructor que va usar el enemigo, extendido de la clase entidad
    public Enemigo(float x, float y, int width, int height, int tipoEnemigo) {
        super(x, y, width, height);
        this.tipoEnemigo = tipoEnemigo;
        vidaMax = getVidaMax(tipoEnemigo);
        vidaActual = vidaMax;
    }

    //comprueba en el primer frame si el enemigo esta en el aire o en el suelo
    protected void checkPrimeraActualizacion(int[][] datosNivel) {
        primeraActualizacion = false;
        if (!solido(hitbox.x, hitbox.y + 1, datosNivel)) {
            enAire = true;
        } else {
            //si ya esta en el suelo desde el inicio calculamos tileY aqui
            tileY = (int)(hitbox.y / Juego.TILES_SIZE);
        }
    }

    //aplica gravedad y detecta cuando el enemigo toca el suelo
    protected void actualizarEnAire(int[][] datosNivel) {
        if (puedeMoverse(hitbox.x, hitbox.y + velocidadCaida, hitbox.width, hitbox.height, datosNivel)) {
            hitbox.y += velocidadCaida;
            velocidadCaida += gravedad;
        } else {
            enAire = false;
            hitbox.y = GetYPosTechoOSuelo(hitbox, velocidadCaida, hitbox.y + velocidadCaida);
            tileY = (int) hitbox.y / Juego.TILES_SIZE;
        }
    }

    //mueve al enemigo en su direccion actual y cambia de direccion si choca con una pared o un borde
    protected void movimiento(int[][] datosNivel) {
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

    //cambia el estado del enemigo y reinicia los contadores de animacion
    protected void nuevoEstado(int estadoEnemigo) {
        this.estadoEnemigo = estadoEnemigo;
        aniTick = 0;
        aniIndice = 0;
    }

    //devuelve true si el enemigo tiene linea de vision al jugador y estan en la misma fila de tiles
    protected boolean verJugador(int[][] datosNivel, Jugador jugador) {
        int tileYJugador = (int)(jugador.getHitbox().y / Juego.TILES_SIZE);
        if (tileYJugador == tileY) {
            if (estaEnRango(jugador)) {
                if (vistaDespejada(datosNivel, hitbox, jugador.hitbox, tileY)) {
                    return true;
                }
            }
        }
        return false;
    }

    //orienta al enemigo hacia la posicion horizontal del jugador
    protected void dirigirseAJugador(Jugador jugador) {
        if (jugador.hitbox.x > hitbox.x) {
            direccionAndar = DERECHA;
            mirandoDerecha = true;
        } else {
            direccionAndar = IZQUIERDA;
            mirandoDerecha = false;
        }
    }

    public boolean isActivo() {
        return activo;
    }

    //devuelve true si el jugador esta dentro del rango visual del enemigo (5 veces la distancia de ataque)
    protected boolean estaEnRango(Jugador jugador) {
        int valorAbsoluto = (int) Math.abs(jugador.hitbox.x - hitbox.x);
        return valorAbsoluto <= distanciAtaque * 5;
    }

    //devuelve true si el jugador esta lo suficientemente cerca para recibir un ataque
    protected boolean cercaParaAtacar(Jugador jugador) {
        int valorAbsoluto = (int) Math.abs(jugador.hitbox.x - hitbox.x);
        return valorAbsoluto <= distanciAtaque;
    }

    //avanza el contador de animacion y gestiona las transiciones de estado al terminar cada ciclo
    protected void actualizarAnimacionTick() {
        aniTick++;
        if (aniTick >= aniVel) {
            aniTick = 0;
            aniIndice++;
            if (aniIndice >= getSpriteAmount(tipoEnemigo, estadoEnemigo)) {
                aniIndice = 0;

                //al terminar ataque o golpe vuelve a idle, al terminar muerte se desactiva
                switch (estadoEnemigo) {
                    case ATAQUE, GOLPE:
                        nuevoEstado(IDLE);
                        break;
                    case MUERTE:
                        activo = false;
                        break;
                }
            }
        }
    }

    //invierte la direccion de desplazamiento del enemigo
    protected void cambiarDireccionAndar() {
        if (direccionAndar == IZQUIERDA) {
            direccionAndar = DERECHA;
        } else {
            direccionAndar = IZQUIERDA;
        }
    }

    //devuelve el indice del frame actual de la animacion
    public int getAniIndice() {
        return aniIndice;
    }

    //devuelve el estado actual del enemigo
    public int getEstadoEnemigo() {
        return estadoEnemigo;
    }

    //cambia el estado y reinicia la animacion, alternativa a nuevoEstado con mismo comportamiento
    protected void cambiarEstado(int nuevoEstado) {
        this.estadoEnemigo = nuevoEstado;
        this.aniIndice = 0;
        this.aniTick = 0;
    }

    //aplica daño al enemigo y decide si muere o entra en estado de golpe
    public void daño(int daño) {
        vidaActual -= daño;
        if (vidaActual <= 0) {
            nuevoEstado(MUERTE);
        } else {
            if (estadoEnemigo != MUERTE) {
                nuevoEstado(GOLPE);
            }
        }
    }

    //comprueba si el boxAtaque del enemigo golpea al jugador y le aplica el daño correspondiente
    public void revisarGolpeEnemigo(Rectangle2D.Float boxAtaque, Jugador jugador) {
        if (boxAtaque.intersects(jugador.hitbox)) {
            jugador.cambiarSalud(-getDañoEnemigo(tipoEnemigo));
        }
        ataqueRealizado = true;
    }
}