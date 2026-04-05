package entidades;

import utils.Constantes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utils.Constantes.ConstantesJugador.CORRIENDO;
import static utils.Constantes.ConstantesJugador.PREDETERMINADO;
import static utils.Constantes.Direcciones.*;
import static utils.Constantes.Direcciones.ABAJO;

public class Jugador extends entidad {

    private BufferedImage[][] animaciones;
    private int tickAnim, indiceAnim, velocidadAnim = 20;
    private int accionJugador = PREDETERMINADO;
    private boolean izquierda, derecha, arriba, abajo;
    private boolean movimiento = false;
    private float velocidadJugador = 2.0f;

    public Jugador(float x, float y) {
        super(x, y);
        cargarAnimaciones();
    }

    public void update(){

        actualizarPosicion();
        actualizarAnimacion();
        setAnimacion();

    }

    public void render(Graphics g){
        g.drawImage(animaciones[accionJugador][indiceAnim], (int)x, (int)y,256,160, null);
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
        movimiento = false;

        if(izquierda && !derecha){
            x-=velocidadJugador;
            movimiento = true;
        }else if(derecha && !izquierda){
            x+=velocidadJugador;
            movimiento = true;
        }


        if(arriba && !abajo){
            y-=velocidadJugador;
            movimiento = true;
        } else if (abajo && !arriba) {
            y+=velocidadJugador;
            movimiento = true;
        }
    }

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

    private void cargarAnimaciones() {
        InputStream entrada = getClass().getResourceAsStream("/Ordenado2.png");
        try {
            BufferedImage imagen = ImageIO.read(entrada);
            animaciones = new BufferedImage[5][8];
            for(int j = 0; j < animaciones.length; j++) {
                int cantidad = Constantes.ConstantesJugador.GetCantidadSprite(j);

                for(int i = 0; i < cantidad; i++) {
                    animaciones[j][i] = imagen.getSubimage(i * 64, j * 40, 64, 40);
                }
            }
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

    public boolean isAbajo() {
        return abajo;
    }

    public boolean isArriba() {
        return arriba;
    }

    public boolean isDerecha() {
        return derecha;
    }

    public boolean isIzquierda() {
        return izquierda;
    }

    public void setAbajo(boolean abajo) {
        this.abajo = abajo;
    }

    public void setArriba(boolean arriba) {
        this.arriba = arriba;
    }

    public void setDerecha(boolean derecha) {
        this.derecha = derecha;
    }

    public void setIzquierda(boolean izquierda) {
        this.izquierda = izquierda;
    }
}
