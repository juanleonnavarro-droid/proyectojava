package util;
/**
     * Clase Persona, para hacer herencia con la clase ClienteDTO
     * @author Juan Leon
    */
public abstract class Persona {
    protected String nombre;
    protected String telefono;
    protected String email;
    

    /**
     * Constructor vacío y con atributos
    */
    public Persona(){}
    public Persona(String email, String nombre, String telefono) {
        this.email = email;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    /**
     * getters y setters
     */

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Método toString para formatear los atributos de la clase Persona
     * @return String
     */
    @Override
    public String toString() {
        return "Nombre: "+this.nombre+" Teléfono: "+this.telefono+" Email: "+this.email;
    }

    
}
