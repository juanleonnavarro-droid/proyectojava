package dto;

import util.Verificable;
import util.Persona;

public class ClienteDTO extends Persona implements Verificable  {
    private String dni;
    private String direccion;
    public ClienteDTO(){}

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

   @Override
    public String toString() {
        return super.toString()+" DNI: "+this.dni+" Dirección: "+this.direccion;
    }

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
