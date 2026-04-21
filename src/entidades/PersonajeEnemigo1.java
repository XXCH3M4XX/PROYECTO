package entidades;
import main.Juego;

import static utils.Constantes.constantesDelEnemigo.*;
public class PersonajeEnemigo1 extends Enemigo{

    public PersonajeEnemigo1(float x, float y ) {
        super(x, y, ENEMIGO1_WIDTH, ENEMIGO_HEIGHT, ENEMIGO1);
        iniciarHitbox(x, y, (int)(20 * Juego.ESCALA), (int)(29 * Juego.ESCALA)); // más altainiciarHitbox(x, y, (int)(50 * Juego.ESCALA), (int)(28 * Juego.ESCALA));

    }

}
