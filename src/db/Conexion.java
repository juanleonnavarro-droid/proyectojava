package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    /**
     * Clase encargada de gestionar el ciclo de vida de las conexiones con la
     * base de datos.
     * <p>
     * Proporciona los métodos centralizados para establecer y liberar el enlace
     * físico con el motor MySQL
     * </p>
     *
     * * @author Juan Leon Navarro
     */
    /**
     * URL de acceso a la base de datos local que fuerza el uso de codificación
     * UTF-8.
     */
    private final String URL = "jdbc:mysql://localhost:3306/SABORES_GOURMET_BBDD?useUnicode=true&characterEncoding=UTF-8";
    /**
     * Nombre de usuario por defecto
     */
    private final String USER = "root";
    /**
     * Contraseña asociada al usuario
     */
    private final String PASS = "";

    /**
     * Intenta cargar el driver JDBC y establecer una conexión con el servidor
     * MySQL.
     * <p>
     * Si el enlace es exitoso, devuelve la instancia de la conexión y muestra
     * un mensaje en consola. En caso de fallo, captura la excepción, la
     * notifica en consola y devuelve un valor nulo.
     * </p>
     *
     * * @return Un objeto Connection o null si la conexión falló.
     */
    public Connection abrirConexion() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Conexión exitosa a la BBDD");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectar con la BBDD " + e.getMessage());
        }
        return con;
    }

    /**
     * Libera de forma la conexión pasada por parámetro
     * <p>
     * El método comprueba primero que el objeto de conexión no sea nulo. Si
     * está activo, procede a invocar el cierre del canal de comunicación con MySQL.
     * </p>
     * * @param con El objeto Connection que se desea cerrar.
     */
    public void cerrarConexion(Connection con) {
        if (con != null) {
            try {
                con.close();
                System.out.println("Conexion cerrada");
            } catch (SQLException e) {
                System.out.println("Error al cerra la conexion " + e.getMessage());
            }
        }

    }
}
