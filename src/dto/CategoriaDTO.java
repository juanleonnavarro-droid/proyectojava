package dto;

import util.Verificable;
/**
 * @author Juan Leon
 * Clase CategoriaDTO para guardar los atributos de la entidad de la base de datos Categoria
 * Se implementa la clase Verificable para validar los datos que se introduzcan
 */

public class CategoriaDTO implements Verificable{
    private int id;
    private String nombre;

    /**
     * Constructor vació, si se quieren insertar datos será usando los setters
     */

    public CategoriaDTO() {
    }

    /**
     * Getters y setters
     */

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método toString para formatear los atributos de la clase
     * @return String
     */

    @Override
    public String toString() {
        return "ID Categoría: "+id+" Nombre Categoria: "+nombre;
    }

    /**
     * Método validarDatos para verificar que los campos nombre e id son válidos
     * @return boolean
     */

    @Override
    public boolean validarDatos() {
        if(this.nombre==null || this.nombre.trim().isEmpty()){
            return false;
        }
        if(this.id<0){
            return false;
        }
        return true;
    }
    
}
