package ui;

import main.Juego;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

//boton de engranaje para acceder a las opciones desde el menu principal
public class BotonOpciones {

    private BufferedImage[] imgs;
    private int indice;
    private boolean mouseOver, mousePressed;
    private int x, y, ancho, alto;
    private Rectangle bordes;

    private static final int ANCHO_DEFAULT = 51;
    private static final int ALTO_DEFAULT = 53;

    public BotonOpciones(int x, int y) {
        this.ancho = (int)(ANCHO_DEFAULT * Juego.ESCALA);
        this.alto = (int)(ALTO_DEFAULT * Juego.ESCALA);
        this.x = x;
        this.y = y;
        bordes = new Rectangle(x, y, ancho, alto);
        cargarImagenes();
    }

    private void cargarImagenes() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BOTON_OPCIONES);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * ANCHO_DEFAULT, 0, ANCHO_DEFAULT, ALTO_DEFAULT);
        }
    }

    public void update() {
        indice = 0;
        if (mouseOver) indice = 1;
        if (mousePressed) indice = 2;
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[indice], x, y, ancho, alto, null);
    }

    public boolean isMouseOver() { return mouseOver; }
    public void setMouseOver(boolean mouseOver) { this.mouseOver = mouseOver; }
    public boolean isMousePressed() { return mousePressed; }
    public void setMousePressed(boolean mousePressed) { this.mousePressed = mousePressed; }
    public Rectangle getBordes() { return bordes; }
    public void resetBools() { mouseOver = false; mousePressed = false; }
}