package utils;

import entidades.PersonajeEnemigo1;
import entidades.BossFinal;
import main.Juego;
import entidades.EnemigoProyectil;
import objetos.ContenedorJuego;
import objetos.Pinchos;
import objetos.Pocion;
import objetos.Proyectil;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.Juego.TILES_SIZE;
import static utils.Constantes.constantesObjetos.*;

//funciones auxiliares para el control de colisiones, fisicas y carga de niveles
public class Miscelaneos {

    //comprueba si los cuatro puntos de la caja de colision estan libres de obstaculos
    public static boolean puedeMoverse(float x, float y, float width, float height, int[][] datosNivel) {
        //verifica la esquina superior izquierda
        if (solido(x, y, datosNivel)) return false;
        //verifica la esquina superior derecha
        if (solido(x + width, y, datosNivel)) return false;
        //verifica la esquina inferior izquierda
        if (solido(x, y + height, datosNivel)) return false;
        //verifica la esquina inferior derecha
        if (solido(x + width, y + height, datosNivel)) return false;
        //si ningun punto toca un tile solido permite el movimiento
        return true;
    }

    //devuelve true si el centro del proyectil toca un tile solido del nivel
    public static boolean huesoGolpeaNivel(Proyectil p, int[][] datosNivel) {
        return solido(p.getHitbox().x + p.getHitbox().width / 2,
                p.getHitbox().y + p.getHitbox().height / 2, datosNivel);
    }

    //devuelve true si la coordenada en pixeles corresponde a un tile solido o esta fuera de limites
    public static boolean solido(float x, float y, int[][] datosNivel) {
        int widthMax = datosNivel[0].length * TILES_SIZE;

        //fuera de los limites del nivel se considera solido para bloquear el movimiento
        if (x < 0 || x >= widthMax || y < 0) {
            return true;
        }

        //calculo de la posicion del tile correspondiente segun la coordenada pixel
        int xTile = (int)(x / TILES_SIZE);
        int yTile = (int)(y / TILES_SIZE);

        //si esta por debajo del nivel no es solido, permite la caida
        if (xTile < 0 || xTile >= datosNivel[0].length || yTile < 0) {
            return true;
        }
        if (yTile >= datosNivel.length) {
            return false; // ← por debajo del nivel no es solido
        }

        return tileSolido(xTile, yTile, datosNivel);
    }

    //devuelve true si no hay ningun tile solido entre el esqueleto y el jugador en la misma fila
    public static boolean esqueletoPuedeVerJudator(int[][] datosNivel, Rectangle2D.Float hitboxJugador,
                                                   Rectangle2D.Float hitboxEsqueleto, int tileY) {
        int xTileJugador = (int)(hitboxJugador.x / TILES_SIZE);
        int xTileEsqueleto = (int)(hitboxEsqueleto.x / TILES_SIZE);

        if (xTileJugador > xTileEsqueleto) {
            //el jugador esta a la derecha, comprueba desde el tile siguiente al esqueleto
            return direccionesLimpias(xTileEsqueleto + 1, xTileJugador, tileY, datosNivel);
        } else {
            //el jugador esta a la izquierda, comprueba desde el tile siguiente al jugador
            return direccionesLimpias(xTileJugador + 1, xTileEsqueleto, tileY, datosNivel);
        }
    }

    //devuelve true si todos los tiles entre xInicio y xFinal en la fila y estan libres
    public static boolean direccionesLimpias(int xInicio, int xFinal, int y, int[][] datosNivel) {
        for (int i = xInicio; i < xFinal; i++) {
            if (tileSolido(i, y, datosNivel)) return false;
        }
        return true;
    }

    //devuelve true si el tile en la posicion dada es solido, cualquier tile que no sea el 11
    public static boolean tileSolido(int xTile, int yTile, int[][] datosNivel) {
        //evita errores de desbordamiento fuera de los limites del array del nivel
        if (xTile < 0 || yTile < 0 || yTile >= datosNivel.length || xTile >= datosNivel[0].length) {
            return true;
        }
        return datosNivel[yTile][xTile] != 11;
    }

