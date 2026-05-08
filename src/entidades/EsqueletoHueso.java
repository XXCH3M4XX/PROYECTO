package entidades;

import main.Juego;
import objetos.ObjetosJuego;

import static utils.Constantes.VELOCIDAD_ANIMACION;
import static utils.Constantes.constantesObjetos.ESQUELETO;
import static utils.Constantes.constantesObjetos.getFramesEsqueleto;

//representa el enemigo esqueleto que lanza huesos, hereda de ObjetosJuego
public class EsqueletoHueso extends ObjetosJuego {

    //fila del tile en la que esta el esqueleto, usada para detectar si ve al jugador
    private int direccionY;

    //estados del esqueleto, cada uno corresponde a una fila del spritesheet
    public static final int IDLE = 0;        // sin animacion por ahora
    public static final int DESCOMPONE = 1;  // ← fila 1
    public static final int REGENERA = 2;    // ← fila 2
    public static final int DISPARO = 3;     // ← fila 3
    public static final int EN_SUELO = 4;    // ← estado de espera, sin fila propia
    public static final int GOLPE = 5;       // ← fila 5

    //true cuando el proyectil ya fue creado en este ciclo de disparo
    private boolean proyectilLanzado = false;

    //true cuando la animacion llega al ultimo frame y toca crear el proyectil
    private boolean proyectilPendiente = false;

    //estado actual y contador de espera para EN_SUELO
    private int estado = IDLE;
    private int esperaTick = 0;
    private static final int ESPERA_MAX = 400;
    private int ultimaDireccion = 1; // 1 derecha, -1 izquierda

    public int getUltimaDireccion() {
        return ultimaDireccion;
    }

    public void setUltimaDireccion(int direccion) {
        this.ultimaDireccion = direccion;
    }

    public EsqueletoHueso(int x, int y, int tipoObjeto) {
        super(x, y, tipoObjeto);
        direccionY = y / Juego.TILES_SIZE;
        iniciarHitbox(10, 20); // ← antes era 72, 32 — ajusta a tu gusto
        hitbox.x -= (int)(20 * Juego.ESCALA); // ← ajusta para centrar horizontalmente
        hitbox.y += (int)(6 * Juego.ESCALA);  // ← ajusta para centrar verticalmente
    }

    //aplica golpe solo si esta en un estado que lo permita
    public void recibirGolpe() {
        if (estado == IDLE || estado == DISPARO || estado == EN_SUELO) {
            estado = GOLPE;
            aniIndice = 0;
            aniTick = 0;
            animacion = true;
        }
    }

    //devuelve la direccion de disparo segun el tipo de esqueleto
    public int getDireccionDisparo() {
        return tipoObjeto == ESQUELETO ? -1 : 1;
    }

    //devuelve true solo cuando toca crear el proyectil y no se ha creado aun
    public boolean debeDispararProyectil() {
        return proyectilPendiente && !proyectilLanzado;
    }

    //marca que el proyectil ya fue creado y limpia el flag pendiente
    public void marcarProyectilLanzado() {
        proyectilLanzado = true;
        proyectilPendiente = false;
    }

    public void update() {
        switch (estado) {
            case GOLPE:
                //avanza la animacion de golpe y pasa a descomponer al terminar
                actualizarAnimacionTick();
                if (!animacion) {
                    estado = DESCOMPONE;
                    aniIndice = 0;
                    aniTick = 0;
                    animacion = true;
                }
                break;

            case DESCOMPONE:
                //avanza la animacion de descomposicion y pasa a espera en suelo al terminar
                actualizarAnimacionTick();
                if (!animacion) {
                    estado = EN_SUELO;
                    esperaTick = 0;
                }
                break;

            case EN_SUELO:
                //espera un numero de ticks antes de regenerarse
                esperaTick++;
                if (esperaTick >= ESPERA_MAX) {
                    estado = REGENERA;
                    aniIndice = 0;
                    aniTick = 0;
                    animacion = true;
                    esperaTick = 0;
                }
                break;

            case REGENERA:
                if (animacion) {
                    actualizarAnimacionTick();
                } else {
                    //al terminar la regeneracion vuelve a idle y permite disparar de nuevo
                    estado = IDLE;
                    aniIndice = 0;
                    aniTick = 0;
                    proyectilLanzado = false;
                }
                break;

            case DISPARO:
                if (animacion) {
                    actualizarAnimacionTick();
                    if (!animacion) {
                        //al terminar la animacion de disparo vuelve a idle y resetea los flags
                        estado = IDLE;
                        aniIndice = 0;
                        aniTick = 0;
                        esperaTick = 0;
                        proyectilPendiente = false;
                        proyectilLanzado = false;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void reset() {
        super.reset();
        estado = IDLE;
        esperaTick = 0;
        aniIndice = 0;
        aniTick = 0;
        animacion = false;
        proyectilLanzado = false;
        proyectilPendiente = false;
    }

    public int getEstado() {
        return estado;
    }

    //activa el estado de disparo solo si esta en idle
    public void setEstadoDisparo() {
        if (estado == IDLE) {
            estado = DISPARO;
            animacion = true;
        }
    }

    @Override
    protected void actualizarAnimacionTick() {
        aniTick++;
        if (aniTick >= VELOCIDAD_ANIMACION) {
            aniTick = 0;
            aniIndice++;

            //activa el flag de proyectil pendiente en el ultimo frame de la animacion de disparo
            if (estado == DISPARO && aniIndice == getFramesEsqueleto(DISPARO) - 1 && !proyectilLanzado) {
                proyectilPendiente = true;
            }

            //cuando el indice supera el maximo de frames reinicia la animacion
            if (aniIndice >= getFramesEsqueleto(estado)) {
                aniIndice = 0;
                animacion = false;
            }
        }
    }

    public int getDireccionY() {
        return direccionY;
    }
}