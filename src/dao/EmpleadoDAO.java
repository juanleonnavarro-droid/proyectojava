package dao;

import dto.EmpleadoDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Clase de Acceso a Datos (DAO) encargada de gestionar los datos de los
 * empleados de la base de datos.
 * <p>
 * Implementa las operaciones CRUD haciendo uso de PreparedStatement y la
 * invocación de funciones almacenadas de la base de datos a través de
 * CallableStatement.
 * </p>
 *
 * @author Juan Leon Navarro
 */
public class EmpleadoDAO {

    private Connection conexion;

    /**
     * Construye el DAO de empleados inyectándole una conexión hacia la base de
     * datos.
     *
     * @param conexion Objeto de conexión Connection que utilizará la clase.
     */
    public EmpleadoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * Registra un nuevo empleado en la base de datos.
     * <p>
     * Extrae los datos del DTO y las envía mediante una consulta de inserción.
     * </p>
     *
     * @param em Objeto EmpleadoDTO que contiene los datos del empleado.
     * @return true si el registro se insertó correctamente, false en caso
     * contrario.
     */
    public boolean insertarEmpleado(EmpleadoDTO em) {
        String sql = "INSERT INTO EMPLEADO (NOMBRE, CARGO, TURNO_TRABAJO, ANOS_EXPE) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, em.getNombre());
            ps.setString(2, em.getCargo());
            ps.setString(3, em.getTurno_trabajo());
            ps.setInt(4, em.getAnos_expe());
            int insercion = ps.executeUpdate();
            return insercion > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar el empleado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los datos modificables de un registro de empleado existente
     * utilizando su ID.
     * <p>
     * Modifica las columnas correspondientes al nombre, cargo, turno de trabajo
     * y años de experiencia basándose en el ID como filtrado.
     * </p>
     *
     * @param em Objeto EmpleadoDTO que incluye las modificaciones junto con su
     * ID de destino.
     * @return true si los datos del empleado fueron actualizados con éxito,
     * false si falló la sentencia SQL.
     */
    public boolean modificarEmpleado(EmpleadoDTO em) {
        String sql = "UPDATE EMPLEADO SET NOMBRE=?, CARGO=?, TURNO_TRABAJO=?, ANOS_EXPE=? WHERE ID=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, em.getNombre());
            ps.setString(2, em.getCargo());
            ps.setString(3, em.getTurno_trabajo());
            ps.setInt(4, em.getAnos_expe());
            ps.setInt(5, em.getId());
            int modifi = ps.executeUpdate();
            return modifi > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar empleado: " + e.getMessage());
            return false;
        }

    }

    /**
     * Muestra la información de un único empleado a partir de su ID.
     *
     * @param id El identificador numérico único del empleado que se desea
     * buscar.
     * @return Un objeto EmpleadoDTO que contiene la información del empleado
     * correspondiente, o null si ningún registro coincide con el identificador
     * suministrado.
     */
    public EmpleadoDTO buscarPorId(int id) {
        String sql = "SELECT * FROM EMPLEADO WHERE ID=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EmpleadoDTO empleado = new EmpleadoDTO();
                    empleado.setId(rs.getInt("ID"));
                    empleado.setNombre(rs.getString("NOMBRE"));
                    empleado.setCargo(rs.getString("CARGO"));
                    empleado.setTurno_trabajo(rs.getString("TURNO_TRABAJO"));
                    empleado.setAnos_expe(rs.getInt("ANOS_EXPE"));
                    return empleado;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al encontrar el empleado: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene la lista completa de todos los registros de la tabla de
     * empleados.
     * <p>
     * Se hace una sentencia para sacar la información de todos los empleados y
     * se almacenan en un ArrayList.
     * </p>
     *
     * @return Un ArrayList repleto de instancias EmpleadoDTO, estará vacío si
     * no hay registros.
     */
    public ArrayList<EmpleadoDTO> listarEmpleados() {
        ArrayList<EmpleadoDTO> empleados = new ArrayList<>();
        String sql = "SELECT * FROM EMPLEADO";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                EmpleadoDTO emp = new EmpleadoDTO();
                emp.setId(rs.getInt("ID"));
                emp.setNombre(rs.getString("NOMBRE"));
                emp.setCargo(rs.getString("CARGO"));
                emp.setTurno_trabajo(rs.getString("TURNO_TRABAJO"));
                emp.setAnos_expe(rs.getInt("ANOS_EXPE"));
                empleados.add(emp);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los empleados: " + e.getMessage());
        }
        return empleados;
    }

    /**
     * Invoca una función almacenada para calcular la comisión de un empleado.
     * <p>
     * El método registra el parámetro de salida como tipo Double. Además,
     * realiza la conversión de objetos de fecha LocalDate hacia el formato
     * compatible con el driver JDBC Date.
     * </p>
     *
     * @param id El identificador numérico del empleado.
     * @param inicio Fecha inicial.
     * @param fin Fecha final
     * @return El importe total acumulado como un valor de tipo double.
     */
    public double calcularComision(int id, LocalDate inicio, LocalDate fin) {
        double comision = 0.00;
        String sql = "{? = call calculo_comision_ventas(?, ?, ?)}";
        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.DOUBLE);
            cs.setInt(2, id);
            cs.setDate(3, Date.valueOf(inicio));
            cs.setDate(4, Date.valueOf(fin));
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    comision = rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al calcular la comisión: " + e.getMessage());
        }
        return comision;
    }

}
