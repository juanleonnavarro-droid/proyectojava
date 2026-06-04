package util;

/**
 * Excepción personalizada utilizada para capturar errores de validación de datos en la aplicación.
 * <p>
 * Se lanza cuando los atributos de un objeto DTO (como DNI, teléfono, email,
 * comensales, etc.) no cumplen con los formatos requeridos antes de procesar
 * una inserción o modificación.
 * </p>
 *
 * * @author Juan Leon Navarro
 */
public class DatosInvalidosException extends Exception {

    /**
     * Construye una nueva excepción con un mensaje descriptivo específico sobre el fallo de validación.
     * <p>
     * El mensaje proporcionado se envía a la superclase Exception ypuede ser recuperado posteriormente mediante el método
     * {@code getMessage()} para mostrarlo en la consola.
     * </p>
     *
     * * @param mensaje Texto detallado que explica el motivo exacto por el cual los datos son inválidos.
     */
    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }

}
