package dao;

import dto.ClienteDTO;
import dto.HistorialDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase sirve para gestionar toda la información de los clientes en la
 * base de datos. Desde aquí podemos dar de alta a nuevos clientes, cambiar sus
 * datos, buscarlos por su DNI o ver qué platos piden más y quién es nuestro
 * mejor cliente.
 *
 * @author Juan Leon Navarro
 */
public class ClienteDAO {

    private Connection conexion;

    /**
     * Prepara el gestor de clientes pasándole la conexión que usaremos para
     * conectar con la base de datos.
     *
     * @param conexion La conexión activa.
     */
    public ClienteDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * Registra un nuevo cliente en el sistema con todos sus datos (DNI, nombre,
     * teléfono, etc.).
     *
     * @param c El objeto que contiene toda la información del cliente que
     * queremos guardar.
     * @return true si el cliente se guardó bien en la base de datos, false si
     * hubo algún problema.
     */
    public boolean insertarCliente(ClienteDTO c) {
        String sql = "INSERT INTO CLIENTE (DNI, NOMBRE, TELEFONO, EMAIL, DIRECCION) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getDireccion());
            int insercion = ps.executeUpdate();
            return insercion > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar el cliente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Modifica los datos de un cliente que ya está registrado. Usamos su DNI
     * para buscarlo y actualizamos su nombre, teléfono, email y dirección con
     * los nuevos cambios.
     *
     * @param c El objeto con los datos ya actualizados del cliente.
     * @return true si los datos se cambiaron correctamente, false si falló la
     * actualización.
     */
    public boolean modificar(ClienteDTO c) {
        String sql = "UPDATE CLIENTE SET NOMBRE = ?, TELEFONO = ?, EMAIL = ?, DIRECCION = ? WHERE DNI = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getTelefono());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getDireccion());
            ps.setString(5, c.getDni());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar el cliente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca a un cliente en la base de datos usando su DNI.
     *
     * @param dni El DNI del cliente que estamos intentando localizar.
     * @return Un objeto ClienteDTO con todos los datos del cliente si lo
     * encuentra, o null si no hay nadie registrado con ese DNI.
     */
    public ClienteDTO buscarPorDni(String dni) {
        String sql = "SELECT * FROM CLIENTE WHERE DNI=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClienteDTO cliente = new ClienteDTO();
                    cliente.setDni(rs.getString("DNI"));
                    cliente.setNombre(rs.getString("NOMBRE"));
                    cliente.setDireccion(rs.getString("DIRECCION"));
                    cliente.setEmail(rs.getString("EMAIL"));
                    cliente.setTelefono(rs.getString("TELEFONO"));
                    return cliente;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar al cliente: " + e.getMessage());
        }
        return null;
    }

    /**
     * Saca una lista con todo lo que ha comido un cliente en el restaurante a
     * lo largo del tiempo.
     * <p>
     * Para conseguirlo, llama a un procedimiento interno de la base de datos
     * que revisa todas sus visitas, los platos que pidió, las cantidades y lo
     * que pagó en cada momento.
     * </p>
     *
     * @param dni El DNI del cliente del que queremos ver el historial.
     * @return Una lista de RankingDTO con todo el historial de platos y
     * reservas del cliente, estará vacía si nunca ha pedido nada.
     */
    public List<HistorialDTO> obtenerHistorialConsumo(String dni) {
        List<HistorialDTO> historial = new ArrayList<>();
        String sql = "{call PLATOS_CONSUMIDOS_CLIENTE(?)}";
        try (CallableStatement cs = conexion.prepareCall(sql)) {
            cs.setString(1, dni);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    HistorialDTO fila = new HistorialDTO();
                    fila.setFechaReserva(rs.getTimestamp("Fecha_Reserva").toLocalDateTime());
                    fila.setNombrePlato(rs.getString("Nombre_Plato"));
                    fila.setCantidad(rs.getInt("Cantidad"));
                    fila.setPrecioEnEseMomento(rs.getDouble("Importe_Total"));
                    historial.add(fila);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener el historial del cliente: " + e.getMessage());
        }

        return historial;
    }

    /**
     * Averigua quién es nuestro Cliente VIP (el que más reservas ha hecho).
     * <p>
     * Le pide este dato directamente a un procedimiento de la base de datos y
     * nos devuelve la ficha completa de esa persona.
     * </p>
     *
     * @return Los datos del cliente VIP del restaurante almacenado en un objeto
     * ClienteDTO, o null si por algún motivo no se pudo calcular.
     */
    public ClienteDTO obtenerClienteVip() {
        String sql = "{call CLIENTE_VIP()}";
        try (CallableStatement cs = conexion.prepareCall(sql); ResultSet rs = cs.executeQuery()) {
            if (rs.next()) {
                ClienteDTO vip = new ClienteDTO();
                vip.setDni(rs.getString("DNI"));
                vip.setNombre(rs.getString("NOMBRE"));
                vip.setTelefono(rs.getString("TELEFONO"));
                vip.setEmail(rs.getString("EMAIL"));
                vip.setDireccion(rs.getString("DIRECCION"));
                return vip;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el cliente VIP: " + e.getMessage());
        }
        return null;
    }

}
