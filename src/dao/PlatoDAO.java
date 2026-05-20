package dao;

import dto.PlatoDTO;
import dto.RankingDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlatoDAO {
    private Connection conexion;

    public PlatoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public ArrayList<PlatoDTO> listarPlatos(){
        ArrayList<PlatoDTO> lista= new ArrayList<>();
        String sql="SELECT * FROM PLATO";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs= ps.executeQuery()) {
            while(rs.next()){
                PlatoDTO p= new PlatoDTO();
                p.setId(rs.getInt("ID"));
                p.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                p.setNombre(rs.getString("NOMBRE"));
                p.setDescripcion(rs.getString("DESCRIPCION"));
                p.setPrecio(rs.getDouble("PRECIO"));
                p.setDisponibilidad(rs.getBoolean("DISPONIBILIDAD"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los platos: "+e.getMessage());
        }
        return lista;
    }

    public PlatoDTO buscarPorId(int id){
        String sql="SELECT * FROM PLATO WHERE ID=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs=ps.executeQuery()) {
                if(rs.next()){
                    PlatoDTO p = new PlatoDTO();
                    p.setIdCategoria(rs.getInt("ID_CATEGORIA"));
                    p.setNombre(rs.getString("NOMBRE"));
                    p.setDescripcion(rs.getString("DESCRIPCION"));
                    p.setPrecio(rs.getDouble("PRECIO"));
                    p.setDisponibilidad(rs.getBoolean("DISPONIBILIDAD"));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar plato por id: "+e.getMessage());
        }
        return null;
    }

    public ArrayList<RankingDTO> obtenerTop5Ventas(){
        ArrayList<RankingDTO> ranking= new ArrayList<>();
        String sql="{call top5_ventas()}";
        try (CallableStatement cs= conexion.prepareCall(sql);
            ResultSet rs= cs.executeQuery()) {
            while(rs.next()){
                RankingDTO r= new RankingDTO();
                r.setNombrePlato(rs.getString("NOMBRE"));
                r.setTotalVendido(rs.getInt("Total_Vendido"));
                ranking.add(r);
            }
        } catch (Exception e) {
            System.out.println("Error al listar los 5 platos más vendidos: "+e.getMessage());
        }
        return ranking;
    }

    public int obtenerNPlatosSinVentas(int mes){
        int nplatos=0;
        String sql="{?=call platos_baja_rotacion()}";
        try (CallableStatement cs=conexion.prepareCall(sql)) {
           cs.registerOutParameter(1, java.sql.Types.INTEGER);
           cs.execute();
           nplatos = cs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error al listar platos sin ventas: "+e.getMessage());
        }
        return nplatos;
    }

    public boolean insertarPlato(PlatoDTO p){
        String sql="INSERT INTO PLATO (ID_CATEGORIA, NOMBRE, DESCRIPCION, PRECIO, DISPONIBILIDAD) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps=conexion.prepareStatement(sql)) {
            ps.setInt(1, p.getIdCategoria());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setDouble(4, p.getPrecio());
            ps.setBoolean(5, p.isDisponibilidad());
            int insercion=ps.executeUpdate();
            return insercion>0;
        } catch (SQLException e) {
            System.out.println("Error al insertar plato: "+e.getMessage());
            return false;
        }
    }

    public boolean eliminarPlato(PlatoDTO p){
        String sql="DELETE FROM MESA WHERE ID=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            int eliminar=ps.executeUpdate();
            return eliminar>0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar plato: "+e.getMessage());
            return false;
        }
    }

    public boolean modificarPlato(PlatoDTO p){
        String sql="UPDATE PLATO SET ID_CATEGORIA=?, NOMBRE=?, DESCRIPCION=?, PRECIO=?, DISPONIBILIDAD=? WHERE ID=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ps.setInt(2, p.getIdCategoria());
            ps.setString(3, p.getNombre());
            ps.setString(4, p.getDescripcion());
            ps.setDouble(5, p.getPrecio());
            ps.setBoolean(6, p.isDisponibilidad());
            int modifi=ps.executeUpdate();
            return modifi>0;
        } catch (SQLException e) {
            System.out.println("Error al modificar plato: "+e.getMessage());
            return false;
        }
    }


}
