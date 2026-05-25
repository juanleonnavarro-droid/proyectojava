package dto;

import util.Persona;
import util.Verificable;

/**
 * @author Juan Leon
 * Clase ClienteDTO para guardar los atributos de la entidad de la base de datos Cliente
 * Se implementa la clase Verificable para validar los datos que se introduzcan
 */

public class ClienteDTO extends Persona implements Verificable  {
    private String dni;
    private String direccion;

    /**
     * Constructor vació, si se quieren insertar datos será usando los setters
     */

    public ClienteDTO(){}

    /**
     * Getters y setters
     */

    public String getDireccion() {
        return direccion;
    }

    public String getDni() {
        return dni;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Método toString para formatear los atributos de la clase
     * @return String
     */

   @Override
    public String toString() {
        return super.toString()+" DNI: "+this.dni+" Dirección: "+this.direccion;
    }

    /**
     * Método validarDatos para verificar que los campos dni y email son válidos
     * @return boolean
     */

    @Override
    public boolean validarDatos() {
        if(this.dni==null || this.dni.length()<9){
      return false;
    }
    if(!getEmail().contains("@")){
        return false;
    }
    return true;
    }
    
}
