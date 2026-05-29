package dto;

import java.time.LocalDateTime;
import util.Verificable;

/**
 * @author Juan Leon
 * Clase ReservaDTO para guardar los atributos de la entidad de la base de datos Reserva
 * Se implementa la clase Verificable para validar los datos que se introduzcan
 */

public class ReservaDTO implements Verificable{
    private int id, idMesa, idEmpleado;
    private String dniCliente;
    private LocalDateTime fecha;
    private double importeTotal;
    private int comensales;

    /**
     * Constructor vació, si se quieren insertar datos será usando los setters
     */
    public ReservaDTO(){}

    /**
     * Getters y setters
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public int getComensales() {
        return comensales;
    }

    public void setComensales(int comensales) {
        this.comensales = comensales;
    }

    /**
     * Método toString para formatear los atributos de la clase
     * @return String
     */

    @Override
    public String toString() {
        return "ID reserva: "+this.id+" ID mesa: "+this.idMesa+" ID empleado: "+this.idEmpleado+" DNI cliente: "+this.dniCliente+" Fecha y hora: "+this.fecha+" Importe total: "+this.importeTotal+" Comensales: "+this.comensales;

    }

    /**
     * Método validarDatos para verificar que los campos fecha, importe y dni son válidos
     * @return boolean
     */

    @Override
    public boolean validarDatos() {
        if(this.fecha.isBefore(LocalDateTime.now())) return false;
        if(this.importeTotal<0) return false;
        if(this.dniCliente==null || this.dniCliente.isEmpty()) return false;
        if(this.comensales<=0) return false;
        return true;
    }

    

    

}
