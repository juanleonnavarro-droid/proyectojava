package dto;

import java.time.LocalDateTime;
import util.Verificable;

/**
 * @author Juan Leon
 * Clase HistorialDTO, es una clase de apoyo para poder devolver el historial de platos consumidos de un cliente (procedimiento de la base de datos)
 * Se implementa la clase Verificable para validar los datos que se introduzcan
 */

public class HistorialDTO implements Verificable{
    private LocalDateTime fechaReserva;
    private String nombrePlato;
    private int cantidad;
    private double precioEnEseMomento;

    /**
     * Constructor vació, si se quieren insertar datos será usando los setters
     */

    public HistorialDTO(){}

    /**
     * Getters y setters
     */

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioEnEseMomento() {
        return precioEnEseMomento;
    }

    public void setPrecioEnEseMomento(double precioEnEseMomento) {
        this.precioEnEseMomento = precioEnEseMomento;
    }

    /**
     * Método toString para formatear los atributos de la clase
     * @return String
     */

    @Override
    public String toString() {
        return "Fecha de la reserva: "+this.fechaReserva+" Nombre del plato: "+this.nombrePlato+" Cantidad: "+this.cantidad+" Precio pagado: "+this.precioEnEseMomento;
    }

    /**
     * Método validarDatos para verificar que los campos nombrePlato, cantidad y fechaReserva son válidos
     * @return boolean
     */

    @Override
    public boolean validarDatos() {
        if(this.nombrePlato==null || this.nombrePlato.trim().isEmpty()) return false;
        if(this.cantidad<=0 || this.precioEnEseMomento<=0) return false;
        if(this.fechaReserva==null || this.fechaReserva.isAfter(LocalDateTime.now())) return false;
        return true;

    }

    
    

}
