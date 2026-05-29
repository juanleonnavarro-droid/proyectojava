package dto;

import util.Persona;
import util.Verificable;

/**
 * @author Juan Leon
 * Clase ReservaDTO para guardar los atributos de la entidad de la base de datos Reserva
 * Se implementa la clase Verificable para validar los datos que se introduzcan
 */

public class EmpleadoDTO extends Persona implements Verificable{
    private int id, anos_expe;
    private String cargo, turno_trabajo;

    /**
     * Constructor vació, si se quieren insertar datos será usando los setters
     */

    public EmpleadoDTO(){}

    /**
     * Getters y setters
     */

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAnos_expe() {
        return anos_expe;
    }
    public void setAnos_expe(int anos_expe) {
        this.anos_expe = anos_expe;
    }
    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    public String getTurno_trabajo() {
        return turno_trabajo;
    }
    public void setTurno_trabajo(String turno_trabajo) {
        this.turno_trabajo = turno_trabajo;
    }

    /**
     * Método toString para formatear los atributos de la clase
     * @return String
     */

    @Override
    public String toString() {
        return  "Nombre: "+getNombre()+" ID de empleado: "+this.id+" Cargo: "+this.cargo+" Turno de trabajo: "+this.turno_trabajo+" Años de experiencia: "+this.anos_expe;
    }

    /**
     * Método validarDatos para verificar que el campo cargo es válido
     * @return boolean
     */

    @Override
    public boolean validarDatos() {
        if(this.nombre.isEmpty() || this.nombre==null){
            return false;
        }
        if(this.anos_expe<0){
            return false;
        }
        if(this.turno_trabajo.isEmpty() || this.turno_trabajo==null) return false;
        if(!this.cargo.toLowerCase().equals("camarero") 
        && !this.cargo.toLowerCase().equals("cocinero")
        && !this.cargo.toLowerCase().equals("encargado")
        && !this.cargo.toLowerCase().trim().equals("jefedesala")
        ){
            return false;
        }
        return true;
    }

    
    
}