    //calcula la x donde debe quedar la hitbox pegada a la pared usando la posicion tentativa newX
    public static float GetXPosPared(Rectangle2D.Float hitbox, float xVelocidad, float newX) {
        if (xVelocidad > 0) {
            //yendo a la derecha alineamos el borde derecho con el tile de impacto
            int tileActual = (int)((newX + hitbox.width) / TILES_SIZE);
            return tileActual * TILES_SIZE - hitbox.width - 1;
        } else {
            //yendo a la izquierda alineamos el borde izquierdo con el tile de impacto
            int tileActual = (int)(newX / TILES_SIZE);
            return (tileActual + 1) * TILES_SIZE + 1;
        }
    }

    //devuelve true si hay suelo solido bajo la hitbox en la direccion de movimiento dada
    public static boolean esSuelo(Rectangle2D.Float hitbox, float xVelocidad, int[][] datosNivel) {
        float checkY = hitbox.y + hitbox.height + 1;
        if (xVelocidad > 0) {
            return solido(hitbox.x + xVelocidad, checkY, datosNivel)
                    || solido(hitbox.x + hitbox.width + xVelocidad, checkY, datosNivel);
        } else {
            return solido(hitbox.x + xVelocidad, checkY, datosNivel)
                    || solido(hitbox.x + xVelocidad, checkY, datosNivel);
        }
    }

    //devuelve true si no hay ningun tile solido entre las dos hitboxes en la fila dada
    public static boolean vistaDespejada(int[][] datosNivel, Rectangle2D.Float hitbox1,
                                         Rectangle2D.Float hitbox2, int tileY) {
        int xTile1 = (int)(hitbox1.x / TILES_SIZE);
        int xTile2 = (int)(hitbox2.x / TILES_SIZE);

        if (xTile1 > xTile2) {
            return sePuedeAndar(xTile2, xTile1, tileY, datosNivel);
        } else {
            return sePuedeAndar(xTile1, xTile2, tileY, datosNivel);
        }
    }

    //devuelve true si todos los tiles entre xInicio y xFinal en la fila y tienen suelo caminable
    public static boolean sePuedeAndar(int xInicio, int xFinal, int y, int[][] datosNivel) {
        for (int i = xInicio; i <= xFinal; i++) {
            float pixelX = i * TILES_SIZE + TILES_SIZE / 2f;
            float pixelY = y * TILES_SIZE + TILES_SIZE / 2f;
            if (solido(pixelX, pixelY, datosNivel)) return false;
        }
        return true;
    }

    //calcula la y donde debe quedar la hitbox al tocar suelo o techo usando la posicion tentativa newY
    public static float GetYPosTechoOSuelo(Rectangle2D.Float hitbox, float velY, float newY) {
        if (velY > 0) {
            //cayendo: alineamos el borde inferior con la parte superior del tile de suelo
            int tileY = (int)((newY + hitbox.height) / TILES_SIZE);
            return tileY * TILES_SIZE - hitbox.height - 1;
        } else {
            //subiendo: alineamos el borde superior con la parte inferior del tile de techo
            int tileY = (int)(newY / TILES_SIZE);
            return (tileY + 1) * TILES_SIZE + 1;
        }
    }

    //genera la matriz del nivel leyendo los valores de color de cada pixel de la imagen
    public static int[][] conseguirDatosNivel(BufferedImage imagen) {
        int[][] datosNivel = new int[imagen.getHeight()][imagen.getWidth()];
        for (int i = 0; i < imagen.getHeight(); i++) {
            for (int j = 0; j < imagen.getWidth(); j++) {
                Color color = new Color(imagen.getRGB(j, i));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                //los pixeles con verde dominante son objetos, se tratan como tile vacio
                if (g > 150 && g > r * 2 && g > b * 2) {
                    datosNivel[i][j] = 11;
                    continue;
                }

                //el canal rojo determina el indice del tile, se limita a 47 como maximo
                int valor = r;
                if (valor >= 48) valor = 0;
                datosNivel[i][j] = valor;
            }
        }
        return datosNivel;
    }

