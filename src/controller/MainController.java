package controller;

import java.sql.Connection;
import java.util.Scanner;

public class MainController {
    private Connection conexion;
    private Scanner entrada;
    public MainController(Connection conexion) {
        this.conexion = conexion;
        entrada=new Scanner(System.in);
    }

    public void arrancarAplicacion(){
        int opcion=0;
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
                        AdminController adminCtrl= new AdminController();
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
        } while (opcion!=3);
    }
}
