package objetos;

import main.Juego;

public class Pinchos extends ObjetosJuego{

    public Pinchos(int x, int y, int tipoObjeto) {
        super(x, y, tipoObjeto);

        iniciarHitbox(32, 16);
        xDrawOffset = 0;
        yDrawOffset = (int)(Juego.ESCALA * 10);
        hitbox.y += yDrawOffset;

    }

}
