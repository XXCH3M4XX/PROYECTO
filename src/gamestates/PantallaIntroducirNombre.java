package gamestates;

import audio.AudioPlayer;
import main.Juego;
import utils.GestorRecords;
import utils.RegistroPartida;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

//pantalla que aparece al completar el juego para introducir el nombre del jugador
public class PantallaIntroducirNombre extends State implements Statemethods {

    //imagen de fondo del panel
    private BufferedImage fondo;
    private int fondoX, fondoY, fondoW, fondoH;
    private BufferedImage fondoPantalla;

    //nombre que va escribiendo el jugador
    private String nombreActual = "";

    //contador para el cursor parpadeante
    private int tickCursor = 0;
    private boolean mostrarCursor = true;

    public PantallaIntroducirNombre(Juego juego) {
        super(juego);
        cargarFondo();
    }

    private void cargarFondo() {
        fondoPantalla = LoadSave.GetSpriteAtlas(LoadSave.FONDO_PANTALLA);
////        fondo = LoadSave.GetSpriteAtlas(LoadSave.FONDO_MENU);
//        fondoW = (int)(fondo.getWidth() * Juego.ESCALA);
//        fondoH = (int)(fondo.getHeight() * Juego.ESCALA);
//        fondoX = Juego.GAME_WIDTH / 2 - fondoW / 2;
//        fondoY = Juego.GAME_HEIGHT / 2 - fondoH / 2;
    }

    //resetea el nombre al entrar en esta pantalla
    public void reset() {
        nombreActual = "";
        tickCursor = 0;
        mostrarCursor = true;
    }

    @Override
    public void update() {
        //parpadeo del cursor cada 30 ticks
        tickCursor++;
        if (tickCursor >= 30) {
            tickCursor = 0;
            mostrarCursor = !mostrarCursor;
        }
    }

    @Override
    public void draw(Graphics g) {
        //dibuja el fondo de pantalla completa
        g.drawImage(fondoPantalla, 0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT, null);

        //overlay semitransparente para que el texto se lea mejor
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT);

        //panel central comentado hasta que se haga el png
//  g.drawImage(panelCentral, fondoX, fondoY, fondoW, fondoH, null);

        //variables de posicion y tiempo
        int centroX = Juego.GAME_WIDTH / 2 - (int)(100 * Juego.ESCALA);
        long tiempo = juego.getPlaying().getTiempoSegundos();
        long minutos = tiempo / 60;
        long segundos = tiempo % 60;

        //fuente base
        g.setFont(new Font("Arial", Font.BOLD, (int)(14 * Juego.ESCALA)));

        //titulo en amarillo
        g.setColor(new Color(255, 215, 0));
        g.drawString("JUEGO COMPLETADO", centroX, (int)(100 * Juego.ESCALA));

        //estadisticas en blanco
        g.setColor(Color.WHITE);
        g.drawString("Tiempo: " + minutos + ":" + String.format("%02d", segundos), centroX, (int)(150 * Juego.ESCALA));
        g.drawString("Muertes: " + juego.getPlaying().getMuertes(), centroX, (int)(185 * Juego.ESCALA));
        g.drawString("Daño recibido: " + juego.getPlaying().getDañoRecibido(), centroX, (int)(220 * Juego.ESCALA));
        g.drawString("Pociones: " + juego.getPlaying().getPorcionesRecogidas(), centroX, (int)(255 * Juego.ESCALA));

        //texto de introducir nombre en verde
        g.setColor(new Color(100, 220, 100));
        g.drawString("Introduce tu nombre:", centroX, (int)(305 * Juego.ESCALA));

        //nombre que escribe el jugador en cyan
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, (int)(14 * Juego.ESCALA)));
        String texto = nombreActual + (mostrarCursor ? "_" : " ");
        g.drawString(texto, centroX, (int)(340 * Juego.ESCALA));

        //instruccion en gris
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.PLAIN, (int)(10 * Juego.ESCALA)));
        g.drawString("ENTER para guardar   |   ESCAPE para saltar", centroX, (int)(375 * Juego.ESCALA));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                //salta el guardado y vuelve al menu sin guardar la partida
                //llamamos a resetearPartidaCompleta para quw la barra de poder SIEMPRE salga a 0 en una partida nueva
                juego.getPlaying().resetearPartidaCompleta();
                Gamestate.state = Gamestate.MENU;
                juego.getAudioPlayer().playCancion(AudioPlayer.menu);
                break;
            case KeyEvent.VK_ENTER:
                //solo guarda si hay nombre introducido
                if (!nombreActual.isEmpty()) {
                    guardarYSalir();
                }
                break;
            case KeyEvent.VK_BACK_SPACE:
                //borra la ultima letra
                if (nombreActual.length() > 0) {
                    nombreActual = nombreActual.substring(0, nombreActual.length() - 1);
                }
                break;
            default:
                //solo acepta letras, numeros y espacios con un maximo de 10 caracteres
                if (nombreActual.length() < 10 && Character.isLetterOrDigit(e.getKeyChar())) {
                    nombreActual += e.getKeyChar();
                }
                break;
        }
    }

    //guarda el record y vuelve al menu
    private void guardarYSalir() {
        RegistroPartida registro = juego.getPlaying().getEstadisticasActuales(nombreActual);
        GestorRecords.añadirRecord(registro);
        Gamestate.state = Gamestate.MENU;
        juego.getAudioPlayer().playCancion(AudioPlayer.menu);
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}