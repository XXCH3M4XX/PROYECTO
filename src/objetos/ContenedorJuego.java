package objetos;

import main.Juego;

import static utils.Constantes.constantesObjetos.*;
//clase para la caja del juego
public class ContenedorJuego extends ObjetosJuego{
    public ContenedorJuego(int x, int y, int tipoObjeto) {
        super(x, y, tipoObjeto);
        crearHitbox();
    }

    private void crearHitbox() {

        if(tipoObjeto == CAJA){
            iniciarHitbox(25, 18);

            xDrawOffset = (int)(7 * Juego.ESCALA);
            yDrawOffset = (int)(12 * Juego.ESCALA);
        }else{
            iniciarHitbox(23,25);
            xDrawOffset = (int)(8 * Juego.ESCALA);
            yDrawOffset = (int)(5 * Juego.ESCALA);
        }
        hitbox.y += yDrawOffset + (int)(Juego.ESCALA * 2);
        hitbox.x += (float) xDrawOffset / 2;
    }

    public void update(){
        if(animacion){
            actualizarAnimacionTick();
        }
    }

}
