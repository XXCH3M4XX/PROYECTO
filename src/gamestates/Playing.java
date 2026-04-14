package gamestates;

import entidades.Jugador;
import main.Juego;
import niveles.AjusteNivel;
import ui.PausaOverlay;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static main.Juego.ESCALA_JUGADOR;
import static main.Juego.TILES_SIZE;

public class Playing extends State implements Statemethods{
    private Jugador jugador;
    private AjusteNivel ajusteNivel;
    private boolean espacioAnterior = false;
    private boolean pausado = false;
    private PausaOverlay pausaOverlay;

    //un offset es la diferencia de distancia entre un punto de referencia y lo que tu quieras
    private int OffsetXNivel;
    private int bordeIzquierdo = (int)(0.2 * Juego.GAME_WIDTH);
    private int bordeDerecho = (int) (0.8 * Juego.GAME_WIDTH);
    private int tilesAnchoNivel = LoadSave.conseguirDatosNivel()[0].length;
    private int tilesMaximoOffset = tilesAnchoNivel - Juego.TILES_IN_WIDTH;
    private int tilesMaximosOffsetX= tilesMaximoOffset * TILES_SIZE;

    public Playing(Juego juego) {
        super(juego);
        initClasses();
    }

    //crea el nivel y coloca al jugador encima del tile de suelo correspondiente
    private void initClasses() {
        ajusteNivel = new AjusteNivel(juego);

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

        pausaOverlay = new PausaOverlay(this);
    }

    //devuelve el jugador para que otros sistemas puedan acceder a el
    public Jugador getJugador(){
        return jugador;
    }

    //cuando la ventana pierde el foco detiene el movimiento para evitar que se quede andando solo
    public void windowFocusLost(){
        jugador.resetDirBooleans();
    }


    public void mouseDragged(MouseEvent e){
        if(pausado){
            pausaOverlay.mouseDragged(e);
        }
    }

    @Override
    public void update() {
        if(!pausado){
            ajusteNivel.update();
            jugador.update();
            comprobarBorde();
        }else {
            pausaOverlay.actualizar();
        }
    }

    private void comprobarBorde() {
        int posicionX = (int) jugador.getHitbox().x;
        int diferencia = posicionX - OffsetXNivel;

        if(diferencia > bordeDerecho) {
            OffsetXNivel += diferencia - bordeDerecho;
        } else if(diferencia < bordeIzquierdo) {
            OffsetXNivel += diferencia - bordeIzquierdo;
        }
        if(OffsetXNivel > tilesMaximosOffsetX) {
            OffsetXNivel = tilesMaximosOffsetX;
        } else if(OffsetXNivel < 0) {
            OffsetXNivel = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        ajusteNivel.draw(g, OffsetXNivel);
        jugador.render(g, OffsetXNivel);
        if (pausado){
            g.setColor(new Color(0,0,0,100));
            g.fillRect(0,0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT);
            pausaOverlay.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            jugador.setAtaque(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(pausado){
            pausaOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(pausado){
            pausaOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(pausado){
            pausaOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_A:
                jugador.setIzquierda(true);
                break;
            case KeyEvent.VK_D:
                jugador.setDerecha(true);
                break;
            case KeyEvent.VK_SPACE:
                if (!espacioAnterior) {
                    jugador.setSalto(true);
                    espacioAnterior = true;
                }
                break;
            case KeyEvent.VK_ESCAPE:
                pausado = !pausado;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_A:
                jugador.setIzquierda(false);
                break;
            case KeyEvent.VK_D:
                jugador.setDerecha(false);
                break;
            case KeyEvent.VK_SPACE:
                espacioAnterior = false;
                jugador.setSalto(false);
                break;
        }
    }

    public void unpauseJuego(){
        pausado = false;
    }

}
