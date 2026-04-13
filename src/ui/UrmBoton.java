package ui;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utils.Constantes.UI.URMBotones.*;
//clase para los botones interactivos, volver, reanudar, home
public class UrmBoton extends BotonesPausa{
    private BufferedImage[] imgs;
    private int rowIndex, indice;
    private boolean mouseOver, mousePressed;

    public UrmBoton(int x, int y, int widht, int height, int rowIndex) {
        super(x, y, widht, height);
        this.rowIndex = rowIndex;
        loadImages();
    }

    private void loadImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BOTONES_URM);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
        }
    }


    public void update(){
        indice = 0;
        if (mouseOver){
            indice = 1;
        }
        if (mousePressed){
            indice = 2;
        }
    }
    public void draw(Graphics g){
        g.drawImage(imgs[indice], x, y, URM_SIZE, URM_SIZE, null);
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

