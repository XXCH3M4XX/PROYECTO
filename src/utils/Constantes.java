package utils;

import main.Juego;

public class Constantes {

    public static final int VELOCIDAD_ANIMACION = 25;
    public static class Proyectiles {
        public static int HUESO_PROYECTIL_ANCHO = 45;
        public static int HUESO_PROYECTIL_ALTO = 45;
        public static int PROYECTIL_ANCHO = (int)(Juego.ESCALA * HUESO_PROYECTIL_ANCHO );
        public static int PROYECTIL_ALTO = (int)(Juego.ESCALA * HUESO_PROYECTIL_ALTO);
        public static final float velocidad = 0.5f * Juego.ESCALA;
    }

    public static class constantesObjetos {

        public static final int POCION_ROJA = 0;
        public static final int POCION_AZUL = 1;
        public static final int BARRIL = 2;
        public static final int CAJA = 3;
        public static final int PINCHO = 4;
        public static final int EH_DERECHA = 5;
        public static final int ESQUELETO = 6;

        public static final int VALOR_POCION_ROJA = 50;
        public static final int VALOR_POCION_AZUL = 100;

        public static final int ANCHO_CONTENEDOR_DEFAULT = 40;
        public static final int ALTO_CONTENEDOR_DEFAULT = 30;
        public static final int ANCHO_CONTENEDOR = (int) (Juego.ESCALA * ANCHO_CONTENEDOR_DEFAULT);
        public static final int ALTO_CONTENEDOR = (int) (Juego.ESCALA * ALTO_CONTENEDOR_DEFAULT);

        public static final int ANCHO_POCION_DEFAULT = 12;
        public static final int ALTO_POCION_DEFAULT = 16;
        public static final int ANCHO_POCION = (int) (Juego.ESCALA * ANCHO_POCION_DEFAULT);
        public static final int ALTO_POCION = (int) (Juego.ESCALA * ALTO_POCION_DEFAULT);

        public static final int PINCHO_WIDTH_DEFAULT = 32;
        public static final int PINCHO_HEIGHT_DEFAULT = 32;
        public static final int PINCHO_WIDTH = (int) (Juego.ESCALA * PINCHO_WIDTH_DEFAULT);
        public static final int PINCHO_HEIGHT = (int) (Juego.ESCALA * PINCHO_HEIGHT_DEFAULT);

        public static final int ANCHO_ESQUELETO_HUESO = 120;
        public static final int ALTO_ESQUELETO_HUESO = 50;
        public static final int ANCHO_EH = (int) (ANCHO_ESQUELETO_HUESO * Juego.ESCALA);
        public static final int ALTO_EH = (int) (ALTO_ESQUELETO_HUESO * Juego.ESCALA);

        public static int obtenerCantidadSprites(int tipoObjeto) {
            switch (tipoObjeto) {
                case POCION_ROJA, POCION_AZUL:
                    return 7;
                case BARRIL, CAJA:
                    return 8;
            }
            return 1;
        }

        public static int getFramesEsqueleto(int estado) {
            switch (estado) {
                case 1: return 5;
                case 2: return 5;
                case 3: return 7;
                case 5: return 3;
                default: return 1;
            }
        }
    }

    public static class constantesDelEnemigo {
        public static final int ENEMIGO1 = 0;
        public static final int DIO = 7;

        public static final int IDLE = 0;
        public static final int IDLE_PATRULLA = 1;
        public static final int CORRER = 2;
        public static final int ATAQUE = 3;
        public static final int GOLPE = 4;
        public static final int MUERTE = 5;

        public static final int ENEMIGO1_WIDTH_DEFAULT = 72;
        public static final int ENEMIGO1_HEIGHT_DEFAULT = 32;

        public static final int ENEMIGO1_WIDTH = (int)(ENEMIGO1_WIDTH_DEFAULT * Juego.ESCALA);
        public static final int ENEMIGO_HEIGHT = (int)(ENEMIGO1_HEIGHT_DEFAULT * Juego.ESCALA);

        public static final int ENEMIGO1_DRAWOFFSET_X = (int)(42 * Juego.ESCALA);
        public static final int ENEMIGO1_DRAWOFFSET_Y = (int)(14 * Juego.ESCALA);