    //busca en la imagen el pixel con verde 100 y devuelve su posicion como punto de spawn
    public static Point getSpawnJugador(BufferedImage imagen) {
        for (int i = 0; i < imagen.getHeight(); i++) {
            for (int j = 0; j < imagen.getWidth(); j++) {
                Color color = new Color(imagen.getRGB(j, i));
                if (color.getGreen() == 100) {
                    return new Point(j * TILES_SIZE, i * TILES_SIZE);
                }
            }
        }
        //posicion de fallback si no se encuentra el pixel de spawn en la imagen
        return new Point(1 * TILES_SIZE, 1 * TILES_SIZE);
    }

    //busca en la imagen los pixeles de enemigo zombie y crea una instancia por cada uno
    public static ArrayList<PersonajeEnemigo1> getPersonajeEnemigo1(BufferedImage imagen) {
        ArrayList<PersonajeEnemigo1> lista = new ArrayList<>();
        for (int i = 0; i < imagen.getHeight(); i++) {
            for (int j = 0; j < imagen.getWidth(); j++) {
                Color color = new Color(imagen.getRGB(j, i));
                if (color.getGreen() == Constantes.constantesDelEnemigo.ENEMIGO1) {
                    lista.add(new PersonajeEnemigo1(j * TILES_SIZE, i * TILES_SIZE));
                }
            }
        }
        return lista;
    }

    //busca en la imagen los pixeles de Dio (verde == 7) y crea una instancia por cada uno
    public static ArrayList<BossFinal> getPersonajeDio(BufferedImage imagen) {
        ArrayList<BossFinal> lista = new ArrayList<>();
        for (int i = 0; i < imagen.getHeight(); i++) {
            for (int j = 0; j < imagen.getWidth(); j++) {
                Color color = new Color(imagen.getRGB(j, i));
                if (color.getBlue() == Constantes.constantesDelEnemigo.DIO) {
                    lista.add(new BossFinal(j * TILES_SIZE, i * TILES_SIZE));
                }
            }
        }
        return lista;
    }

    //busca en la imagen los pixeles de pocion y crea una instancia por cada uno
    public static ArrayList<Pocion> getPociones(BufferedImage imagen) {
        ArrayList<Pocion> lista = new ArrayList<>();
        for (int i = 0; i < imagen.getHeight(); i++) {
            for (int j = 0; j < imagen.getWidth(); j++) {
                Color color = new Color(imagen.getRGB(j, i));
                int valor = color.getBlue();
                if (valor == POCION_ROJA || valor == POCION_AZUL) {
                    lista.add(new Pocion(j * Juego.TILES_SIZE, i * Juego.TILES_SIZE, valor));
                }
            }
        }
        return lista;
    }

    //busca en la imagen los pixeles de contenedor y crea una instancia por cada uno
    public static ArrayList<ContenedorJuego> getContenedor(BufferedImage imagen) {
        ArrayList<ContenedorJuego> lista = new ArrayList<>();
        for (int i = 0; i < imagen.getHeight(); i++) {
            for (int j = 0; j < imagen.getWidth(); j++) {
                Color color = new Color(imagen.getRGB(j, i));
                int valor = color.getBlue();
                if (valor == BARRIL || valor == CAJA) {
                    lista.add(new ContenedorJuego(j * Juego.TILES_SIZE, i * Juego.TILES_SIZE, valor));
                }
            }
        }
        return lista;
    }

    //busca en la imagen los pixeles de pincho y crea una instancia por cada uno
    public static ArrayList<Pinchos> GetPinchos(BufferedImage img) {
        ArrayList<Pinchos> lista = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int valor = color.getBlue();
                if (valor == PINCHO) {
                    lista.add(new Pinchos(j * Juego.TILES_SIZE, i * Juego.TILES_SIZE, PINCHO));
                }
            }
        }
        return lista;
    }

    //busca en la imagen los pixeles de esqueleto y crea una instancia por cada uno
    public static ArrayList<EnemigoProyectil> getEsqueletoHueso(BufferedImage img) {
        ArrayList<EnemigoProyectil> lista = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int valor = color.getBlue();
                //acepta tanto el tipo ESQUELETO como EH_DERECHA para cubrir ambas orientaciones
                if (valor == ESQUELETO || valor == EH_DERECHA) {
                    lista.add(new EnemigoProyectil(j * Juego.TILES_SIZE, i * Juego.TILES_SIZE, ESQUELETO));
                }
            }
        }
        return lista;
    }
}