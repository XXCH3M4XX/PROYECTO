package gamestates;

import main.Juego;
import utils.GestorRecords;
import utils.RegistroPartida;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utils.LoadSave;

//pantalla que muestra el top 3 de mejores partidas guardadas en disco
public class Stats extends State implements Statemethods {

    private BufferedImage fondoPantalla;

    //indice del record seleccionado actualmente, -1 si no hay ninguno
    private int seleccionado = 0;

    public Stats(Juego juego) {
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
        g.drawString("MEJORES PARTIDAS", (int)(Juego.GAME_WIDTH / 2 - 150 * Juego.ESCALA), (int)(60 * Juego.ESCALA));

        //cabecera en naranja
        g.setFont(new Font("Arial", Font.BOLD, (int)(12 * Juego.ESCALA)));
        g.setColor(new Color(255, 140, 0));
        g.drawString("#", (int)(80 * Juego.ESCALA), (int)(120 * Juego.ESCALA));
        g.drawString("NOMBRE", (int)(120 * Juego.ESCALA), (int)(120 * Juego.ESCALA));
        g.drawString("TIEMPO", (int)(280 * Juego.ESCALA), (int)(120 * Juego.ESCALA));
        g.drawString("MUERTES", (int)(380 * Juego.ESCALA), (int)(120 * Juego.ESCALA));
        g.drawString("DAÑO", (int)(480 * Juego.ESCALA), (int)(120 * Juego.ESCALA));
        g.drawString("POCIONES", (int)(560 * Juego.ESCALA), (int)(120 * Juego.ESCALA));

        ArrayList<RegistroPartida> records = GestorRecords.cargarRecords();
        g.setFont(new Font("Arial", Font.PLAIN, (int)(12 * Juego.ESCALA)));

        if (records.isEmpty()) {
            g.setColor(Color.WHITE);
            g.drawString("No hay partidas guardadas todavia", (int)(Juego.GAME_WIDTH / 2 - 150 * Juego.ESCALA), (int)(200 * Juego.ESCALA));
        } else {
            //limita la seleccion al numero de records disponibles
            if (seleccionado >= records.size()) seleccionado = records.size() - 1;

            for (int i = 0; i < records.size(); i++) {
                RegistroPartida r = records.get(i);
                int y = (int)((160 + i * 50) * Juego.ESCALA);

                //dibuja el fondo de seleccion en el record seleccionado
                if (i == seleccionado) {
                    g.setColor(new Color(255, 255, 255, 50));
                    g.fillRect((int)(70 * Juego.ESCALA), y - (int)(20 * Juego.ESCALA),
                            (int)(580 * Juego.ESCALA), (int)(30 * Juego.ESCALA));
                }

                if (i == 0) g.setColor(new Color(255, 215, 0));
                else if (i == 1) g.setColor(new Color(192, 192, 192));
                else g.setColor(new Color(205, 127, 50));

                g.drawString((i + 1) + ".", (int)(80 * Juego.ESCALA), y);

                //nombre en azulito
                g.setColor(Color.CYAN);
                g.drawString(r.getNombre(), (int)(120 * Juego.ESCALA), y);

                //resto de datos en blanco
                g.setColor(Color.WHITE);
                g.drawString(r.getTiempoFormateado(), (int)(280 * Juego.ESCALA), y);
                g.drawString(String.valueOf(r.getMuertes()), (int)(380 * Juego.ESCALA), y);
                g.drawString(String.valueOf(r.getDañoRecibido()), (int)(480 * Juego.ESCALA), y);
                g.drawString(String.valueOf(r.getPorcionesRecogidas()), (int)(560 * Juego.ESCALA), y);
            }
        }

        //instrucciones
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, (int)(10 * Juego.ESCALA)));
        g.drawString(" | ↑↓ para seleccionar |  SUPR para borrar | R para borrar todo |  ESCAPE para volver |", (int)(80 * Juego.ESCALA), (int)(350 * Juego.ESCALA));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        ArrayList<RegistroPartida> records = GestorRecords.cargarRecords();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                //vuelve al menu principal
                Gamestate.state = Gamestate.MENU;
                break;
            case KeyEvent.VK_UP:
                //sube la seleccion
                if (seleccionado > 0) seleccionado--;
                break;
            case KeyEvent.VK_DOWN:
                //baja la seleccion
                if (seleccionado < records.size() - 1) seleccionado++;
                break;
            case KeyEvent.VK_DELETE:
                //borra el record seleccionado
                if (!records.isEmpty()) {
                    GestorRecords.borrarRecord(seleccionado);
                    if (seleccionado >= records.size() - 1) seleccionado = Math.max(0, records.size() - 2);
                }
                break;
            case KeyEvent.VK_R:
                //borra todos los records de una vez
                GestorRecords.borrarTodosLosRecords();
                seleccionado = 0;
                break;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}