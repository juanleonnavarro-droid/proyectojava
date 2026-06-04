package dao;

import dto.ReservaDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase de Acceso a Datos (DAO) encargada de realizar operaciones de las
 * reservas en la base de datos.
 * <p>
 * Implementa consultas mediante PreparedStatement y llamadas a procedimientos
 * almacenados con CallableStatement
 * </p>
 *
 * * @author Juan Leon Navarro
 */
public class ReservaDAO {

    private Connection conexion;

    /**
     * Construye el DAO de reservas inyectándole una conexión activa del
     * sistema.
     *
     * * @param conexion Objeto de conexión Connection compartido.
     */
    public ReservaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * Busca una mesa libre, registra la reserva y ocupa la mesa.
     * <p>
     * El método realiza tres operaciones en la base de datos:
     * <ol>
     * <li>Busca la primera mesa libre que coincida con la ubicación (zona) y
     * que iguale o supere la capacidad requerida.</li>
     * <li>Inserta la reserva asignando de forma aleatoria un empleado con cargo
     * 'Camarero'.</li>
     * <li>Actualiza el estado de la mesa encontrada a 'Reservada'.</li>
     * </ol>
     * </p>
     *
     * * @param reserva Objeto ReservaDTO que contiene los datos base de la
     * solicitud.
     * @param zona Nombre de la ubicación física de la mesa solicitada (Terraza,
     * Salón Principal...).
     * @param numComensales Cantidad de personas asociadas a la reserva.
     * @return true si la mesa fue encontrada y el flujo completado con éxito,
     * false en caso contrario.
     */
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

    /**
     * Invoca un procedimiento almacenado de la base de datos para calcular la
     * recaudación total dividida por turnos.
     * <p>
     * Realiza una llamada a facturacion_por_turno() y mapea los resultados
     * devueltos en una estructura clave-valor
     * </p>
     *
     * * @return Un Map que asocia el nombre del turno (clave) con su
     * facturación total (valor).
     */
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

    /**
     * Actualiza los datos modificables de una reserva existente mediante su ID.
     *
     * * @param r Objeto ReservaDTO que contiene los datos actualizados y el ID
     * de destino.
     * @return true si se modificó al menos una fila en la base de datos, false
     * si hubo un error o no existía el ID.
     */
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

    /**
     * Libera las mesas cuyo tiempo de uso haya expirado.
     * <p>
     * Invoca el procedimiento almacenado verificar_mesas_tiempo(), el cual
     * compara los horarios actuales y vuelve a cambiar a 'Libre' aquellas mesas
     * que lleven ocupadas más de 3 horas.
     * </p>
     */
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

    /**
     * Elimina un registro de reserva del sistema basándose en su ID.
     * <p>
     * Verifica previamente la existencia real de la reserva antes de enviar la
     * instrucción de borrado.
     * </p>
     *
     * * @param id Identificador único de la reserva que se pretende eliminar.
     * @return true si la reserva existía y fue eliminada correctamente, false
     * si no se localizó o falló la consulta.
     */
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

    /**
     * Busca una reserva específica mapeando sus columnas a un objeto de
     * transferencia.
     *
     * * @param id Identificador único de la reserva a buscar.
     * @return Un objeto ReservaDTO que contiene la información de la base de
     * datos, o null si el registro no existe.
     */
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

    /**
     * Obtiene el listado histórico y actual de todas las reservas registradas
     * en la base de datos.
     * <p>
     * Recorre el conjunto de resultados y va instanciando objetos ReservaDTO
     * para empaquetarlos dentro de una lista.
     * </p>
     *
     * * @return Un ArrayList repleto de objetos ReservaDTO. Estará vacío si no
     * hay registros.
     */
    public ArrayList<ReservaDTO> mostrarReservas() {
        ArrayList<ReservaDTO> reservas = new ArrayList<>();
        String sql = "Select * from reserva";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ReservaDTO r = new ReservaDTO();
                r.setId(rs.getInt("ID"));
                r.setDniCliente(rs.getString("DNI_CLIENTE"));
                r.setIdMesa(rs.getInt("ID_MESA"));
                r.setIdEmpleado(rs.getInt("ID_EMPLEADO"));
                r.setFecha(rs.getTimestamp("FECHA").toLocalDateTime());
                r.setComensales(rs.getInt("COMENSALES"));
                r.setImporteTotal(rs.getDouble("IMPORTE_TOTAL"));
                reservas.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar las reservas " + e.getMessage());
        }
        return reservas;
    }
}
