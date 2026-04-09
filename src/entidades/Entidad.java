package entidades;

import java.awt.*;
import java.awt.geom.Rectangle2D;

//clase base abstracta para todos los objetos moviles o interactuables
public class Entidad {
    protected float x;
    protected float y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;

    //constructor que define la posicion y dimensiones basicas de la entidad
    public Entidad(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    //dibuja el rectangulo de colision para facilitar pruebas y depuracion
    protected void pintarHitbox(Graphics g) {
        //para depurar la caja de colision
        g.setColor(Color.RED);
        g.drawRect((int)hitbox.x, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }

    //crea el objeto de rectangulo que servira para detectar impactos
    protected void iniciarHitbox(float x, float y, int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    //metodo comentado para actualizar la posicion de la hitbox segun la entidad
//    protected void updateHitbox() {
//        hitbox.x = (int)x;
//        hitbox.y = (int)y;
//    }

    //permite que otros objetos consulten el area de colision de esta entidad
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }


}