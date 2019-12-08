package tefor.tickets.dto;

public class Paciente extends DTO {

    private int codigoID;
    private String nombre;
    private String fechaNacimiento;
    private String sexo;
    private String tieneHemorragia;
    private String estaConsciente;
    private String estaEmbarazada;
    private int ticket;
    private String fueAtendido;
    private String token;

    public Paciente() {
    }

    public Paciente(int codigoID, String nombre, String fechaNacimiento, String sexo,
                    String tieneHemorragia, String estaConsciente, String estaEmbarazada,
                    int ticket, String fueAtendido, String token) {
        this.codigoID = codigoID;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.tieneHemorragia = tieneHemorragia;
        this.estaConsciente = estaConsciente;
        this.estaEmbarazada = estaEmbarazada;
        this.ticket = ticket;
        this.fueAtendido = fueAtendido;
        this.token = token;
    }

    // <editor-fold defaultstate="collapsed" desc="getters, setters y toString">

    public int getCodigoID() {
        return codigoID;
    }

    public void setCodigoID(int codigoID) {
        this.codigoID = codigoID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTieneHemorragia() {
        return tieneHemorragia;
    }

    public void setTieneHemorragia(String tieneHemorragia) {
        this.tieneHemorragia = tieneHemorragia;
    }

    public String getEstaConsciente() {
        return estaConsciente;
    }

    public void setEstaConsciente(String estaConsciente) {
        this.estaConsciente = estaConsciente;
    }

    public String getEstaEmbarazada() {
        return estaEmbarazada;
    }

    public void setEstaEmbarazada(String estaEmbarazada) {
        this.estaEmbarazada = estaEmbarazada;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public String getFueAtendido() {
        return fueAtendido;
    }

    public void setFueAtendido(String fueAtendido) {
        this.fueAtendido = fueAtendido;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "codigoID=" + codigoID +
                ", nombre='" + nombre + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", sexo='" + sexo + '\'' +
                ", tieneHemorragia='" + tieneHemorragia + '\'' +
                ", estaConsciente='" + estaConsciente + '\'' +
                ", estaEmbarazada='" + estaEmbarazada + '\'' +
                ", ticket=" + ticket +
                ", fueAtendido='" + fueAtendido + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    // </editor-fold>

}
