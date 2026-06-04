package dao;

import dto.CategoriaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Esta clase sirve para gestionar las categorías de los platos en el
 * restaurante.
 *
 * @author Juan Leon Navarro
 */
public class CategoriaDAO {

    private Connection conexion;

    /**
     * Nos prepara el gestor de categorías guardando la conexión que usaremos.
     *
     * @param conexion La conexión activa que usará la aplicación.
     */
    public CategoriaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * Muestra una lista con todas las categorías que tenemos creadas en la base
     * de datos.
     *
     * @return Una lista con todas las categorías encontradas (con su ID y
     * nombre). Si no hay ninguna, la lista vendrá vacía.
     */
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

    /**
     * Guarda una nueva categoría en la base de datos.
     *
     * @param categoria El objeto con el nombre de la sección que queremos
     * añadir.
     * @return true si la categoría se guardó correctamente, false si hubo algún
     * error.
     */
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

    /**
     * Busca una sola categoría utilizando su ID.
     *
     * @param id El número de ID de la categoría que queremos localizar.
     * @return El objeto CategoriaDTO con los datos de esa categoría si la
     * encuentra, o null si no existe ninguna sección con ese número.
     */
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
