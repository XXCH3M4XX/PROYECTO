package objetos;

import main.Juego;

public class Cañon extends ObjetosJuego{

    private int direccionY;

    public Cañon(int x, int y, int tipoObjeto) {
        super(x, y, tipoObjeto);
        direccionY = y / Juego.TILES_SIZE;
        iniciarHitbox(40,26);
        hitbox.x -= (int)(4 * Juego.ESCALA);
        hitbox.y += (int)(6 * Juego.ESCALA);
    }

    public void update(){
        if (animacion){
            actualizarAnimacionTick();
        }
    }

    public int getDireccionY(){
        return direccionY;
    }

}
