package dto;

import util.Verificable;

/**
 * @author Juan Leon
 * Clase PlatoDTO para guardar los atributos de la entidad de la base de datos Plato
 * Se implementa la clase Verificable para validar los datos que se introduzcan
 */

public class PlatoDTO implements Verificable{
    private int id, idCategoria;
    private String nombre, descripcion;
    private double precio;
    private boolean disponibilidad;
    
    /**
     * Constructor vació, si se quieren insertar datos será usando los setters
     */

    public PlatoDTO(){}

    /**
     * Getters y setters
     */
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    /**
     * Método toString para formatear los atributos de la clase
     * @return String
     */

    @Override
    public String toString() {
        String textoDisponibilidad = (this.disponibilidad) ? "Disponible" : "No disponible";
        return "ID plato: "+this.id+" ID categoría: "+this.idCategoria+" Nombre del plato: "+this.nombre+" Descripción: "+this.descripcion+" Precio: "+this.precio+" Disponibilidad: "+textoDisponibilidad;
    }

    /**
     * Método validarDatos para verificar que los campos precio y nombre son válidos
     * @return boolean
     */

    @Override
    public boolean validarDatos() {
        if(this.precio<=0) return false;
        if(this.nombre==null || this.nombre.trim().isEmpty()) return false;
        return true;
    }

    

}
