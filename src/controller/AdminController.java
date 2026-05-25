package controller;

import db.Conexion;
import java.sql.Connection;
import java.util.Scanner;

public class AdminController {
    private Conexion con= new Conexion();
    private Connection conexion=null;
    private Scanner entrada;
    private static String usr="admin";
    private static String pass="admin";

    public AdminController() {
        conexion = con.abrirConexion();
        entrada=new Scanner(System.in);
    }

    public void mostrarMenu(){
        int intentos=3;
        String usu;
        String pasw;
        int opcion=0;
        do{
        if(intentos==0)System.out.println("Demasiados intentos");
        System.out.println("Introduce el usuario");
        usu=entrada.nextLine();
        System.out.println("Introduce la contraseña");
        pasw=entrada.nextLine();
        if(usu.equals(usr) && pasw.equals(pass)){
            do {
                System.out.println("1. Gestión de platos");
                System.out.println("2. Gestión de empleados");
                System.out.println("3. Gestión de clientes");
                System.out.println("4. Gestión de reservas");
                System.out.println("5. Gestión de mesas");
                System.out.println("6. Gestión de categorías");
                System.out.println("7. Salir");
                try {
                    opcion=Integer.parseInt(entrada.nextLine());
                    switch (opcion) {
                        case 1:
                            
                            break;
                        case 2:

                            break;
                        case 3:
                            ClienteController clientectrl= new ClienteController();
                            clientectrl.mostrarMenuAdmin();
                            break;
                        case 4:
                            ReservaController reservactrl= new ReservaController();
                            reservactrl.mostrarMenu();
                            break;
                        case 7:
                            System.out.println("Saliendo...");
                            break;
                        default:
                            throw new AssertionError();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Introduce un número válido");
                }
                
            } while (opcion!=7);
        } else{
            System.out.println("Contraseña o usuario incorrectos, vuelva a intentarlo (intentos restantes): "+intentos);
            intentos--;
        }
        
    } while(intentos >=0 && opcion!=7);
    
    }
    /*
    Para hacer:
    menu de clientes
    subcontrolador de platos
    subcontrolador de empleados
    hacer el menú del controlador de reservas e implementarlo
    subcontrolador de mesas
    subcontrolador de categorias
    */
}
