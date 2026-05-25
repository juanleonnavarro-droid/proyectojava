package dto;

import util.Verificable;

/**
 * @author Juan Leon
 * Clase DetalleReservaDTO para guardar los atributos de la entidad de la base de datos Detalle_Reserva
 * Se implementa la clase Verificable para validar los datos que se introduzcan
 */

public class DetalleReservaDTO implements Verificable{
    private int id, id_reserva, id_plato, cantidad;
    private double precio_plato;

    /**
     * Constructor vació, si se quieren insertar datos será usando los setters
     */

    public DetalleReservaDTO(){}

    /**
     * Getters y setters
     */

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId_reserva() {
        return id_reserva;
    }
    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }
    public int getId_plato() {
        return id_plato;
    }
    public void setId_plato(int id_plato) {
        this.id_plato = id_plato;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public double getPrecio_plato() {
        return precio_plato;
    }
    public void setPrecio_plato(double precio_plato) {
        this.precio_plato = precio_plato;
    }

    /**
     * Método toString para formatear los atributos de la clase
     * @return String
     */

    @Override
    public String toString() {
        return "ID del detalle de la reserva: "+this.id+" Id de Reserva: "+this.id_reserva+" ID del plato: "+this.id_plato+" Cantidad: "+this.cantidad+" Precio del plato: "+this.precio_plato;
    }

    /**
     * Método validarDatos para verificar que el campo cantidad es válido
     * @return boolean
     */

    @Override
    public boolean validarDatos() {
        if(this.cantidad<=0){
            return false;
        }
        return true;
    }

    
    
}
