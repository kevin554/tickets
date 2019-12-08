package tefor.tickets.activities;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import tefor.tickets.R;
import tefor.tickets.adaptadores.AdaptadorListaPacientes;
import tefor.tickets.dao.FactoryDAO;
import tefor.tickets.dao.PacienteDAO;
import tefor.tickets.dto.Paciente;
import tefor.tickets.utiles.Contexto;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton botonFlotante;
    private ListView pacientesListView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();

        botonFlotante.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actualizarLista();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        botonFlotante.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                android.content.ClipData clipData = android.content.ClipData.newPlainText("", "");
                android.view.View.DragShadowBuilder shadowBuilder =
                        new android.view.View.DragShadowBuilder(v);
                v.startDrag(clipData, shadowBuilder, v, 0);

                return true;
            }

        });
    }

    private void inicializarComponentes() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        botonFlotante = (FloatingActionButton) findViewById(R.id.botonFlotante);
        pacientesListView = (ListView) findViewById(R.id.contentMain_pacientesListView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.contentMain_swipeRefreshLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        actualizarLista();
    }

    private void actualizarLista() {
        new CargarListaTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_refrescar:
                actualizarLista();
                break;

            case R.id.accion_acerca:
                mostrarInformacionApp();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void mostrarInformacionApp() {
        DialogFragment fragmento = new AcercaDe();
        fragmento.show(getFragmentManager(), Contexto.APP_TAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.botonFlotante:
                agregarPaciente();
        }
    }

    private void agregarPaciente() {
        Intent intent = new Intent(getApplicationContext(), RegistrarPaciente.class);
        startActivity(intent);
    }

    private class CargarListaTask extends AsyncTask<Void, String, List<Paciente>> {

        private ProgressDialog progreso;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progreso = new ProgressDialog(MainActivity.this);
            progreso.setIndeterminate(true);
            progreso.setTitle("obteniendo datos");
            progreso.setCancelable(false);
            progreso.show();
        }

        @Override
        protected List<Paciente> doInBackground(Void... params) {
            publishProgress("por favor espere...");

            FactoryDAO factory = FactoryDAO.getOrCreate();
            PacienteDAO dao = factory.newPacienteDAO();

            List<Paciente> lista = null;

            try {
                lista = dao.seleccionarTodos();
            } catch (Exception ex) {
                Log.e(Contexto.APP_TAG, "Hubo un error al cargar la lista");
            }

            return lista;
        }

        @Override
        protected void onPostExecute(List<Paciente> pacientes) {
            super.onPostExecute(pacientes);

            progreso.dismiss();

            if (pacientes == null) {
                Toast.makeText(MainActivity.this, R.string.error_obtener_datos,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            AdaptadorListaPacientes adaptador = new AdaptadorListaPacientes(MainActivity.this,
                    pacientes);
            pacientesListView.setAdapter(adaptador);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            progreso.setMessage(values[0]);
        }

    }

}
