package audio;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.Random;

//gestiona toda la reproduccion de musica y efectos de sonido del juego
public class AudioPlayer {

    //indices de las canciones, coinciden con el orden del array de cargarCanciones
    //lo tenemos
    public static int creditosIniciales = 0;
    //lo tenemos
    public static int menu = 1;
    //lo tenemos
    public static int musicaNiveles = 2;

    //indices de los efectos, coinciden con el orden del array de cargarEfectos
    //lo tenemos
    public static int enemigoMuere = 0;
    //lo tenemos
    public static int gameOver = 1;
    //lo tenemos
    public static int salto = 2;
    //lo tenemos
    public static int nivelCompletado = 3;
    //lo tenemos
    public static int pinchos = 4;
    //lo tenemos
    public static int recomponerEsqueleto = 5;
    //lo tenemos
    public static int descomponerEsqueleto = 6;
    //lo tenemos
    public static int huesoProyectil = 7;

    //lo tenemos
    public static int morir = 9;
    //lo tenemos
    public static int ataquePatada = 10;
    //lo tenemos
    public static int ataquePatada2 = 11;
    //lo tenemos
    public static int ataquePatada3 = 12;
    //lo tenemos
    public static int golpeJonathan = 13;
    //lo tenemos
    public static final int pociones = 14;
    //lo tenemos
    public static final int superataque = 15;

    // constantes de dio
    public static int ganchoDio = 16;
    public static int dañoDio = 17;
    public static int muerteDio = 18;

    //volumen global aplicado tanto a canciones como a efectos, entre 0.0 y 1.0
    private float volumen = 1f;

    //arrays de clips de audio, canciones para musica de fondo y efectos para sfx
    private Clip[] canciones, efectos;

    //indice de la cancion que se esta reproduciendo actualmente
    private int idCancionActual;

    //flags que indican si la musica o los efectos estan silenciados
    private boolean cancionMuteada, sonidoMuteado;

    //generador aleatorio para seleccionar variantes del sonido de ataque
    private Random random = new Random();

    //carga todos los clips y empieza reproduciendo los creditos iniciales
    public AudioPlayer() {
        cargarCanciones();
        cargarEfectos();
        //para empezar reproduciendo la musica de los creditos iniciales
        playCancion(creditosIniciales);
    }

    //carga los clips de musica de fondo en el orden que coincide con los indices de las constantes
    private void cargarCanciones() {
        String[] nombres = {"creditosIniciales", "musicaMenuPrincipal", "musicaNiveles"};
        canciones = new Clip[nombres.length];
        for (int i = 0; i < canciones.length; i++) {
            canciones[i] = getClip(nombres[i]);
        }
    }

    //actualiza el volumen global y lo aplica a canciones y efectos
    public void setVolumen(float volumen) {
        this.volumen = volumen;
        updateVolumenCanciones();
        updateVolumenEfectos();
    }

    //para la cancion actual si esta reproduciendose
    public void pararCancion() {
        if (canciones[idCancionActual] != null && canciones[idCancionActual].isActive()) {
            canciones[idCancionActual].stop();
        }
    }

    //cambia la cancion actual por la indicada, usado al cambiar de nivel o estado
    public void setCancionNivel(int indiceCancion) {
        playCancion(indiceCancion);
    }

    //para la cancion actual y reproduce el efecto de nivel completado
    public void nivelCompletado() {
        pararCancion();
        playEfecto(nivelCompletado);
    }

    //carga los clips de efectos de sonido en el orden que coincide con los indices de las constantes
    private void cargarEfectos() {
        String[] nombresEfectos = {"enemigoMuerte", "gameOverMusica", "salto", "victoria", "pincho",
                "recomponerEsqueleto", "descomponerEsqueleto", "huesoProyectil", "lanzaEnemigo",
                "muerte", "patada1", "patada2", "patada3", "golpeJonathan", "pociones" , "superataque",
                "gancho", "daño1", "muerteDio"};
        efectos = new Clip[nombresEfectos.length];
        for (int i = 0; i < efectos.length; i++) {
            efectos[i] = getClip(nombresEfectos[i]);
        }
        updateVolumenEfectos();
    }

