package entidades;

import main.Juego;
import utils.Constantes;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utils.Constantes.ConstantesJugador.*;
import static utils.Miscelaneos.*;

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
    //Comentado porque no se usa
    public static final int SPRITE_W = (int)(64 * Juego.ESCALA);
    public static final int SPRITE_H = (int)(40 * Juego.ESCALA);

    //variable booleana que nos permite saber si esta mirando a la derecha o a la izquierda
    boolean mirandoDerecha = true;

    //desplazamiento del sprite respecto a la hitbox para que coincidan visualmente
    private int offsetX = 50;
    private int offsetY = 25;

//    private BufferedImage imagenFondo;

    //inicializa animaciones y coloca la hitbox en la posicion de spawn
    public Jugador(float x, float y, int width, int height) {
        super(x, y, width, height);
        cargarAnimaciones();
        iniciarHitbox(x, y, HITBOX_W, HITBOX_H);

    }

    //punto de entrada del bucle del juego, llama a los tres sistemas en orden
    public void update(){
        actualizarPosicion();
        actualizarAnimacion();
        setAnimacion();
    }

    //inyecta los datos del nivel para que el jugador pueda comprobar colisiones
    public void cargarDatosNivel(int[][] datosNivel){
        this.datosNivel = datosNivel;
    }

    //pinta el sprite usando los offsets para alinear visualmente con la hitbox
    public void render(Graphics g, int nivelOffset){
        int drawX = (int)(hitbox.x - offsetX) - nivelOffset;  // punto fijo, no depende de width
        int drawY = (int)(hitbox.y - offsetY);

        if (mirandoDerecha) {
            g.drawImage(
                    animaciones[accionJugador][indiceAnim],
                    drawX, drawY,
                    width, height,
                    null
            );
        } else {
            g.drawImage(
                    animaciones[accionJugador][indiceAnim],
                    drawX + width, drawY,  // desplazamos el origen al borde derecho
                    -width, height,        // y dibujamos hacia la izquierda
                    null
            );
        }




    }

    //decide que animacion reproducir segun el estado del jugador, el aire tiene prioridad sobre el movimiento
    private void setAnimacion() {
        int startAni = accionJugador;

        if(movimiento) {
            accionJugador = CORRIENDO;
        } else {
            accionJugador = PREDETERMINADO;
        }

        //el aire sobreescribe el movimiento horizontal porque tiene mas prioridad visual
        if(aire) {
            //velocidad negativa significa que sube, positiva que cae
            if(velocidadAire < 0) {
                accionJugador = SALTANDO;
            } else {
                accionJugador = CAYENDO;
            }
        }

        //el ataque sobreescribe todo lo demas
        if(ataque){
            accionJugador = PATADA;
        }

        //si la accion cambio reiniciamos el contador para no empezar por la mitad
        if(startAni != accionJugador){
            resetAniTick();
        }
    }

    //reinicia los contadores de animacion al cambiar de accion
    private void resetAniTick(){
        tickAnim = 0;
        indiceAnim = 0;
    }

    //devuelve true si hay un tile solido justo debajo de los pies
    private boolean EnSuelo(Rectangle2D.Float hitbox, int[][] datosNivel) {
        return !puedeMoverse(hitbox.x, hitbox.y + 1, hitbox.width, hitbox.height, datosNivel);
    }

    //calcula el movimiento vertical y horizontal y resuelve colisiones antes de aplicarlo
    private void actualizarPosicion() {
        movimiento = false;

        if (jump) saltar();

        //si no esta en el aire comprobamos si hay suelo, si no lo hay lo ponemos en caida libre
        if (!aire) {
            if (!EnSuelo(hitbox, datosNivel)) {
                aire = true;
            }
        }

        float xVelocidad = 0;
        if (izquierda) {
            xVelocidad -= velocidadJugador;
            mirandoDerecha = false;
        }
        if (derecha) {
            xVelocidad += velocidadJugador;
            mirandoDerecha =  true;
        }

        if (aire) {
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
        }
    }

    //avanza el contador de animacion y cambia de frame cuando toca
    private void actualizarAnimacion() {
        tickAnim++;

        if(tickAnim >= velocidadAnim) {
            tickAnim = 0;
            indiceAnim++;

            //si llegamos al ultimo frame volvemos al principio y desactivamos el ataque
            if(indiceAnim >= Constantes.ConstantesJugador.GetCantidadSprite(accionJugador)) {
                indiceAnim = 0;
                ataque = false;
            }
        }
    }

    //carga el atlas y recorta cada frame de cada animacion en su posicion correspondiente
    private void cargarAnimaciones() {
        BufferedImage imagen = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        //7 filas de animaciones con hasta 4 frames cada una
        animaciones = new BufferedImage[7][4];

        for (int j = 0; j < animaciones.length; j++) {
            int cantidad = Constantes.ConstantesJugador.GetCantidadSprite(j);

            //cada frame mide 64x40 pixeles en el atlas original sin escalar
            for (int i = 0; i < cantidad; i++) {
                animaciones[j][i] = imagen.getSubimage(i * 64, j * 40, 64, 40);
            }
        }

        System.out.println("Imagen cargada correctamente");
    }

    //para el movimiento del jugador, se llama cuando la ventana pierde el foco
    public void resetDirBooleans(){
        izquierda = derecha = arriba = abajo = false;
    }

    public void setAtaque(boolean ataque){ this.ataque = ataque; }
    public void setSalto(boolean salto) { this.jump = salto; }

    public boolean isAbajo() { return abajo; }
    public boolean isArriba() { return arriba; }
    public boolean isDerecha() { return derecha; }
    public boolean isIzquierda() { return izquierda; }

    public void setAbajo(boolean abajo) { this.abajo = abajo; }
    public void setArriba(boolean arriba) { this.arriba = arriba; }
    public void setDerecha(boolean derecha) { this.derecha = derecha; }
    public void setIzquierda(boolean izquierda) { this.izquierda = izquierda; }
}