package battleship;

import java.util.Scanner;

public class Juego {
    private Tablero tablero1;
    private Tablero tablero2;
    private boolean turnoJugador; // true = jugador1 > false = jugador2
    private String ganador;
    private SalidasConsola salidas;
    private Scanner sc;

    /*
     * Constructor de la clase Juego
     */
    public Juego() {
        // Se crear las variables del tamaño del tablero y se inicializa por defecto en 10
        int tamanio_x = 10, tamanio_y = 10;
        // Scanner la entrada de datos
        sc = new Scanner(System.in);
        // Inicializar el turno del jugador, quiere decir que el jugador 1 empezará
        turnoJugador = true;

        // Declaramos la variable de la opcion
        int opcion;
        // Iniciamos el ciclo del menú, que se repetirá hasta recibir la opción de salir
        do {
            // Se imprimé el mensaje del menú
            System.out.print("Bienvenido al juego Battleship\n1) Jugar\n2) Salirse\n3) Definir tamaño del tablero\nNúmero de la opción a seleccionar: ");
            // Se lee la opción dada
            opcion = sc.nextInt();
            switch (opcion) {
                // En caso de que su opción haya sido la 1
                case 1:
                    // Iniciamos los tableros 1 y 2 con el tamaño dado (Si no se seleccionó tamaño se usa el de por defecto)
                    tablero1 = new Tablero(tamanio_x, tamanio_y);
                    tablero2 = new Tablero(tamanio_x, tamanio_y);
                    // Se inicializa el objeto de salidas en consola
                    // Esta clase se encarga de manejar las salidas de texto en la consola
                    salidas = new SalidasConsola(tablero1, tablero2);
                    // Limpia la pantalla (Imprime varias lineas en blanco)
                    salidas.limpiarPantalla();
                    // Inicia el juego
                    do {
                        System.out.println("----------------------------------------------------------");
                        if (turnoJugador) {
                            salidas.imprimir("Tú turno");
                            // Pide datos para hacer tiro
                            hacerTiro();
                        } else {
                            salidas.imprimir("Turno del contrincante");
                            // No pide datos, solo hace tiro aleatoriamente a una casilla
                            tiroAleatorio();
                        }
                        // Mientras no haya un ganador se repite
                    } while (ganador == null);

                    salidas.limpiarPantalla();
                    // Aquí imprime al ganador
                    salidas.imprimirGanador(ganador);
                break;
                // Si seleccina la opcion 3 entra aquí
                case 3:
                    // En caso que ingrese un número muy grande o pequeño indicará que hay error y vuelve a pedir el tamaño en X
                    do {
                        // Se empieza a pedir datos para cambiar el tamaño del tablero
                        System.out.print("Tamaño en el eje X (columnas): ");
                        // Se lee numero
                        tamanio_x = sc.nextInt();
                        if(tamanio_x < 8 || tamanio_x > 26) {
                            System.out.println("X > Error, el tablero debe de ser mínimo de 8 casilllas y máximo de 20");
                        }
                        // el tablero mínimo debe de ser de 8 para que quepan todos los barcos y máximo de 26 que es hasta donde llega el abecedario
                    } while(tamanio_x < 8 || tamanio_x > 26);

                    // Pide de la misma forma que X pero ahora en Y
                    do {
                        System.out.print("Tamaño en el eje Y (filas): ");
                        tamanio_y = sc.nextInt();
                        if(tamanio_y < 8 || tamanio_y > 26) {
                            System.out.println("Y > Error, el tablero debe de ser mínimo de 8 casilllas y máximo de 20");
                        }
                    } while(tamanio_y < 8 || tamanio_y > 26);
            }
            // Si la opción dada es 2 se sale (porque se indica en el menú que 2 es para salir)
        } while(opcion != 2);
    }

    /*
     * Método para que el jugador pueda hacer tiros al contricante
     */
    public void hacerTiro() {
        // Checa si quedan aun barcos a cuales hundir y si no quedan termina y no se puede hacer tiro
        if(noQuedanBarcosRestantes())
            return;

        // Muestra los dos tableros
        salidas.mostrarTableros();
        char x;
        int y = -1, x_en_numero;

        do {
            System.out.print("++ Posicion en X > Dame la letra: ");
            x = sc.next().charAt(0);
            x_en_numero = Casilla.convertirANumero(x);
        } while(x_en_numero > tablero2.getColumnas()-1 || x_en_numero < 0);
        do {
            System.out.print("-- Posicion en Y > Dame el número: ");
            try {
                y = sc.nextInt();
            } catch(Exception e) { }
        } while(y > tablero2.getFilas() || y < 0);

        if(tablero2.hacerTiro(x,y)) {
            if(tablero2.getAcertado()) {
                salidas.imprimir("Haz acertado!");
                hacerTiro();
            } else
                cambiarTurno();
        } else {
            salidas.imprimir("Ya haz tirado ahí, intenta de nuevo.");
            hacerTiro();
        }
    }

    /*
     * Método para realizar un tiro a una casilla aleatoria
     */
    public void tiroAleatorio() {
        // Checa que no queden barcos restantes, en caso de que no haya termina aquí el método
        if(noQuedanBarcosRestantes())
            return;

        // Mostrar tableros
        salidas.mostrarTableros();
        // Genera número aleatorio y lo convierte a letra (que sería para el eje X)
        // Genera un número desde 0 hasta máximo el número de columnas del tablero
        char x = Casilla.convertirALetra((int) (Math.random() * tablero1.getColumnas()));
        // Genera número aleatoria y suma 1 porque no puede empezar en cero (No existe como fila, se empieza en 1)
        int y = (int) (Math.random() * tablero1.getFilas() + 1);

        // Checa si es posible hacer el tiro
        if(tablero1.hacerTiro(x,y)) {
            // Si es posible entonces pues realiza el tiro
            // Y ahora checa si el tiro acertó a un barco
            if(tablero1.getAcertado()) {
                System.out.println("Haz acertado!");
                // Y como acertó vuelve a tener otro tiro
                tiroAleatorio();
                // Y si no acierta solo cambia de turno
            } else
                cambiarTurno();
            // Si no es posible hacer el tiro puede que se ya se ha tirado en esa casilla, así que vuelve a intentar el tiro a otra casilla
        } else
            tiroAleatorio();
    }

    /*
     * Este método solo cambia la variable "turnoJugador" al contrario (turnoJugador = false si turnoJugador es true y viceversa)
     */
    public void cambiarTurno() {
        turnoJugador = !turnoJugador;
    }

    /*
     * Método para comprobar si ya no existen barcos para hundir
     * regresa true si ya no hay barcos
     * false si sí hay barcos para seguir hundiendo
     */
    public boolean noQuedanBarcosRestantes() {
        // Se resta el total de barcos generados menos el total de barcos que se han hundido en ambos tableros
        int barcosRestantes_T1 = tablero1.getTotalBarcos() - tablero1.getBarcosAcertados();
        int barcosRestantes_T2 = tablero2.getTotalBarcos() - tablero2.getBarcosAcertados();

        // si el jugador 2 ya no tiene barcos significa que el jugador 1 ha ganado
        if(barcosRestantes_T2 == 0) {
            // Asigana un mensaje del ganador
            ganador = "Haz ganado!";
            return true;
        }
        // Si el tablero del jugador 1 ya no tiene barcos ha ganado el jugador 2
        if(barcosRestantes_T1 == 0) {
            // Asigna el mensaje que ha ganado el programa
            ganador = "Ha ganado el programa";
            return true;
        }
        // si no se ha entrado a las condicionales anteriores pasa directo a aquí
        return false;
    }
}
