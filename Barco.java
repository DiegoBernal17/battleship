package battleship;

public class Barco {
    private int tamanio; // Tamaño del barco
    private boolean orientacion; // true = horizontal > false = vertical
    private Tablero tablero; // El tablero donde se encuentra
    private Casilla[][] casillas; // las casillas
    private int partesRestantes; // por si le han atinado a este barco
    private int posicion_x; // posición inicial en x donde se encuentra
    private int posicion_y; // posicion inicial en y

    /*
     * Constructor de la clase Barco
     * Recibe como parametro el tablero y el tamaño del barcp
     */
    public Barco(Tablero tablero, int tamanio) {
        // Se asignan variables
        this.tablero = tablero;
        this.casillas = tablero.getCasillas();
        this.tamanio = tamanio;
        // Se asigna una orientacion por defecto (horizontal)
        this.orientacion = true;
        // Las partes restantes deben ser el tamaño del barco
        partesRestantes = tamanio;
    }

    /*
     *
     */
    public boolean posicionar(char posicionInicial_x, int posicionInicial_y) {
        boolean posicionado = false;
        posicion_x = Casilla.convertirANumero(posicionInicial_x);
        posicion_y = posicionInicial_y-1;
        Casilla casilla;
        for(int i=0; i<tamanio; i++) {
            try {
                if (orientacion) {
                    casilla = casillas[posicion_y][posicion_x + i];
                } else {
                    casilla = casillas[posicion_y + i][posicion_x];
                }
            } catch(Exception e) {
                revertirPosicionar(i);
                return false;
            }

            if( casilla.getBarco() == null && tablero.checarVecinos(casilla, this) ) {
                casilla.setBarco(this);
                posicionado = true;
            } else {
                revertirPosicionar( i);
                return false;
            }
        }
        return posicionado;
    }

    /*
     * Este método se usa en caso que se esté posicionando el barco y marque un error y se tenga que revertir lo que se hizo
     * recibe como parametro hastaDonde para revertir hasta donde dio el error
     */
    public void revertirPosicionar(int hastaDonde) {
        Casilla casilla;
        for(int i=0; i<hastaDonde; i++) {
            if(orientacion) {
                casilla = casillas[posicion_y][posicion_x+i];
            } else {
                casilla = casillas[posicion_y+i][posicion_x];
            }
            // Se habia asigando un barco pero se revierte poniendo null para que no tenga barco
            casilla.setBarco(null);
        }
    }

    public int getTamanio() {
        return tamanio;
    }

    public boolean getOrientacion() { return orientacion; }

    /*
     * Método que solo cambia la variable orientación a lo contrario que contenga su valor
     * si es true cambia a false y viceversa
     */
    public void cambiaOrientacion() {
        orientacion = !orientacion;
    }

    public int getPartesRestantes() {
        return partesRestantes;
    }

    public void setPartesRestantes(int partesRestantes) {
        this.partesRestantes = partesRestantes;
    }

    public int getPosicion_x() {
        return posicion_x;
    }

    public int getPosicion_y() {
        return posicion_y;
    }
}
