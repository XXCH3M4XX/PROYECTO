package utils;

public class Constantes {

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
