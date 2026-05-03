package objetos;

import main.Juego;

import static utils.Constantes.constantesObjetos.*;

//representa los contenedores destructibles del nivel, como cajas y barriles
public class ContenedorJuego extends ObjetosJuego {

    public ContenedorJuego(int x, int y, int tipoObjeto) {
        super(x, y, tipoObjeto);
        crearHitbox();
    }

    //define la hitbox y los offsets de dibujo segun el tipo de contenedor
    private void crearHitbox() {
        if (tipoObjeto == CAJA) {
            iniciarHitbox(25, 18);
            xDrawOffset = (int)(7 * Juego.ESCALA);
            yDrawOffset = (int)(12 * Juego.ESCALA);
        } else {
            iniciarHitbox(23, 25);
            xDrawOffset = (int)(8 * Juego.ESCALA);
            yDrawOffset = (int)(5 * Juego.ESCALA);
        }
        //ajusta la posicion de la hitbox para que coincida visualmente con el sprite
        hitbox.y += yDrawOffset + (int)(Juego.ESCALA * 2);
        hitbox.x += (float) xDrawOffset / 2;
    }

    //solo avanza la animacion si esta activa, evita que se reproduzca en reposo
    public void update() {
        if (animacion) {
            actualizarAnimacionTick();
        }
    }

    //resetea el estado y recalcula la hitbox desde la posicion original
    @Override
    public void reset() {
        super.reset();
        crearHitbox();
    }
}