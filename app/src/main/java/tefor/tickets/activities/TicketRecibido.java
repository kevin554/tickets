package tefor.tickets.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import tefor.tickets.R;
import tefor.tickets.dao.FactoryDAO;
import tefor.tickets.dao.PacienteDAO;
import tefor.tickets.dto.Paciente;
import tefor.tickets.servicios.Notificacion;
import tefor.tickets.utiles.Contexto;

public class TicketRecibido extends AppCompatActivity {

    public static final String PACIENTE_ID = "paciente_id";
    public static final String SEXO = "sexo";
    private TextView mensajeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_recibido);

        inicializarComponentes();

        Intent intent = getIntent();
        String pacienteID = intent.getStringExtra(PACIENTE_ID);
        // String paciente = intent.getStringExtra(Notificacion.MENSAJE);
        // String sexo = intent.getStringExtra(SEXO);

        /*
        if (sexo.equals("HOMBRE")) {
            mensajeTextView.setText(String.format(getString(R.string.notificacion_masculino),
                    paciente));
        } else {
            mensajeTextView.setText(String.format(getString(R.string.notificacion_femenino),
                    paciente));
        }
         */

        new ActualizarPacienteTask().execute(Integer.parseInt(pacienteID));
    }

    private void inicializarComponentes() {
        mensajeTextView = (TextView) findViewById(R.id.activityTicketRecibido_mensajeTextView);
    }

    private class ActualizarPacienteTask extends AsyncTask<Integer, String, Paciente> {

        private ProgressDialog progreso;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progreso = new ProgressDialog(TicketRecibido.this);
            progreso.setIndeterminate(true);
            progreso.setTitle("enviando solicitud");
            progreso.setCancelable(false);
            progreso.show();
        }

        @Override
        protected Paciente doInBackground(Integer... params) {
            publishProgress("por favor espere...");

            FactoryDAO factory = FactoryDAO.getOrCreate();
            PacienteDAO dao = factory.newPacienteDAO();

            int ID = params[0];

            Paciente objPaciente = null;

            try {
                objPaciente = (Paciente) dao.seleccionar(ID);
                objPaciente.setFueAtendido("VERDADERO");
            } catch (Exception ex) {
                Log.e(Contexto.APP_TAG, "Hubo un error al obtener un paciente con ID " + ID);
            }

            try {
                dao.actualizar(objPaciente);
            } catch (Exception ex) {
                Log.e(Contexto.APP_TAG, "Hubo un error al actuazliar un paciente con ID " + ID);
            }

            return objPaciente;
        }

        @Override
        protected void onPostExecute(Paciente objPaciente) {
            super.onPostExecute(objPaciente);

            if (objPaciente == null) {
                Toast.makeText(TicketRecibido.this, R.string.error_carga_notificacion,
                        Toast.LENGTH_SHORT).show();
                progreso.dismiss();
                return;
            }

            if (objPaciente.getSexo().equals("HOMBRE")) {
                mensajeTextView.setText(String.format(getString(R.string.notificacion_masculino),
                        objPaciente.getNombre()));
            } else {
                mensajeTextView.setText(String.format(getString(R.string.notificacion_femenino),
                        objPaciente.getNombre()));
            }

            progreso.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

    }

}
