package main;

import entidades.Jugador;
import niveles.AjusteNivel;

import java.awt.*;

//nucleo del juego, gestiona el bucle principal y coordina todos los sistemas
public class Juego implements Runnable {

    private PantallaJuego pantallaJuego;
    private PanelJuego panelJuego;
    private Thread hiloJuego;

    //limite de fotogramas por segundo que se van a renderizar
    private final static int FPS_OBJETIVO = 120;

    //limite de actualizaciones de logica por segundo, mayor que fps para mayor precision fisica
    private final int UPS_ESTABLECER = 200;

    private Jugador jugador;
    private AjusteNivel ajusteNivel;

    //tamaño original de cada tile en pixeles antes de aplicar la escala
    public final static int TILES_DEFAULT_SIZE = 32;

    //factor de escala global que se aplica a tiles, hitboxes y fisica
    public final static float ESCALA = 1.5f;

    //factor de escala exclusivo para el sprite del jugador
    public final static float ESCALA_JUGADOR = 2.0f;

    //numero de tiles que caben horizontalmente y verticalmente en pantalla
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;

    //tamaño final de cada tile en pixeles ya escalado
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * ESCALA);

    //resolucion total de la ventana en pixeles
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    //inicializa todos los sistemas y arranca el bucle
    public Juego() {
        initClasses();
        panelJuego = new PanelJuego(this);
        pantallaJuego = new PantallaJuego(panelJuego);
        //esto va a leer el input del teclado; es decir, lee las teclas que pulsamos
        panelJuego.requestFocusInWindow();
        empezarBucle();
    }

    //crea el nivel y coloca al jugador encima del tile de suelo correspondiente
    private void initClasses() {
        ajusteNivel = new AjusteNivel(this);

        int[][] datosNivel = ajusteNivel.getNivelActual().getDatosNivel();

        //fila y columna del tile sobre el que aparecera el jugador al iniciar
        int filaSuelo = 8;
        int colInicial = 3;

        //convertimos la posicion en tiles a posicion en pixeles
        int xInicial = colInicial * TILES_SIZE;

        //restamos la altura de la hitbox para que los pies queden sobre el tile y no dentro
        int yInicial = filaSuelo * TILES_SIZE - Jugador.HITBOX_H;

        jugador = new Jugador(xInicial, yInicial, (int)(64 * ESCALA_JUGADOR), (int)(40 * ESCALA_JUGADOR));
        jugador.cargarDatosNivel(datosNivel);
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

    //dibuja el nivel primero y el jugador encima para que quede en primer plano
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

        //acumuladores de tiempo para desacoplar logica y renderizado
        double deltaU = 0;
        double deltaF = 0;

        //bucle del juego
        //llama constantemente al metodo repaint para crear el juego
        while(true) {
            //marca el ahora en nanosegundos
            ahora = System.nanoTime();
            long currentTime = System.nanoTime();

            //acumulamos el tiempo transcurrido normalizado respecto a cada intervalo
            deltaU += (currentTime - tiempoAnterior) / tiempoRecarga;
            deltaF += (currentTime - tiempoAnterior) / tiempoFrame;
            tiempoAnterior = currentTime;

            //ejecutamos tantas actualizaciones de logica como intervalos hayan pasado
            if (deltaU >= 1){
                update();
                actualizacion++;
                deltaU--;
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

    //devuelve el jugador para que otros sistemas puedan acceder a el
    public Jugador getJugador(){
        return jugador;
    }

    //cuando la ventana pierde el foco detiene el movimiento para evitar que se quede andando solo
    public void windowFocusLost(){
        jugador.resetDirBooleans();
    }
}