package ui;

import utils.LoadSave;
import static utils.Constantes.UI.BotonesPausa.*;

import java.awt.*;
import java.awt.image.BufferedImage;

//esta clase es para establecer todos los parametros para los botones de sonido
public class BotonesDeSonido extends BotonesPausa{
    private BufferedImage[][] imagenesSonido;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex, columnIndex;
    public BotonesDeSonido(int x, int y, int widht, int height) {
        super(x, y, widht, height);

        cargarImagenesSonido();
    }

    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }

    private void cargarImagenesSonido() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BOTONES_VOLUMEN);
        imagenesSonido = new BufferedImage[2][3];
        for (int i = 0; i < imagenesSonido.length; i++){
            for (int j = 0; j<imagenesSonido[i].length; j++){
                imagenesSonido[i][j] = temp.getSubimage(j* TAMAÑO_SONIDO_PORDEFECTO, i*TAMAÑO_SONIDO_PORDEFECTO , TAMAÑO_SONIDO_PORDEFECTO, TAMAÑO_SONIDO_PORDEFECTO);
            }
        }
    }
    public void update(){
        if (muted){
            rowIndex = 0;
        } else {
            rowIndex = 1;
        }

        if (mousePressed){
            columnIndex = 2;
        } else if (mouseOver){
            columnIndex = 1;
        } else {
            columnIndex = 0;
        }
    }
    public void draw(Graphics g){
        g.drawImage(imagenesSonido[rowIndex][columnIndex], x, y, widht, height, null);
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }
}
