
import controller.MainController;
import db.Conexion;
import java.sql.Connection;
/**
 * Clase principal de entrada para la aplicación Sabores Gourmet.
 * <p>
 * inicializa la conexión con la base de datos MySQL, delega el control al controlador principal 
 * si la conexión es exitosa y garantiza el cierre de los recursos al finalizar.
 * </p>
 * * @author Juan Leon Navarro
 */

public class App {
    /**
 * Realiza las siguientes acciones:
 * <ol>
 * <li>Instancia el objeto de gestión de conectividad.</li>
 * <li>Intenta abrir la conexión con la base de datos.</li>
 * <li>Si la conexión es válida, instancia el MainController y arranca el menú.</li>
 * <li>Finalmente, asegura la desconexión de la base de datos.</li>
 * </ol>
 * * @param args
 * @throws Exception
 */
    public static void main(String[] args) throws Exception {
        Connection conexion;
        Conexion c= new Conexion();
        conexion=c.abrirConexion();
        if(conexion!=null){
            MainController mainC= new MainController(conexion);
            mainC.arrancarAplicacion();
        }
        c.cerrarConexion(conexion);
    }
}
