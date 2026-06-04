package dao;

import dto.DetalleReservaDTO;
import dto.PlatoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Esta clase se encarga de gestionar los platos individuales que los clientes
 * añaden a sus reservas.
 *
 *
 * @author Juan Leon Navarro
 */
public class DetalleReservaDAO {

    private Connection conexion;

    /**
     * Crea el gestor de detalles de reserva y le pasa la conexión de la base de
     * datos.
     *
     * @param conexion La conexión a la base de datos que vamos a usar.
     */
    public DetalleReservaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * Añade un plato con su cantidad a una reserva concreta.
     * <p>
     * Antes de guardarlo, el método busca el plato por su ID usando el PlatoDAO
     * para comprobar que existe. Si existe, saca de ahí su precio actual
     * automáticamente y lo guarda todo en la base de datos.
     * </p>
     *
     * @param de El objeto con los datos del plato que queremos añadir (reserva,
     * plato y cantidad).
     * @return true si el plato se pudo añadir bien a la reserva, false si el
     * plato no existía o hubo algún fallo en la base de datos.
     */
    public boolean insertarLinea(DetalleReservaDTO de) {
        String sql = "INSERT INTO DETALLE_RESERVA (ID_RESERVA, ID_PLATO, CANTIDAD, PRECIO_PLATO) VALUES (?, ?, ?, ?)";
        PlatoDAO platoDAO = new PlatoDAO(this.conexion);
        PlatoDTO plato = platoDAO.buscarPorId(de.getId_plato());
        if (plato == null) {
            System.out.println("Error: El plato que intentas añadir no existe.");
            return false;
        }
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, de.getId_reserva());
            ps.setInt(2, de.getId_plato());
            ps.setInt(3, de.getCantidad());
            ps.setDouble(4, plato.getPrecio());
            int insercion = ps.executeUpdate();
            return insercion > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar la línea de detalle: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca y devuelve todos los platos que se han pedido dentro de una misma
     * reserva.
     * <p>
     * Sirve para saber todo lo que ha pedido una mesa.
     * </p>
     *
     * @param idReserva El número de identificación de la reserva que queremos
     * consultar.
     * @return Una lista con todos los platos y cantidades de esa reserva. Si no
     * han pedido nada todavía, devolverá la lista vacía.
     */
    public ArrayList<DetalleReservaDTO> listarPorReserva(int idReserva) {
        ArrayList<DetalleReservaDTO> detalles = new ArrayList<>();
        String sql = "SELECT * FROM DETALLE_RESERVA WHERE ID_RESERVA=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleReservaDTO detalle = new DetalleReservaDTO();
                    detalle.setId(rs.getInt("ID"));
                    detalle.setId_reserva(rs.getInt("ID_RESERVA"));
                    detalle.setId_plato(rs.getInt("ID_PLATO"));
                    detalle.setCantidad(rs.getInt("CANTIDAD"));
                    detalle.setPrecio_plato(rs.getDouble("PRECIO_PLATO"));
                    detalles.add(detalle);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los detalles de la reserva: " + e.getMessage());
        }
        return detalles;
    }

    /**
     * Busca un único apunte o línea de plato específica usando su ID.
     *
     * @param id El ID de la línea de detalle que queremos encontrar.
     * @return El objeto DetalleReservaDTO con toda la información de ese plato
     * pedido, o null si no se encuentra ese ID.
     */
    public DetalleReservaDTO buscarPorId(int id) {
        String sql = "SELECT * FROM DETALLE_RESERVA WHERE ID=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DetalleReservaDTO r = new DetalleReservaDTO();
                    r.setId(rs.getInt("ID"));
                    r.setId_reserva(rs.getInt("ID_RESERVA"));
                    r.setId_plato(rs.getInt("ID_PLATO"));
                    r.setCantidad(rs.getInt("CANTIDAD"));
                    r.setPrecio_plato(rs.getDouble("PRECIO_PLATO"));
                    return r;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por ID " + e.getMessage());
        }
        return null;
    }

    /**
     * Borra un plato de la reserva. Viene muy bien por si el cliente se
     * arrepiente y quiere quitar un plato que había pedido antes.
     *
     * @param idDetalle El ID de la línea de plato que queremos quitar.
     * @return true si se eliminó correctamente, false si hubo algún problema o
     * no se encontró el registro.
     */
    public boolean eliminarLinea(int idDetalle) {
        String sql = "DELETE FROM DETALLE_RESERVA WHERE ID=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);
            int eliminar = ps.executeUpdate();
            return eliminar > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar linea: " + e.getMessage());
            return false;
        }
    }

}
