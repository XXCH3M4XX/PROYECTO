package utils;

import main.Juego;

public class Constantes {

    public static class constantesDelEnemigo{
        public static final int ENEMIGO1 = 0;
        public static final int IDLE = 0;
        public static final int CORRER = 1;
        public static final int ATAQUE = 2;
        public static final int GOLPE = 3;
        public static final int MUERTE = 4;


        public static final int ENEMIGO1_WIDTH_DEFAULT = 72;
        public static final int ENEMIGO1_HEIGHT_DEFAULT = 32;

        public static final int ENEMIGO1_WIDTH = (int)(ENEMIGO1_WIDTH_DEFAULT * Juego.ESCALA);
        public static final int ENEMIGO_HEIGHT = (int)(ENEMIGO1_HEIGHT_DEFAULT * Juego.ESCALA);

        public static final int ENEMIGO1_DRAWOFFSET_X = (int)(36 * Juego.ESCALA); // sube desde 36
        public static final int ENEMIGO1_DRAWOFFSET_Y = (int)(14 * Juego.ESCALA); // baja desde 35

        //metodo para obtener
        public static int getSpriteAmount(int tipoEnemigo, int estadoEnemigo){
            switch(tipoEnemigo){
                case ENEMIGO1:
                    switch (estadoEnemigo){
                        case IDLE:
                            return 1;
                        case CORRER:
                            return 4;
                        case ATAQUE:
                            return 12;
                        case GOLPE:
                            return 6;
                        case MUERTE:
                            return 5;
                    }
            }
            return 0;
        }
    }

    //constantes relacionadas con los elementos visuales del entorno del nivel
    public static class entorno {

        //tamaño original de las montañas en pixeles sin escalar
        public static final int anchuraInicialMontañas = 896;
        public static final int alturaInicialMontañas = 260;

        //tamaño original de las nubes en pixeles sin escalar
        public static final int nubeAnchuraPixeles = 74;
        public static final int nubeAlturaPixeles = 24;

        //tamaño final de las montañas ya escalado segun la escala del juego
        public static final int anchuraMontañas = (int)(anchuraInicialMontañas * Juego.ESCALA);
        public static final int alturaMontañas = (int)(alturaInicialMontañas * Juego.ESCALA);

        //tamaño final de las nubes ya escalado segun la escala del juego
        public static final int anchuraNube = (int)(nubeAnchuraPixeles * Juego.ESCALA);
        public static final int alturaNube = (int)(nubeAlturaPixeles * Juego.ESCALA);
    }

    //con esto definimos el tamaño de los botones del menu
    public static class UI {

        public static class botones {
            //tamaño por defecto de los botones del menu sin escalar
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;

            //tamaño final de los botones del menu ya escalado
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Juego.ESCALA);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Juego.ESCALA);
        }

        public static class BotonesPausa {
            //tamaño por defecto del boton de sonido sin escalar
            public static final int TAMAÑO_SONIDO_PORDEFECTO = 42;

            //tamaño final del boton de sonido ya escalado
            public static final int TAMAÑO_SONIDO = (int) (TAMAÑO_SONIDO_PORDEFECTO * Juego.ESCALA);
        }

        public static class URMBotones {
            //tamaño por defecto de los botones undo/restart/menu sin escalar
            public static final int URM_DEFAULT_SIZE = 56;

            //tamaño final de los botones undo/restart/menu ya escalado
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Juego.ESCALA);
        }

        public static class BotonesVolumen {
            //dimensiones por defecto del boton y slider de volumen sin escalar
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            //dimensiones finales del boton y slider de volumen ya escalados
            public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * Juego.ESCALA);
            public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Juego.ESCALA);
            public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Juego.ESCALA);
        }
    }

    //para saber en que direccion te estas moviendo y que animaciones reproducir
    public static class Direcciones {
        public static final int IZQUIERDA = 0;
        public static final int DERECHA = 1;
        public static final int ARRIBA = 2;
        public static final int ABAJO = 3;
    }

    //estas constantes identifican cada tipo de movimiento
    //y asi otorgar una animacion en base a la constante
    public static class ConstantesJugador {
        public static final int CORRIENDO = 0;
        public static final int PATADA = 1;
        public static final int SALTANDO = 2;
        public static final int CAYENDO = 3;
        public static final int PREDETERMINADO = 5;

        //no esta hecho todavia
        //public static final int GOLPE = 5;

        //devuelve la cantidad de frames que tiene cada animacion
        //necesario para saber hasta donde recorrer el array multidimensional
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