package entidades;
import main.Juego;
import utils.Constantes;
import utils.Miscelaneos;

import static utils.Constantes.constantesDelEnemigo.*;
import static utils.Miscelaneos.*;
import static utils.Constantes.Direcciones.*;

public abstract class Enemigo extends Entidad {
    //declaracion de variables
    private int aniIndice, estadoEnemigo, tipoEnemigo;
    private int aniTick, aniVel = 25;
    private boolean primeraActualizacion = true;
    private boolean enAire = false;
    private float velocidadCaida;
    private float gravedad = 0.004f * Juego.ESCALA;
    private float velocidadAndar = Juego.ESCALA;
    private int direccionAndar = IZQUIERDA;
    private float xVelocidad = 0.00f;
    public static final int SPRITE_W = (int)(64 * Juego.ESCALA);
    public static final int SPRITE_H = (int)(40 * Juego.ESCALA);

    //constructor que va usar el enemigo, extrendido de la clase entidad
    public Enemigo(float x, float y, int width, int height, int tipoEnemigo) {
        super(x, y, width, height);
        this.tipoEnemigo = tipoEnemigo;
//        iniciarHitbox(x, y, (int)(20 * Juego.ESCALA), (int)(23 * Juego.ESCALA));
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

    private void actualizarAnimacionTick(){
        aniTick++;
        if(aniTick >= aniVel){
            aniTick = 0;
            aniIndice++;
            if (aniIndice >= getSpriteAmount(tipoEnemigo, estadoEnemigo)){
                aniIndice = 0;
            }
        }

    }

    //metodo para actualizar la animacion (en esta usa el metodo donde se define el cuerpo de lo que es la acutalizacion)
    public void update(int [][] datosNivel){
        actualizarMovimiento(datosNivel);
        actualizarAnimacionTick();
    }

    private void actualizarMovimiento(int [][] datosNivel){
        if (primeraActualizacion){
            primeraActualizacion = false;
            if(!solido(hitbox.x, hitbox.y + 1, datosNivel)){
                enAire = true;
            }
        }
        if (enAire){
            if (puedeMoverse(hitbox.x, hitbox.y + velocidadCaida, hitbox.width, hitbox.height, datosNivel)){
                hitbox.y += velocidadCaida;
                velocidadCaida += gravedad;
            }else{
                enAire = false;
                hitbox.y = GetYPosTechoOSuelo(hitbox, velocidadCaida,hitbox.y + velocidadCaida);
            }
        }else {
            switch (estadoEnemigo){
                case IDLE:
                    cambiarEstado(CORRER);
                    break;
                case CORRER:

                    if(direccionAndar == IZQUIERDA){
                        xVelocidad = -velocidadAndar;
                    }else{
                        xVelocidad = velocidadAndar;
                    }
                    if(puedeMoverse(hitbox.x + xVelocidad, hitbox.y, hitbox.width, hitbox.height, datosNivel)){
                        if (esSuelo(hitbox, xVelocidad, datosNivel)){
                            hitbox.x += xVelocidad;
                            return;
                        }
                    }
                    cambiarDireccionAndar();
                    break;
            }
        }
}

    private void cambiarDireccionAndar() {
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

    private void cambiarEstado(int nuevoEstado) {
        this.estadoEnemigo = nuevoEstado;
        this.aniIndice = 0;
        this.aniTick = 0;
    }
}
