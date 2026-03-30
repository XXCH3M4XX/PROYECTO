package main;

public class Juego implements Runnable {

    private PantallaJuego pantallaJuego;
    private PanelJuego panelJuego;
    private Thread hiloJuego;
    private final int FPS_OBJETIVO = 120;

    public Juego() {
        panelJuego = new PanelJuego();
        pantallaJuego = new PantallaJuego(panelJuego);
        //esto va a leer el input del teclado; es decir, lee las teclas que pulsamos
        panelJuego.requestFocus();
        empezarBucle();


    }
    private void empezarBucle() {
        hiloJuego = new Thread(this);
        hiloJuego.start();
    }

    @Override
    public void run() {

        //un segundo es mil millones de nanosegundos
        double tiempoFrame = 1000000000.0/FPS_OBJETIVO;

        long ultimoFrame = System.nanoTime();
        long ahora;
        int frames = 0;
        long lastCheck = System.currentTimeMillis();

        //bucle del juego
        //llama constantemente al metodo repaint para crear el juego
        while(true) {
            //marca el ahora en nanosegundos
            ahora = System.nanoTime();
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
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }
    //nigger
}
