package entidades;
import main.Juego;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import static utils.Constantes.Direcciones.IZQUIERDA;
import static utils.Constantes.constantesDelEnemigo.*;
import static utils.Miscelaneos.*;

public class PersonajeEnemigo1 extends Enemigo{

    private Rectangle2D.Float boxAtaque;
    private int boxAtaqueOffsetX;

    //temporizador para interrumpir la patrulla con la animacion idle
    private int ticksIdle = 0;
    private int maxTicksIdle = 0;
    private boolean enPausaPatrulla = false;
    private boolean animacionIdleCompletada = false;
    private int ciclosIdleCompletados = 0;
    private static final int MAX_CICLOS_IDLE = 4;

    public PersonajeEnemigo1(float x, float y) {
        super(x, y, ENEMIGO1_WIDTH, ENEMIGO_HEIGHT, ENEMIGO1);
        iniciarHitbox(x, y, (int)(17 * Juego.ESCALA), (int)(29 * Juego.ESCALA));
        iniciarHitboxAtaque();
        maxTicksIdle = 800 + new Random().nextInt(600); // ← añade esto
    }

    private void iniciarHitboxAtaque() {
        boxAtaque = new Rectangle2D.Float(x, y, (int)(82 * Juego.ESCALA), (int)(29 * Juego.ESCALA));
        boxAtaqueOffsetX = (int)(Juego.ESCALA * 30);
    }

    //metodo para actualizar la animacion (en esta usa el metodo donde se define el cuerpo de lo que es la acutalizacion)
    public void update(int [][] datosNivel, Jugador jugador){
        actualizarMovimiento(datosNivel, jugador);
        actualizarAnimacionTick();
        actualizarBoxAtaque();
    }

    private void actualizarBoxAtaque() {
        boxAtaque.x = hitbox.x - boxAtaqueOffsetX;
        boxAtaque.y = hitbox.y;
    }

    private void actualizarMovimiento(int [][] datosNivel, Jugador jugador){
        if (primeraActualizacion){
            checkPrimeraActualizacion(datosNivel);
        }
        if (enAire){
            actualizarEnAire(datosNivel);
        }else {
            switch (estadoEnemigo){
                case IDLE_PATRULLA:
                    if (verJugador(datosNivel, jugador)) {
                        enPausaPatrulla = false;
                        animacionIdleCompletada = false;
                        ciclosIdleCompletados = 0;
                        nuevoEstado(CORRER);
                        break;
                    }
                    if (aniIndice >= getSpriteAmount(ENEMIGO1, IDLE_PATRULLA) - 1) {
                        animacionIdleCompletada = true;
                    }
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
                    if (verJugador(datosNivel, jugador)) {
                        enPausaPatrulla = false;
                        nuevoEstado(CORRER);
                        break;
                    }
                    // cuando termina la animacion idle vuelve a correr
                    // actualizarAnimacionTick ya resetea a IDLE al terminar,
                    // asi que necesitamos detectar cuando ha completado un ciclo
                    if (aniIndice == 0 && aniTick == 0 && enPausaPatrulla) {
                        enPausaPatrulla = false;
                        nuevoEstado(CORRER);
                    }
                    break;
                case CORRER:
                    // el contador sube siempre, independientemente de si ve al jugador
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

                    if (verJugador(datosNivel, jugador)) {
                        dirigirseAJugador(jugador);
                        if (cercaParaAtacar(jugador)) {
                            nuevoEstado(ATAQUE);
                        }
                    }
                    movimiento(datosNivel);
                    break;
                case ATAQUE:
                    if (aniIndice == 0){
                        ataqueRealizado = false;
                    }
                    if (aniIndice == 3 && !ataqueRealizado){
                        revisarGolpeEnemigo(boxAtaque,jugador);
                    }
                    break;
                case GOLPE:
                    break;
            }
        }
    }

    public void resetearEnemigo(){
        hitbox.x = x;
        hitbox.y = y;
        primeraActualizacion = true;
        vidaActual = vidaMax;
        nuevoEstado(CORRER); // ← empieza patrullando
        activo = true;
        velocidadCaida = 0;
        ticksIdle = 0;
        enPausaPatrulla = false;
        maxTicksIdle = 800 + new Random().nextInt(600);
        animacionIdleCompletada = false;
        ciclosIdleCompletados = 0;
    }

    public void drawBoxAtaque (Graphics g, int nivelOffset){
        g.setColor(Color.red);
        g.drawRect((int)(boxAtaque.x - nivelOffset), (int)boxAtaque.y, (int)boxAtaque.width, (int)boxAtaque.height);
    }

}
