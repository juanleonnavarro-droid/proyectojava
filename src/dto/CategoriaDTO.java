package dto;

import util.Verificable;

public class CategoriaDTO implements Verificable{
    private int id;
    private String nombre;
    public CategoriaDTO() {
    }
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

    @Override
    public String toString() {
        return "ID Categoría: "+id+" Nombre Categoria: "+nombre;
    }

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
