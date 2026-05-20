package dto;

import util.Verificable;

/**
 * @author Juan Leon
 * Clase MesaDTO para guardar los atributos de la entidad de la base de datos Mesa
 * Se implementa la clase Verificable para validar los datos que se introduzcan
 */

public class MesaDTO implements Verificable{
    private int id, capacidadMaxima;
    private String ubicacion, estado;

    /**
     * Constructor vació, si se quieren insertar datos será usando los setters
     */

    public MesaDTO(){}

    /**
     * Getters y setters
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Método toString para formatear los atributos de la clase
     * @return String
     */

    @Override
    public String toString() {
        return "ID mesa: "+this.id+" Capacidad máxima: "+this.capacidadMaxima+" Ubicación: "+this.ubicacion+" Estado: "+this.estado;
    }

    /**
     * Método validarDatos para verificar que los campos capacidadMaxima y estado son válidos
     * El campo estado tiene que cumplir con alguna de las 3 opciones: Libre, Ocupada o Reservada
     * @return boolean
     */

    @Override
    public boolean validarDatos() {
        if(this.capacidadMaxima<=0) return false;
        if(!this.estado.toLowerCase().equals("libre")
            || !this.estado.toLowerCase().equals("ocupada")
            || !this.estado.toLowerCase().equals("reservada")    
        ) return false;
        return true;
    }

    
}
