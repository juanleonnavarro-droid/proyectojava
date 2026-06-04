package controller;

import dao.ClienteDAO;
import db.Conexion;
import dto.ClienteDTO;
import dto.HistorialDTO;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
import util.DatosInvalidosException;

/**
 * Esta clase sirve como el controlador encargado de todas las pantallas e
 * interacciones con los clientes. Se encarga dar la opción de registrarse o ver
 * sus consumiciones, como de ofrecer herramientas al administrador para buscar,
 * modificar o premiar a los clientes del restaurante.
 *
 * @author Juan Leon Navarro
 */
public class ClienteController {

    private Conexion con = new Conexion();
    private Connection conexion = null;
    private Scanner entrada;

    /**
     * Prepara el controlador de clientes, conectando la aplicación con la base
     * de datos e inicializando el Scanner para capturar las opciones
     * introducidas.
     */
    public ClienteController() {
        conexion = con.abrirConexion();
        this.entrada = new Scanner(System.in, "UTF-8");
    }

    /**
     * Muestra el menú destinado a los propios clientes del restaurante.
     * <p>
     * A través de esta pantalla, un usuario puede interactuar con el sistema
     * para:
     * <ul>
     * <li>Registrarse: Darse de alta introduciendo sus datos personales (DNI,
     * Nombre, Teléfono, etc.).</li>
     * <li>Ver historial de consumo: Introducir su DNI para comprobar la lista
     * de platos y comandas que ha disfrutado anteriormente.</li>
     * <li>Hacer reserva: Saltar directamente al asistente de reservas de
     * mesa.</li>
     * </ul>
     * </p>
     *
     * @throws DatosInvalidosException Si el cliente comete un error de formato
     * o introduce datos que no pasan la validación al registrarse.
     */
    public void mostrarMenu() throws DatosInvalidosException {
        ClienteDTO clienteEncontrado;
        String dniAEncontrar;
        ClienteDAO c = new ClienteDAO(conexion);
        int opcion = 0;

        do {
            System.out.println("Menu clientes");
            System.out.println("1. Registrarse");
            System.out.println("2. Ver historial de consumo");
            System.out.println("3. Hacer reserva");
            System.out.println("4. Salir");
            try {
                opcion = Integer.parseInt(entrada.nextLine());
                switch (opcion) {
                    case 1:
                        ClienteDTO cliente1 = new ClienteDTO();
                        System.out.println("Inserte su DNI:");
                        cliente1.setDni(entrada.nextLine());
                        System.out.println("Inserte su nombre:");
                        cliente1.setNombre(entrada.nextLine());
                        System.out.println("Inserte su teléfono:");
                        cliente1.setTelefono(entrada.nextLine());
                        System.out.println("Inserte su email:");
                        cliente1.setEmail(entrada.nextLine());
                        System.out.println("Inserte su dirección:");
                        cliente1.setDireccion(entrada.nextLine());
                        if (cliente1.validarDatos()) {
                            boolean insertadoOk = c.insertarCliente(cliente1);
                            if (insertadoOk) {
                                System.out.println("Se ha registrado el cliente correctamente");
                                System.out.println(cliente1.toString());
                            } else {
                                System.out.println("Error al insertar el cliente");
                            }
                        } else {
                            try {
                                throw new DatosInvalidosException("Los datos introducidos no son válidos");
                            } catch (DatosInvalidosException e) {
                                System.out.println("Los datos introducidos no son válidos");
                            }
                        }
                        break;
                    case 2:
                        System.out.println("Introduce tu DNI para ver su historial:");
                        dniAEncontrar = entrada.nextLine();
                        clienteEncontrado = c.buscarPorDni(dniAEncontrar);
                        if (clienteEncontrado != null) {
                            System.out.println("Cliente encontrado:");
                            System.out.println(clienteEncontrado.toString());
                            List<HistorialDTO> historial = c.obtenerHistorialConsumo(dniAEncontrar);
                            if (historial.isEmpty()) {
                                System.out.println("Este cliente no tiene consumiciones registradas");
                            } else {
                                System.out.println("Historial del cliente:");
                                historial.stream().forEach(linea -> System.out.println(linea));
                            }
                        } else {
                            System.out.println("Cliente no registrado, para ver su historial de consumo tiene que estar registrado");
                        }
                        break;
                    case 3:
                        ReservaController reservactrl = new ReservaController();
                        reservactrl.mostrarMenu();
                        break;
                    case 4:
                        System.out.println("Saliendo...");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Introduzca un número válido");
            }
        } while (opcion != 4);

    }

    /**
     * Muestra el panel de control avanzado para que el administrador gestione
     * los clientes.
     * <p>
     * Este menú permite a los administradores del restaurante realizar las
     * siguientes tareas de gestión:
     * <ul>
     * <li>Dar de alta de forma manual a un nuevo cliente en el sistema.</li>
     * <li>Buscar la ficha completa de cualquier cliente utilizando su DNI.</li>
     * <li>Modificar o actualizar los datos de contacto de un cliente
     * existente.</li>
     * <li>Auditar el historial completo de visitas y consumiciones de cualquier
     * usuario.</li>
     * <li>Ver cliente VIP del mes: Consulta en la base de datos quién ha sido
     * el cliente estrella que más ha reservado en el mes actual.</li>
     * </ul>
     * </p>
     *
     * @throws DatosInvalidosException Si al modificar o registrar un cliente el
     * administrador introduce campos obligatorios vacíos o incorrectos.
     */
    public void mostrarMenuAdmin() throws DatosInvalidosException {
        ClienteDTO clienteEncontrado;
        String dniAEncontrar;
        ClienteDAO c = new ClienteDAO(conexion);
        int opcion = 0;

        do {
            System.out.println("Menu gestión cliente");
            System.out.println("1. Registrar nuevo cliente");
            System.out.println("2. Buscar cliente");
            System.out.println("3. Modificar cliente");
            System.out.println("4. Ver historial de consumo");
            System.out.println("5. Ver cliente VIP del mes");
            System.out.println("6. Salir");
            try {
                opcion = Integer.parseInt(entrada.nextLine());
                switch (opcion) {
                    case 1:
                        ClienteDTO cliente1 = new ClienteDTO();
                        System.out.println("Inserte el DNI del cliente:");
                        cliente1.setDni(entrada.nextLine());
                        System.out.println("Inserte el nombre cliente:");
                        cliente1.setNombre(entrada.nextLine());
                        System.out.println("Inserte el teléfono del cliente:");
                        cliente1.setTelefono(entrada.nextLine());
                        System.out.println("Inserte el email del cliente:");
                        cliente1.setEmail(entrada.nextLine());
                        System.out.println("Inserte la dirección del cliente:");
                        cliente1.setDireccion(entrada.nextLine());
                        if (cliente1.validarDatos()) {
                            boolean insertadoOk = c.insertarCliente(cliente1);
                            if (insertadoOk) {
                                System.out.println("Se ha registrado el cliente correctamente");
                                System.out.println(cliente1.toString());
                            } else {
                                System.out.println("Error al insertar el cliente");
                            }
                        } else {
                            try {
                                throw new DatosInvalidosException("Los datos introducidos no son válidos");
                            } catch (DatosInvalidosException e) {
                                System.out.println("Los datos introducidos no son válidos");
                            }
                        }
                        break;
                    case 2:
                        System.out.println("Escriba el DNI del cliente que quiere buscar:");
                        dniAEncontrar = entrada.nextLine();
                        clienteEncontrado = c.buscarPorDni(dniAEncontrar);
                        if (clienteEncontrado != null) {
                            System.out.println("Cliente encontrado:");
                            System.out.println(clienteEncontrado.toString());
                        } else {
                            System.out.println("No existe ningún cliente con el DNI " + dniAEncontrar);
                        }
                        clienteEncontrado = null;
                        break;
                    case 3:
                        System.out.println("Escriba el DNI del cliente que quiere modificar:");
                        dniAEncontrar = entrada.nextLine();
                        clienteEncontrado = c.buscarPorDni(dniAEncontrar);
                        if (clienteEncontrado != null) {
                            System.out.println("Cliente encontrado:");
                            System.out.println(clienteEncontrado.toString());
                            System.out.println("Inserte el nombre cliente modificado:");
                            clienteEncontrado.setNombre(entrada.nextLine());
                            System.out.println("Inserte el teléfono del cliente modificado:");
                            clienteEncontrado.setTelefono(entrada.nextLine());
                            System.out.println("Inserte el email del cliente modificado:");
                            clienteEncontrado.setEmail(entrada.nextLine());
                            System.out.println("Inserte la dirección del cliente modificado:");
                            clienteEncontrado.setDireccion(entrada.nextLine());
                            if (clienteEncontrado.validarDatos()) {
                                boolean modificarOk = c.modificar(clienteEncontrado);
                                if (modificarOk) {
                                    System.out.println("Cliente modificado con éxito: ");
                                    System.out.println(clienteEncontrado.toString());
                                } else {
                                    System.out.println("Error al modificar el cliente");
                                }
                            } else {
                                try {
                                    throw new DatosInvalidosException("Los datos introducidos no son válidos");
                                } catch (DatosInvalidosException e) {
                                    System.out.println("Los datos introducidos no son válidos");
                                }
                            }
                        } else {
                            System.out.println("No existe ningún cliente con el DNI " + dniAEncontrar);
                        }
                        break;
                    case 4:
                        System.out.println("Introduce el DNI del cliente que quieres ver el historial:");
                        dniAEncontrar = entrada.nextLine();
                        clienteEncontrado = c.buscarPorDni(dniAEncontrar);
                        if (clienteEncontrado != null) {
                            System.out.println("Cliente encontrado:");
                            System.out.println(clienteEncontrado.toString());
                            List<HistorialDTO> historial = c.obtenerHistorialConsumo(dniAEncontrar);
                            if (historial.isEmpty()) {
                                System.out.println("Este cliente no tiene consumiciones registradas");
                            } else {
                                System.out.println("Historial del cliente:");
                                historial.stream().forEach(linea -> System.out.println(linea));
                            }
                        } else {
                            System.out.println("No existe ningún cliente con DNI " + dniAEncontrar);
                        }
                        break;
                    case 5:
                        ClienteDTO clienteVIP = c.obtenerClienteVip();
                        if (clienteVIP != null) {
                            System.out.println("Cliente VIP del mes:");
                            System.out.println(clienteVIP.toString());
                        } else {
                            System.out.println("No se han registrado reservas ni consumiciones en el mes actual");
                        }
                        break;
                    case 6:
                        System.out.println("Saliendo del menú de gestión de clientes, volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Introduzca un número válido");
            }
        } while (opcion != 6);
    }

}
