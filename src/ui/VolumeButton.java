package ui;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utils.Constantes.UI.BotonesVolumen.*;

public class VolumeButton extends BotonesPausa{
    private BufferedImage[]imgs;
    private BufferedImage slider;
    private int indice = 0;
    private boolean mouseOver, mousePressed;
    private int botonX, minX, maxX;

    public VolumeButton(int x, int y, int widht, int height) {
        super(x + widht/2, y, VOLUME_WIDTH, height);
        bordes.x -= VOLUME_WIDTH / 2;
        botonX = x + widht / 2;
        this.x = x;
        this.widht = widht;
        minX = x;
        maxX = x + widht;
        loadImages();
    }
    private void loadImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BOTON_NIVEL_VOLUMEN);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH,0,VOLUME_DEFAULT_WIDTH,VOLUME_DEFAULT_HEIGHT);
        }
        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0 ,SLIDER_DEFAULT_WIDTH ,VOLUME_DEFAULT_HEIGHT);

        }

    public void update(){
        indice = 0;
        if(mouseOver){
            indice = 1;
        }
        if (mousePressed){
            indice = 2;
        }
    }
    public void draw(Graphics g){
        g.drawImage(slider, x, y, widht, height, null);
        g.drawImage(imgs[indice], botonX - VOLUME_WIDTH /2, y, VOLUME_WIDTH, height, null);
    }

    public void changeX(int x){
        if(x < minX){
            botonX = minX;
        } else if (x > maxX) {
            botonX = maxX;
        }else {
            botonX = x;
        }
        bordes.x = botonX - VOLUME_WIDTH / 2;
    }

    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}
