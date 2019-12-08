package tefor.tickets.dao;

import java.util.List;

import tefor.tickets.dto.Paciente;

public abstract class PacienteDAO implements IDAO {

    /**
     * Devuelve una lista con todos los registros de la tabla
     * Pacientes
     */
    public abstract List<Paciente> seleccionarTodos();
    protected String[] columnas;

    public PacienteDAO(String... columnas) {
		this.columnas = columnas;
    }

}