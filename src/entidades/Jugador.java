package entidades;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Juego;
import utils.Constantes;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utils.Constantes.ConstantesJugador.*;
import static utils.Constantes.ConstantesJugador.PREDETERMINADO;
import static utils.Constantes.ConstantesJugador.MUERTE;
import static utils.Miscelaneos.*;
import static utils.Constantes.*;

//controla la logica, animaciones y colisiones del personaje principal
public class Jugador extends Entidad {

    //matriz de frames de cada animacion, primer indice es la accion y el segundo el frame
    private BufferedImage[][] animaciones;

    //tick cuenta los updates transcurridos, indice es el frame actual y velocidad los ticks por frame
    private int tickAnim, indiceAnim, velocidadAnim = 45;

    //accion que se esta reproduciendo actualmente, empieza en reposo
    private int accionJugador = PREDETERMINADO;

    //flags de direccion y salto que activa el teclado
    private boolean izquierda, derecha, arriba, abajo, jump;

    //indica si el jugador se esta moviendo este frame, se recalcula cada update
    private boolean movimiento = false;

    //pixeles por update que avanza el jugador horizontalmente
    private float velocidadJugador = Juego.ESCALA;

    //flag que indica si el jugador esta ejecutando un ataque
    private boolean ataque = false;

    //matriz de tiles del nivel, se usa para comprobar colisiones
    private int[][] datosNivel;

    //velocidad vertical actual, positivo es hacia abajo
    private float velocidadAire = 0f;

    //aceleracion que se suma cada update cuando el jugador esta en el aire
    private float gravedad = 0.04f * Juego.ESCALA;

    //velocidad inicial del salto, negativa porque y crece hacia abajo
    private float velocidadSalto = -2.25f * Juego.ESCALA;

    //velocidad maxima de caida, actualmente sin usar pero disponible para limitarla
    private float velocidadCaida = 0.5f * Juego.ESCALA;

    //true cuando el jugador no esta apoyado en el suelo
    private boolean aire = false;

    //tamaño de la hitbox en pixeles ya escalados, mas pequeña que el sprite
    public static final int HITBOX_W = (int)(19 * Juego.ESCALA);
    public static final int HITBOX_H = (int)(28 * Juego.ESCALA);

    //tamaño del sprite completo en pixeles escalados, solo para el renderizado
    public static final int SPRITE_W = (int)(64 * Juego.ESCALA);
    public static final int SPRITE_H = (int)(40 * Juego.ESCALA);

    //true si el jugador mira hacia la derecha, false si mira hacia la izquierda
    boolean mirandoDerecha = true;

    //desplazamiento del sprite respecto a la hitbox para que coincidan visualmente
    private int offsetX = 50;
    private int offsetY = 25;

    //imagen de la barra de estado que se superpone sobre la barra de vida
    private BufferedImage imagenBarraEstado;

    //posicion y tamaño de la barra de estado en pantalla ya escalados
    private int xBarraEstado = (int) (10 * Juego.ESCALA);
    private int yBarraEstado = (int) (10 * Juego.ESCALA);
    private int anchoBarraEstado = (int) (345 * Juego.ESCALA);
    private int altoBarraEstado  = (int) (87  * Juego.ESCALA);

    //dimensiones y posicion del segmento verde que representa la vida actual
    private int anchoBarraVida   = (int) (180 * Juego.ESCALA);
    private int altoBarraVida    = (int) (4   * Juego.ESCALA);
    private int xInicioBarraVida = (int) (61  * Juego.ESCALA);
    private int yInicioBarraVida = (int) (22  * Juego.ESCALA);

    private int anchuraBarraSuperAtaque = (int) (90 * Juego.ESCALA);
    private int alturaBarraSuperAtaque = (int) (6 * Juego.ESCALA);
    private int superAtaqueBarraXInicio = (int) (83 * Juego.ESCALA);
    private int superAtaqueBarraYInicio = (int) (51  * Juego.ESCALA);
    private int anchuraSuperAtaque = anchuraBarraSuperAtaque;
    private int superAtaqueValorMaximo = 200;
    private int superAtaqueValor = 0;

    //vida maxima del jugador y vida actual, anchoSalud se recalcula cada frame
    private int saludMaxima = 120;
    private int saludActual = saludMaxima;
    private int anchoSalud = anchoBarraVida;

    private boolean recibioGolpe = false;
    private int ticksGolpe = 0;
    private static final int DURACION_GOLPE = 45;

