package controller;

import dao.ClienteDAO;
import dao.EmpleadoDAO;
import dao.MesaDAO;
import dao.ReservaDAO;
import db.Conexion;
import dto.ClienteDTO;
import dto.EmpleadoDTO;
import dto.MesaDTO;
import dto.ReservaDTO;
import java.sql.Connection;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import util.DatosInvalidosException;

/**
 * Esta clase controla todo lo relacionado con las reservas. Se encarga de
 * interactuar directamente con el usuario por consola, mostrándole menús,
 * leyendo lo que escribe por teclado y conectando esas respuestas con las bases
 * de datos.
 *
 * @author Juan Leon Navarro
 */
public class ReservaController {

    private Conexion con = new Conexion();
    private Connection conexion;
    private Scanner entrada;
    private String zona;
    DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Prepara el controlador de reservas. Lo que hace es abrir la conexión con
     * la base de datos y configurar el Scanner para recibir lo que escriba el
     * usuario.
     */
    public ReservaController() {
        conexion = con.abrirConexion();
        entrada = new Scanner(System.in, "UTF-8");
    }

    /**
     * Este es el menú público para que un cliente pueda hacer una reserva por
     * su cuenta.
     * <p>
     * El método guía al cliente paso a paso: primero le pide el DNI para
     * comprobar que ya está registrado, si es así, le pregunta en qué zona
     * quiere sentarse, cuántas personas van a ir y en qué fecha y hora. Tras
     * validar que todo sea correcto, busca una mesa libre de forma automática y
     * confirma la reserva.
     * </p>
     */
    public void mostrarMenu() {
        LocalDateTime fechaHoraReserva = null;

        int opcion = 0;
        int numComensales = 1;
        ClienteDAO clientedao = new ClienteDAO(conexion);
        System.out.println("Introduzca su DNI");
        ClienteDTO cliente = clientedao.buscarPorDni(entrada.nextLine());
        if (cliente != null) {
            System.out.println("Cliente encontrado: ");
            System.out.println(cliente.toString());
            do {
                System.out.println("En qué zona quiere estar?? (1. Terraza, 2. Salón Principal, 3. Zona Privada)");
                try {
                    opcion = Integer.parseInt(entrada.nextLine());
                    switch (opcion) {
                        case 1:
                            zona = "Terraza";
                            System.out.println("Zona: Terraza seleccionada");
                            break;
                        case 2:
                            zona = "Salón Principal";
                            System.out.println("Zona: Salón Principal seleccionada");
                            break;
                        case 3:
                            zona = "Zona Privada";
                            System.out.println("Zona: Zona Privada seleccionada");
                            break;
                        default:
                            System.out.println("Elija una opción válida");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Elija un número válido");
                }
            } while (opcion <= 0 || opcion > 3);
            do {
                System.out.println("Cuántas personas sois??");
                numComensales = Integer.parseInt(entrada.nextLine());
                if (numComensales <= 0) {
                    System.out.println("Error, no se puede crear una reserva para menos de 1 persona");
                }
            } while (numComensales < 1);
            do {
                System.out.println("Qué día y hora?? (formato: DD/MM/AAAA HH:MM)");
                try {
                    fechaHoraReserva = LocalDateTime.parse(entrada.nextLine(), formateador);
                } catch (DateTimeException e) {
                    System.out.println("Formato de fecha incorrecto");
                    fechaHoraReserva = null;
                }
            } while (fechaHoraReserva == null);
            ReservaDTO reserva = new ReservaDTO();
            ReservaDAO r = new ReservaDAO(conexion);
            reserva.setDniCliente(cliente.getDni());
            reserva.setFecha(fechaHoraReserva);
            reserva.setComensales(numComensales);
            if (reserva.validarDatos()) {
                if (r.registrarReservaAutomatica(reserva, zona, numComensales)) {
                    System.out.println("Reserva creada con éxito");
                } else {
                    System.out.println("La reserva no se creó correctamente");
                }
            } else {
                try {
                    throw new DatosInvalidosException("Los datos introducidos no son válidos");
                } catch (DatosInvalidosException e) {
                    System.out.println("Los datos introducidos no son válidos");
                }
            }
        } else {
            System.out.println("Cliente no registrado, regístrese antes de hacer una reserva");
        }
    }

    /**
     * Este es el panel de control exclusivo para los administradores.
     * <p>
     * Muestra un menú interactivo con opciones de gestión, tales como:
     * <ul>
     * <li>Ver cuánto dinero se ha facturado en cada turno del día.</li>
     * <li>Modificar las fechas, mesas o camareros de una reserva ya hecha.</li>
     * <li>Liberar de forma automática las mesas de clientes que ya llevan
     * demasiado tiempo comiendo.</li>
     * <li>Cancelar reservas, buscar una concreta por su número o listar todas
     * las que hay.</li>
     * </ul>
     * </p>
     *
     * @throws DatosInvalidosException Si el administrador introduce datos que
     * no cuadran o no son válidos al modificar una reserva.
     */
    public void mostrarMenuAdmin() throws DatosInvalidosException {
        ReservaDTO reserva = new ReservaDTO();
        int idABuscar = 0;
        ReservaDAO r = new ReservaDAO(conexion);
        int opc = 0;
        do {
            System.out.println("Gestión de reservas:");
            System.out.println("1. Obtener facturación diaria por turno");
            System.out.println("2. Modificar reserva");
            System.out.println("3. Verificar mesas excedidas de tiempo");
            System.out.println("4. Cancelar reserva");
            System.out.println("5. Buscar reserva por ID");
            System.out.println("6. Mostrar todas las reservas");
            System.out.println("7. Salir");
            try {
                opc = Integer.parseInt(entrada.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido");
            }
            switch (opc) {
                case 1:
                    System.out.println("Facturación diaria por turno");
                    System.out.println(r.obtenerFacturacionDiariaPorTurno());
                    break;
                case 2:
                    System.out.println("Inserte el ID de la reserva a modificar");
                    try {
                        idABuscar = Integer.parseInt(entrada.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("El ID tiene que ser un número");
                        break;
                    }
                    reserva = r.buscarPorID(idABuscar);
                    if (reserva != null) {
                        System.out.println("Reserva encontrada");
                        System.out.println(reserva.toString());
                        System.out.println("Introduce la fecha modificada (DD/MM/YYYY HH:MM)");
                        try {
                            reserva.setFecha(LocalDateTime.parse(entrada.nextLine(), formateador));
                        } catch (DateTimeException ex) {
                            System.out.println("La fecha es incorrecta");
                            break;
                        }

                        try {
                            MesaDAO m = new MesaDAO(conexion);
                            System.out.println("Introduce el nueva ID de la mesa");
                            reserva.setIdMesa(Integer.parseInt(entrada.nextLine()));
                            MesaDTO mesa = m.buscarMesaPorId(reserva.getIdMesa());
                            if (mesa == null) {
                                System.out.println("No se ha encontrado ninguna mesa con ID " + reserva.getIdMesa());
                                break;
                            }
                            EmpleadoDAO e = new EmpleadoDAO(conexion);
                            System.out.println("Introduce el nuevo ID de empleado");
                            reserva.setIdEmpleado(Integer.parseInt(entrada.nextLine()));
                            EmpleadoDTO emp = e.buscarPorId(reserva.getIdEmpleado());
                            if (emp == null) {
                                System.out.println("No se ha encontrado ningún empleado con ID " + reserva.getIdEmpleado());
                                break;
                            }
                            System.out.println("Introduce el nuevo número de comensales");
                            reserva.setComensales(Integer.parseInt(entrada.nextLine()));
                            if (reserva.getComensales() <= mesa.getCapacidadMaxima()) {
                                if (reserva.validarDatos()) {
                                    boolean modificarOk = r.modificarReserva(reserva);
                                    if (modificarOk) {
                                        System.out.println("Reserva modificada correctamente");
                                        System.out.println(reserva.toString());
                                    } else {
                                        System.out.println("Error al modificar la reserva");
                                    }
                                } else {
                                    try {
                                        throw new DatosInvalidosException("Los datos introducidos no son válidos");
                                    } catch (DatosInvalidosException ex) {
                                        System.out.println("Los datos introducidos no son válidos");
                                    }
                                }
                            } else {
                                System.out.println("La capacidad de la mesa es menor a los comensales que asistirán a la reserva");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("El ID y los comensales tienen que ser número enteros");
                            return;
                        }

                    } else {
                        System.out.println("No se ha encontrado una reserva con ID " + idABuscar);
                    }
                    break;
                case 3:
                    System.out.println("Mesas excedidas de tiempo");
                    r.verificarMesasExcedidasTiempo();
                    break;
                case 4:
                    System.out.println("Introduce el ID de la reserva a cancelar");
                    try {
                        idABuscar = Integer.parseInt(entrada.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("El ID tiene que ser un número entero");
                        break;
                    }
                    reserva = r.buscarPorID(idABuscar);
                    if (reserva != null) {
                        System.out.println("Reserva encontrada");
                        System.out.println(reserva.toString());
                        boolean eliminarOk = r.cancelarReserva(idABuscar);
                        if (eliminarOk) {
                            System.out.println("Reserva cancelada correctamente");
                        } else {
                            System.out.println("Error al cancelar la reserva");
                        }
                    } else {
                        System.out.println("No se ha encontrado la reserva con la ID " + idABuscar);
                    }
                    break;
                case 5:
                    System.out.println("Introduce el ID de la reserva a buscar");
                    try {
                        idABuscar = Integer.parseInt(entrada.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("El ID tiene que ser un número entero");
                        break;
                    }
                    reserva = r.buscarPorID(idABuscar);
                    if (reserva != null) {
                        System.out.println("Reserva encontrada");
                        System.out.println(reserva.toString());
                    } else {
                        System.out.println("No se ha encontrado la reserva con la ID " + idABuscar);
                    }
                    break;
                case 6:
                    System.out.println("Reservas:");
                    ArrayList<ReservaDTO> reservas = r.mostrarReservas();
                    reservas.stream().forEach(re -> System.out.println(re));
                    break;
                case 7:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Introduce una opción válida");
                    break;
            }
        } while (opc != 7);

    }
}
