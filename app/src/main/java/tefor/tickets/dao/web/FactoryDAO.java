package tefor.tickets.dao.web;

import tefor.tickets.dao.PacienteDAO;

public class FactoryDAO extends tefor.tickets.dao.FactoryDAO {

    @Override
    public PacienteDAO newPacienteDAO() {
        return new tefor.tickets.dao.web.PacienteDAO();
    }

}