    //carga un archivo wav desde la carpeta de recursos y lo devuelve como Clip listo para reproducir
    private Clip getClip(String nombre) {
        URL url = getClass().getResource("/audio/" + nombre + ".wav");
        AudioInputStream ais;
        Clip c = null;

        //pongo exception e porque me estoy haciendo lio con los que necesitan
        try {
            ais = AudioSystem.getAudioInputStream(url);
            //Clip es una interfaz, por lo que necesitamos AudioSystem para instanciarla
            c = AudioSystem.getClip();
            c.open(ais);
            return c;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //si hubo error devuelve null
        return null;
    }

    //alterna el silencio de todas las canciones
    public void mutearCanciones() {
        this.cancionMuteada = !cancionMuteada;
        for (Clip c : this.canciones) {
            BooleanControl controlador = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            controlador.setValue(cancionMuteada);
        }
    }

    //alterna el silencio de todos los efectos y reproduce un pitido de confirmacion al desmutear
    public void mutearEfectos() {
        this.sonidoMuteado = !sonidoMuteado;
        for (Clip c : this.efectos) {
            BooleanControl controlador = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            controlador.setValue(sonidoMuteado);
        }
        if (!sonidoMuteado) {
            //sonido para que el usuario sepa que se esta muteando o desmuteando
            playEfecto(salto);
        }
    }

    //selecciona aleatoriamente una de las tres variantes del sonido de ataque
    public void playSonidoAtaque() {
        int start = 10;
        start += random.nextInt(3);
        playEfecto(start);
    }

    //para la cancion actual y reproduce la nueva en bucle infinito
    public void playCancion(int cancion) {
        pararCancion();
        idCancionActual = cancion;
        updateVolumenCanciones();
        canciones[idCancionActual].setMicrosecondPosition(0);
        //utilizamos esta constante de clip para que sea un bucle infinito para la cancion
        canciones[idCancionActual].loop(Clip.LOOP_CONTINUOUSLY);
    }

    //reproduce un efecto de sonido desde el principio, reiniciando si ya estaba sonando
    public void playEfecto(int efecto) {
        //lo ponemos a 0 antes de empezar, porque si no el puntero se queda al final
        //despues de reproducirse la primera vez
        efectos[efecto].setMicrosecondPosition(0);
        efectos[efecto].start();
    }

    //para un efecto de sonido si esta reproduciendose actualmente
    public void pararEfecto(int efecto) {
        if (efectos[efecto] != null && efectos[efecto].isActive()) {
            efectos[efecto].stop();
        }
    }

    //actualiza el volumen de la cancion actual usando el control de ganancia maestra
    private void updateVolumenCanciones() {
        FloatControl controlGanancia = (FloatControl) canciones[idCancionActual].getControl(FloatControl.Type.MASTER_GAIN);
        //convierte volumen lineal a decibelios correctamente
        float dB = (float)(Math.log10(Math.max(volumen, 0.0001)) * 20);
        dB = Math.max(controlGanancia.getMinimum(), Math.min(controlGanancia.getMaximum(), dB));
        controlGanancia.setValue(dB);
    }

    //actualiza el volumen de todos los efectos usando el control de ganancia maestra
    private void updateVolumenEfectos() {
        for (Clip c : efectos) {
            FloatControl controlGanancia = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float)(Math.log10(Math.max(volumen, 0.0001)) * 20);
            dB = Math.max(controlGanancia.getMinimum(), Math.min(controlGanancia.getMaximum(), dB));
            controlGanancia.setValue(dB);
        }
    }

    //metodo que evita colisiones en audios
    public void playEfectoSinInterrumpir(int efecto) {
        if (!efectos[efecto].isActive()) {
            efectos[efecto].setMicrosecondPosition(0);
            efectos[efecto].start();
        }
    }
}