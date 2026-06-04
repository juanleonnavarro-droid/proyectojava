package dao;

import dto.PlatoDTO;
import dto.RankingDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase de Acceso a Datos (DAO) encargada de gestionar los platos del menú en
 * la base de datos.
 * <p>
 * Se usan las operaciones CRUD tradicionales mediante PreparedStatement, la
 * ejecución de reportes específicos mediante procedimientos almacenados, y la
 * extracción de datos a través de funciones de la base de datos con
 * CallableStatement.
 * </p>
 *
 * @author Juan Leon Navarro
 */
public class PlatoDAO {

    private Connection conexion;

    /**
     * Construye el DAO de platos asociándole una conexión activa con la base de
     * datos.
     *
     * @param conexion Objeto de conexión Connection que se utilizará para las
     * consultas.
     */
    public PlatoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * Muestra un listado completo de todos los platos registrados en la base de
     * datos.
     * <p>
     * Realiza una consulta global a la tabla PLATO, cada registro se guarda en
     * objetos PlatoDTO para almacenarlos en una lista.
     * </p>
     *
     * @return Un ArrayList con todos los platos encontrados, la lista estará
     * vacía si no hay registros.
     */
    public ArrayList<PlatoDTO> listarPlatos() {
        ArrayList<PlatoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM PLATO";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PlatoDTO p = new PlatoDTO();
                p.setId(rs.getInt("ID"));
                p.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                p.setNombre(rs.getString("NOMBRE"));
                p.setDescripcion(rs.getString("DESCRIPCION"));
                p.setPrecio(rs.getDouble("PRECIO"));
                p.setDisponibilidad(rs.getBoolean("DISPONIBILIDAD"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los platos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca un único plato en la base de datos utilizando su ID.
     *
     * @param id El identificador único del plato que se desea localizar.
     * @return Un objeto PlatoDTO con los datos correspondientes, o null si el
     * ID coincide con ningún registro.
     */
    public PlatoDTO buscarPorId(int id) {
        String sql = "SELECT * FROM PLATO WHERE ID=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PlatoDTO p = new PlatoDTO();
                    p.setId(rs.getInt("ID"));
                    p.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                    p.setNombre(rs.getString("NOMBRE"));
                    p.setDescripcion(rs.getString("DESCRIPCION"));
                    p.setPrecio(rs.getDouble("PRECIO"));
                    p.setDisponibilidad(rs.getBoolean("DISPONIBILIDAD"));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar plato por id: " + e.getMessage());
        }
        return null;
    }

    /**
     * Invoca un procedimiento almacenado para obtener los 5 platos más
     * vendidos.
     * <p>
     * Llama a top5_ventas() y extrae los datos para encapsularlos dentro de
     * instancias de RankingDTO, ordenados de mayor a menor volumen.
     * </p>
     *
     * @return Un ArrayList de objetos RankingDTO que representan el top 5 de
     * platos más vendidos.
     */
    public ArrayList<RankingDTO> obtenerTop5Ventas() {
        ArrayList<RankingDTO> ranking = new ArrayList<>();
        String sql = "{call top5_ventas()}";
        try (CallableStatement cs = conexion.prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                RankingDTO r = new RankingDTO();
                r.setNombrePlato(rs.getString("NOMBRE"));
                r.setTotalVendido(rs.getInt("Total_Vendido"));
                ranking.add(r);
            }
        } catch (Exception e) {
            System.out.println("Error al listar los 5 platos más vendidos: " + e.getMessage());
        }
        return ranking;
    }

    /**
     * Ejecuta una función almacenada para contabilizar los platos con baja
     * rotación
     *
     * @param mes El número identificador del mes objeto del análisis.
     * @return El número entero total de platos que no registraron ninguna venta
     * en ese mes.
     */
    public int obtenerNPlatosSinVentas(int mes) {
        int nplatos = 0;
        String sql = "{?=call platos_baja_rotacion()}";
        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.execute();
            nplatos = cs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error al listar platos sin ventas: " + e.getMessage());
        }
        return nplatos;
    }

    /**
     * Registra un nuevo plato dentro de la base de datos.
     * <p>
     * Transfiere los atributos de un DTO a una sentencia de inserción,
     * omitiendo la columna del identificador ya que está definida como
     * AUTO_INCREMENT.
     * </p>
     *
     * @param p Objeto PlatoDTO cargado con la información correspondiente al
     * nuevo producto.
     * @return true si la inserción afectó correctamente a las filas de la
     * tabla, false en caso de error.
     */
    public boolean insertarPlato(PlatoDTO p) {
        String sql = "INSERT INTO PLATO (ID_CATEGORIA, NOMBRE, DESCRIPCION, PRECIO, DISPONIBILIDAD) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, p.getIdCategoria());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setDouble(4, p.getPrecio());
            ps.setBoolean(5, p.isDisponibilidad());
            int insercion = ps.executeUpdate();
            return insercion > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar plato: " + e.getMessage());
            return false;
        }
    }

    /**
     * Borra permanentemente un registro de plato de la base de datos basándose
     * en su ID.
     * <p>
     * Esta consulta fallará si el identificador del plato está siendo
     * referenciado como clave foránea en la tabla de detalle_reserva.
     * </p>
     *
     * @param p Objeto PlatoDTO del cual se extraerá el identificador único para
     * procesar la baja.
     * @return true si el plato fue suprimido exitosamente, false si no se
     * localizó o falló la restricción SQL.
     */
    public boolean eliminarPlato(PlatoDTO p) {
        String sql = "DELETE FROM PLATO WHERE ID=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            int eliminar = ps.executeUpdate();
            return eliminar > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar plato: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los valores de todos los campos modificables de un plato
     * existente mediante su ID.
     * <p>
     * Sobrescribe las columnas categoría, nombre, descripción, precio y
     * disponibilidad utilizando el ID como filtro de la cláusula WHERE.
     * </p>
     *
     * @param p Objeto PlatoDTO que contiene los nuevos datos de actualización
     * junto con el ID de destino.
     * @return true si los datos del registro se modificaron con éxito en el
     * servidor, false en caso contrario.
     */
    public boolean modificarPlato(PlatoDTO p) {
        String sql = "UPDATE PLATO SET ID_CATEGORIA=?, NOMBRE=?, DESCRIPCION=?, PRECIO=?, DISPONIBILIDAD=? WHERE ID=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, p.getIdCategoria());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setDouble(4, p.getPrecio());
            ps.setBoolean(5, p.isDisponibilidad());
            ps.setInt(6, p.getId());
            int modifi = ps.executeUpdate();
            return modifi > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar plato: " + e.getMessage());
            return false;
        }
    }

}
