package objetos;

import main.Juego;

public class Pocion extends ObjetosJuego{

    private float offsetFlote;
    private int maxFloteOffset, direccionFlote = 1;
    //clase de uno de los objetos
    public Pocion(int x, int y, int tipoObjeto) {
        super(x, y, tipoObjeto);
        animacion = true;
        iniciarHitbox(7, 14);
        xDrawOffset = (int)(3 * Juego.ESCALA);
        yDrawOffset = (int)(2 * Juego.ESCALA);

        maxFloteOffset = (int)(10 * Juego.ESCALA);
    }

    public void update(){
        actualizarAnimacionTick();
        actualizarFlote();
    }

    private void actualizarFlote() {
        offsetFlote += (0.12f * Juego.ESCALA * direccionFlote);
        if (offsetFlote >= maxFloteOffset){
            direccionFlote =-1;
        } else if (offsetFlote < 0) {
            direccionFlote = 1;
        }
        hitbox.y = y+offsetFlote;
    }

}
