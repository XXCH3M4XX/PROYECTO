package entidades;
import main.Juego;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constantes.Direcciones.IZQUIERDA;
import static utils.Constantes.constantesDelEnemigo.*;
import static utils.Miscelaneos.*;

public class PersonajeEnemigo1 extends Enemigo{

    private Rectangle2D.Float boxAtaque;
    private int boxAtaqueOffsetX;

    public PersonajeEnemigo1(float x, float y ) {
        super(x, y, ENEMIGO1_WIDTH, ENEMIGO_HEIGHT, ENEMIGO1);
        iniciarHitbox(x, y, (int)(17 * Juego.ESCALA), (int)(29 * Juego.ESCALA)); // más altainiciarHitbox(x, y, (int)(50 * Juego.ESCALA), (int)(28 * Juego.ESCALA));
        iniciarHitboxAtaque();
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
                case IDLE:
                    nuevoEstado(CORRER);
                    break;
                case CORRER:
                    if(verJugador(datosNivel, jugador)) {
                        dirigirseAJugador(jugador);
                        if(cercaParaAtacar(jugador)) {
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
        nuevoEstado(IDLE);
        activo = true;
        velocidadCaida = 0;
    }


    public void drawBoxAtaque (Graphics g, int nivelOffset){
        g.setColor(Color.red);
        g.drawRect((int)(boxAtaque.x - nivelOffset), (int)boxAtaque.y, (int)boxAtaque.width, (int)boxAtaque.height);
    }

}
