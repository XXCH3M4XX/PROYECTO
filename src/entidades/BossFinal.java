package entidades;

import main.Juego;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import static utils.Constantes.constantesDelEnemigo.*;
import static utils.Constantes.ConstantesDio.*;

//enemigo Dio que patrulla, persigue y ataca al jugador cuerpo a cuerpo
public class BossFinal extends Enemigo {

    //hitbox del ataque y su desplazamiento horizontal respecto a la hitbox del enemigo
    private Rectangle2D.Float boxAtaque;
    private int boxAtaqueOffsetX;

    //contador de ticks para interrumpir la patrulla con una animacion idle periodica
    private int ticksIdle = 0;
    private int maxTicksIdle = 0;

    //true mientras el enemigo esta en la pausa de patrulla reproduciendo la animacion idle
    private boolean enPausaPatrulla = false;

    //coloca al enemigo en su posicion inicial y arranca en animacion predeterminada
    public BossFinal(float x, float y) {
        super(x, y, DIO_WIDTH, DIO_HEIGHT, DIO);
        iniciarHitbox(x, y, (int)(17 * Juego.ESCALA), (int)(29 * Juego.ESCALA));
        iniciarHitboxAtaque();
        maxTicksIdle = 800 + new Random().nextInt(600);
        nuevoEstado(PREDETERMINADO);
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
        if (mirandoDerecha) {
            boxAtaque.x = hitbox.x + boxAtaqueOffsetX;
        } else {
            boxAtaque.x = hitbox.x - boxAtaqueOffsetX;
        }
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
                case PREDETERMINADO:
                    //bucle de animacion en reposo, sale al ver al jugador
                    if (verJugador(datosNivel, jugador)) {
                        nuevoEstado(CORRIENDO);
                    }
                    break;

                case CORRIENDO:
                    //acumula ticks y hace una pausa periodica con duracion aleatoria
                    if (!enPausaPatrulla) {
                        ticksIdle++;
                        if (ticksIdle >= maxTicksIdle) {
                            ticksIdle = 0;
                            maxTicksIdle = 800 + new Random().nextInt(600);
                            enPausaPatrulla = true;
                            nuevoEstado(PREDETERMINADO);
                            return;
                        }
                    }
                    //si ve al jugador se dirige hacia el y ataca si esta cerca
                    if (verJugador(datosNivel, jugador)) {
                        dirigirseAJugador(jugador);
                        if (cercaParaAtacar(jugador)) {
                            nuevoEstado(GANCHO);
                        }
                    }
                    movimiento(datosNivel, enemigos);
                    break;

                case GANCHO:
                    if (aniIndice == 0) {
                        ataqueRealizado = false;
                    }
                    if (aniIndice == 3 && !ataqueRealizado) {
                        revisarGolpeEnemigo(boxAtaque, jugador);
                        ataqueRealizado = true;  // ← AÑADIR: evita golpe múltiple por frame
                    }
                    break;

                case DAÑO:
                    //la animacion de daño se gestiona en actualizarAnimacionTick, sin logica extra
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
        nuevoEstado(PREDETERMINADO); // arranca quieto
        activo = true;
        velocidadCaida = 0;
        ticksIdle = 0;
        enPausaPatrulla = false;
        maxTicksIdle = 800 + new Random().nextInt(600);
    }



    @Override
    protected void actualizarAnimacionTick() {
        aniTick++;
        if (aniTick >= aniVel) {
            aniTick = 0;
            aniIndice++;
            if (aniIndice >= utils.Constantes.ConstantesDio.GetCantidadSpriteDio(estadoEnemigo)) {
                aniIndice = 0;
                switch (estadoEnemigo) {
                    case GANCHO:
                    case DAÑO:
                        nuevoEstado(CORRIENDO);
                        break;
                    case PREDETERMINADO:

                        break;
                    case MUERTEDIO:
                        //no resetea aniIndice, se congela en el ultimo frame
                        activo = false;
                        break;
                }
            }
        }
    }

    @Override
    public void daño(int daño) {
        if (estadoEnemigo == MUERTEDIO) return;

        vidaActual -= daño;
        if (vidaActual <= 0) {
            nuevoEstado(MUERTEDIO);
        } else {
            nuevoEstado(DAÑO);
        }
    }
}