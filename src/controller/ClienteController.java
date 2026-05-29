package controller;

import dao.ClienteDAO;
import db.Conexion;
import dto.ClienteDTO;
import dto.HistorialDTO;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
import util.DatosInvalidosException;

/*
Para hacer:
parte de hacer reserva (todavía no esta hecho el ReservaController)
 */
public class ClienteController {

    private Conexion con = new Conexion();
    private Connection conexion = null;
    private Scanner entrada;

    public ClienteController() {
        conexion = con.abrirConexion();
        this.entrada = new Scanner(System.in);
    }

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
