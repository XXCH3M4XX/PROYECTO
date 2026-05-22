package gamestates;

import main.Juego;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

//pantalla que muestra los controles del juego
public class Controles extends State implements Statemethods {

    //fondo de pantalla completa reutilizado del menu principal
    private BufferedImage fondoPantalla;

    public Controles(Juego juego) {
        super(juego);
        fondoPantalla = LoadSave.GetSpriteAtlas(LoadSave.FONDO_PANTALLA);
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics g) {
        //dibuja el fondo de pantalla completa
        g.drawImage(fondoPantalla, 0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT, null);

        //overlay semitransparente para que el texto se lea mejor
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT);

        //titulo en amarillo
        g.setColor(new Color(255, 215, 0));
        g.setFont(new Font("Arial", Font.BOLD, (int)(20 * Juego.ESCALA)));
        g.drawString("CONTROLES", (int)(Juego.GAME_WIDTH / 2 - 80 * Juego.ESCALA), (int)(60 * Juego.ESCALA));

        //controles en blanco
        g.setFont(new Font("Arial", Font.PLAIN, (int)(14 * Juego.ESCALA)));
        int x = (int)(80 * Juego.ESCALA);
        int separacion = (int)(50 * Juego.ESCALA);
        int yInicio = (int)(120 * Juego.ESCALA);

        //tecla en cyan y descripcion en blanco
        dibujarControl(g, "A", "Moverse a la izquierda", x, yInicio);
        dibujarControl(g, "D", "Moverse a la derecha", x, yInicio + separacion);
        dibujarControl(g, "ESPACIO", "Saltar", x, yInicio + separacion * 2);
        dibujarControl(g, "CLICK IZQUIERDO", "Ataque normal, pulsar botones...", x, yInicio + separacion * 3);
        dibujarControl(g, "CLICK DERECHO", "Super ataque (barra llena)", x, yInicio + separacion * 4);
        dibujarControl(g, "ESCAPE", "Pausa", x, yInicio + separacion * 5);

        //instruccion en gris
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, (int)(10 * Juego.ESCALA)));
        g.drawString("Pulsa ESCAPE para volver", x, (int)(400 * Juego.ESCALA));
    }

    //dibuja la tecla en cyan y la descripcion en blanco en la misma linea
    private void dibujarControl(Graphics g, String tecla, String descripcion, int x, int y) {
        g.setColor(Color.CYAN);
        g.drawString("[" + tecla + "]", x, y);
        g.setColor(Color.WHITE);
        g.drawString(descripcion, x + (int)(150 * Juego.ESCALA), y);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //vuelve al menu al pulsar escape
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Gamestate.state = Gamestate.MENU;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}