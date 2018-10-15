package utn.rodrigo_anton.primerparcial.classes;

public class User {
    private String Nombre;
    private String Apellido;
    private String Username;
    private int AccessCode;
    private String AccessName;

    public User (String username, String nombre, String apellido, String accesname, int accesscode) {
        Nombre=nombre;
        Apellido=apellido;
        Username=username;
        AccessName=accesname;
        AccessCode=accesscode;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public String getUsername() {
        return Username;
    }

    public String getAccessName() {
        return AccessName;
    }
}
