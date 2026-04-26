package utils;

import main.Juego;

import java.awt.geom.Rectangle2D;

//funciones auxiliares para el control de colisiones y fisicas
public class Miscelaneos {

    //comprueba si los cuatro puntos de la caja de colision estan libres de obstaculos
    public static boolean puedeMoverse(float x, float y, float width, float height, int[][] datosNivel) {
        //verifica la esquina superior izquierda
        if(solido(x, y, datosNivel)) {
            return false;
        }
        //verifica la esquina superior derecha
        if(solido(x + width, y, datosNivel)) {
            return false;
        }
        //verifica la esquina inferior izquierda
        if(solido(x, y + height, datosNivel)) {
            return false;
        }
        //verifica la esquina inferior derecha
        if(solido(x + width, y + height, datosNivel)) {
            return false;
        }

        //si ningun punto toca un tile solido permite el movimiento
        return true;
    }

    //metodo privado para determinar si una coordenada especifica es solida
    public static boolean solido(float x, float y, int[][] datosNivel) {
        int widthMax = datosNivel[0].length * Juego.TILES_SIZE;
        //comprobacion de los limites de la ventana del juego
        if (x < 0 || x >= widthMax || y < 0 || y >= Juego.GAME_HEIGHT) {
            return true;
        }

        //calculo de la posicion del tile correspondiente segun la coordenada pixel
        int xTile = (int)(x / Juego.TILES_SIZE);
        int yTile = (int)(y / Juego.TILES_SIZE);

        //evita errores de desbordamiento fuera de los limites del array del nivel
        if (xTile < 0 || yTile < 0 || yTile >= datosNivel.length || xTile >= datosNivel[0].length) {
            return true;
        }

        //obtenemos el identificador del sprite en esa posicion
        int valor = datosNivel[yTile][xTile];

        //se considera solido cualquier tile que no sea el indice 11
        return tileSolido((int) xTile, yTile, datosNivel);
    }
    public static boolean tileSolido(int xTile, int yTile, int[][] datosNivel) {
        //evita errores de desbordamiento fuera de los limites del array del nivel
        if (xTile < 0 || yTile < 0 || yTile >= datosNivel.length || xTile >= datosNivel[0].length) {
            return true;
        }

        //obtenemos el identificador del sprite en esa posicion
        int valor = datosNivel[yTile][xTile];

        //se considera solido cua
        return valor != 11;
    }

    //calcula la x donde debe quedar la hitbox pegada a la pared usando la posicion tentativa newx
    public static float GetXPosPared(Rectangle2D.Float hitbox, float xVelocidad, float newX) {
        if (xVelocidad > 0) {
            //yendo a la derecha alineamos el borde derecho con el tile de impacto
            int tileActual = (int)((newX + hitbox.width) / Juego.TILES_SIZE);
            return tileActual * Juego.TILES_SIZE - hitbox.width - 1;
        } else {
            //yendo a la izquierda alineamos el borde izquierdo con el tile de impacto
            int tileActual = (int)(newX / Juego.TILES_SIZE);
            return (tileActual + 1) * Juego.TILES_SIZE + 1;
        }
    }
    //limpieza de codigo
    public static boolean sePuedeAndar(int xInicio, int xFinal, int y, int[][] datosNivel) {
        for (int i = xInicio; i <= xFinal; i++) {
            //comprobamos el centro vertical del tile en coordenadas pixel
            float pixelX = i * Juego.TILES_SIZE + Juego.TILES_SIZE / 2f;
            float pixelY = y * Juego.TILES_SIZE + Juego.TILES_SIZE / 2f;
            if (solido(pixelX, pixelY, datosNivel)) {
                return false;
            }
        }
        return true;
    }
    //este metodo esta aqui porque se puede usar tambien para proyectiles
    //podemos reutilizar el codigo
    public static boolean vistaDespejada(int[][] datosNivel,
                                         Rectangle2D.Float hitbox1,
                                         Rectangle2D.Float hitbox2,
                                         int tileY) {
        int xTile1 = (int)(hitbox1.x / Juego.TILES_SIZE);
        int xTile2 = (int)(hitbox2.x / Juego.TILES_SIZE);

        //¿Hay algun obstaculo?
        if(xTile1 > xTile2) {
            return sePuedeAndar(xTile2, xTile1, tileY, datosNivel);
        } else {
            return sePuedeAndar(xTile1, xTile2, tileY, datosNivel);

        }

    }

    //calcula la y donde debe quedar la hitbox al tocar suelo o techo usando la posicion tentativa newy
    public static float GetYPosTechoOSuelo(Rectangle2D.Float hitbox, float velY, float newY) {
        if (velY > 0) {
            //cayendo: alineamos el borde inferior con la parte superior del tile de suelo
            int tileY = (int)((newY + hitbox.height) / Juego.TILES_SIZE);
            return tileY * Juego.TILES_SIZE - hitbox.height - 1;
        } else {
            //subiendo: alineamos el borde superior con la parte inferior del tile de techo
            int tileY = (int)(newY / Juego.TILES_SIZE);
            return (tileY + 1) * Juego.TILES_SIZE + 1;
        }
    }

    public static boolean esSuelo(Rectangle2D.Float hitbox, float xVelocidad, int[][] datosNivel) {
        float checkY = hitbox.y + hitbox.height + 1;
        //asi evitamos que los enemigos se salgan de las esquinas
        if(xVelocidad > 0) {
            return solido(hitbox.x + xVelocidad, checkY, datosNivel)
                    || solido(hitbox.x + hitbox.width + xVelocidad, checkY, datosNivel);
        } else {

            return solido(hitbox.x + xVelocidad, checkY, datosNivel)
                    || solido(hitbox.x + xVelocidad, checkY, datosNivel);
        }

    }

}