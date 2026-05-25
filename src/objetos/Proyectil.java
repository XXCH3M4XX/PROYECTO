package objetos;

import java.awt.geom.Rectangle2D;
import static utils.Constantes.Proyectiles.*;

//representa un proyectil de hueso lanzado por el esqueleto, gestiona su movimiento e impacto
public class Proyectil {
    //hitbox del proyectil, mas pequeña que el sprite para mayor precision
    private Rectangle2D.Float hitbox;

    //direccion de movimiento: 1 derecha, -1 izquierda
    private int dirreccion;

    //true mientras el proyectil esta en vuelo, false cuando impacta
    private boolean activo = true;

    //true durante la animacion de impacto, false cuando termina
    private boolean impacto = false;

    //contadores de animacion compartidos para vuelo e impacto
    private int aniTick;
    private int aniIndice;

    //desplazamiento del sprite respecto a la hitbox para alinearlos visualmente
    private int spriteOffsetX;
    private int spriteOffsetY;

    //ticks por frame de animacion, tanto en vuelo como en impacto
    private static final int ANI_VELOCIDAD = 6;

    //calcula los offsets y coloca la hitbox centrada respecto al sprite
    public Proyectil(int x, int y, int dirreccion) {
        int hitboxAncho = (int)(PROYECTIL_ANCHO * 0.5f);
        int hitboxAlto  = (int)(PROYECTIL_ALTO  * 0.5f);

        //ajuste manual fino para centrar correctamente el sprite sobre la hitbox
        spriteOffsetX = (PROYECTIL_ANCHO - hitboxAncho) / 2 - 9;
        spriteOffsetY = (PROYECTIL_ALTO  - hitboxAlto)  / 2 - 10;

        hitbox = new Rectangle2D.Float(x + spriteOffsetX, y + spriteOffsetY, hitboxAncho, hitboxAlto);
        this.dirreccion = dirreccion;
    }

    //devuelve el desplazamiento horizontal del sprite respecto a la hitbox
    public int getSpriteOffsetX() { return spriteOffsetX; }

    //devuelve el desplazamiento vertical del sprite respecto a la hitbox
    public int getSpriteOffsetY() { return spriteOffsetY; }

    //mueve el proyectil en su direccion y avanza la animacion de vuelo
    public void updatePosicion() {
        hitbox.x += dirreccion * velocidad;
        actualizarAnimacion();
    }

    //avanza el contador de animacion de vuelo y lo reinicia al llegar al ultimo frame
    private void actualizarAnimacion() {
        aniTick++;
        if (aniTick >= ANI_VELOCIDAD) {
            aniTick = 0;
            aniIndice++;
            if (aniIndice >= 6) {
                aniIndice = 0;
            }
        }
    }

    //devuelve el frame actual de la animacion, usado tanto para vuelo como para impacto
    public int getAniIndice() { return aniIndice; }

    //reinicia los contadores de animacion, se llama al iniciar la animacion de impacto
    public void resetAniIndice() {
        aniTick = 0;
        aniIndice = 0;
    }

    //avanza la animacion de impacto y desactiva el proyectil al terminar
    public void actualizarAnimacionImpacto() {
        aniTick++;
        if (aniTick >= ANI_VELOCIDAD) {
            aniTick = 0;
            aniIndice++;
            if (aniIndice >= 6) {
                //cuando termina la animacion de impacto el proyectil desaparece completamente
                impacto = false;
                aniIndice = 0;
            }
        }
    }



    //devuelve true si el proyectil esta reproduciendo la animacion de impacto
    public boolean impacto() { return impacto; }

    //devuelve la hitbox del proyectil para comprobar colisiones con el jugador o el nivel
    public Rectangle2D.Float getHitbox() { return hitbox; }

    //devuelve true si el proyectil sigue en vuelo
    public boolean estaActivada() { return activo; }

    //activa o desactiva el vuelo del proyectil
    public void setActivo(boolean activo) { this.activo = activo; }

    //activa o desactiva el estado de impacto del proyectil
    public void setImpacto(boolean b) { this.impacto = b; }
}