package entidades;
import main.Juego;

import static utils.Constantes.Direcciones.IZQUIERDA;
import static utils.Constantes.constantesDelEnemigo.*;
import static utils.Miscelaneos.*;

public class PersonajeEnemigo1 extends Enemigo{

    public PersonajeEnemigo1(float x, float y ) {
        super(x, y, ENEMIGO1_WIDTH, ENEMIGO_HEIGHT, ENEMIGO1);
        iniciarHitbox(x, y, (int)(17 * Juego.ESCALA), (int)(29 * Juego.ESCALA)); // más altainiciarHitbox(x, y, (int)(50 * Juego.ESCALA), (int)(28 * Juego.ESCALA));

    }
    //metodo para actualizar la animacion (en esta usa el metodo donde se define el cuerpo de lo que es la acutalizacion)
    public void update(int [][] datosNivel, Jugador jugador){
        actualizarMovimiento(datosNivel, jugador);
        actualizarAnimacionTick();
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
            }
        }
    }

}
