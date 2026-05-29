package dao;

import dto.ReservaDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReservaDAO {

    private Connection conexion;

    public ReservaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public boolean registrarReservaAutomatica(ReservaDTO reserva, String zona, int numComensales) {
        String sqlBuscarMesa = "SELECT id FROM mesa WHERE ubicacion = ? AND capacidad_maxima >= ? AND estado = 'Libre' LIMIT 1";
        String sqlInsertar = "INSERT INTO reserva (dni_cliente, id_mesa, id_empleado, fecha, comensales) "
                + "VALUES (?, ?, (SELECT id FROM empleado WHERE CARGO='Camarero' ORDER BY RAND() LIMIT 1), ?, ?)";
        String sqlActualizarMesa = "UPDATE mesa SET estado = 'Reservada' WHERE id = ?";
        try (PreparedStatement psBuscar = conexion.prepareStatement(sqlBuscarMesa)) {
            psBuscar.setString(1, zona);
            psBuscar.setInt(2, numComensales);
            ResultSet rs = psBuscar.executeQuery();

            if (rs.next()) {
                int idMesaEncontrada = rs.getInt("id");
                try (PreparedStatement psInsertar = conexion.prepareStatement(sqlInsertar)) {
                    psInsertar.setString(1, reserva.getDniCliente());
                    psInsertar.setInt(2, idMesaEncontrada);
                    psInsertar.setTimestamp(3, java.sql.Timestamp.valueOf(reserva.getFecha()));
                    psInsertar.setInt(4, numComensales);

                    psInsertar.executeUpdate();
                }
                try (PreparedStatement psActualizar = conexion.prepareStatement(sqlActualizarMesa)) {
                    psActualizar.setInt(1, idMesaEncontrada);
                    psActualizar.executeUpdate();
                }

                return true;
            } else {
                System.out.println("No hay mesas libres en esa zona con esa capacidad.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error en el proceso de reserva: " + e.getMessage());
            return false;
        }
    }

    public Map<String, Double> obtenerFacturacionDiariaPorTurno() {
        Map<String, Double> mapaFacturacion = new HashMap<>();
        String sql = "{call facturacion_por_turno()}";
        try (CallableStatement cs = conexion.prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                String turno = rs.getString("Turno");
                double total = rs.getDouble("Facturacion_Total");
                mapaFacturacion.put(turno, total);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la facturación por turnos: " + e.getMessage());
        }
        return mapaFacturacion;
    }

    public boolean modificarReserva(ReservaDTO r) {
        String sql = "UPDATE RESERVA SET FECHA = ?, ID_MESA = ?, ID_EMPLEADO = ?, COMENSALES = ? WHERE ID = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(r.getFecha()));
            ps.setInt(2, r.getIdMesa());
            ps.setInt(3, r.getIdEmpleado());
            ps.setInt(4, r.getComensales());
            ps.setInt(5, r.getId());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar la reserva: " + e.getMessage());
            return false;
        }
    }

    public void verificarMesasExcedidasTiempo() {
        String sql = "{call verificar_mesas_tiempo()}";

        try (CallableStatement cs = conexion.prepareCall(sql)) {
            int filasModificadas = cs.executeUpdate();

            if (filasModificadas > 0) {
                System.out.println("Se han liberado mesas que excedieron las 3 horas.");
            } else {
                System.out.println("Todas las mesas ocupadas están dentro del tiempo correcto.");
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar el procedimiento de control de tiempo: " + e.getMessage());
        }
    }

    public boolean cancelarReserva(int id) {
        ReservaDTO r = buscarPorID(id);
        if (r != null) {
            String sql = "DELETE FROM RESERVA WHERE ID=?";
            try (PreparedStatement ps = conexion.prepareCall(sql)) {
                ps.setInt(1, id);
                int eliminado = ps.executeUpdate();
                return eliminado > 0;
            } catch (SQLException e) {
                System.out.println("Error al cancelar la reserva: " + e.getMessage());
                return false;
            }
        } else {
            return false;
        }

    }

    public ReservaDTO buscarPorID(int id) {
        
        String sql = "SELECT * FROM RESERVA WHERE ID=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReservaDTO r = new ReservaDTO();
                    r.setId(rs.getInt("ID"));
                    r.setIdMesa(rs.getInt("ID_MESA"));
                    r.setIdEmpleado(rs.getInt("ID_EMPLEADO"));
                    r.setDniCliente(rs.getString("DNI_CLIENTE"));
                    r.setFecha(rs.getTimestamp("FECHA").toLocalDateTime());
                    r.setImporteTotal(rs.getInt("IMPORTE_TOTAL"));
                    r.setComensales(rs.getInt("COMENSALES"));
                    return r;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al encontrar la reserva: " + e.getMessage());
        }
        return null;
    }
}
