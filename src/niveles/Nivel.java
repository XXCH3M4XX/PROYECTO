package niveles;

//clase que representa los datos estructurales de un nivel individual
public class Nivel {
    private int[][] datosNivel;

    //constructor que asigna la matriz de indices para la construccion del mapa
    public Nivel(int[][] datosNivel) {
        this.datosNivel = datosNivel;
    }

    //obtiene el valor del tile en una posicion especifica de la cuadricula
    public int getIndiceSprite(int x, int y) {
        return datosNivel[y][x];
    }

    //devuelve la matriz completa con todos los datos del nivel
    public int[][]  getDatosNivel() {
        return datosNivel;
    }
}