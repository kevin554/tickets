package tefor.tickets.dao.web;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import tefor.tickets.clienteHTTP.HttpConnection;
import tefor.tickets.clienteHTTP.MethodType;
import tefor.tickets.clienteHTTP.RequestConfiguration;
import tefor.tickets.clienteHTTP.StandarRequestConfiguration;
import tefor.tickets.dto.DTO;
import tefor.tickets.dto.Paciente;
import tefor.tickets.utiles.Contexto;

/**
 * La implementacion DAO para conectarse al servidor Web
 */
class PacienteDAO extends tefor.tickets.dao.PacienteDAO{

    private static final String URL = "http://192.168.43.32:8080/Tickets/ServletRegistro";
    // private static final String URL = "http://192.168.42.100:8080/Tickets/ServletRegistro";

    private static final String CODIGO_ID = "codigoID";
    private static final String NOMBRE = "nombre";
    private static final String FECHA_NACIMIENTO = "fechaNacimiento";
    private static final String SEXO = "sexo";
    private static final String TIENE_HEMORRAGIA = "tieneHemorragia";
    private static final String ESTA_CONSCIENTE = "estaConsciente";
    private static final String ESTA_EMBARAZADA = "estaEmbarazada";
    private static final String TICKET = "ticket";
    private static final String FUE_ATENDIDO = "fueAtendido";
    private static final String TOKEN = "token";

    @Override
    public List<Paciente> seleccionarTodos() {
        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put("evento", "todos");

        RequestConfiguration configuration =
                new StandarRequestConfiguration(URL, MethodType.GET, parametros);

        String pacientes = HttpConnection.sendRequest(configuration);

        List<Paciente> lista = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(pacientes);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Paciente obj = obtenerObjPacienteDeObjJson(jsonObject);
                lista.add(obj);
            }
        } catch (Exception ex) {
            Log.e(Contexto.APP_TAG, "Hubo un error al obtener todos los pacientes de un JSON Array");
        }

        return lista;
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

        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put("evento", "seleccionar");
        parametros.put("codigo_id", Integer.toString(ID));

        RequestConfiguration configuracion =
                new StandarRequestConfiguration(URL, MethodType.GET, parametros);

        String objStr = HttpConnection.sendRequest(configuracion);

        Paciente obj = null;
        try {
            obj = obtenerObjPacienteDeObjJson(new JSONObject(objStr));
        } catch (Exception ex) {
            Log.e(Contexto.APP_TAG, ex.getMessage());
        }

        return obj;
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

        Gson gson = new Gson();

        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put("evento", "registrar_paciente");
        parametros.put("obj", gson.toJson(obj));

        RequestConfiguration configuration =
                new StandarRequestConfiguration(URL, MethodType.GET, parametros);

        String resultado = HttpConnection.sendRequest(configuration);
        int ID = (Integer.parseInt(resultado));

        objPaciente.setCodigoID(ID);
    }

    @Override
    public void actualizar(DTO obj) {
        if (obj == null) {
            throw new IllegalArgumentException("El objeto a insertar no puede ser nulo");
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

        Gson gson = new Gson();

        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put("evento", "actualizar");
        parametros.put("obj", gson.toJson(obj));

        RequestConfiguration configuration =
                new StandarRequestConfiguration(URL, MethodType.GET, parametros);

        HttpConnection.sendRequest(configuration);
    }

    @Override
    public void eliminar(DTO obj) {
        if (obj == null) {
            throw new IllegalArgumentException("El objeto a eliminar no puede ser nulo");
        }

        Paciente objUsuario = (Paciente) obj;
        if (objUsuario.getCodigoID() <= 0) {
            throw new IllegalArgumentException("No se puede eliminar un usuario con ID <= 0");
        }

        Gson gson = new Gson();

        Hashtable<String, String> parametros = new Hashtable<>();
        parametros.put("evento", "eliminar");
        parametros.put("obj", gson.toJson(objUsuario));

        RequestConfiguration configuration =
                new StandarRequestConfiguration(URL, MethodType.GET, parametros);

        HttpConnection.sendRequest(configuration);
    }

    private Paciente obtenerObjPacienteDeObjJson(JSONObject jsonObject) throws JSONException {
        Paciente obj = new Paciente();

        obj.setCodigoID(jsonObject.getInt(CODIGO_ID));
        obj.setNombre(jsonObject.getString(NOMBRE));
        obj.setFechaNacimiento(jsonObject.getString(FECHA_NACIMIENTO));
        obj.setSexo(jsonObject.getString(SEXO));
        obj.setTieneHemorragia(jsonObject.getString(TIENE_HEMORRAGIA));
        obj.setEstaConsciente(jsonObject.getString(ESTA_CONSCIENTE));
        obj.setEstaEmbarazada(jsonObject.getString(ESTA_EMBARAZADA));
        obj.setTicket(jsonObject.getInt(TICKET));
        obj.setFueAtendido(jsonObject.getString(FUE_ATENDIDO));
        obj.setToken(jsonObject.getString(TOKEN));

        return obj;
    }

}
