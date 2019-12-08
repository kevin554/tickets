package tefor.tickets.dao.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import tefor.tickets.conexion.Conexion;
import tefor.tickets.conexion.Tablas;
import tefor.tickets.dto.DTO;
import tefor.tickets.dto.Paciente;

/**
 * La implementacion DAO para SQLite de la tabla Paciente
 */
class PacienteDAO extends tefor.tickets.dao.PacienteDAO {

    private static final String CODIGO_ID = "codigo_id";
    private static final String NOMBRE = "nombre";
    private static final String FECHA_NACIMIENTO = "fecha_nacimiento";
    private static final String SEXO = "sexo";
    private static final String TIENE_HEMORRAGIA = "tiene_hemorragia";
    private static final String ESTA_CONSCIENTE = "esta_consciente";
    private static final String ESTA_EMBARAZADA = "esta_embarazada";
    private static final String TICKET = "ticket";
    private static final String FUE_ATENDIDO = "fue_atendido";
    private static final String TOKEN = "token";

    PacienteDAO() {
		super(CODIGO_ID, NOMBRE, FECHA_NACIMIENTO, SEXO, TIENE_HEMORRAGIA, ESTA_CONSCIENTE,
                ESTA_EMBARAZADA, TICKET, FUE_ATENDIDO, TOKEN);
	}

    @Override
    public DTO seleccionar(Object llave) {
        if (!(llave instanceof Integer)) {
            throw new IllegalArgumentException("La llave debe ser un entero");
        }

        int ID = (int) llave;
        if (ID <= 0) {
            throw new IllegalArgumentException("El ID debe ser un entero positivo");
        }

        Conexion con = Conexion.getOrCreate();

        String where = "codigo_id = ?";
        String[] parametrosWhere = { String.valueOf(ID) };

        Cursor cursor = con.ejecutarConsulta(Tablas.paciente, columnas, where, parametrosWhere);
        Paciente objPaciente = null;

        if (cursor.moveToFirst()) {
            objPaciente = obtenerPacienteDeCursor(cursor);
        }

        return objPaciente;
    }

    @Override
    public void insertar(DTO obj) {
        if (obj == null) {
            throw new IllegalArgumentException("El objeto a insertar no puede ser nulo");
        }

        Paciente objPaciente = (Paciente) obj;

		if (objPaciente.getNombre().trim().isEmpty()) {
		    throw new IllegalArgumentException("El nombre no puede estar vacio");
		}

		if (objPaciente.getFechaNacimiento().trim().isEmpty()) {
		    throw new IllegalArgumentException("La fecha de nacimiento no puede estar vacia");
		}

		if (objPaciente.getSexo().trim().isEmpty()) {
		    throw new IllegalArgumentException("El sexo no puede estar vacio");
		}

		if (objPaciente.getTieneHemorragia().trim().isEmpty()) {
		    throw new IllegalArgumentException("Se debe saber si tiene hemorragia o no");
		}

		if (objPaciente.getEstaConsciente().trim().isEmpty()) {
		    throw new IllegalArgumentException("Se debe saber si esta consciente o no");
		}

		if (objPaciente.getEstaEmbarazada().trim().isEmpty()) {
		    throw new IllegalArgumentException("Se debe saber si esta embarazada o no");
		}

        ContentValues valores = new ContentValues();
		valores.put(NOMBRE, objPaciente.getNombre());
		valores.put(FECHA_NACIMIENTO, objPaciente.getFechaNacimiento());
		valores.put(SEXO, objPaciente.getSexo());
		valores.put(TIENE_HEMORRAGIA, objPaciente.getTieneHemorragia());
		valores.put(ESTA_CONSCIENTE, objPaciente.getEstaConsciente());
		valores.put(ESTA_EMBARAZADA, objPaciente.getEstaEmbarazada());
        valores.put(TICKET, objPaciente.getTicket());
        valores.put(FUE_ATENDIDO, objPaciente.getFueAtendido());
        valores.put(TOKEN, objPaciente.getToken());

        Conexion con = Conexion.getOrCreate();
        int ID = con.insertar(Tablas.paciente, valores);

        objPaciente.setCodigoID(ID);
    }

