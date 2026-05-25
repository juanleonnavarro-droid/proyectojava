package controller;

import dao.PlatoDAO;
import db.Conexion;
import dto.PlatoDTO;
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

    public void menuPlatos(){
        int opc=0;
        PlatoDTO plato= new PlatoDTO();
        PlatoDAO p = new PlatoDAO(conexion);
        int idABuscar;
        do {
            System.out.println("Gestión de platos:");
            System.out.println("1. Insertar plato");
            System.out.println("2. Modificar plato");
            System.out.println("3. Eliminar plato");
            System.out.println("4. Listar platos");
            System.out.println("5. Buscar por ID");
            System.out.println("6. Obtener top 5 platos más vendidos");
            System.out.println("7. Obtener platos con baja rotación");
            System.out.println("8. Salir");
            
            try {
                opc= Integer.parseInt(entrada.nextLine());    
            } catch (NumberFormatException e) {
                System.out.println("Inserta un número válido");
            }
            switch (opc) {
                case 1:
                    System.out.println("Inserte el nombre del plato");
                    plato.setNombre(entrada.nextLine());
                    System.out.println("Inserte la descripción del plato (opcional)");
                    plato.setDescripcion(entrada.nextLine());
                    System.out.println("Inserte el precio del plato");
                    plato.setPrecio(Double.parseDouble(entrada.nextLine()));
                    if(plato.validarDatos()){
                        boolean insertadoOk=p.insertarPlato(plato);
                        if(insertadoOk){
                            System.out.println("Se ha insertado el plato correctamente");
                            plato.toString();
                        } else{
                            System.out.println("Error al insertar el plato");
                        }
                    } else{
                        System.out.println("Los datos introducidos no son válidos");
                    }
                    break;
                case 2:
                    System.out.println("Inserta el ID del plato a modificar:");
                    idABuscar= Integer.parseInt(entrada.nextLine());
                    plato=p.buscarPorId(idABuscar);
                    if(plato!=null){
                        System.out.println("Plato encontrado");
                        System.out.println(plato.toString());
                        System.out.println("Introduzca el nombre modificado");
                        plato.setNombre(entrada.nextLine());
                        System.out.println("Introduzca la descripción modificada");
                        plato.setDescripcion(entrada.nextLine());
                        System.out.println("Introduzca el precio modificado");
                        plato.setPrecio(Double.parseDouble(entrada.nextLine()));
                        boolean modificarOk=p.modificarPlato(plato);
                        if(modificarOk){
                            System.out.println("Se ha modificado el plato correctamente");
                            System.out.println(plato.toString());
                        } else{
                            System.out.println("Error al modificar el plato");
                        }
                    } else{
                        System.out.println("Los datos introducidos no son válidos");
                    }
                    break;
                case 3:
                    System.out.println("Introduce el ID del plato que quiere eliminar");
                    idABuscar=Integer.parseInt(entrada.nextLine());
                    plato=p.buscarPorId(idABuscar);
                    if(plato!=null){
                        System.out.println("Plato encontrado, eliminando plato...");
                        p.eliminarPlato(plato);
                    } else{
                        System.out.println("Plato no encontrado");
                    }
                    break;
                case 4:
                    System.out.println("Lista de platos:");
                    p.listarPlatos();
                    break;
                case 5:
                    System.out.println("Introduzca el ID del plato a buscar:");
                    idABuscar=Integer.parseInt(entrada.nextLine());
                    plato=p.buscarPorId(idABuscar);
                    if(plato!=null){
                        System.out.println("Plato encontrado:");
                        System.out.println(plato.toString());
                    } else{
                        System.out.println("Plato no encontrado");
                    }
                    break;
                case 6:
                    System.out.println("Top 5 platos más vendidos:");
                    p.obtenerTop5Ventas();
                    break;
                case 7:
                    System.out.println("Sobre qué mes quieres realizar la consulta?");
                    p.obtenerNPlatosSinVentas(Integer.parseInt(entrada.nextLine()));
                    break;
                case 8:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Introduzca una opción válida");
            }
        } while (opc!=8);
        
        

    }
}
