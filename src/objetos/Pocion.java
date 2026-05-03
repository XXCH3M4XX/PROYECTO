package objetos;

import main.Juego;

//representa una pocion que flota verticalmente y puede ser recogida por el jugador
public class Pocion extends ObjetosJuego {

    //desplazamiento vertical actual del efecto de flote
    private float offsetFlote;

    //limite maximo del flote y direccion actual (1 sube, -1 baja)
    private int maxFloteOffset, direccionFlote = 1;

    public Pocion(int x, int y, int tipoObjeto) {
        super(x, y, tipoObjeto);
        animacion = true;
        iniciarHitbox(7, 14);
        xDrawOffset = (int)(3 * Juego.ESCALA);
        yDrawOffset = (int)(2 * Juego.ESCALA);
        maxFloteOffset = (int)(10 * Juego.ESCALA);
    }

    public void update() {
        actualizarAnimacionTick();
        actualizarFlote();
    }

    //resetea la animacion, el flote y la posicion de la hitbox al estado inicial
    @Override
    public void reset() {
        super.reset();
        offsetFlote = 0;
        direccionFlote = 1;
        hitbox.y = y;
        hitbox.x = x;
    }

    //mueve la hitbox verticalmente para simular el efecto de flotacion
    private void actualizarFlote() {
        offsetFlote += (0.12f * Juego.ESCALA * direccionFlote);
        if (offsetFlote >= maxFloteOffset) {
            direccionFlote = -1;
        } else if (offsetFlote < 0) {
            direccionFlote = 1;
        }
        hitbox.y = y + offsetFlote;
    }
}