    @Override
    public void actualizar(DTO obj) {
        if (obj == null) {
            throw new IllegalArgumentException("El objeto a actualizar no puede ser nulo");
        }

        Paciente objPaciente = (Paciente) obj;

		if (objPaciente.getCodigoID() <= 0) {
		    throw new IllegalArgumentException("El ID no puede ser menor o igual que cero");
		}

		if (objPaciente.getNombre().trim().isEmpty()) {
		    throw new IllegalArgumentException("El nombre no puede estar vacio");
		}

		if (objPaciente.getFechaNacimiento().trim().isEmpty()) {
		    throw new IllegalArgumentException("La fecha de nacimiento no puede estar vacia");
		}

		if (objPaciente.getSexo().trim().isEmpty()) {
		    throw new IllegalArgumentException("El sexo no puede estar vacio");
		}

        if (objPaciente.getTieneHemorragia().trim().isEmpty()) {
            throw new IllegalArgumentException("Se debe saber si tiene hemorragia o no");
        }

        if (objPaciente.getEstaConsciente().trim().isEmpty()) {
            throw new IllegalArgumentException("Se debe saber si esta consciente o no");
        }

        if (objPaciente.getEstaEmbarazada().trim().isEmpty()) {
            throw new IllegalArgumentException("Se debe saber si esta embarazada o no");
        }

        ContentValues valores = new ContentValues();
		valores.put(NOMBRE, objPaciente.getNombre());
		valores.put(FECHA_NACIMIENTO, objPaciente.getFechaNacimiento());
		valores.put(SEXO, objPaciente.getSexo());
		valores.put(TIENE_HEMORRAGIA, objPaciente.getTieneHemorragia());
		valores.put(ESTA_CONSCIENTE, objPaciente.getEstaConsciente());
		valores.put(ESTA_EMBARAZADA, objPaciente.getEstaEmbarazada());
        valores.put(TICKET, objPaciente.getTicket());
        valores.put(FUE_ATENDIDO, objPaciente.getFueAtendido());
        valores.put(TOKEN, objPaciente.getToken());

		String where = "codigo_id = ?";
        String[] parametrosWhere = { String.valueOf(objPaciente.getCodigoID()) };

        Conexion con = Conexion.getOrCreate();
        con.actualizar(Tablas.paciente, valores, where, parametrosWhere);
    }

    @Override
    public void eliminar(DTO obj) {
        if (obj == null) {
            throw new IllegalArgumentException("El objeto no puede ser nulo");
        }

        Paciente objPaciente = (Paciente) obj;
        if (objPaciente.getCodigoID() < 0) {
            throw new IllegalArgumentException("No se puede eliminar un Paciente con ID <= 0");
        }

        String where = "codigo_id = ?";
        String[] parametrosWhere = { String.valueOf(objPaciente.getCodigoID()) };

        Conexion con = Conexion.getOrCreate();
        con.eliminar(Tablas.paciente, where, parametrosWhere);
    }

    @Override
    public List<Paciente> seleccionarTodos() {
        Conexion con = Conexion.getOrCreate();

        Cursor cursor = con.ejecutarConsulta(Tablas.paciente, columnas, null, null);
        List<Paciente> lista = new ArrayList<>();

        while (cursor.moveToNext()) {
            Paciente objPaciente = obtenerPacienteDeCursor(cursor);
            lista.add(objPaciente);
        }

        return lista;
    }

    private Paciente obtenerPacienteDeCursor(Cursor cursor) {
        Paciente objPaciente = new Paciente();

		objPaciente.setCodigoID(cursor.getInt(cursor.getColumnIndex(CODIGO_ID)));
		objPaciente.setNombre(cursor.getString(cursor.getColumnIndex(NOMBRE)));
		objPaciente.setFechaNacimiento(cursor.getString(cursor.getColumnIndex(FECHA_NACIMIENTO)));
		objPaciente.setSexo(cursor.getString(cursor.getColumnIndex(SEXO)));
		objPaciente.setTieneHemorragia(cursor.getString(cursor.getColumnIndex(TIENE_HEMORRAGIA)));
		objPaciente.setEstaConsciente(cursor.getString(cursor.getColumnIndex(ESTA_CONSCIENTE)));
		objPaciente.setEstaEmbarazada(cursor.getString(cursor.getColumnIndex(ESTA_EMBARAZADA)));
        objPaciente.setTicket(cursor.getInt(cursor.getColumnIndex(TICKET)));
        objPaciente.setFueAtendido(cursor.getString(cursor.getColumnIndex(FUE_ATENDIDO)));
        objPaciente.setToken(cursor.getString(cursor.getColumnIndex(TOKEN)));

        return objPaciente;
    }

}