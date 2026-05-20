package dto;

import util.Verificable;
/**
 * @author Juan Leon
 * Clase RankingDTO, es una clase de apoyo para poder devolver un top 5 platos más vendidos (procedimiento de la base de datos)
 * Se implementa la clase Verificable para validar los datos que se introduzcan
 */
public class RankingDTO implements Verificable{
    private String nombrePlato;
    private int totalVendido;

    /**
     * Constructor vació, si se quieren insertar datos será usando los setters
     */
    public RankingDTO(){}

    /**
     * Getters y setters
     */

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public int getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(int totalVendido) {
        this.totalVendido = totalVendido;
    }

    /**
     * Método toString para formatear los atributos de la clase
     * @return String
     */

    @Override
    public String toString() {
        return "Nombre del plato: "+this.nombrePlato+" Total vendido: "+this.totalVendido;
    }

    /**
     * Método validarDatos para verificar que los campos nombrePlato y totalVendido son válidos
     * @return boolean
     */

    @Override
    public boolean validarDatos() {
        if(this.nombrePlato==null || this.nombrePlato.trim().isEmpty()) return false;
        if(this.totalVendido<=0) return false;
        return true;
    }

    
    
}
