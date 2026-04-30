package gamestates;

import entidades.AjusteEnemigo;
import entidades.Jugador;
import main.Juego;
import niveles.AjusteNivel;
import objetos.AjusteDeObjetos;
import ui.NivelCompletadoOverlay;
import ui.OverOverlayJuego;
import ui.PausaOverlay;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static main.Juego.ESCALA_JUGADOR;
import static main.Juego.TILES_SIZE;
import static utils.Constantes.entorno.*;

public class Playing extends State implements Statemethods {
    private Jugador jugador;
    private AjusteNivel ajusteNivel;
    private AjusteEnemigo ajusteEnemigo;
    private AjusteDeObjetos ajusteDeObjetos;

    //evita que el salto se repita si se mantiene pulsada la tecla
    private boolean espacioAnterior = false;

    //indica si el juego esta en pausa
    private boolean pausado = false;
    private PausaOverlay pausaOverlay;
    private OverOverlayJuego overlay;
    private NivelCompletadoOverlay nivelCompletadoMenu;
    //desplazamiento horizontal actual del nivel en pixeles
    private int OffsetXNivel;

    //limites en pixeles que determinan cuando la camara empieza a desplazarse
    private int bordeIzquierdo = (int)(0.2 * Juego.GAME_WIDTH);
    private int bordeDerecho = (int)(0.8 * Juego.GAME_WIDTH);

    //ancho total del nivel en tiles y offset maximo que puede alcanzar la camara
    private int tilesMaximosOffsetX;


    //imagenes del fondo, montañas y nubes
    private BufferedImage imagenFondo;
    private BufferedImage imagenMontañas;
    private BufferedImage nube;

    //posiciones Y aleatorias de cada nube para distribuirlas verticalmente
    private int[] nubesPosicion;
    private Random random = new Random();
    private boolean gameOver = false;
    private boolean nivelCompletado = false;

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
        calcularOffsetNivel();
        cargarNivel();
    }
    public void cargarSiguienteNivel() {
        resetAll();
        ajusteNivel.cargarSiguienteNivel();
        jugador.setSpawn(ajusteNivel.getNivelActual().getSpawnJugador());
    }

    private void cargarNivel() {
        ajusteEnemigo.cargarEnemigos(ajusteNivel.getNivelActual());
        ajusteDeObjetos.cargarObjetos(ajusteNivel.getNivelActual());
    }

    private void calcularOffsetNivel() {
        // TODO Auto-generated method
        tilesMaximosOffsetX = ajusteNivel.getNivelActual().getOffsetNivel();

    }

    //crea el nivel y coloca al jugador encima del tile de suelo correspondiente
    private void initClasses() {
        ajusteNivel = new AjusteNivel(juego);
        ajusteEnemigo = new AjusteEnemigo(this);
        ajusteDeObjetos = new AjusteDeObjetos(this);

        int[][] datosNivel = ajusteNivel.getNivelActual().getDatosNivel();

        //fila y columna del tile sobre el que aparecera el jugador al iniciar
        int filaSuelo = 8;
        int colInicial = 3;

        //convertimos la posicion en tiles a posicion en pixeles
        int xInicial = colInicial * TILES_SIZE;

        //restamos la altura de la hitbox para que los pies queden sobre el tile y no dentro
        int yInicial = filaSuelo * TILES_SIZE - Jugador.HITBOX_H;

        jugador = new Jugador(xInicial, yInicial, (int)(64 * ESCALA_JUGADOR), (int)(40 * ESCALA_JUGADOR), this);
        jugador.cargarDatosNivel(datosNivel);
        jugador.setSpawn(ajusteNivel.getNivelActual().getSpawnJugador());

        pausaOverlay = new PausaOverlay(this);
        overlay = new OverOverlayJuego(this);
        nivelCompletadoMenu = new NivelCompletadoOverlay(this);
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
        if(!gameOver) {
            if (pausado) {
                pausaOverlay.mouseDragged(e);
            }
        }
    }
    public void setNivelCompletado(boolean nivelCompletado) {
        this.nivelCompletado = nivelCompletado;
    }

    @Override
    public void update() {
        if(pausado) {
            pausaOverlay.actualizar();
        } else if(nivelCompletado) {
            nivelCompletadoMenu.update();
        } else if (!gameOver) {
            ajusteNivel.update();
            ajusteDeObjetos.update();
            jugador.update();
            ajusteEnemigo.update(ajusteNivel.getNivelActual().getDatosNivel(), jugador);
            comprobarBorde();
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
        ajusteDeObjetos.draw(g, OffsetXNivel);

        //overlay semitransparente de pausa encima de todo lo demas
        if (pausado) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT);
            pausaOverlay.draw(g);
        } else if (gameOver) {
            overlay.draw(g);

        } else if(nivelCompletado) {
            nivelCompletadoMenu.draw(g);
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

    public void resetAll(){
        gameOver = false;
        pausado = false;
        nivelCompletado = false;
        jugador.resetearTodo();

        //fundamental para que funcione el boton de reiniciar, si no no se reinicia la posicion
        ajusteEnemigo.resetearTodosEnemigos();
        OffsetXNivel = 0;
        ajusteDeObjetos.resetearTodosLosObjetos();

    }

    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    public void revisarGolpeEnemigo(Rectangle2D.Float boxAtaque){
        ajusteEnemigo.golpeEnemigo(boxAtaque);
    }

    public void checkPocionTocada(Rectangle2D.Float hitbox) {
        ajusteDeObjetos.checkObjetoTocado(hitbox);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            //el click izquierdo activa el ataque del jugador
            if (e.getButton() == MouseEvent.BUTTON1) {
                jugador.setAtaque(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver) {
            if (pausado) {
                pausaOverlay.mousePressed(e);
            } else if(nivelCompletado) {
                nivelCompletadoMenu.mousePressed(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver) {
            if (pausado) {
                pausaOverlay.mouseReleased(e);
            } else if(nivelCompletado) {
                nivelCompletadoMenu.mouseReleased(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver) {
            if (pausado) {
                pausaOverlay.mouseMoved(e);
            } else if(nivelCompletado) {
                nivelCompletadoMenu.mouseMoved(e);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver){
            overlay.teclaPresionada(e);
        }else {
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver){
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

    }
    public void setOffsetNivelMaximo(int offsetNivel){
        this.tilesMaximosOffsetX = offsetNivel;
    }
    public AjusteEnemigo getAjusteEnemigo() {
        return ajusteEnemigo;
    }

    //reactiva el estado de juego desde la pantalla de pausa
    public void depausarJuego() {
        pausado = false;
    }

    public AjusteDeObjetos getAjusteDeObjetos(){
        return ajusteDeObjetos;
    }

    public void checkObjetoGolpeado(Rectangle2D.Float boxAtaque) {
        ajusteDeObjetos.chekGolpeoAlObjeto(boxAtaque);
    }
}