    //hitbox del ataque, se reposiciona cada frame segun la direccion del jugador
    private Rectangle2D.Float boxAtaque;

    //posicion de aparicion del jugador, se guarda para poder volver a ella al morir
    private float spawnX, spawnY;

    //evita que el golpe se registre mas de una vez por animacion de ataque
    private boolean ataqueRevisado;

    //referencia al estado de juego para comunicar eventos como muerte o golpes
    private Playing playing;

    //fila de tile en la que se encuentra el jugador, usada para logica de nivel
    private int direccionY = 0;

    private boolean superAtaqueActivado;
    private int tickSuperAtaque;
    private int crecimientoPoder = 15;
    private int crecimientoPoderTick;

    //inicializa animaciones y coloca la hitbox en la posicion de spawn
    public Jugador(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        cargarAnimaciones();
        iniciarHitbox(x, y, HITBOX_W, HITBOX_H);
        iniciarHitboxAtaque();
        imagenBarraEstado = LoadSave.GetSpriteAtlas(LoadSave.BARRA_SALUD);
    }

    //establece el punto de aparicion del jugador y mueve la hitbox a esa posicion
    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        this.spawnX = spawn.x;
        this.spawnY = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    //crea la hitbox de ataque con un tamaño fijo escalado, se reposiciona en cada update
    private void iniciarHitboxAtaque() {
        boxAtaque = new Rectangle2D.Float(x, y, (int)(20 * Juego.ESCALA), (int)(20 * Juego.ESCALA));
    }

    //punto de entrada del bucle del juego, gestiona muerte, movimiento, ataque y animacion
    public void update() {
        actualizarBarraDeVida();
        actualizarBarraDeSuperAtaque();
        if (saludActual <= 0) {
            if (accionJugador != MUERTE) {
                accionJugador = MUERTE;
                tickAnim = 0;
                indiceAnim = 0;
                ataque = false;
                ataqueRevisado = false;
                playing.setJugadorMuerte(true);
                playing.getJuego().getAudioPlayer().playEfecto(AudioPlayer.morir);
                playing.registrarMuerte();
            } else if (indiceAnim == GetCantidadSprite(MUERTE) - 1
                    && tickAnim >= VELOCIDAD_ANIMACION - 1) {
                playing.setGameOver(true);
                playing.getJuego().getAudioPlayer().pararCancion();
                playing.getJuego().getAudioPlayer().playEfecto(AudioPlayer.gameOver);
            } else {
                actualizarAnimacion();
            }
            return;
        }
        actualizarHitboxAtaque();
        actualizarPosicion();

        //el contador del super ataque avanza siempre, no solo cuando hay movimiento
        if (superAtaqueActivado) {
            tickSuperAtaque++;
            if (tickSuperAtaque >= 120) {
                tickSuperAtaque = 0;
                superAtaqueActivado = false;
            }
        }

        if (movimiento) {
            checkPocionTocada();
            checkPinchosTocados();
            direccionY = (int)(hitbox.y / Juego.TILES_SIZE);
        }

        if (ataque || superAtaqueActivado) checkAtaque();
        setAnimacion();
        actualizarAnimacion();
    }

    //delega en playing la comprobacion de si el jugador toca pinchos este frame
    private void checkPinchosTocados() {
        playing.checkPinchosTocados(this);
    }

    //delega en playing la comprobacion de si la hitbox toca una pocion este frame
    private void checkPocionTocada() {
        playing.checkPocionTocada(hitbox);
    }

    //registra el golpe del ataque en el frame correcto y evita que se repita en la misma animacion
    private void checkAtaque() {
        if (superAtaqueActivado) {
            //durante el super ataque hace daño continuamente sin necesidad de frame especifico
            playing.revisarGolpeEnemigo(boxAtaque);
            playing.checkObjetoGolpeado(boxAtaque);
            return; // ← sin sonido durante el dash
        }
        if (ataqueRevisado || indiceAnim != 1) {
            return;
        }
        ataqueRevisado = true;
        playing.revisarGolpeEnemigo(boxAtaque);
        playing.checkObjetoGolpeado(boxAtaque);
        playing.getJuego().getAudioPlayer().playSonidoAtaque();
    }

