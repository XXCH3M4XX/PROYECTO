package ui;

import main.Juego;
import utils.CargaSprites;

import java.awt.*;
import java.awt.image.BufferedImage;

//boton para mostrar los controles del juego en el menu de pausa
public class BotonControles extends BotonesPausa {

    private BufferedImage[] imgs;
    private int indice;
    private boolean mouseOver, mousePressed; // ← añade esto

    private static final int ANCHO_DEFAULT = 61;
    private static final int ALTO_DEFAULT = 59;

    public BotonControles(int x, int y) {
        super(x, y, (int)(61 * Juego.ESCALA), (int)(59 * Juego.ESCALA));
        cargarImagenes();
    }

    private void cargarImagenes() {
        BufferedImage temp = CargaSprites.GetSpriteAtlas(CargaSprites.BOTON_CONTROLES);
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
        g.drawImage(imgs[indice], x, y, widht, height, null);
    }

    public boolean isMouseOver() { return mouseOver; }
    public void setMouseOver(boolean mouseOver) { this.mouseOver = mouseOver; }
    public boolean isMousePressed() { return mousePressed; }
    public void setMousePressed(boolean mousePressed) { this.mousePressed = mousePressed; }
    public void resetBools() { mouseOver = false; mousePressed = false; }
}