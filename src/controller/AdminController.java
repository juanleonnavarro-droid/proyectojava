package controller;

import dao.EmpleadoDAO;
import dao.MesaDAO;
import dao.PlatoDAO;
import db.Conexion;
import dto.EmpleadoDTO;
import dto.MesaDTO;
import dto.PlatoDTO;
import java.sql.Connection;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AdminController {

    private Conexion con = new Conexion();
    private Connection conexion = null;
    private Scanner entrada;
    private static String usr = "admin";
    private static String pass = "admin";

    public AdminController() {
        conexion = con.abrirConexion();
        entrada = new Scanner(System.in);
    }

    public void mostrarMenu() {
        int intentos = 3;
        String usu;
        String pasw;
        int opcion = 0;
        do {
            if (intentos == 0) {
                System.out.println("Demasiados intentos");
            }
            System.out.println("Introduce el usuario");
            usu = entrada.nextLine();
            System.out.println("Introduce la contraseña");
            pasw = entrada.nextLine();
            if (usu.equals(usr) && pasw.equals(pass)) {
                do {
                    System.out.println("1. Gestión de platos");
                    System.out.println("2. Gestión de empleados");
                    System.out.println("3. Gestión de clientes");
                    System.out.println("4. Gestión de reservas");
                    System.out.println("5. Gestión de mesas");
                    System.out.println("6. Gestión de categorías");
                    System.out.println("7. Salir");
                    try {
                        opcion = Integer.parseInt(entrada.nextLine());
                        switch (opcion) {
                            case 1:
                                menuPlatos();
                                break;
                            case 2:
                                menuEmpleados();
                                break;
                            case 3:
                                ClienteController clientectrl = new ClienteController();
                                clientectrl.mostrarMenuAdmin();
                                break;
                            case 4:
                                ReservaController reservactrl = new ReservaController();
                                reservactrl.mostrarMenu();
                                break;
                            case 5:
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

                } while (opcion != 7);
            } else {
                System.out.println("Contraseña o usuario incorrectos, vuelva a intentarlo (intentos restantes): " + intentos);
                intentos--;
            }

        } while (intentos >= 0 && opcion != 7);

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
    public void menuPlatos() {
        int opc = 0;
        PlatoDTO plato = new PlatoDTO();
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
                opc = Integer.parseInt(entrada.nextLine());
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
                    if (plato.validarDatos()) {
                        boolean insertadoOk = p.insertarPlato(plato);
                        if (insertadoOk) {
                            System.out.println("Se ha insertado el plato correctamente");
                            plato.toString();
                        } else {
                            System.out.println("Error al insertar el plato");
                        }
                    } else {
                        System.out.println("Los datos introducidos no son válidos");
                    }
                    break;
                case 2:
                    System.out.println("Inserta el ID del plato a modificar:");
                    idABuscar = Integer.parseInt(entrada.nextLine());
                    plato = p.buscarPorId(idABuscar);
                    if (plato != null) {
                        System.out.println("Plato encontrado");
                        System.out.println(plato.toString());
                        System.out.println("Introduzca el nombre modificado");
                        plato.setNombre(entrada.nextLine());
                        System.out.println("Introduzca la descripción modificada");
                        plato.setDescripcion(entrada.nextLine());
                        System.out.println("Introduzca el precio modificado");
                        plato.setPrecio(Double.parseDouble(entrada.nextLine()));
                        boolean modificarOk = p.modificarPlato(plato);
                        if (modificarOk) {
                            System.out.println("Se ha modificado el plato correctamente");
                            System.out.println(plato.toString());
                        } else {
                            System.out.println("Error al modificar el plato");
                        }
                    } else {
                        System.out.println("Los datos introducidos no son válidos");
                    }
                    break;
                case 3:
                    System.out.println("Introduce el ID del plato que quiere eliminar");
                    idABuscar = Integer.parseInt(entrada.nextLine());
                    plato = p.buscarPorId(idABuscar);
                    if (plato != null) {
                        System.out.println("Plato encontrado, eliminando plato...");
                        p.eliminarPlato(plato);
                    } else {
                        System.out.println("Plato no encontrado");
                    }
                    break;
                case 4:
                    System.out.println("Lista de platos:");
                    p.listarPlatos();
                    break;
                case 5:
                    System.out.println("Introduzca el ID del plato a buscar:");
                    idABuscar = Integer.parseInt(entrada.nextLine());
                    plato = p.buscarPorId(idABuscar);
                    if (plato != null) {
                        System.out.println("Plato encontrado:");
                        System.out.println(plato.toString());
                    } else {
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
        } while (opc != 8);
    }

    public void menuEmpleados() {
        LocalDate inicio = null;
        LocalDate fin = null;
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int idABuscar;
        EmpleadoDAO e = new EmpleadoDAO(conexion);
        EmpleadoDTO emp = new EmpleadoDTO();
        int opc;
        do {
            System.out.println("Gestión de empleados:");
            System.out.println("1. Insertar empleado");
            System.out.println("2. Modificar empleado");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Listar todos los empleados");
            System.out.println("5. Calcular comisión de empleado");
            System.out.println("6. Salir");
            opc = Integer.parseInt(entrada.nextLine());
            switch (opc) {
                case 1:
                    System.out.println("Inserte el nombre del empleado");
                    emp.setNombre(entrada.nextLine());
                    System.out.println("Inserte el cargo del empleado");
                    emp.setCargo(entrada.nextLine());
                    System.out.println("Inserte el turno de trabajo del empleado");
                    emp.setTurno_trabajo(entrada.nextLine());
                    System.out.println("Inserte los años de experiencia del empleado");
                    emp.setAnos_expe(Integer.parseInt(entrada.nextLine()));
                    if (emp.validarDatos()) {
                        boolean insertarOk = e.insertarEmpleado(emp);
                        if (insertarOk) {
                            System.out.println("Empleado insertado correctamente");
                            System.out.println(emp.toString());
                        } else {
                            System.out.println("Error al insertar el empleado");
                        }
                    } else {
                        System.out.println("Los datos introducidos no son válidos");
                    }
                    break;
                case 2:
                    System.out.println("Inserte el ID del plato que quiere modificar");
                    idABuscar = Integer.parseInt(entrada.nextLine());
                    emp = e.buscarPorId(idABuscar);
                    if (emp != null) {
                        System.out.println("Empleado encontrado");
                        System.out.println(emp.toString());
                        System.out.println("Inserte el nombre modificado");
                        emp.setNombre(entrada.nextLine());
                        System.out.println("Inserte el cargo a modificado");
                        emp.setCargo(entrada.nextLine());
                        System.out.println("Inserte el turno de trabajo modificado");
                        emp.setTurno_trabajo(entrada.nextLine());
                        System.out.println("Inserte los años de experiencia modificados");
                        emp.setAnos_expe(Integer.parseInt(entrada.nextLine()));
                        if (emp.validarDatos()) {
                            boolean modificarOk = e.modificarEmpleado(emp);
                            if (modificarOk) {
                                System.out.println("Empleado modificado");
                                System.out.println(emp.toString());
                            } else {
                                System.out.println("Error al modificar al empleado");
                            }
                        } else {
                            System.out.println("Los datos introducidos son incorrectos");
                        }
                    }
                    break;
                case 4:
                    System.out.println("Introduce el ID del empleado a buscar");
                    idABuscar = Integer.parseInt(entrada.nextLine());
                    emp = e.buscarPorId(idABuscar);
                    if (emp != null) {
                        System.out.println("Empleado encontrado:");
                        System.out.println(emp.toString());
                    } else {
                        System.out.println("No se ha encontrado el empleado");
                    }
                    break;
                case 5:
                    System.out.println("Introduce el ID del empleado para calcular la comisión");
                    idABuscar = Integer.parseInt(entrada.nextLine());
                    emp = e.buscarPorId(idABuscar);
                    if (emp != null) {
                        System.out.println("Empleado encontrado");
                        System.out.println(emp.toString());
                        do {
                            try {
                                System.out.println("Introduce la fecha de inicio (DD/MM/AAAA)");
                                inicio = LocalDate.parse(entrada.nextLine(), formateador);
                                System.out.println("Introduce la fecha de fin (DD/MM/AAAA)");
                                fin = LocalDate.parse(entrada.nextLine(), formateador);
                            } catch (DateTimeException ex) {
                                System.out.println("Formato de fecha incorrecto");
                            }
                            if (inicio.isAfter(fin)) {
                                System.out.println("La fecha de inicio no puede ser después que la de fin");
                            }
                            if (inicio == null || fin == null) {
                                System.out.println("Introduce las 2 fechas");
                            }
                        } while (inicio.isAfter(fin) || inicio == null || fin == null);
                        System.out.println("La comisión del empleado " + emp.getNombre() + " es: " + e.calcularComision(idABuscar, inicio, fin));

                    } else {
                        System.out.println("Empleado no encontrado");
                    }
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Inserta un número válido");
            }
        } while (opc != 6);

    }

    public void menuMesas() {
        String zona="";
        String estado="";
        int opc = 0;
        MesaDTO mesa = new MesaDTO();
        MesaDAO m = new MesaDAO(conexion);
        int idABuscar;
        do {
            System.out.println("Gestión de mesas:");
            System.out.println("1. Insertar mesa");
            System.out.println("2. Modificar mesa");
            System.out.println("3. Eliminar mesa");
            System.out.println("4. Buscar mesa por ID");
            System.out.println("5. Actualizar estado");
            System.out.println("6. Ver ocupación por zona");
            System.out.println("7. Listar todas las mesas");
            System.out.println("8. Salir");
            try {
                opc = Integer.parseInt(entrada.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido");
            }
            switch (opc) {
                case 1:
                    System.out.println("Inserte la capacidad máxima");
                    try {
                        mesa.setCapacidadMaxima(Integer.parseInt(entrada.nextLine()));
                    } catch (NumberFormatException e) {
                        System.out.println("Introduce un número válido");
                        break;
                    }
                    System.out.println("En qué zona está la mesa?? (1. Terraza, 2. Salón Principal, 3. Zona Privada)");
                    try {
                        opc = Integer.parseInt(entrada.nextLine());
                        switch (opc) {
                            case 1:
                                mesa.setUbicacion("Terraza");
                                System.out.println("Zona: Terraza seleccionada");
                                break;
                            case 2:
                                mesa.setUbicacion("Salon principal");
                                System.out.println("Zona: Salón Principal seleccionada");
                                break;
                            case 3:
                                mesa.setUbicacion("Zona Privada");
                                System.out.println("Zona: Zona Privada seleccionada");
                                break;
                            default:
                                System.out.println("Elija una opción válida");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Elija un número válido");
                    }
                    mesa.setEstado("Libre");
                    if (mesa.validarDatos()) {
                        boolean insertarOk = m.insertarMesa(mesa);
                        if (insertarOk) {
                            System.out.println("Mesa insertada correctamente");
                            System.out.println(mesa.toString());
                        } else {
                            System.out.println("Error al insertar la mesa");
                        }
                    } else {
                        System.out.println("Los datos introducidos no son válidos");
                    }
                    break;
                case 2:
                    System.out.println("Inserte el ID de la mesa a modificar");
                    try {
                        idABuscar = Integer.parseInt(entrada.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Inserte un número como ID");
                        break;
                    }
                    mesa = m.buscarMesaPorId(idABuscar);
                    if (mesa != null) {
                        System.out.println("Mesa encontrada");
                        System.out.println(mesa.toString());
                        System.out.println("Introduce la capacidad máxima modificada");
                        try {
                            mesa.setCapacidadMaxima(Integer.parseInt(entrada.nextLine()));
                        } catch (NumberFormatException e) {
                            System.out.println("Introduzca un número válido para la capacidad máxima");
                            break;
                        }
                        System.out.println("En qué zona está la mesa?? (1. Terraza, 2. Salón Principal, 3. Zona Privada)");
                        try {
                            opc = Integer.parseInt(entrada.nextLine());
                            switch (opc) {
                                case 1:
                                    mesa.setUbicacion("Terraza");
                                    System.out.println("Zona: Terraza seleccionada");
                                    break;
                                case 2:
                                    mesa.setUbicacion("Salon principal");
                                    System.out.println("Zona: Salón Principal seleccionada");
                                    break;
                                case 3:
                                    mesa.setUbicacion("Zona Privada");
                                    System.out.println("Zona: Zona Privada seleccionada");
                                    break;
                                default:
                                    System.out.println("Elija una opción válida");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Elija un número válido");
                        }
                        opc = 0;
                        do {
                            System.out.println("Elija el estado de la mesa:");
                            System.out.println("1. Libre");
                            System.out.println("2. Reservada");
                            System.out.println("3. Ocupada");
                            try {
                                opc = Integer.parseInt(entrada.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Elija un número válido");
                            }
                            switch (opc) {
                                case 1:
                                    mesa.setUbicacion("Libre");
                                    break;
                                case 2:
                                    mesa.setUbicacion("Reservada");
                                    break;
                                case 3:
                                    mesa.setUbicacion("Ocupada");
                                    break;
                                default:
                                    System.out.println("Opción inválida");
                            }
                        } while (opc != 1 && opc != 2 && opc != 3);
                        boolean modificarOk = m.modificarMesa(mesa);
                        if (modificarOk) {
                            System.out.println("Se ha modificado la mesa correctamente");
                            System.out.println(mesa.toString());
                        } else {
                            System.out.println("Error al modificar la mesa");
                        }

                    } else {
                        System.out.println("No se ha encontrado la mesa");
                    }
                    break;
                case 3:
                    System.out.println("Introduce el ID de la mesa a eliminar");
                    try {
                        idABuscar = Integer.parseInt(entrada.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("El ID tiene que ser un número entero");
                        break;
                    }
                    mesa = m.buscarMesaPorId(idABuscar);
                    if (mesa != null) {
                        System.out.println("Mesa encontrada");
                        System.out.println(mesa.toString());
                        boolean eliminarOk = m.eliminarMesa(idABuscar);
                        if (eliminarOk) {
                            System.out.println("Mesa eliminada correctamente");
                        } else {
                            System.out.println("Error al eliminar la mesa");
                        }
                    } else {
                        System.out.println("Mesa no encontrada");
                    }
                    break;
                case 4:
                    System.out.println("Introduce el ID de la mesa a eliminar");
                    try {
                        idABuscar = Integer.parseInt(entrada.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("El ID tiene que ser un número entero");
                        break;
                    }
                    mesa = m.buscarMesaPorId(idABuscar);
                    if (mesa != null) {
                        System.out.println("Mesa encontrada");
                        System.out.println(mesa.toString());
                    } else {
                        System.out.println("Mesa no encontrada");
                    }
                    break;
                case 5:
                    System.out.println("Introduzca el ID de la mesa a la que quiere cambiar el estado");
                    try {
                        idABuscar = Integer.parseInt(entrada.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("El ID tiene que ser un número entero");
                        break;
                    }
                    mesa = m.buscarMesaPorId(idABuscar);
                    if (mesa != null) {
                        System.out.println("Mesa encontrada");
                        System.out.println(mesa.toString());
                        do {
                            System.out.println("Elija el estado de la mesa:");
                            System.out.println("1. Libre");
                            System.out.println("2. Reservada");
                            System.out.println("3. Ocupada");
                            try {
                                opc = Integer.parseInt(entrada.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Elija un número válido");
                            }
                            switch (opc) {
                                case 1:
                                    estado = "Libre";
                                    break;
                                case 2:
                                    estado = "Reservada";
                                    break;
                                case 3:
                                    estado = "Ocupada";
                                    break;
                                default:
                                    System.out.println("Opción inválida");
                            }
                        } while (opc != 1 && opc != 2 && opc != 3);
                        boolean actualizarOk=m.actualizarEstado(opc, estado);
                        if(actualizarOk) System.out.println("El estado de la mesa se cambió a "+mesa.getEstado());
                        else System.out.println("Error al modificar el estado");
                    } else System.out.println("No se encontró la mesa");
                    break;
                case 6:
                    System.out.println("De qué zona quieres cosultar la ocupación??");
                    try {
                            opc = Integer.parseInt(entrada.nextLine());
                            switch (opc) {
                                case 1:
                                    zona="Terraza";
                                    System.out.println("Zona: Terraza seleccionada");
                                    break;
                                case 2:
                                    zona="Salón Principal";
                                    System.out.println("Zona: Salón Principal seleccionada");
                                    break;
                                case 3:
                                    zona="Zona Privada";
                                    System.out.println("Zona: Zona Privada seleccionada");
                                    break;
                                default:
                                    System.out.println("Elija una opción válida");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Elija un número válido");
                        }
                    System.out.println("El porcentaje de ocupación en "+zona+" es: "+m.ocupacionPorUbicacion(zona));
                    break;
                case 7:
                    System.out.println("Lista de mesas:");
                    System.out.println(m.listarMesas());
                    break;
                case 8:
                    System.out.println("Saliendo...");
                    break;
                default:
                    throw new AssertionError();
            }
        } while (opc != 8);
    }
}