    //reposiciona la hitbox de ataque delante del jugador segun la direccion en que mira
    private void actualizarHitboxAtaque() {
        if (derecha) {
            boxAtaque.x = hitbox.x + hitbox.width + (int)(Juego.ESCALA * 1);
        } else if (izquierda) {
            boxAtaque.x = hitbox.x - hitbox.width - (int)(Juego.ESCALA * 1);
        }
        boxAtaque.y = hitbox.y + (Juego.ESCALA * 10);
    }

    //recalcula el ancho del segmento verde en proporcion a la vida actual sobre la maxima
    private void actualizarBarraDeVida() {
        anchoSalud = (int)((saludActual / (float)saludMaxima) * anchoBarraVida);
    }

    //
    private void actualizarBarraDeSuperAtaque(){
        anchuraSuperAtaque = (int)((superAtaqueValor / (float)superAtaqueValorMaximo) * anchuraBarraSuperAtaque);


    }

    //inyecta los datos del nivel para que el jugador pueda comprobar colisiones
    public void cargarDatosNivel(int[][] datosNivel) {
        this.datosNivel = datosNivel;
    }

    //pinta el sprite usando los offsets para alinear visualmente con la hitbox
    public void render(Graphics g, int nivelOffset) {
        int drawX = (int)(hitbox.x - offsetX) - nivelOffset;
        int drawY = (int)(hitbox.y - offsetY);

        if (mirandoDerecha) {
            g.drawImage(animaciones[accionJugador][indiceAnim],
                    drawX, drawY, width, height, null);
        } else {
            //espeja el sprite horizontalmente desplazando el origen al borde derecho
            g.drawImage(animaciones[accionJugador][indiceAnim],
                    drawX + width, drawY, -width, height, null);
        }
        dibujarUI(g);
    }

    //dibuja el rectangulo rojo de la hitbox de ataque, util para depuracion
    private void dibujarHitboxDeAtaque(Graphics g, int nivelOffsetX) {
        g.setColor(Color.red);
        g.drawRect((int)boxAtaque.x - nivelOffsetX, (int)boxAtaque.y,
                (int)boxAtaque.width, (int)boxAtaque.height);
    }

    //dibuja la imagen de la barra de estado y rellena el segmento de vida con color verde
    private void dibujarUI(Graphics g) {
        Color verdeVida = new Color(0x5daf03);
        g.drawImage(imagenBarraEstado, xBarraEstado, yBarraEstado,
                anchoBarraEstado, altoBarraEstado, null);
        g.setColor(verdeVida);
        g.fillRect(xInicioBarraVida + xBarraEstado, yInicioBarraVida + yBarraEstado,
                anchoSalud, altoBarraVida);
        g.setColor(Color.YELLOW);
        g.fillRect(superAtaqueBarraXInicio + xBarraEstado, superAtaqueBarraYInicio + yBarraEstado,
                anchuraSuperAtaque, // ← este es el que cambia dinamicamente
                alturaBarraSuperAtaque);
    }

    //decide que animacion reproducir segun el estado del jugador, el aire tiene prioridad sobre el movimiento
    private void setAnimacion() {
        int startAni = accionJugador;

        if (movimiento) {
            accionJugador = CORRIENDO;
        } else {
            accionJugador = PREDETERMINADO;
        }

        if (aire) {
            if (velocidadAire < 0) {
                accionJugador = SALTANDO;
            } else {
                accionJugador = CAYENDO;
            }
        }

        // el daño sobreescribe el movimiento y el aire pero no el ataque
        if (recibioGolpe) {
            accionJugador = DAÑO;
            ticksGolpe++;
            if (ticksGolpe >= DURACION_GOLPE) {
                recibioGolpe = false;
                ticksGolpe = 0;
            }
            if (startAni != DAÑO) {
                resetAniTick();
            }
            return; // ← sale antes de llegar al ataque
        }

        if(superAtaqueActivado){
            if (startAni != PUÑETAZO) {
                accionJugador = PUÑETAZO;
                resetAniTick();  // solo reinicia al cambiar de accion
            } else {
                accionJugador = PUÑETAZO;
            }
            return;
        }

        // el ataque sobreescribe todo lo demas
        if (ataque) {
            accionJugador = PATADA;
            if (startAni != PATADA) {
                indiceAnim = 1;
                tickAnim = 0;
                return;
            }
        }

        if (startAni != accionJugador) {
            resetAniTick();
        }
    }

    //reinicia los contadores de animacion al cambiar de accion
    private void resetAniTick() {
        tickAnim = 0;
        indiceAnim = 0;
    }

