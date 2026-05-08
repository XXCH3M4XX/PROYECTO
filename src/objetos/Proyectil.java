package objetos;

import java.awt.geom.Rectangle2D;
import static utils.Constantes.Proyectiles.*;

public class Proyectil {
    private Rectangle2D.Float hitbox;
    private int dirreccion;
    private boolean activo = true;
    private boolean impacto = false;
    private int aniTick;
    private int aniIndice;
    private int spriteOffsetX;
    private int spriteOffsetY;


    private static final int ANI_VELOCIDAD = 6;

    public Proyectil(int x, int y, int dirreccion) {
        int hitboxAncho = (int)(PROYECTIL_ANCHO * 0.5f);
        int hitboxAlto = (int)(PROYECTIL_ALTO * 0.5f);

        spriteOffsetX = (PROYECTIL_ANCHO - hitboxAncho) / 2 - 9 ;
        spriteOffsetY = (PROYECTIL_ALTO - hitboxAlto) / 2 - 10;

        hitbox = new Rectangle2D.Float(x + spriteOffsetX, y + spriteOffsetY, hitboxAncho, hitboxAlto);
        this.dirreccion = dirreccion;
    }

    public int getSpriteOffsetX() { return spriteOffsetX; }
    public int getSpriteOffsetY() { return spriteOffsetY; }

    public void updatePosicion() {
        hitbox.x += dirreccion * velocidad;
        actualizarAnimacion();
    }
    private void actualizarAnimacion() {
        aniTick++;

        if(aniTick >= ANI_VELOCIDAD){
            aniTick = 0;
            aniIndice++;

            if(aniIndice >= 6){
                aniIndice = 0;
            }
        }
    }
    public int getAniIndice() {
        return aniIndice;
    }
    public void resetAniIndice() {
        aniTick = 0;
        aniIndice = 0;
    }

    public void actualizarAnimacionImpacto() {
        aniTick++;
        if(aniTick >= ANI_VELOCIDAD){
            aniTick = 0;
            aniIndice++;
            if(aniIndice >= 6) {
                // cuando termina la animacion de impacto se desactiva completamente
                impacto = false;
                aniIndice = 0;
            }
        }
    }
    public void setPosicion(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
    }
    public boolean impacto() {
        return impacto;
    }
    public Rectangle2D.Float getHitbox() {
        //para comprobar si golpea al jugador o no
        return hitbox;
    }
    public boolean estaActivada() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setImpacto(boolean b) {
        this.impacto = b;
    }
}
