package controller;

import dao.DetalleReservaDAO;
import dao.PlatoDAO;
import dao.ReservaDAO;
import db.Conexion;
import dto.DetalleReservaDTO;
import dto.PlatoDTO;
import dto.ReservaDTO;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;
import util.DatosInvalidosException;

/**
 * Esta clase funciona como el menú interactivo para gestionar los platos que se
 * piden en cada reserva. Permite añadir platos a una mesa, quitarlos, ver la
 * comanda completa de una reserva o buscar un apunte específico.
 *
 * @author Juan Leon Navarro
 */
public class DetalleReservaController {

    private Conexion con = new Conexion();
    private Connection conexion = null;
    private Scanner entrada;

    /**
     * Prepara el controlador de detalles abriendo la conexión con la base de
     * datos y dejando listo el Scanner para las respuestas del usuario.
     */
    public DetalleReservaController() {
        conexion = con.abrirConexion();
        entrada = new Scanner(System.in, "UTF-8");
    }

    /**
     * Abre el menú en la consola con todas las opciones para gestionar los
     * platos de las reservas.
     * <p>
     * Este método se encarga de pintar las opciones en pantalla y controlar los
     * pasos del usuario:
     * <ul>
     * <li>Opción 1 (Insertar): Pide una reserva, comprueba que exista, pide un
     * plato, mira si está disponible en la cocina, pregunta cuántas raciones se
     * quieren y calcula el precio para guardarlo en la cuenta.</li>
     * <li>Opción 2 (Eliminar): Busca una línea de pedido por su ID y la borra
     * (por si el cliente se arrepiente de un plato).</li>
     * <li>Opción 3 (Listar): Muestra en una lista ordenada todo lo que ha
     * pedido una mesa en concreto.</li>
     * <li>Opción 4 (Buscar): Localiza y muestra los datos de una sola línea de
     * consumición usando su ID.</li>
     * </ul>
     * El método está protegido para que, si el usuario escribe una letra en vez
     * de un número, el programa no se rompa y le avise amablemente.
     * </p>
     *
     * @throws DatosInvalidosException Si al intentar guardar un plato en la
     * base de datos los datos introducidos no son correctos o lógicos.
     */
    public void mostrarMenu() throws DatosInvalidosException {
        int opc = 0;
        int idABuscar = 0;
        DetalleReservaDAO drdao = new DetalleReservaDAO(conexion);
        ReservaDAO rdao = new ReservaDAO(conexion);
        do {
            System.out.println("Gestión de detalle de reserva");
            System.out.println("1. Insertar línea de detalle");
            System.out.println("2. Eliminar línea de detalle");
            System.out.println("3. Listar líneas de detalle de une reserva");
            System.out.println("4. Buscar línea de detalle por ID");
            System.out.println("5. Salir");
            try {
                opc = Integer.parseInt(entrada.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("La opción tiene que ser un número entero");
            }
            switch (opc) {
                case 1:
                    int cantidad = 0;
                    DetalleReservaDTO drdto = new DetalleReservaDTO();
                    PlatoDTO pdto = new PlatoDTO();
                    PlatoDAO pdao = new PlatoDAO(conexion);
                    ReservaDTO rdto = new ReservaDTO();
                    System.out.println("Escriba el ID de la reserva");
                    try {
                        idABuscar = Integer.parseInt(entrada.nextLine());
                        rdto = rdao.buscarPorID(idABuscar);
                    } catch (NumberFormatException e) {
                        System.out.println("El ID tiene que ser un número entero");
                        return;
                    }
                    if (rdto != null) {
                        System.out.println("Reserva encontrada");
                        System.out.println(rdto.toString());
                        drdto.setId_reserva(rdto.getId());
                        System.out.println("Escriba el ID del plato a insertar en la línea");
                        try {
                            idABuscar = Integer.parseInt(entrada.nextLine());
                            pdto = pdao.buscarPorId(idABuscar);

                        } catch (NumberFormatException e) {
                            System.out.println("El ID tiene que ser un número entero");
                            return;
                        }
                        if (pdto != null) {
                            if (pdto.isDisponibilidad()) {
                                System.out.println("Plato encontrado");
                                System.out.println(pdto.toString());
                                drdto.setId_plato(pdto.getId());
                                drdto.setPrecio_plato(pdto.getPrecio());
                                do {
                                    System.out.println("Cuánta cantidad de " + pdto.getNombre() + " quieres añadir a la línea");
                                    try {
                                        cantidad = Integer.parseInt(entrada.nextLine());
                                    } catch (NumberFormatException e) {
                                        System.out.println("La cantidad tiene que ser un número entero");
                                    }
                                    if (cantidad <= 0) {
                                        System.out.println("La cantidad tiene que ser mínimo 1");
                                    }
                                } while (cantidad <= 0);
                                drdto.setCantidad(cantidad);
                                if (drdto.validarDatos()) {
                                    boolean insertarOk = drdao.insertarLinea(drdto);
                                    if (insertarOk) {
                                        System.out.println("Línea insertada correctamente");
                                        System.out.println(drdto.toString());
                                    } else {
                                        System.out.println("Error al insertar línea de detalle");
                                    }
                                } else {
                                    try {
                                        throw new DatosInvalidosException("Los datos introducidos no son válidos");
                                    } catch (DatosInvalidosException e) {
                                        e.getMessage();
                                    }
                                }
                            } else {
                                System.out.println("El plato no está disponible");
                            }

                        } else {
                            System.out.println("Plato con ID " + idABuscar + " no encontrado");
                        }
                    } else {
                        System.out.println("No se encontró ninguna reserva con ID " + idABuscar);
                    }
                    break;
                case 2:
                    System.out.println("Inserta el ID de la línea que quiere eliminar");
                    try {
                        idABuscar = Integer.parseInt(entrada.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("El ID tiene que ser un número entero");
                        return;
                    }
                    drdto = drdao.buscarPorId(idABuscar);
                    if (drdto != null) {
                        System.out.println("Línea de detalle encontrada");
                        System.out.println(drdto.toString());
                        boolean eliminarOk = drdao.eliminarLinea(drdto.getId());
                        if (eliminarOk) {
                            System.out.println("Línea eliminada correctamente");
                        } else {
                            System.out.println("Error al eliminar la línea de detalle");
                        }
                    } else {
                        System.out.println("No se ha encontrado ninguna línea de detalle con ID " + idABuscar);
                    }
                    break;
                case 3:
                    System.out.println("Inserte el ID de reserva del que quieres ver las líneas de detalle");
                    try {
                        idABuscar = Integer.parseInt(entrada.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("El ID tiene que ser un número entero");
                        return;
                    }
                    rdto = rdao.buscarPorID(idABuscar);
                    if (rdto != null) {
                        System.out.println("Reserva encontrada");
                        System.out.println(rdto.toString());
                        System.out.println("Lista de líneas de detalle");
                        ArrayList<DetalleReservaDTO> detalles = drdao.listarPorReserva(rdto.getId());
                        detalles.stream().forEach(dr -> System.out.println(dr));
                    } else {
                        System.out.println("No se ha encontrado ninguna reserva con ID " + idABuscar);
                    }
                    break;
                case 4:
                    System.out.println("Escriba el ID de la línea que quiere buscar");
                    try {
                        idABuscar = Integer.parseInt(entrada.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("El ID tiene que ser un número entero");
                        return;
                    }
                    drdto = drdao.buscarPorId(idABuscar);
                    if (drdto != null) {
                        System.out.println("Línea de detalle encontrada");
                        System.out.println(drdto.toString());
                    } else {
                        System.out.println("No se ha encontrado ninguna línea de detalle con ID " + idABuscar);
                    }
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Elige una opción válida");
                    break;
            }
        } while (opc != 5);
    }
}
