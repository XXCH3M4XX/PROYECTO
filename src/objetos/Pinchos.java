package objetos;

import main.Juego;

//representa un pincho del nivel que mata al jugador al tocarlo
public class Pinchos extends ObjetosJuego {

    //coloca el pincho en su posicion y ajusta la hitbox para que coincida con la parte visible
    public Pinchos(int x, int y, int tipoObjeto) {
        super(x, y, tipoObjeto);

        //hitbox mas pequeña que el sprite para que solo la punta del pincho haga daño
        iniciarHitbox(32, 16);
        xDrawOffset = 0;
        //desplaza la hitbox hacia abajo para alinearla con la punta visible del pincho
        yDrawOffset = (int)(Juego.ESCALA * 10);
        hitbox.y += yDrawOffset;
    }
}