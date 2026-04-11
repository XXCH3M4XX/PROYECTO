package gamestates;

import main.Juego;
import ui.BotonMenu;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

//esta clase es para el menu de pausa
public class Menu extends State implements Statemethods{
    private BufferedImage fondo;
    private int menuX, menuY, menuWidth, menuHeight;
    private BotonMenu[] botones = new BotonMenu[3];
    private BufferedImage fondoPantalla;


    public Menu(Juego juego) {
        super(juego);
        loadButtons();
        loadFondo();
    }


    private void loadFondo() {
        //carga el panel central del menu
        fondo = LoadSave.GetSpriteAtlas(LoadSave.FONDO_MENU);
        menuWidth = (int)(fondo.getWidth() * Juego.ESCALA);
        menuHeight = (int)(fondo.getHeight() * Juego.ESCALA);
        menuX = Juego.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int)(45 * Juego.ESCALA);

        //carga el fondo de pantalla completa
        fondoPantalla = LoadSave.GetSpriteAtlas(LoadSave.FONDO_PANTALLA);
    }

    private void loadButtons() {
        botones[0] = new BotonMenu(Juego.GAME_WIDTH / 2, (int)(150 * Juego.ESCALA), 0, Gamestate.PLAYING);
        botones[1] = new BotonMenu(Juego.GAME_WIDTH / 2, (int)(220 * Juego.ESCALA), 1, Gamestate.OPTIONS);
        botones[2] = new BotonMenu(Juego.GAME_WIDTH / 2, (int)(290 * Juego.ESCALA), 2, Gamestate.QUIT);
    }

    @Override
    public void update() {
        for (BotonMenu mb : botones){
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        //primero el fondo de pantalla completa
        g.drawImage(fondoPantalla, 0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT, null);

        //encima el panel del menu
        g.drawImage(fondo, menuX, menuY, menuWidth, menuHeight, null);

        //encima los botones
        for (BotonMenu mb : botones){
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (BotonMenu mb : botones){
            if (isIn(e, mb)){
                mb.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (BotonMenu mb : botones){
            if(isIn(e, mb)){
                if (mb.isMousePressed()){
                    mb.applyGameState();
                    break;
                }
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for (BotonMenu mb : botones){
            mb.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (BotonMenu mb : botones){
            mb.setMouseOver(false);
        }
        for (BotonMenu mb : botones){
            if(isIn(e,mb)){
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            Gamestate.state = Gamestate.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
