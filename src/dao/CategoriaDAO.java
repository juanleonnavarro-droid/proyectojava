package dao;

import dto.CategoriaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriaDAO {

    private Connection conexion;

    public CategoriaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public ArrayList<CategoriaDTO> listarCategorias() {
        ArrayList<CategoriaDTO> lista = new ArrayList<>();
        String sql = "SELECT ID, NOMBRE FROM CATEGORIA";
        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CategoriaDTO cat = new CategoriaDTO();
                cat.setId(rs.getInt("ID"));
                cat.setNombre(rs.getString("NOMBRE"));
                lista.add(cat);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar las categorías: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(CategoriaDTO categoria) {
        String sql = "INSERT INTO CATEGORIA (NOMBRE) VALUES (?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, categoria.getNombre());
            int insercion = ps.executeUpdate();
            if (insercion > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        categoria.setId(idGenerado);
                    }

                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar categoría: " + e.getMessage());

        }
        return false;
    }

    public CategoriaDTO buscarPorId(int id) {
        String sql = "SELECT ID, NOMBRE FROM CATEGORIA WHERE ID = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CategoriaDTO cat = new CategoriaDTO();
                    cat.setId(rs.getInt("ID"));
                    cat.setNombre(rs.getString("NOMBRE"));
                    return cat;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar la categoría: " + e.getMessage());
        }
        return null;
    }

}
