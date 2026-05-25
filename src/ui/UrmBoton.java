package ui;

import utils.CargaSprites;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utils.Constantes.UI.URMBotones.*;

//boton interactivo de tipo URM (undo, replay, menu) con tres estados visuales: reposo, hover y pulsado
public class UrmBoton extends BotonesPausa {

    //frames del boton: 0 reposo, 1 hover, 2 pulsado
    private BufferedImage[] imgs;

    //fila del spritesheet que corresponde a este boton e indice del frame activo
    private int rowIndex, indice;

    //flags que indican si el raton esta encima o pulsando el boton
    private boolean mouseOver, mousePressed;

    //inicializa el boton con su posicion, tamaño y fila del atlas
    public UrmBoton(int x, int y, int widht, int height, int rowIndex) {
        super(x, y, widht, height);
        this.rowIndex = rowIndex;
        loadImages();
    }

    //carga los tres frames del boton desde el spritesheet de botones URM
    private void loadImages() {
        BufferedImage temp = CargaSprites.GetSpriteAtlas(CargaSprites.BOTONES_NAVEGACION);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE,
                    URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
        }
    }

    //selecciona el frame correcto segun el estado del raton
    public void update() {
        indice = 0;
        if (mouseOver) indice = 1;
        if (mousePressed) indice = 2;
    }

    //dibuja el frame activo del boton en su posicion
    public void draw(Graphics g) {
        g.drawImage(imgs[indice], x, y, URM_SIZE, URM_SIZE, null);
    }

    //resetea los flags de hover y pulsado al soltar el raton
    public void reiniciarBooleanos() {
        mouseOver = false;
        mousePressed = false;
    }



    //activa o desactiva el efecto hover del boton
    public void setMouseOver(boolean mouseOver) { this.mouseOver = mouseOver; }

    //devuelve true si el boton esta siendo pulsado
    public boolean isMousePressed() { return mousePressed; }

    //activa o desactiva el estado de pulsado del boton
    public void setMousePressed(boolean mousePressed) { this.mousePressed = mousePressed; }
}