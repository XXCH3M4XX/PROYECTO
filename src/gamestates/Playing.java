package gamestates;

import entidades.Jugador;
import main.Juego;
import niveles.AjusteNivel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static main.Juego.ESCALA_JUGADOR;
import static main.Juego.TILES_SIZE;

public class Playing extends State implements Statemethods{
    private Jugador jugador;
    private AjusteNivel ajusteNivel;
    private boolean espacioAnterior = false;

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
    }

    //devuelve el jugador para que otros sistemas puedan acceder a el
    public Jugador getJugador(){
        return jugador;
    }

    //cuando la ventana pierde el foco detiene el movimiento para evitar que se quede andando solo
    public void windowFocusLost(){
        jugador.resetDirBooleans();
    }

    @Override
    public void update() {
        ajusteNivel.update();
        jugador.update();
    }

    @Override
    public void draw(Graphics g) {
        ajusteNivel.draw(g);
        jugador.render(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            jugador.setAtaque(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

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
            case KeyEvent.VK_BACK_SPACE:
                Gamestate.state = Gamestate.MENU;
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
}
