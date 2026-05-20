
import controller.MainController;
import db.Conexion;
import java.sql.Connection;


public class App {
    public static void main(String[] args) throws Exception {
        Connection conexion;
        Conexion c= new Conexion();
        conexion=c.abrirConexion();
        MainController mainC= new MainController(conexion);
        mainC.arrancarAplicacion();
        c.cerrarConexion(conexion);
    }
}
