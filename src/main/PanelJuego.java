package main;

import inputs.MouseInputs;
import inputs.KeyboardInputs;
import utils.Constantes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import static utils.Constantes.ConstantesJugador.*;
import static utils.Constantes.Direcciones.*;

//esto es como el lienzo del juego
public class PanelJuego extends JPanel {
    private MouseInputs inputRaton;
    //la posicion del rectangulo
    private float xDelta = 100, yDelta = 100;
    private BufferedImage imagen;
    private BufferedImage[][] animaciones;
    private int tickAnim, indiceAnim, velocidadAnim = 30;
    private int accionJugador = PREDETERMINADO;
    private int direccion = -1;
    private boolean movimiento = false;




    public PanelJuego() {

        inputRaton = new MouseInputs(this);
        importarImagen();
        cargarAnimaciones();
        tamañoPantalla();
        setFocusable(true);

        //escuchador de teclado y de raton que sabe cuando lo presionamos, liberamos o mantenemos
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(inputRaton);
        addMouseMotionListener(inputRaton);

    }

    /*
     * Inicializa la matriz de animaciones recortando cada sprite individual
     * de la hoja de imágenes principal basándose en las constantes del jugador.
     */
    private void cargarAnimaciones() {
        animaciones = new BufferedImage[5][8];

        int ancho = 64;
        int alto = 40;
        int fila = 3;

        for(int j = 0; j < animaciones.length; j++) {
            int cantidad = Constantes.ConstantesJugador.GetCantidadSprite(j);

            for(int i = 0; i < cantidad; i++) {
                animaciones[j][i] = imagen.getSubimage(i * 64, j * 40, 64, 40);
            }
        }

    }

    //metodo para importar la imagen con los sprites de jonathan
    private void importarImagen() {
        InputStream entrada = getClass().getResourceAsStream("/Ordenado2.png");
        try {
            imagen = ImageIO.read(entrada);
            System.out.println("Imagen cargada correctamente");
        }catch(IOException e){
            System.out.println("Error encontrando las imagenes.");
        } finally {
            //libera recursos y evita problemas
            try {
                entrada.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    //metodo para ajustar el tamaño de la pantalla, 800p es ideal por el pixelart
    private void tamañoPantalla() {
        Dimension tamaño = new Dimension(1280, 800);
        setPreferredSize(tamaño);
        setMaximumSize(tamaño);
    }

    /*
     * Establece la dirección actual hacia la que se mueve el jugador
     * y activa la bandera de movimiento.
     */
    public void setDireccion(int direccion) {
        this.direccion = direccion;
        movimiento = true;
    }

    /*
     * Permite activar o desactivar el estado de movimiento del jugador.
     */
    public void setMovimiento(boolean movimiento) {
        this.movimiento = movimiento;
    }

    //con la clase Graphics podemos pintar dentro de la pantalla del juego
    //se tiene que llamar igual que la del padre si no no funciona
    public void paintComponent(Graphics g) {

        //llamamos a jpanel (padre) para que haga lo que necesite
        //si no se hace asi puede dar errores
        super.paintComponent(g);
        actualizarAnimacion();
        setAnimacion();
        actualizarPosicion();



        //pintamos la imagen
        //el primer parentesis es la columna y fila de la imagen de sprites
        //el resto de valores es la posicion de la imagen en la pantalla y su tamaño
        //hacemos que se pueda mover con xdelta e ydelta
        g.drawImage(animaciones[accionJugador][indiceAnim], (int)xDelta, (int)yDelta,256,160, null);


    }

    /*
     * Determina la acción actual del jugador (como CORRIENDO o PREDETERMINADO)
     * basándose en si está en movimiento o quieto.
     */
    private void setAnimacion() {
        if(movimiento) {
            accionJugador = CORRIENDO;

        } else {
            accionJugador = PREDETERMINADO;
        }
    }

    /*
     * Modifica las coordenadas X e Y del jugador sumando o restando píxeles
     * según la dirección en la que se esté moviendo actualmente.
     */
    private void actualizarPosicion() {
        if(movimiento) {
            switch (direccion) {
                case IZQUIERDA:
                    xDelta -= 5;
                    break;
                case ARRIBA:
                    yDelta -= 5;
                    break;
                case DERECHA:
                    xDelta += 5;
                    break;
                case ABAJO:
                    yDelta += 5;
                    break;

            }
        }
    }

    /*
     * Controla el tiempo entre fotogramas de la animación para avanzar el índice
     * del sprite actual y reiniciarlo cuando llega al final del ciclo.
     */
    private void actualizarAnimacion() {
        tickAnim++;

        if(tickAnim >= velocidadAnim) {
            tickAnim = 0;

            indiceAnim++;

            if(indiceAnim >= Constantes.ConstantesJugador.GetCantidadSprite(accionJugador)) {
                indiceAnim = 0;
            }
        }
    }
}