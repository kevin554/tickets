package tefor.tickets.dao.sqlite;

import tefor.tickets.dao.PacienteDAO;

/**
 * Devuelve implementaciones DAO para el SGBD SQLite
 */
public class FactoryDAO extends tefor.tickets.dao.FactoryDAO {

    @Override
    public PacienteDAO newPacienteDAO() {
		return new tefor.tickets.dao.sqlite.PacienteDAO();
    }

}