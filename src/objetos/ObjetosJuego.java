package objetos;

import main.Juego;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constantes.VELOCIDAD_ANIMACION;
import static utils.Constantes.constantesObjetos.*;

//clase para poder definir animaciones y hitbox de los objetos del videojuego
public class ObjetosJuego {
    protected int x,y,tipoObjeto;
    protected Rectangle2D.Float hitbox;
    protected boolean animacion, activa = true;
    protected int aniTick, aniIndice;
    protected int xDrawOffset, yDrawOffset;

    public ObjetosJuego(int x, int y, int tipoObjeto){
        this.x = x;
        this.y = y;
        this.tipoObjeto = tipoObjeto;
    }
    //Animacion de los objetos del juego
    protected void actualizarAnimacionTick() {
        aniTick++;
        if (aniTick >= VELOCIDAD_ANIMACION ) {
            aniTick = 0;
            aniIndice++;
            if (aniIndice >= obtenerCantidadSprites(tipoObjeto)) {
                aniIndice = 0;
                if(tipoObjeto == BARRIL || tipoObjeto == CAJA){
                    animacion = false;
                    activa = false;
                }
            }
        }
    }
    //al resetear el juego, se resetean las animaciones
    public void reset(){
        aniIndice = 0;
        aniTick = 0;
        activa = true;
        if(tipoObjeto == BARRIL || tipoObjeto == CAJA){
            animacion = false;
        }else {
            animacion = true;
        }
    }
    //inica la hitbox del objeto para poder interactuar con el
    protected void iniciarHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int)(width * Juego.ESCALA),(int) (height * Juego.ESCALA));
    }
    //metodo opcional para poder ver y trabajar con la hitbox
    public void pintarHitbox(Graphics g, int OffsetXNivel) {
        //para depurar la caja de colision
        g.setColor(Color.RED);
        g.drawRect((int)hitbox.x - OffsetXNivel, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }
    //getters y setters
    public boolean isActiva() {
        return activa;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setActiva(boolean activa){
        this.activa = activa;
    }

    public int getTipoObjeto() {
        return tipoObjeto;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getAniIndice(){
        return aniIndice;
    }

    public void setAnimacion(boolean animacion){
        this.animacion = animacion;
    }

}
