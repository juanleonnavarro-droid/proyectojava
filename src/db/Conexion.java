package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private final String URL = "jdbc:mysql://localhost:3306/sabores_gourmet_bbdd";
    private final String USER = "root";
    private final String PASS = "";
    
    public Connection abrirConexion(){
        Connection con=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Conexión exitosa a la BBDD");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectar con la BBDD "+e.getMessage());
        }
        return con;
    }

    public void cerrarConexion(Connection con){
        if(con!=null){
            try {
                con.close();
                System.out.println("Conexion cerrada");
            } catch (SQLException e) {
                System.out.println("Error al cerra la conexion "+e.getMessage());
            }
        }

    }
}
