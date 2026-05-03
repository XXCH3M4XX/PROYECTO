package ui;

import utils.LoadSave;
import static utils.Constantes.UI.BotonesPausa.*;

import java.awt.*;
import java.awt.image.BufferedImage;

//establece todos los parametros para los botones de sonido del menu de pausa
public class BotonesDeSonido extends BotonesPausa {

    //matriz de frames indexada por [fila silenciado/activo][columna estado hover/pressed]
    private BufferedImage[][] imagenesSonido;
    private boolean mouseOver, mousePressed;

    //indica si el sonido esta silenciado, determina la fila del sprite a usar
    private boolean muted;

    //indices de fila y columna del frame actual en el spritesheet
    private int rowIndex, columnIndex;

    public BotonesDeSonido(int x, int y, int widht, int height) {
        super(x, y, widht, height);
        cargarImagenesSonido();
    }

    //resetea los flags de interaccion al salir del boton
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    //carga el spritesheet y recorta cada frame en su posicion correspondiente
    private void cargarImagenesSonido() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BOTONES_VOLUMEN);
        imagenesSonido = new BufferedImage[2][3];
        for (int i = 0; i < imagenesSonido.length; i++) {
            for (int j = 0; j < imagenesSonido[i].length; j++) {
                imagenesSonido[i][j] = temp.getSubimage(
                        j * TAMAÑO_SONIDO_PORDEFECTO,
                        i * TAMAÑO_SONIDO_PORDEFECTO,
                        TAMAÑO_SONIDO_PORDEFECTO,
                        TAMAÑO_SONIDO_PORDEFECTO);
            }
        }
    }

    //decide que frame dibujar segun si esta silenciado y el estado del raton
    public void update() {
        //fila 0 silenciado, fila 1 activo
        if (muted) {
            rowIndex = 0;
        } else {
            rowIndex = 1;
        }

        //pressed tiene prioridad sobre hover, hover sobre reposo
        if (mousePressed) {
            columnIndex = 2;
        } else if (mouseOver) {
            columnIndex = 1;
        } else {
            columnIndex = 0;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(imagenesSonido[rowIndex][columnIndex], x, y, widht, height, null);
    }

    public boolean isMuted() { return muted; }
    public void setMuted(boolean muted) { this.muted = muted; }
    public boolean isMousePressed() { return mousePressed; }
    public void setMousePressed(boolean mousePressed) { this.mousePressed = mousePressed; }
    public boolean isMouseOver() { return mouseOver; }
    public void setMouseOver(boolean mouseOver) { this.mouseOver = mouseOver; }
}