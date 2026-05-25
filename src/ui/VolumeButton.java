package ui;

import utils.CargaSprites;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utils.Constantes.UI.BotonesVolumen.*;

//slider de volumen con un boton arrastrable que se mueve horizontalmente
public class VolumeButton extends BotonesPausa {

    //frames del boton deslizador: 0 reposo, 1 hover, 2 pulsado
    private BufferedImage[] imgs;

    //imagen de la barra sobre la que se desliza el boton
    private BufferedImage slider;

    //indice del frame activo del boton
    private int indice = 0;

    //flags que indican si el raton esta encima o pulsando el boton
    private boolean mouseOver, mousePressed;

    //posicion actual del boton y limites horizontales dentro del slider
    private int botonX, minX, maxX;
    private float floatValue = 0f;

    //coloca el slider y el boton en su posicion inicial y calcula los limites de arrastre
    public VolumeButton(int x, int y, int widht, int height) {
        super(x + widht / 2, y, VOLUME_WIDTH, height);
        //centra el rectangulo de colision sobre el boton
        bordes.x -= VOLUME_WIDTH / 2;
        botonX = x + widht / 2;
        this.x = x;
        this.widht = widht;
        //el boton no puede salir de los extremos del slider
        minX = x;
        maxX = x + widht;
        loadImages();
    }

    //carga los frames del boton y la imagen del slider desde el spritesheet de volumen
    private void loadImages() {
        BufferedImage temp = CargaSprites.GetSpriteAtlas(CargaSprites.BOTON_NIVEL_VOLUMEN);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0,
                    VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        }
        //la barra del slider ocupa la cuarta columna del spritesheet
        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0,
                SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    //selecciona el frame correcto del boton segun el estado del raton
    public void update() {
        indice = 0;
        if (mouseOver) indice = 1;
        if (mousePressed) indice = 2;
    }

    //dibuja primero la barra del slider y luego el boton centrado en su posicion actual
    public void draw(Graphics g) {
        g.drawImage(slider, x, y, widht, height, null);
        g.drawImage(imgs[indice], botonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    //mueve el boton a la posicion del raton limitandolo a los extremos del slider
    public void changeX(int x) {
        if (x < minX) {
            botonX = minX;
        } else if (x > maxX) {
            botonX = maxX;
        } else {
            botonX = x;
        }
        updateFloatValue();
        //sincroniza el rectangulo de colision con la nueva posicion del boton
        bordes.x = botonX - VOLUME_WIDTH / 2;
    }

    private void updateFloatValue() {
        float rango = maxX - minX;
        float valor = botonX - minX;
        floatValue = valor/rango;
    }
    public Float getFloatValue() {
        return floatValue;
    }

    //resetea los flags de hover y pulsado al soltar el raton
    public void resetBools() {
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