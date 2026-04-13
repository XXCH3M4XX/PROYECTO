package utils;

import main.Juego;

public class Constantes {

    //con esto definimos el tamaño de los botones del menu
    public static class UI{
        public static class botones{
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Juego.ESCALA);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Juego.ESCALA);

        }
        public static class BotonesPausa{
            public static final int TAMAÑO_SONIDO_PORDEFECTO = 42;
            public static final int TAMAÑO_SONIDO = (int) (TAMAÑO_SONIDO_PORDEFECTO * Juego.ESCALA);
        }
        public static class URMBotones{
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Juego.ESCALA);
        }

        public static class BotonesVolumen{
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;
            public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * Juego.ESCALA);
            public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Juego.ESCALA);
            public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Juego.ESCALA);

        }
    }

    //para saber en que direccion te estas moviendo y que animaciones reproducir
    public static class Direcciones{
        public static final int IZQUIERDA = 0;
        public static final int DERECHA = 1;
        public static final int ARRIBA = 2;
        public static final int ABAJO = 3;

    }

    //Estas constantes es para identificar cada tipo de movimiento
    //y asi otorgar una animacion en base a la constante
    public static class ConstantesJugador {
        public static final int CORRIENDO = 0;
        public static final int PATADA = 1;
        public static final int SALTANDO = 2;
        public static final int CAYENDO = 3;
        public static final int PREDETERMINADO = 5;


        //no esta hecho todavia
        //public static final int GOLPE = 5;

        //la cantidad de sprites que tiene cada animacion necesaria para saber cuanto tiene que recorrer
        //en cada animacion el array multidimensional de animacion
        public static int GetCantidadSprite(int accionJugador) {
            switch (accionJugador) {

                case CORRIENDO:
                    return 4;

                case PATADA:
                    return 3;

                case SALTANDO:
                    return 2;

                case CAYENDO:
                    return 3;

                case PREDETERMINADO:
                    return 3;

                default:
                    return 1;
            }
        }
    }
}
