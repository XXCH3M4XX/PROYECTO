package ui;

import gamestates.Gamestate;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constantes.UI.botones.*;

//esta clase es para los botones del menu
public class BotonMenu {
    private int xPos, yPos, rowIndex, indice;
    private int xOffsetCenter = B_WIDTH / 2;
    private Gamestate state;
    //este array es para guardar los botones del menu
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bordes;

    public BotonMenu(int xPos, int yPos, int rowIndex, Gamestate state){
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        cargarImagenes();
        initBordes();
    }

    private void initBordes() {
        bordes = new Rectangle(xPos-xOffsetCenter,yPos, B_WIDTH, B_HEIGHT);
    }

    private void cargarImagenes(){
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BOTONES_MENU);
        for (int i = 0; i< imgs.length; i++){
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT , B_HEIGHT_DEFAULT);
        }
    }

    public void draw(Graphics g){
        g.drawImage(imgs[indice], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    public void update(){
        indice = 0;
        if(mouseOver){
            indice = 1;
        }
        if(mousePressed){
            indice = 2;
        }
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

    public void applyGameState(){
        Gamestate.state = state;
    }

    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }

    public Rectangle getBordes(){
        return bordes;
    }

}
