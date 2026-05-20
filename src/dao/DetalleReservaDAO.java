package dao;

import dto.DetalleReservaDTO;
import dto.PlatoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class DetalleReservaDAO {
    private Connection conexion;
    
    public DetalleReservaDAO(Connection conexion){
        this.conexion=conexion;
    }

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

    public ArrayList<DetalleReservaDTO> listarPorReserva(int idReserva){
        ArrayList<DetalleReservaDTO> detalles=new ArrayList<>();
        String sql="SELECT * FROM DETALLE_RESERVA WHERE ID=?";
        try (PreparedStatement ps= conexion.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            try(ResultSet rs= ps.executeQuery()){
                while(rs.next()){
                    DetalleReservaDTO detalle= new DetalleReservaDTO();
                    detalle.setId(rs.getInt("ID"));
                    detalle.setId_reserva(rs.getInt("ID_RESERVA"));
                    detalle.setId_plato(rs.getInt("ID_PLATO"));
                    detalle.setCantidad(rs.getInt("CANTIDAD"));
                    detalle.setPrecio_plato(rs.getDouble("PRECIO_PLATO"));
                    detalles.add(detalle);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los detalles de la reserva: "+e.getMessage());
        }
        return detalles;
    }

    public boolean eliminarLinea(int idDetalle){
        String sql="DELETE FROM DETALLE_RESERVA WHERE ID=?";
        try (PreparedStatement ps=conexion.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);
            int eliminar=ps.executeUpdate();
            return eliminar>0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar linea: "+e.getMessage());
            return false;
        }
    }

    

}
