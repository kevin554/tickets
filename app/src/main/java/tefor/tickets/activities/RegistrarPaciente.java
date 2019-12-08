package tefor.tickets.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;

import tefor.tickets.R;
import tefor.tickets.clienteHTTP.HttpConnection;
import tefor.tickets.clienteHTTP.MethodType;
import tefor.tickets.clienteHTTP.StandarRequestConfiguration;
import tefor.tickets.dao.FactoryDAO;
import tefor.tickets.dao.PacienteDAO;
import tefor.tickets.dto.Paciente;
import tefor.tickets.utiles.Contexto;
import tefor.tickets.utiles.HerramientasFecha;

public class RegistrarPaciente extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText nombreTextInputEditText, fechaNacimientoTextInputEditText;
    private RadioButton hombreRadioButton, mujerRadioButton;
    private CheckBox hemorragiaCheckBox, conscienteCheckBox, embarazadaCheckBox;
    private Button fechaNacimientoButton, registrarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_paciente);

        inicializarComponentes();
        fechaNacimientoButton.setOnClickListener(this);
        registrarButton.setOnClickListener(this);
    }

    private void inicializarComponentes() {
        nombreTextInputEditText = (TextInputEditText)
                findViewById(R.id.activityRegistrarPaciente_nombreTextInputEditText);
        fechaNacimientoTextInputEditText = (TextInputEditText)
                findViewById(R.id.activityRegistrarPaciente_fechaNacimientoTextInputEditText);
        fechaNacimientoButton = (Button)
                findViewById(R.id.activityRegistrarPaciente_fechaNacimientoButton);
        registrarButton = (Button) findViewById(R.id.activityRegistrarPaciente_registrarButton);
        hombreRadioButton = (RadioButton)
                findViewById(R.id.activityRegistrarPaciente_hombreRadioButton);
        mujerRadioButton = (RadioButton)
                findViewById(R.id.activityRegistrarPaciente_mujerRadioButton);
        hemorragiaCheckBox = (CheckBox)
                findViewById(R.id.activityRegistrarPaciente_hemorragiaCheckBox);
        conscienteCheckBox = (CheckBox)
                findViewById(R.id.activityRegistrarPaciente_conscienteCheckBox);
        embarazadaCheckBox = (CheckBox)
                findViewById(R.id.activityRegistrarPaciente_embarazadaCheckBox);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activityRegistrarPaciente_fechaNacimientoButton:
                mostrarFechaNacimiento();
                break;

            case R.id.activityRegistrarPaciente_registrarButton:
                registrarUsuario();
                break;
        }
    }

    private void mostrarFechaNacimiento() {
        final Calendar calendario = Calendar.getInstance();

        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, 0,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                        calendario.set(year, month, dayOfMonth);
                        fechaNacimientoTextInputEditText.setText(
                                formatoFecha.format(calendario.getTime()));
                    }

                }, anio, mes, dia).show();
    }

    private void registrarUsuario() {
        String nombre = nombreTextInputEditText.getText().toString().trim();
        if (nombre.isEmpty()) {
            nombreTextInputEditText.setError(getString(R.string.error_nombre));
            return;
        }

        String fechaNacimiento = fechaNacimientoTextInputEditText.getText().toString().trim();
        if (fechaNacimiento.isEmpty()) {
            fechaNacimientoTextInputEditText.setError(getString(R.string.error_fecha_nacimiento));
            return;
        }

        if (!hombreRadioButton.isChecked() && !mujerRadioButton.isChecked()) {
            Toast.makeText(this, R.string.error_sexo, Toast.LENGTH_SHORT).show();
            return;
        }

        String sexo = hombreRadioButton.isChecked() ? "HOMBRE" : "MUJER";

        int edad = HerramientasFecha.obtenerEdad(fechaNacimiento);
        if (edad == 0) {
            Toast.makeText(this, R.string.error_edad, Toast.LENGTH_SHORT).show();
            return;
        }

        String hemorragia = hemorragiaCheckBox.isChecked() ? "VERDADERO" : "FALSO";
        String consciente = conscienteCheckBox.isChecked() ? "VERDADERO" : "FALSO";

        String embarazada = embarazadaCheckBox.isChecked() ? "VERDADERO" : "FALSO";
        if (sexo.equals("HOMBRE") && embarazada.equals("VERDADERO")) {
            Toast.makeText(this, R.string.error_embarazo, Toast.LENGTH_SHORT).show();
            return;
        }

        Paciente objPaciente = new Paciente();
        objPaciente.setNombre(nombre);
        objPaciente.setFechaNacimiento(fechaNacimiento);
        objPaciente.setSexo(sexo);
        objPaciente.setTieneHemorragia(hemorragia);
        objPaciente.setEstaConsciente(consciente);
        objPaciente.setEstaEmbarazada(embarazada);
        objPaciente.setTicket(0);
        objPaciente.setFueAtendido("FALSO");

        new EnviarTicketTarea().execute(objPaciente);
    }

    private class EnviarTicketTarea extends AsyncTask<Paciente, String, String> {

        private ProgressDialog progreso;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progreso = new ProgressDialog(RegistrarPaciente.this);
            progreso.setIndeterminate(true);
            progreso.setTitle("enviando solicitud");
            progreso.setCancelable(false);
            progreso.show();
        }

        @Override
        protected String doInBackground(Paciente... pacientes) {
            publishProgress("por favor espere...");

            FactoryDAO factory = FactoryDAO.getOrCreate();
            PacienteDAO dao = factory.newPacienteDAO();

            String token = FirebaseInstanceId.getInstance().getToken();

            Paciente obj = pacientes[0];
            obj.setToken(token);

            try {
                dao.insertar(obj);
            } catch (Exception ex) {
                Log.e(Contexto.APP_TAG, "La respuesta que devolvio el servidor no era un numero" +
                        " entero");
                return "0";
            }

            // String URL = "http://192.168.42.100:8080/Tickets/ServletRegistro";
            String URL = "http://192.168.43.32:8080/Tickets/ServletRegistro";

            Gson gson = new Gson();

            Hashtable<String, String> parametros = new Hashtable<>();
            parametros.put("evento", "registrar_paciente");
            parametros.put("obj", gson.toJson(obj));

            String respuesta = HttpConnection.sendRequest(new StandarRequestConfiguration(URL,
                    MethodType.POST, parametros));

            try {
                obj.setTicket(Integer.parseInt(respuesta));
                dao.actualizar(obj);
            } catch (Exception ex) {
                dao.eliminar(obj);
                Log.e(Contexto.APP_TAG, "La respuesta que devolvio el servidor no era un numero" +
                        " entero");
            }

            return respuesta;

            // return Integer.toString(obj.getCodigoID());
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            try {
                Integer.parseInt(resultado);
                Toast.makeText(RegistrarPaciente.this, R.string.ticket_correcto,
                        Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(RegistrarPaciente.this, R.string.ticket_incorrecto,
                        Toast.LENGTH_SHORT).show();
            }

            /*
            if (Integer.parseInt(resultado) != 0) {
                Toast.makeText(RegistrarPaciente.this, R.string.ticket_correcto,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegistrarPaciente.this, R.string.ticket_incorrecto,
                        Toast.LENGTH_SHORT).show();
                return;
            }
             */

            progreso.dismiss();
            finish();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

    }

}
