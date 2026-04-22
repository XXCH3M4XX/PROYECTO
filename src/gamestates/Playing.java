package gamestates;

import entidades.AjusteEnemigo;
import entidades.Jugador;
import main.Juego;
import niveles.AjusteNivel;
import ui.PausaOverlay;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.Juego.ESCALA_JUGADOR;
import static main.Juego.TILES_SIZE;
import static utils.Constantes.entorno.*;

public class Playing extends State implements Statemethods {
    private Jugador jugador;
    private AjusteNivel ajusteNivel;
    private AjusteEnemigo ajusteEnemigo;

    //evita que el salto se repita si se mantiene pulsada la tecla
    private boolean espacioAnterior = false;

    //indica si el juego esta en pausa
    private boolean pausado = false;
    private PausaOverlay pausaOverlay;

    //desplazamiento horizontal actual del nivel en pixeles
    private int OffsetXNivel;

    //limites en pixeles que determinan cuando la camara empieza a desplazarse
    private int bordeIzquierdo = (int)(0.2 * Juego.GAME_WIDTH);
    private int bordeDerecho = (int)(0.8 * Juego.GAME_WIDTH);

    //ancho total del nivel en tiles y offset maximo que puede alcanzar la camara
    private int tilesAnchoNivel = LoadSave.conseguirDatosNivel()[0].length;
    private int tilesMaximoOffset = tilesAnchoNivel - Juego.TILES_IN_WIDTH;
    private int tilesMaximosOffsetX = tilesMaximoOffset * TILES_SIZE;

    //imagenes del fondo, montañas y nubes
    private BufferedImage imagenFondo;
    private BufferedImage imagenMontañas;
    private BufferedImage nube;

    //posiciones Y aleatorias de cada nube para distribuirlas verticalmente
    private int[] nubesPosicion;
    private Random random = new Random();

    public Playing(Juego juego) {
        super(juego);
        initClasses();

        //carga las imagenes del entorno
        imagenFondo = LoadSave.GetSpriteAtlas(LoadSave.FONDO_JUGANDO);
        imagenMontañas = LoadSave.GetSpriteAtlas(LoadSave.MONTAÑAS_YBOSQUES);
        nube = LoadSave.GetSpriteAtlas(LoadSave.nubes);

        //asigna una altura aleatoria a cada nube dentro de un rango
        nubesPosicion = new int[8];
        for (int i = 0; i < nubesPosicion.length; i++) {
            nubesPosicion[i] = (int)(90 * Juego.ESCALA) + random.nextInt((int)(100 * Juego.ESCALA));
        }
    }

    //crea el nivel y coloca al jugador encima del tile de suelo correspondiente
    private void initClasses() {
        ajusteNivel = new AjusteNivel(juego);
        ajusteEnemigo = new AjusteEnemigo(this);

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
    public Jugador getJugador() {
        return jugador;
    }

    //cuando la ventana pierde el foco detiene el movimiento para evitar que se quede andando solo
    public void windowFocusLost() {
        jugador.resetDirBooleans();
    }

    public void mouseDragged(MouseEvent e) {
        if (pausado) {
            pausaOverlay.mouseDragged(e);
        }
    }

    @Override
    public void update() {
        if (!pausado) {
            ajusteNivel.update();
            jugador.update();
            ajusteEnemigo.update(ajusteNivel.getNivelActual().getDatosNivel(), jugador);
            comprobarBorde();
        } else {
            pausaOverlay.actualizar();
        }
    }

    //desplaza la camara cuando el jugador se acerca a los bordes de la pantalla
    private void comprobarBorde() {
        int posicionX = (int) jugador.getHitbox().x;
        int diferencia = posicionX - OffsetXNivel;

        if (diferencia > bordeDerecho) {
            OffsetXNivel += diferencia - bordeDerecho;
        } else if (diferencia < bordeIzquierdo) {
            OffsetXNivel += diferencia - bordeIzquierdo;
        }

        //limita el offset para que la camara no salga de los limites del nivel
        if (OffsetXNivel > tilesMaximosOffsetX) {
            OffsetXNivel = tilesMaximosOffsetX;
        } else if (OffsetXNivel < 0) {
            OffsetXNivel = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        //dibuja en orden: fondo, entorno, nivel, jugador y pausa encima de todo
        g.drawImage(imagenFondo, 0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT, null);
        pintarMontañasYNubes(g);
        ajusteNivel.draw(g, OffsetXNivel);
        jugador.render(g, OffsetXNivel);
        ajusteEnemigo.draw(g, OffsetXNivel);

        //overlay semitransparente de pausa encima de todo lo demas
        if (pausado) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT);
            pausaOverlay.draw(g);
        }
    }

    //dibuja las montañas y nubes con parallax, moviendose mas despacio que el nivel
    private void pintarMontañasYNubes(Graphics g) {

        //calcula cuantas repeticiones hacen falta para cubrir la pantalla sin huecos
        int repeticiones = (Juego.GAME_WIDTH / anchuraMontañas) + 2;

        //las montañas se anclan al borde inferior de la pantalla automaticamente
        for (int i = 0; i < repeticiones; i++) {
            g.drawImage(imagenMontañas,
                    i * anchuraMontañas - (int)(OffsetXNivel * 0.3),
                    Juego.GAME_HEIGHT - alturaMontañas,
                    anchuraMontañas, alturaMontañas, null);
        }

        //las nubes se mueven al 50% de la velocidad del nivel para dar sensacion de profundidad
        for (int i = 0; i < nubesPosicion.length; i++) {
            g.drawImage(nube,
                    anchuraNube * 4 * i - (int)(OffsetXNivel * 0.5),
                    nubesPosicion[i],
                    anchuraNube, alturaNube, null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //el click izquierdo activa el ataque del jugador
        if (e.getButton() == MouseEvent.BUTTON1) {
            jugador.setAtaque(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (pausado) {
            pausaOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (pausado) {
            pausaOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (pausado) {
            pausaOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                jugador.setIzquierda(true);
                break;
            case KeyEvent.VK_D:
                jugador.setDerecha(true);
                break;
            case KeyEvent.VK_SPACE:
                //solo permite saltar si la tecla se ha soltado previamente
                if (!espacioAnterior) {
                    jugador.setSalto(true);
                    espacioAnterior = true;
                }
                break;
            case KeyEvent.VK_ESCAPE:
                pausado = !pausado;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                jugador.setIzquierda(false);
                break;
            case KeyEvent.VK_D:
                jugador.setDerecha(false);
                break;
            case KeyEvent.VK_SPACE:
                //al soltar el espacio se permite volver a saltar
                espacioAnterior = false;
                jugador.setSalto(false);
                break;
        }
    }

    //reactiva el estado de juego desde la pantalla de pausa
    public void unpauseJuego() {
        pausado = false;
    }
}