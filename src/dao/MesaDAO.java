package dao;

import dto.MesaDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MesaDAO {
    private Connection conexion;

    public MesaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public boolean insertarMesa(MesaDTO m){
        String sql="INSERT INTO MESA (CAPACIDAD_MAXIMA, UBICACION, ESTADO) VALUES(?, ?, ?)";
        try (PreparedStatement ps=conexion.prepareStatement(sql)) {
            ps.setInt(1, m.getCapacidadMaxima());
            ps.setString(2, m.getUbicacion());
            ps.setString(3, m.getEstado());
            int insercion=ps.executeUpdate();
            return insercion>0;
        } catch (SQLException e) {
            System.out.println("Error al insertar la mesa: "+e.getMessage());
            return false;
        }
    }

    public boolean modificarMesa(MesaDTO m){
        String sql="UPDATE MESA SET CAPACIDAD_MAXIMA=?, UBICACION=?, ESTADO=? WHERE ID=?";
        try (PreparedStatement ps=conexion.prepareStatement(sql)) {
            ps.setInt(1, m.getCapacidadMaxima());
            ps.setString(2, m.getUbicacion());
            ps.setString(3, m.getEstado());
            int modifi=ps.executeUpdate();
            return modifi>0;
        } catch (SQLException e) {
            System.out.println("Error al modificar la mesa: "+e.getMessage());
            return false;
        }
    }

    public boolean eliminarMesa(int id){
        String sql="DELETE FROM MESA WHERE ID=?";
        try (PreparedStatement ps= conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            int eliminar=ps.executeUpdate();
            return eliminar>0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar la mesa: "+e.getMessage());
            return false;
        }
    }


    public MesaDTO buscarMesaPorId(int id){
        MesaDTO m= new MesaDTO();
        String sql="SELECT * FROM MESA WHERE ID=?";
        try (PreparedStatement ps= conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs=ps.executeQuery()) {
                if(rs.next()){
                    m.setId(rs.getInt("ID"));
                    m.setCapacidadMaxima(rs.getInt("CAPACIDAD_MAXIMA"));
                    m.setUbicacion(rs.getString("UBICACION"));
                    m.setEstado(rs.getString("ESTADO"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por id: "+e.getMessage());
        }
        return m;
    }


    public boolean actualizarEstado(int id, String nuevoEstado){
        MesaDTO m= new MesaDTO();
        String sql="UPDATE MESA SET ESTADO=? WHERE ID=?";
        try (PreparedStatement ps=conexion.prepareStatement(sql)) {
            ps.setString(1, m.getEstado());
            ps.setInt(2, m.getId());
            int modifi=ps.executeUpdate();
            return modifi>0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el estado de la mesa: "+e.getMessage());
            return false;
        }
    }

    public double ocupacionPorUbicacion(String zona){
        double porcentaje=0.00;
        String sql="{call ocupacion_ubi(?)}";
        try (CallableStatement cs=conexion.prepareCall(sql)) {
            cs.setString(1, zona);
            try (ResultSet rs= cs.executeQuery()) {
                if(rs.next()){
                    porcentaje=rs.getDouble("porcentaje");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al calcular la ocupación por ubicación: "+e.getMessage());
        }
        return porcentaje;
    }

    public ArrayList<MesaDTO> listarMesas(){
        ArrayList<MesaDTO> lista= new ArrayList<>();
        String sql="SELECT * FROM MESA";
        try (PreparedStatement ps= conexion.prepareStatement(sql);
            ResultSet rs= ps.executeQuery()) {
                while(rs.next()){
                    MesaDTO m= new MesaDTO();
                    m.setId(rs.getInt("ID"));
                    m.setCapacidadMaxima(rs.getInt("CAPACIDAD_MAXIMA"));
                    m.setUbicacion(rs.getString("UBICACION"));
                    m.setEstado(rs.getString("ESTADO"));
                    lista.add(m);
                }
        } catch (SQLException e) {
            System.out.println("Error al listar las mesas: "+e.getMessage());
        }
        return lista;
    }
}
