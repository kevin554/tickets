package tefor.tickets.conexion;

/**
 * Una representacion del nombre de las tablas, para tomar como unica referencia a la hora de
 * manipularlas
 */
public enum Tablas {

    paciente("tblpacientes");

    private final String texto;

    Tablas(final String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto;
    }

}