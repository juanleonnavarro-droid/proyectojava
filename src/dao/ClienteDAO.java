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

public class ClienteDAO {
    private Connection conexion;

    public ClienteDAO(Connection conexion) {
        this.conexion=conexion;
    }

    public boolean insertarCliente(ClienteDTO c){
        String sql="INSERT INTO CLIENTE (DNI, NOMBRE, TELEFONO, EMAIL, DIRECCION) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement ps=conexion.prepareStatement(sql)){
            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getDireccion());
            int insercion=ps.executeUpdate();
            return insercion>0;
        } catch (SQLException e) {
            System.out.println("Error al insertar el cliente: "+e.getMessage());
            return false;
        }
    }

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

    public ClienteDTO buscarPorDni(String dni){
        String sql="SELECT * FROM CLIENTE WHERE DNI=?";

        try(PreparedStatement ps= conexion.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs= ps.executeQuery()){
                if(rs.next()){
                    ClienteDTO cliente=new ClienteDTO();
                    cliente.setDni(rs.getString("DNI"));
                    cliente.setNombre(rs.getString("NOMBRE"));
                    cliente.setDireccion(rs.getString("DIRECCION"));
                    cliente.setEmail(rs.getString("EMAIL"));
                    cliente.setTelefono(rs.getString("TELEFONO"));
                    return cliente;
                }
            } 
        } catch (Exception e) {
            System.out.println("Error al buscar al cliente: "+e.getMessage());
        }
        return null;
    }

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

    public ClienteDTO obtenerClienteVip(){
        String sql="{call CLIENTE_VIP()}";
        try (CallableStatement cs = conexion.prepareCall(sql);
            ResultSet rs=cs.executeQuery()){
                if(rs.next()){
                    ClienteDTO vip= new ClienteDTO();
                    vip.setDni(rs.getString("DNI"));
                    vip.setNombre(rs.getString("NOMBRE"));
                    vip.setTelefono(rs.getString("TELEFONO"));
                    vip.setEmail(rs.getString("EMAIL"));
                    vip.setDireccion(rs.getString("DIRECCION"));
                    return vip;
                }
        } catch (SQLException e) {
            System.out.println("Error al obtener el cliente VIP: "+e.getMessage());
        }
        return null;
    }

}
