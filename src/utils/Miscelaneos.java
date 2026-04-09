package utils;

import main.Juego;

import java.awt.geom.Rectangle2D;

//clase con funciones auxiliares para el control de colisiones y fisicas
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
    private static boolean solido(float x, float y, int[][] datosNivel) {

        //comprobacion de los limites de la ventana del juego
        if (x < 0 || x >= Juego.GAME_WIDTH || y < 0 || y >= Juego.GAME_HEIGHT) {
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
        return valor != 11;
    }
    public static float GetXPosPared(Rectangle2D.Float hitbox, float xVelocidad) {
        int tileActual = (int)(hitbox.x / Juego.TILES_SIZE);
        //se esta moviendo a la derecha
        if(xVelocidad > 0) {
            int tileXPos = tileActual * Juego.TILES_SIZE;
            int xOffset = (int)(Juego.TILES_SIZE - hitbox.width);
            //el -1 es para que no sobrepase un pixel al tile
            return tileXPos + xOffset -1;
        } else {
            return tileActual + Juego.TILES_SIZE;

        }
    }
    public static float GetYPosTechoOSuelo(Rectangle2D.Float hitbox, float velY) {
        if(velY > 0) {
            // tocamos suelo
            return (float)(Math.floor((hitbox.y + hitbox.height) / Juego.TILES_SIZE) * Juego.TILES_SIZE - hitbox.height);
        } else {
            // tocamos techo
            return (float)(Math.floor(hitbox.y / Juego.TILES_SIZE + 1) * Juego.TILES_SIZE);
        }
    }

}