        public static final int DIO_WIDTH_DEFAULT = 64;
        public static final int DIO_HEIGHT_DEFAULT = 40;
        public static final int DIO_WIDTH = (int)(DIO_WIDTH_DEFAULT * Juego.ESCALA);
        public static final int DIO_HEIGHT = (int)(DIO_HEIGHT_DEFAULT * Juego.ESCALA);
        public static final int DIO_DRAWOFFSET_X = (int)(22 * Juego.ESCALA);
        public static final int DIO_DRAWOFFSET_Y = (int)(16 * Juego.ESCALA);

        public static int getSpriteAmount(int tipoEnemigo, int estadoEnemigo){
            switch(tipoEnemigo){
                case ENEMIGO1:
                    switch (estadoEnemigo){
                        case IDLE_PATRULLA: return 6;
                        case IDLE:          return 6;
                        case CORRER:        return 3;
                        case ATAQUE:        return 7;
                        case GOLPE:         return 3;
                        case MUERTE:        return 10;
                    }
                case DIO:
                    //delega directamente en GetCantidadSpriteDio para no duplicar logica
                    return ConstantesDio.GetCantidadSpriteDio(estadoEnemigo);
            }
            return 0;
        }

        public static int getVidaMax(int tipo_enemigo){
            switch(tipo_enemigo){
                case ENEMIGO1: return 10;
                case DIO:      return 120;
                default:       return 1;
            }
        }

        public static int getDañoEnemigo(int tipo_enemigo){
            switch(tipo_enemigo){
                case ENEMIGO1: return 15;
                case DIO:      return 15;
                default:       return 0;
            }
        }
    }

    //constantes relacionadas con los elementos visuales del entorno del nivel
    public static class entorno {

        public static final int anchuraInicialMontañas = 896;
        public static final int alturaInicialMontañas = 260;

        public static final int nubeAnchuraPixeles = 74;
        public static final int nubeAlturaPixeles = 24;

        public static final int anchuraMontañas = (int)(anchuraInicialMontañas * Juego.ESCALA);
        public static final int alturaMontañas = (int)(alturaInicialMontañas * Juego.ESCALA);

        public static final int anchuraNube = (int)(nubeAnchuraPixeles * Juego.ESCALA);
        public static final int alturaNube = (int)(nubeAlturaPixeles * Juego.ESCALA);
    }

    public static class UI {

        public static class botones {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;

            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Juego.ESCALA);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Juego.ESCALA);
        }

        public static class BotonesPausa {
            public static final int TAMAÑO_SONIDO_PORDEFECTO = 42;
            public static final int TAMAÑO_SONIDO = (int) (TAMAÑO_SONIDO_PORDEFECTO * Juego.ESCALA);
        }

        public static class URMBotones {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Juego.ESCALA);
        }

        public static class BotonesVolumen {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * Juego.ESCALA);
            public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Juego.ESCALA);
            public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Juego.ESCALA);
        }
    }

    public static class Direcciones {
        public static final int IZQUIERDA = 0;
        public static final int DERECHA = 1;

    }

    public static class ConstantesJugador {
        public static final int CORRIENDO = 0;
        public static final int PATADA = 1;
        public static final int SALTANDO = 2;
        public static final int CAYENDO = 3;
        public static final int PREDETERMINADO = 5;
        public static final int MUERTE = 8;
        public static final int DAÑO = 7;
        public static final int PUÑETAZO = 9;

        public static int GetCantidadSprite(int accionJugador) {
            switch (accionJugador) {
                case CORRIENDO:     return 4;
                case PATADA:        return 3;
                case SALTANDO:      return 2;
                case CAYENDO:       return 3;
                case PREDETERMINADO:return 3;
                case MUERTE:        return 8;
                case DAÑO:          return 3;
                case PUÑETAZO:      return 8;
                default:            return 1;
            }
        }
    }

    //constantes para dio
    public static class ConstantesDio {
        public static final int CORRIENDO    = 0;
        public static final int GANCHO       = 1;
        public static final int SALTO        = 2;
        public static final int CAYENDO      = 3;
        public static final int PREDETERMINADO = 6;
        public static final int DAÑO         = 7;
        public static final int MUERTEDIO      = 8;

        public static int GetCantidadSpriteDio(int accionJugador) {
            switch (accionJugador) {
                case CORRIENDO:      return 4;
                case GANCHO:         return 5;
                case SALTO:          return 2;
                case CAYENDO:        return 3;
                case PREDETERMINADO: return 2;
                case MUERTEDIO:         return 8;
                case DAÑO:           return 3;
                default:             return 1;
            }
        }
    }
}