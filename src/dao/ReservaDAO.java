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

    public boolean insertarReserva(ReservaDTO r){
        String sql="INSERT INTO RESERVA (FECHA, DNI_CLIENTE, ID_MESA, ID_EMPLEADO, IMPORTE_TOTAL) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(r.getFecha()));
            ps.setString(2, r.getDniCliente());
            ps.setInt(3, r.getIdMesa());
            ps.setInt(4, r.getIdEmpleado());
            ps.setDouble(5, r.getImporteTotal());
            int insercion=ps.executeUpdate();
            return insercion>0;
        } catch (SQLException e) {
            System.out.println("Error al insertar la reserva: "+e.getMessage());
            return false;
        }
    }

    public Map<String, Double> obtenerFacturacionDiariaPorTurno() {
        Map<String, Double> mapaFacturacion = new HashMap<>();
        String sql = "{call facturacion_por_turno()}";
        try (CallableStatement cs = conexion.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                String turno = rs.getString("TURNO_TRABAJO");
                double total = rs.getDouble("Facturacion_Total");
                mapaFacturacion.put(turno, total);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la facturación por turnos: " + e.getMessage());
        }
        return mapaFacturacion;
    }

    public boolean modificarReserva(ReservaDTO r) {
    String sql = "UPDATE RESERVA SET FECHA = ?, ID_MESA = ?, ID_EMPLEADO = ? WHERE ID = ?";

    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setTimestamp(1, java.sql.Timestamp.valueOf(r.getFecha()));
        ps.setInt(2, r.getIdMesa());
        ps.setInt(3, r.getIdEmpleado());
        ps.setInt(4, r.getId());
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
}