    //devuelve true si hay un tile solido justo debajo de los pies
    private boolean EnSuelo(Rectangle2D.Float hitbox, int[][] datosNivel) {
        return !puedeMoverse(hitbox.x, hitbox.y + 1, hitbox.width, hitbox.height, datosNivel);
    }

    //devuelve true si hay un ataque en curso, usado para evitar ataques encadenados
    public boolean isAtacando() {
        return superAtaqueActivado;
    }

    //calcula el movimiento vertical y horizontal y resuelve colisiones antes de aplicarlo
    private void actualizarPosicion() {
        movimiento = false;

        if (jump) saltar();

        //si no esta en el aire comprobamos si hay suelo, si no lo hay lo ponemos en caida libre
        if (!aire) {
            if (!superAtaqueActivado){
                if (!EnSuelo(hitbox, datosNivel)) {
                    aire = true;
                }
            }

        }

        float xVelocidad = 0;
        if (izquierda) {
            xVelocidad -= velocidadJugador;
            mirandoDerecha = false;
        }
        if (derecha) {
            xVelocidad += velocidadJugador;
            mirandoDerecha = true;
        }

        if (superAtaqueActivado){
            if (!izquierda && !derecha){
                if (!mirandoDerecha){
                    xVelocidad = -velocidadJugador;
                }else {
                    xVelocidad = velocidadJugador;
                }
            }
            xVelocidad *= 3;
        }


        if (aire && !superAtaqueActivado) {
            float newY = hitbox.y + velocidadAire;

            if (puedeMoverse(hitbox.x, newY, hitbox.width, hitbox.height, datosNivel)) {
                hitbox.y = newY;
            } else {
                //alineamos la hitbox con el tile de impacto para no quedar solapados
                hitbox.y = GetYPosTechoOSuelo(hitbox, velocidadAire, newY);

                //si cae resetea al suelo, si sube solo detiene la velocidad vertical
                if (velocidadAire > 0) {
                    resetearAltura();
                } else {
                    velocidadAire = 0;
                }
            }

            //solo acumulamos gravedad si seguimos en el aire despues de resolver el impacto
            if (aire) {
                velocidadAire += gravedad;
            }
        }

        actualizarXPos(xVelocidad);

        if (xVelocidad != 0 || aire) {
            movimiento = true;
        }
    }

    //inicia el salto solo si el jugador esta en el suelo
    private void saltar() {
        //descartamos el salto si ya estamos en el aire para evitar saltos infinitos
        if (aire) return;
        playing.getJuego().getAudioPlayer().playEfecto(AudioPlayer.salto);
        aire = true;
        velocidadAire = velocidadSalto;
        //consumimos el flag aqui para que no se repita en el siguiente update
        jump = false;
    }

    //devuelve al jugador al estado de suelo y detiene el movimiento vertical
    private void resetearAltura() {
        aire = false;
        velocidadAire = 0;
    }

    //mueve al jugador horizontalmente o lo pega a la pared si hay colision
    private void actualizarXPos(float xVelocidad) {
        //si no hay velocidad horizontal no hay nada que calcular
        if (xVelocidad == 0) return;

        float newX = hitbox.x + xVelocidad;

        if (puedeMoverse(newX, hitbox.y, hitbox.width, hitbox.height, datosNivel)) {
            hitbox.x = newX;
            x = hitbox.x;
            movimiento = true;
        } else {
            //alineamos el borde de la hitbox con el tile de impacto
            hitbox.x = GetXPosPared(hitbox, xVelocidad, newX);
            x = hitbox.x;
            if (superAtaqueActivado){
                superAtaqueActivado = false;
                tickSuperAtaque = 0;
            }
        }
    }

    //suma o resta vida al jugador y la mantiene dentro del rango valido
    public void cambiarSalud(int valor) {
        saludActual += valor;
        if (saludActual <= 0) {
            saludActual = 0;
        } else if (saludActual >= saludMaxima) {
            saludActual = saludMaxima;
        }
        // si recibe daño activa la animacion y el sonido
        if (valor < 0) {
            recibioGolpe = true;
            ticksGolpe = 0;
            playing.getJuego().getAudioPlayer().playEfecto(AudioPlayer.golpeJonathan);
            playing.registrarDaño(-valor);

        }
    }

