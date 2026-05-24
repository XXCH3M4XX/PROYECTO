package gamestates;

import entidades.AjusteEnemigo;
import entidades.Jugador;
import main.Juego;
import niveles.AjusteNivel;
import objetos.AjusteDeObjetos;
import ui.NivelCompletadoOverlay;
import ui.OverlayGameOver;
import ui.PausaOverlay;
import utils.LoadSave;
import utils.RegistroPartida;

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
    private OverlayGameOver overlay;
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

    private BufferedImage[] fondosNivel;
    private BufferedImage[] montañasNivel;
    private BufferedImage[] nubesNivel;

    //posiciones Y aleatorias de cada nube para distribuirlas verticalmente
    private int[] nubesPosicion;
    private Random random = new Random();

    //flags que controlan el estado general de la partida
    private boolean gameOver = false;
    private boolean nivelCompletado = false;
    private boolean jugadorMuriendo = false;
    private BufferedImage castillo;
    private int anchoCastillo, altoCastillo;

    // contadores de estadisticas de la partida actual
    private int muertes = 0;
    private int dañoRecibido = 0;
    private int porcionesRecogidas = 0;
    private long ticksPartida = 0; // ← cuenta los UPS transcurridos

    //inicializa todos los sistemas y carga las imagenes del entorno
    public Playing(Juego juego) {
        super(juego);
        initClasses();

        //carga las imagenes del entorno
        imagenFondo = LoadSave.GetSpriteAtlas(LoadSave.FONDO_NIVEL1);
        imagenMontañas = LoadSave.GetSpriteAtlas(LoadSave.MONTAÑASYBOSQUES_NIVEL1);
        nube = LoadSave.GetSpriteAtlas(LoadSave.NUBES_NIVEL1);
        castillo = LoadSave.GetSpriteAtlas(LoadSave.CASTILLO);
        anchoCastillo = (int)(castillo.getWidth() * Juego.ESCALA);
        altoCastillo = (int)(castillo.getHeight() * Juego.ESCALA);

        //asigna una altura aleatoria a cada nube dentro de un rango
        nubesPosicion = new int[8];
        for (int i = 0; i < nubesPosicion.length; i++) {
            nubesPosicion[i] = (int)(90 * Juego.ESCALA) + random.nextInt((int)(100 * Juego.ESCALA));
        }
        calcularOffsetNivel();
        cargarNivel();
    }

    //carga los fondos correspondientes al nivel indicado
    private void cargarFondosNivel(int indice) {
        switch (indice) {
            case 1:
                imagenFondo = LoadSave.GetSpriteAtlas(LoadSave.FONDO_NIVEL2);
                imagenMontañas = null;
                nube = LoadSave.GetSpriteAtlas(LoadSave.ARAÑA_NIVEL2);
                castillo = null; // ← sin castillo en nivel 2
                break;
            case 2:
                //nivel 3: solo fondo estatico, sin montañas ni nubes
                imagenFondo = LoadSave.GetSpriteAtlas(LoadSave.FONDO_NIVEL3);
                imagenMontañas = null;
                nube = null;
                castillo = null;
                break;
            default:
                //nivel 1: fondo estrellado con montañas y nubes
                imagenFondo = LoadSave.GetSpriteAtlas(LoadSave.FONDO_NIVEL1);
                imagenMontañas = LoadSave.GetSpriteAtlas(LoadSave.MONTAÑASYBOSQUES_NIVEL1);
                nube = LoadSave.GetSpriteAtlas(LoadSave.NUBES_NIVEL1);
                break;
        }
    }

    //avanza al siguiente nivel reseteando todos los sistemas y cargando los nuevos datos
    public void cargarSiguienteNivel() {
        gameOver = false;
        pausado = false;
        nivelCompletado = false;
        jugador.resetearEntreNiveles();
        ajusteEnemigo.resetearTodosEnemigos();
        ajusteDeObjetos.resetearTodosLosObjetos();
        OffsetXNivel = 0;

        ajusteNivel.cargarSiguienteNivel();
        cargarFondosNivel(ajusteNivel.getIndiceNivel()); // ← añade esto

        jugador.setSpawn(ajusteNivel.getNivelActual().getSpawnJugador());
        jugador.cargarDatosNivel(ajusteNivel.getNivelActual().getDatosNivel());
        tilesMaximosOffsetX = ajusteNivel.getNivelActual().getOffsetNivel();
        ajusteEnemigo.cargarEnemigos(ajusteNivel.getNivelActual());
        ajusteEnemigo.resetearTodosEnemigos();
        ajusteDeObjetos.cargarObjetos(ajusteNivel.getNivelActual());
    }

    //carga los enemigos y objetos del nivel actual en sus gestores correspondientes
    private void cargarNivel() {
        ajusteEnemigo.cargarEnemigos(ajusteNivel.getNivelActual());
        ajusteEnemigo.resetearTodosEnemigos();
        ajusteDeObjetos.cargarObjetos(ajusteNivel.getNivelActual());
    }

    //obtiene el offset maximo del nivel actual y lo guarda para limitar la camara
    private void calcularOffsetNivel() {
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
        overlay = new OverlayGameOver(this);
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

    //reenvía el evento de arrastre del raton a la pantalla de pausa si esta activa
    public void mouseDragged(MouseEvent e) {
        if (!gameOver) {
            if (pausado) {
                pausaOverlay.mouseDragged(e);
            }
        }
    }

    //marca el nivel como completado y desactiva la pausa para mostrar el overlay
    public void setNivelCompletado(boolean nivelCompletado) {
        this.nivelCompletado = nivelCompletado;
        if (nivelCompletado) {
            pausado = false;
            juego.getAudioPlayer().nivelCompletado();
        }

    }

    //bucle principal del estado, delega el update segun el estado activo de la partida
    @Override
    public void update() {
        if (pausado) {
            pausaOverlay.actualizar();
        } else if (nivelCompletado) {
            nivelCompletadoMenu.update();
        } else if (gameOver) {
            overlay.update();
        } else if (jugadorMuriendo) {
            //solo actualiza al jugador para que reproduzca la animacion de muerte
            jugador.update();
        } else {
            ajusteNivel.update();
            ajusteDeObjetos.update(ajusteNivel.getNivelActual().getDatosNivel(), jugador);
            jugador.update();
            ajusteEnemigo.update(ajusteNivel.getNivelActual().getDatosNivel(), jugador);


            if (jugador.getHitbox().y > Juego.GAME_HEIGHT) {
                jugador.muerte();
            }

            comprobarBorde();
            ticksPartida++;
        }
    }

    //desplaza la camara cuando el jugador se acerca a los bordes de la pantalla
    private void comprobarBorde() {
        int posicionX = (int) jugador.getHitbox().x;
        OffsetXNivel = posicionX - Juego.GAME_WIDTH / 2;

        //limita el offset para que la camara no salga de los limites del nivel
        if (OffsetXNivel > tilesMaximosOffsetX) {
            OffsetXNivel = tilesMaximosOffsetX;
        } else if (OffsetXNivel < 0) {
            OffsetXNivel = 0;
        }
    }

    //dibuja en orden: fondo, entorno, nivel, jugador, enemigos, objetos y overlays activos
    @Override
    public void draw(Graphics g) {
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
        } else if (nivelCompletado) {
            nivelCompletadoMenu.draw(g);
        }
    }

    //dibuja las montañas y nubes con parallax, moviendose mas despacio que el nivel
    //dibuja las montañas y nubes con parallax, moviendose mas despacio que el nivel
    private void pintarMontañasYNubes(Graphics g) {
        int anchoNivelPixeles = ajusteNivel.getNivelActual().getDatosNivel()[0].length * Juego.TILES_SIZE;
        int repeticiones = (anchoNivelPixeles / anchuraMontañas) + 2;

        //capa 1: montañas, parallax lento (solo si hay montañas en este nivel)
        if (imagenMontañas != null) {
            for (int i = 0; i < repeticiones; i++) {
                g.drawImage(imagenMontañas,
                        i * anchuraMontañas - (int)(OffsetXNivel * 0.3),
                        Juego.GAME_HEIGHT - alturaMontañas,
                        anchuraMontañas, alturaMontañas, null);
            }
        }

        //capa 2: castillo al final del nivel, solo en nivel 1
        if (castillo != null) {
            int castilloX = (int)((anchoNivelPixeles - anchoCastillo * 3) * 0.5) - (int)(OffsetXNivel * 0.5);
            int castilloY = Juego.GAME_HEIGHT - altoCastillo * 3;
            g.drawImage(castillo, castilloX, castilloY, anchoCastillo * 3, altoCastillo * 3, null);
        }

        //capa 3: nubes o arañas segun el nivel, parallax rapido

        if (nube != null) {
            int numNubes = (anchoNivelPixeles / (anchuraNube * 4)) + 2;
            for (int i = 0; i < numNubes; i++) {
                int yNube = nubesPosicion.length > i ? nubesPosicion[i] : (int)(90 * Juego.ESCALA);
                g.drawImage(nube,
                        anchuraNube * 4 * i - (int)(OffsetXNivel * 0.5),
                        yNube,
                        anchuraNube * 2,
                        alturaNube * 2,
                        null);
            }
        }
    }

    //resetea todos los sistemas al estado inicial para reiniciar la partida desde cero
    public void resetAll() {
        gameOver = false;
        jugadorMuriendo = false;
        pausado = false;
        nivelCompletado = false;
        jugador.resetearTodo(); // ← primero de todo, restaura la salud a saludMaxima
        ajusteNivel.resetNivel();
        cargarFondosNivel(0);
        jugador.cargarDatosNivel(ajusteNivel.getNivelActual().getDatosNivel());
        jugador.setSpawn(ajusteNivel.getNivelActual().getSpawnJugador());
        ajusteEnemigo.cargarEnemigos(ajusteNivel.getNivelActual());
        tilesMaximosOffsetX = ajusteNivel.getNivelActual().getOffsetNivel();
        ajusteDeObjetos.cargarObjetos(ajusteNivel.getNivelActual());
        ajusteEnemigo.resetearTodosEnemigos();
        OffsetXNivel = 0;
        ajusteDeObjetos.resetearTodosLosObjetos();
    }
    public void resetearPartidaCompleta() {
        muertes = 0;
        dañoRecibido = 0;
        porcionesRecogidas = 0;
        ticksPartida = 0;
        resetAll();
        jugador.resetearTodo();

    }
    // se llama desde Jugador cuando recibe daño
    public void registrarDaño(int cantidad) {
        dañoRecibido += cantidad;
    }

    // se llama desde Jugador cuando muere
    public void registrarMuerte() {
        muertes++;
    }

    // se llama desde AjusteDeObjetos cuando se recoge una pocion
    public void registrarPocion() {
        porcionesRecogidas++;
    }

    public int getMuertes() { return muertes; }
    public int getDañoRecibido() { return dañoRecibido; }
    public int getPorcionesRecogidas() { return porcionesRecogidas; }

    // convierte los ticks a segundos usando los UPS del juego
    public long getTiempoSegundos() {
        return ticksPartida / 200; // ← 200 UPS
    }

    // devuelve un RegistroPartida con todas las estadisticas actuales
    public RegistroPartida getEstadisticasActuales(String nombre) {
        return new RegistroPartida(
                nombre,
                getTiempoSegundos(),
                muertes,
                dañoRecibido,
                porcionesRecogidas
        );
    }

    //activa o desactiva la pantalla de game over
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    //delega en el gestor de enemigos la comprobacion de si el boxAtaque golpea a alguno
    public void revisarGolpeEnemigo(Rectangle2D.Float boxAtaque) {
        ajusteEnemigo.golpeEnemigo(boxAtaque);
    }

    //delega en el gestor de objetos la comprobacion de si la hitbox toca una pocion
    public void checkPocionTocada(Rectangle2D.Float hitbox) {
        ajusteDeObjetos.checkObjetoTocado(hitbox);
    }

    //delega en el gestor de objetos la comprobacion de si el jugador toca pinchos
    public void checkPinchosTocados(Jugador j) {
        if (!jugadorMuriendo) {
            ajusteDeObjetos.checkJugadorTocaPinchos(j);
        }
    }

    //el click izquierdo activa el ataque del jugador si la partida esta en curso
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            //solo el super ataque aqui, el ataque normal va en mousePressed
            if (e.getButton() == MouseEvent.BUTTON3) {
                jugador.superAtaque();
            }
        }
    }

    //reenvía el evento de pulsacion al overlay activo segun el estado de la partida
    @Override
    public void mousePressed(MouseEvent e) {
        if (gameOver) {
            overlay.mousePressed(e);
            return;
        }
        //activa el ataque sin bloqueo para que siempre responda al click
        if (e.getButton() == MouseEvent.BUTTON1 && !jugador.isAtacando()) {
            jugador.setAtaque(true);
        }
        if (pausado) {
            pausaOverlay.mousePressed(e);
        } else if (nivelCompletado) {
            nivelCompletadoMenu.mousePressed(e);
        }
    }

    //reenvía el evento de soltar el raton al overlay activo segun el estado de la partida
    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameOver) {
            overlay.mouseReleased(e);
            return;
        }
        if (pausado) {
            pausaOverlay.mouseReleased(e);
        } else if (nivelCompletado) {
            nivelCompletadoMenu.mouseReleased(e);
        }

    }

    //reenvía el evento de movimiento del raton al overlay activo segun el estado de la partida
    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameOver) {
            overlay.mouseMoved(e);
            return;
        }
        if (pausado) {
            pausaOverlay.mouseMoved(e);
        } else if (nivelCompletado) {
            nivelCompletadoMenu.mouseMoved(e);
        }

    }

    //gestiona las teclas segun el estado activo, game over solo acepta escape
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            overlay.keyPressed(e);
        } else {
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

    //al soltar las teclas desactiva los flags correspondientes del jugador
    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver) {
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

    //establece el offset maximo de la camara para el nivel actual
    public void setOffsetNivelMaximo(int offsetNivel) {
        this.tilesMaximosOffsetX = offsetNivel;
    }

    //devuelve el gestor de enemigos para que otros sistemas puedan acceder a el
    public AjusteEnemigo getAjusteEnemigo() {
        return ajusteEnemigo;
    }

    //reactiva el estado de juego desde la pantalla de pausa
    public void depausarJuego() {
        pausado = false;
    }
    public AjusteNivel getAjusteNivel() {
        return ajusteNivel;
    }

    //devuelve el gestor de objetos para que otros sistemas puedan acceder a el
    public AjusteDeObjetos getAjusteDeObjetos() {
        return ajusteDeObjetos;
    }

    //delega en el gestor de objetos la comprobacion de si el boxAtaque golpea un objeto
    public void checkObjetoGolpeado(Rectangle2D.Float boxAtaque) {
        ajusteDeObjetos.chekGolpeoAlObjeto(boxAtaque);
    }

    //activa el flag de jugador muriendo para que el update solo procese su animacion de muerte
    public void setJugadorMuerte(boolean b) {
        this.jugadorMuriendo = true;
    }
}