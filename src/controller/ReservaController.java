package controller;

import dao.ClienteDAO;
import db.Conexion;
import dto.ClienteDTO;
import dto.ReservaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ReservaController {

    private Conexion con = new Conexion();
    private Connection conexion;
    private Scanner entrada;
    private String zona;

    public ReservaController() {
        conexion = con.abrirConexion();
        entrada = new Scanner(System.in);
    }

    public void mostrarMenu() {
        LocalDateTime fechaHoraReserva = null;
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
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
                System.out.println("Qué día y hora?? (formato: DD/MM/AAAA HH:MM");
                try {
                    fechaHoraReserva = LocalDateTime.parse(entrada.nextLine(), formateador);
                } catch (DateTimeException e) {
                    System.out.println("Formato de fecha incorrecto");
                    fechaHoraReserva = null;
                }
            } while (fechaHoraReserva == null);
            ReservaDTO reserva = new ReservaDTO();
            reserva.setDniCliente(cliente.getDni());
            reserva.setFecha(fechaHoraReserva);
            reserva.setComensales(numComensales);
            if(crearReserva(reserva)) System.out.println("Reserva creada con éxito");
            else System.out.println("La reserva no se creó correctamente");
        } else{
            System.out.println("Cliente no registrado, regístrese antes de hacer una reserva");
        }
    }

    public boolean crearReserva(ReservaDTO r) {
        String sql = "INSERT INTO RESERVA (DNI_CLIENTE, ID_MESA, ID_EMPLEADO, FECHA, COMENSALES) "
                + "SELECT ?, m.ID, (SELECT ID FROM EMPLEADO WHERE CARGO = 'Camarero' LIMIT 1), ?, ? "
                + "FROM MESA m "
                + "WHERE m.UBICACION = ? AND m.CAPACIDAD_MAXIMA >= ? AND m.ESTADO = 'Libre' "
                + "LIMIT 1";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, r.getDniCliente());
            ps.setTimestamp(2, Timestamp.valueOf(r.getFecha()));
            ps.setInt(3, r.getComensales());
            ps.setString(4, zona);
            ps.setInt(5, r.getComensales());
            int filasInsertadas = ps.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al intentar registrar la reserva: " + e.getMessage());
            return false;

        }
    }
}