    //avanza el contador de animacion y cambia de frame cuando toca
    private void actualizarAnimacion() {
        tickAnim++;

        int velocidadActual;
        if (accionJugador == PATADA) {
            velocidadActual = 30;
        } else if (accionJugador == PUÑETAZO) {
            velocidadActual = 45;  // ajusta a tu gusto
        } else {
            velocidadActual = velocidadAnim;
        }

        if (tickAnim >= velocidadActual) {
            tickAnim = 0;
            indiceAnim++;

            if (indiceAnim >= Constantes.ConstantesJugador.GetCantidadSprite(accionJugador)) {
                // Si es muerte, se congela en el último frame
                if (accionJugador == MUERTE) {
                    indiceAnim = Constantes.ConstantesJugador.GetCantidadSprite(MUERTE) - 1;
                    return;
                }
                indiceAnim = 0;
                ataque = false;
                ataqueRevisado = false;
            }
        }
    }

    //carga el atlas y recorta cada frame de cada animacion en su posicion correspondiente
    private void cargarAnimaciones() {
        BufferedImage imagen = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        //7 filas de animaciones con hasta 4 frames cada una
        animaciones = new BufferedImage[10][9];

        for (int j = 0; j < animaciones.length; j++) {
            int cantidad = Constantes.ConstantesJugador.GetCantidadSprite(j);

            //cada frame mide 64x40 pixeles en el atlas original sin escalar
            for (int i = 0; i < cantidad; i++) {
                animaciones[j][i] = imagen.getSubimage(i * 64, j * 40, 64, 40);
            }
        }
    }

    //detiene el movimiento del jugador, se llama cuando la ventana pierde el foco
    public void resetDirBooleans() {
        izquierda = derecha = arriba = abajo = false;
    }

    //restaura todos los valores del jugador a su estado inicial y lo mueve al spawn
    public void resetearTodo() {
        resetDirBooleans();
        aire = false;
        ataque = false;
        movimiento = false;
        accionJugador = PREDETERMINADO;
        saludActual = saludMaxima;
        hitbox.x = spawnX;
        hitbox.y = spawnY;
        x = spawnX;
        y = spawnY;
        superAtaqueValor = 0;
        superAtaqueActivado = false;
        recibioGolpe = false;
        ticksGolpe = 0;
    }

    //resetea el jugador entre niveles manteniendo el poder acumulado
    public void resetearEntreNiveles() {
        resetDirBooleans();
        aire = false;
        ataque = false;
        movimiento = false;
        accionJugador = PREDETERMINADO;
        saludActual = saludMaxima;
        hitbox.x = spawnX;
        hitbox.y = spawnY;
        x = spawnX;
        y = spawnY;
        recibioGolpe = false;
        ticksGolpe = 0;
        superAtaqueActivado = false;
        tickSuperAtaque = 0;
        // ← superAtaqueValor no se resetea
    }

    //activa o desactiva el flag de ataque desde el sistema de input
    public void setAtaque(boolean ataque) { this.ataque = ataque; }

    //activa o desactiva el flag de salto desde el sistema de input
    public void setSalto(boolean salto) { this.jump = salto; }

    //activa o desactiva el movimiento hacia la derecha desde el sistema de input
    public void setDerecha(boolean derecha) { this.derecha = derecha; }

    //activa o desactiva el movimiento hacia la izquierda desde el sistema de input
    public void setIzquierda(boolean izquierda) { this.izquierda = izquierda; }

    //placeholder para el sistema de poder, actualmente solo imprime un mensaje
    public void cambiarPoder(int valorPocionAzul) {
        //no permite cambiar el poder durante el super ataque
        if (superAtaqueActivado) return;

        superAtaqueValor += valorPocionAzul;
        if (superAtaqueValor >= superAtaqueValorMaximo) {
            superAtaqueValor = superAtaqueValorMaximo;
        } else if (superAtaqueValor <= 0) {
            superAtaqueValor = 0;
        }
    }

    //devuelve true si el jugador esta pulsando la tecla de bajar
    public boolean isAbajo() { return abajo; }

    //reduce la vida a cero para forzar la muerte del jugador desde sistemas externos
    public void muerte() {
        saludActual = 0;
    }

    //devuelve la fila de tile en la que se encuentra el jugador
    public int getDireccionY() {
        return direccionY;
    }

    public void superAtaque() {
        if (superAtaqueActivado) {
            return;
        }
        if (superAtaqueValor >= superAtaqueValorMaximo) {
            superAtaqueActivado = true;
            superAtaqueValor = 0;
        }
    }
}