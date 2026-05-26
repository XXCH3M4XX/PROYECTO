package entidades;
import main.Juego;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import static utils.Constantes.Direcciones.IZQUIERDA;
import static utils.Constantes.constantesDelEnemigo.*;
import static utils.Miscelaneos.*;

//enemigo zombie que patrulla, persigue y ataca al jugador cuerpo a cuerpo
public class PersonajeEnemigo1 extends Enemigo {

    //hitbox del ataque y su desplazamiento horizontal respecto a la hitbox del enemigo
    private Rectangle2D.Float boxAtaque;
    private int boxAtaqueOffsetX;

    //contador de ticks para interrumpir la patrulla con una animacion idle periodica
    private int ticksIdle = 0;
    private int maxTicksIdle = 0;

    //true mientras el enemigo esta en la pausa de patrulla reproduciendo la animacion idle
    private boolean enPausaPatrulla = false;

    //true cuando la animacion idle llega al ultimo frame del ciclo actual
    private boolean animacionIdleCompletada = false;

    //numero de ciclos idle completados, al llegar al maximo vuelve a correr
    private int ciclosIdleCompletados = 0;
    private static final int MAX_CICLOS_IDLE = 4;

    //coloca al enemigo en su posicion inicial y genera un tiempo de patrulla aleatorio
    public PersonajeEnemigo1(float x, float y) {
        super(x, y, ENEMIGO1_WIDTH, ENEMIGO_HEIGHT, ENEMIGO1);
        iniciarHitbox(x, y, (int)(17 * Juego.ESCALA), (int)(29 * Juego.ESCALA));
        iniciarHitboxAtaque();
        maxTicksIdle = 800 + new Random().nextInt(600);
    }

    //crea la hitbox de ataque con un ancho mayor que la hitbox del cuerpo para cubrir el golpe
    private void iniciarHitboxAtaque() {
        boxAtaque = new Rectangle2D.Float(x, y, (int)(82 * Juego.ESCALA), (int)(29 * Juego.ESCALA));
        boxAtaqueOffsetX = (int)(Juego.ESCALA * 30);
    }

    //actualiza movimiento, animacion y posicion de la hitbox de ataque cada frame
    public void update(int[][] datosNivel, Jugador jugador, ArrayList<Enemigo> enemigos) {
        actualizarMovimiento(datosNivel, jugador, enemigos);
        actualizarAnimacionTick();
        actualizarBoxAtaque();
    }

    //sincroniza la hitbox de ataque con la posicion actual de la hitbox del cuerpo
    private void actualizarBoxAtaque() {
        boxAtaque.x = hitbox.x - boxAtaqueOffsetX;
        boxAtaque.y = hitbox.y;
    }

    //maquina de estados que controla el comportamiento del enemigo segun su estado actual
    private void actualizarMovimiento(int[][] datosNivel, Jugador jugador, ArrayList<Enemigo> enemigos) {
        if (primeraActualizacion) {
            checkPrimeraActualizacion(datosNivel);
        }
        if (enAire) {
            actualizarEnAire(datosNivel);
        } else {
            switch (estadoEnemigo) {
                case IDLE_PATRULLA:
                    //si ve al jugador cancela la pausa y sale a perseguirle
                    if (verJugador(datosNivel, jugador)) {
                        enPausaPatrulla = false;
                        animacionIdleCompletada = false;
                        ciclosIdleCompletados = 0;
                        nuevoEstado(CORRER);
                        break;
                    }
                    //marca el ciclo como completado cuando llega al ultimo frame
                    if (aniIndice >= getSpriteAmount(ENEMIGO1, IDLE_PATRULLA) - 1) {
                        animacionIdleCompletada = true;
                    }
                    //al volver al frame 0 cuenta el ciclo y vuelve a correr si llego al maximo
                    if (animacionIdleCompletada && aniIndice == 0) {
                        animacionIdleCompletada = false;
                        ciclosIdleCompletados++;
                        if (ciclosIdleCompletados >= MAX_CICLOS_IDLE) {
                            ciclosIdleCompletados = 0;
                            enPausaPatrulla = false;
                            nuevoEstado(CORRER);
                        }
                    }
                    break;

                case IDLE:
                    //si ve al jugador sale del idle y lo persigue
                    if (verJugador(datosNivel, jugador)) {
                        enPausaPatrulla = false;
                        nuevoEstado(CORRER);
                        break;
                    }
                    //cuando la animacion idle completa un ciclo vuelve a correr
                    if (aniIndice == 0 && aniTick == 0 && enPausaPatrulla) {
                        enPausaPatrulla = false;
                        nuevoEstado(CORRER);
                    }
                    break;

                case CORRER:
                    //acumula ticks y hace una pausa idle periodica con duracion aleatoria
                    if (!enPausaPatrulla) {
                        ticksIdle++;
                        if (ticksIdle >= maxTicksIdle) {
                            ticksIdle = 0;
                            maxTicksIdle = 800 + new Random().nextInt(600);
                            enPausaPatrulla = true;
                            nuevoEstado(IDLE_PATRULLA);
                            return;
                        }
                    }
                    //si ve al jugador se dirige hacia el y ataca si esta cerca
                    if (verJugador(datosNivel, jugador)) {
                        dirigirseAJugador(jugador);
                        if (cercaParaAtacar(jugador)) {
                            nuevoEstado(ATAQUE);
                        }
                    }
                    movimiento(datosNivel, enemigos);
                    break;

                case ATAQUE:
                    //resetea el flag de ataque al inicio de la animacion para permitir el siguiente
                    if (aniIndice == 0) {
                        ataqueRealizado = false;
                    }
                    //registra el golpe en el frame 3, una sola vez por animacion
                    if (aniIndice == 3 && !ataqueRealizado) {
                        revisarGolpeEnemigo(boxAtaque, jugador);
                    }
                    break;

                case GOLPE:
                    //la animacion de golpe se gestiona en actualizarAnimacionTick, sin logica extra
                    break;
            }
        }
    }

    //restaura todos los valores del enemigo a su estado inicial para reiniciar el nivel
    public void resetearEnemigo() {

        hitbox.x = x;
        hitbox.y = y;
        primeraActualizacion = true;
        vidaActual = vidaMax;
        nuevoEstado(CORRER);
        activo = true;
        velocidadCaida = 0;
        ticksIdle = 0;
        enPausaPatrulla = false;
        maxTicksIdle = 800 + new Random().nextInt(600);
        animacionIdleCompletada = false;
        ciclosIdleCompletados = 0;
        invencible = false;
        ticksInvencible = 0;
        poderOtorgado = false;

    }


}