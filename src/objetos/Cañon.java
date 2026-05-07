package objetos;

import main.Juego;

public class Cañon extends ObjetosJuego {

    private int direccionY;

    public static final int ESTADO_IDLE = 0;
    public static final int ESTADO_GOLPE = 1;
    public static final int ESTADO_EXPLOSION = 2;
    public static final int ESTADO_DISPARO = 3;

    private int estado = ESTADO_IDLE;
    private int esperaTick = 0;
    private static final int ESPERA_MAX = 240;

    public Cañon(int x, int y, int tipoObjeto) {
        super(x, y, tipoObjeto);
        direccionY = y / Juego.TILES_SIZE;
        iniciarHitbox(72, 32);
        hitbox.x -= (int)(20 * Juego.ESCALA);
        hitbox.y += (int)(6 * Juego.ESCALA);
    }

    public void recibirGolpe() {
        if (estado == ESTADO_IDLE || estado == ESTADO_DISPARO) {
            estado = ESTADO_GOLPE;
            aniIndice = 0;
            aniTick = 0;
            animacion = true;
        }
    }

    public void update() {
        switch (estado) {
            case ESTADO_GOLPE:
                if (animacion) {
                    actualizarAnimacionTick();
                } else {
                    estado = ESTADO_EXPLOSION;
                    aniIndice = 0;
                    aniTick = 0;
                    animacion = true;
                }
                break;
            case ESTADO_EXPLOSION:
                if (animacion) {
                    actualizarAnimacionTick();
                } else {
                    estado = ESTADO_IDLE;
                    aniIndice = 0;
                    aniTick = 0;
                }
                break;
            case ESTADO_DISPARO:
                if (animacion) {
                    actualizarAnimacionTick();
                    if (!animacion) {          // la animación acaba de terminar
                        estado = ESTADO_IDLE;
                        aniIndice = 0;
                        aniTick = 0;
                        esperaTick = 0;        // reinicia la espera para el próximo disparo
                    }
                }
                break;
            default:
                break;
        }
    }

    public int getEstado() {
        return estado;
    }

    public void setEstadoDisparo() {
        if (estado == ESTADO_IDLE) {
            estado = ESTADO_DISPARO;
            animacion = true;
        }
    }

    public int getDireccionY() {
        return direccionY;
    }
}