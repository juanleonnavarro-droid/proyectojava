package controller;

import java.sql.Connection;
import java.util.Scanner;
import util.DatosInvalidosException;

/**
 * Esta se encarga de dar la bienvenida al usuario y de redirigirlo al menú
 * correcto según si es un cliente o un administrador.
 *
 * @author Juan Leon Navarro
 */
public class MainController {

    private Connection conexion;
    private Scanner entrada;

    /**
     * Prepara el controlador principal guardando la conexión a la base de datos
     * y dejando listo el Scanner para cuando el usuario elija una opción.
     *
     * @param conexion La conexión activa con la base de datos.
     */
    public MainController(Connection conexion) {
        this.conexion = conexion;
        entrada = new Scanner(System.in, "UTF-8");
    }

    /**
     * Muestra la primera pantalla que ve el usuario.
     * <p>
     * Pinta en la consola el menú de bienvenida y se queda esperando en un
     * bucle a que elijas una opción. Si pulsas 1, te manda al menú de clientes,
     * si pulsas 2, abre el menú de administrador, y si pulsas 3, cierra el
     * programa. Además, si pones una letra en vez de un número, te avisa sin
     * romper el programa.
     * </p>
     *
     * @throws DatosInvalidosException Por si alguno de los submenús (clientes o
     * admin) maneja datos incorrectos y lanza este error hacia arriba.
     */
    public void arrancarAplicacion() throws DatosInvalidosException {
        int opcion = 0;
        do {
            System.out.println("Bienvenido a Sabores Gourmet");
            System.out.println("1. Acceso Clientes");
            System.out.println("2. Acceso Admin");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(entrada.nextLine());

                switch (opcion) {
                    case 1:
                        ClienteController clienteCtrl = new ClienteController();
                        clienteCtrl.mostrarMenu();
                        break;
                    case 2:
                        AdminController adminCtrl = new AdminController();
                        adminCtrl.mostrarMenu();
                        break;
                    case 3:
                        System.out.println("Cerrando aplicación...");
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtelo de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduzca un número válido.");
            }
        } while (opcion != 3);
    }
}
