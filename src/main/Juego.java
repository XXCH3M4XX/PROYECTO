package main;

import entidades.Jugador;
import niveles.AjusteNivel;

import java.awt.*;

public class Juego implements Runnable {

    private PantallaJuego pantallaJuego;
    private PanelJuego panelJuego;
    private Thread hiloJuego;
    private final static int FPS_OBJETIVO = 120;
    private final int UPS_ESTABLECER = 200;

    private Jugador jugador;
    private AjusteNivel ajusteNivel;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float ESCALA = 1.5f;
    public final static float ESCALA_JUGADOR = 2.0f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * ESCALA);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Juego() {
        initClasses();
        panelJuego = new PanelJuego(this);
        pantallaJuego = new PantallaJuego(panelJuego);
        //esto va a leer el input del teclado; es decir, lee las teclas que pulsamos
        panelJuego.requestFocusInWindow();
        empezarBucle();
    }

    private void initClasses(){
        ajusteNivel = new AjusteNivel(this);
        jugador = new Jugador(200, 200, (int)(64 * ESCALA_JUGADOR), (int)(40 * ESCALA_JUGADOR));
        jugador.cargarDatosNivel(ajusteNivel.getNivelActual().getDatosNivel());
    }

    //metodo que empieza el bucle infinito del juego
    private void empezarBucle() {
        hiloJuego = new Thread(this);
        hiloJuego.start();
    }

    //con este metodo, podemos actualizar lo que nosotros queramos(jugador, escenario, etc...)
    public void update(){
        jugador.update();
        ajusteNivel.update();
    }

    public void render(Graphics g){
        ajusteNivel.draw(g);
        jugador.render(g);
    }

    @Override
    public void run() {

        //un segundo es mil millones de nanosegundos
        double tiempoFrame = 1000000000.0/FPS_OBJETIVO;
        double tiempoRecarga = 1000000000.0/UPS_ESTABLECER;

        long ultimoFrame = System.nanoTime();
        long ahora;

        long tiempoAnterior = System.nanoTime();

        int frames = 0;
        int actualizacion = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        //bucle del juego
        //llama constantemente al metodo repaint para crear el juego
        while(true) {
            //marca el ahora en nanosegundos
            ahora = System.nanoTime();
            long currentTime = System.nanoTime();

            deltaU += (currentTime - tiempoAnterior) / tiempoRecarga;
            deltaF += (currentTime - tiempoAnterior) / tiempoFrame;
            tiempoAnterior = currentTime;

            if (deltaU >= 1){
                update();
                actualizacion++;
                deltaU--;
            }

            if (deltaF >= 1){
                frames++;
                deltaF--;
            }

            //si ya ha pasado el tiempo que dura un frame
            //lo repinta (nuevo frame)
            if(ahora - ultimoFrame >= tiempoFrame) {
                panelJuego.repaint();
                ultimoFrame = ahora;
                frames++;

            }

            //cuando llega a 120 frames lo pinta y vuelve a 0
            //contador de fps funcional
            if(System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + "| UPS: " + actualizacion);
                frames = 0;
                actualizacion = 0;
            }
        }
    }

    //lo nuevo que he añadido, del tutorial 6, son variables, que utilizamos para controlar la perdida de frames y recargas que tiene la aplicacion
    public Jugador getJugador(){
        return jugador;
    }

    public void windowFocusLost(){
        jugador.resetDirBooleans();
    }

}
