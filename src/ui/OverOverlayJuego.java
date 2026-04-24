package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Juego;

import java.awt.*;
import java.awt.event.KeyEvent;

public class OverOverlayJuego {
    private Playing playing;
    public OverOverlayJuego(Playing playing){
        this.playing = playing;
    }
    public void draw(Graphics g){
        g.setColor(new Color(0, 0, 200));
        g.fillRect(0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT);

        g.setColor(Color.white);
        g.drawString("Game Over", Juego.GAME_WIDTH / 2, 150);
        g.drawString("Press esc to enter Main Menu!", Juego.GAME_WIDTH / 2, 300);
    }
    public void teclaPresionada(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }
}
