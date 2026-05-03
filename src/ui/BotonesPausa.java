package ui;

import java.awt.*;

//clase base que define la posicion, tamaño y area de colision de los botones del menu de pausa
public class BotonesPausa {

    protected int x, y, widht, height;

    //rectangulo usado para detectar si el raton esta dentro del boton
    protected Rectangle bordes;

    public BotonesPausa(int x, int y, int widht, int height) {
        this.x = x;
        this.y = y;
        this.widht = widht;
        this.height = height;
        crearBordes();
    }

    //inicializa el rectangulo de colision con la posicion y tamaño del boton
    private void crearBordes() {
        bordes = new Rectangle(x, y, widht, height);
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getWidht() { return widht; }
    public void setWidht(int widht) { this.widht = widht; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public Rectangle getBordes() { return bordes; }
    public void setBordes(Rectangle bordes) { this.bordes = bordes; }
}