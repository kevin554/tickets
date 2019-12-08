package tefor.tickets.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tefor.tickets.R;
import tefor.tickets.dto.Paciente;

public class AdaptadorListaPacientes extends BaseAdapter {

    private List<Paciente> lista;
    private Context contexto;

    public AdaptadorListaPacientes(Context contexto, List<Paciente> lista) {
        this.lista = lista;
        this.contexto = contexto;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int posicion) {
        return lista.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return lista.get(posicion).getCodigoID();
    }

    @Override
    public View getView(int posicion, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(contexto).inflate(R.layout.layout_detalle_pacientes, parent,
                    false);
        }

        TextView pacienteTextView = (TextView)
                view.findViewById(R.id.layoutDetallePacientes_pacienteTextView);
        TextView sintomasTextView = (TextView)
                view.findViewById(R.id.layoutDetallePacientes_sintomasTextView);
        TextView atendidoTextView = (TextView)
                view.findViewById(R.id.layoutDetallePacientes_atendidoTextView);

        Paciente objPaciente = lista.get(posicion);

        pacienteTextView.setText(objPaciente.getNombre());

        String sintomas = "";
        if (objPaciente.getTieneHemorragia().equals("VERDADERO")) {
            sintomas = "tiene hemorragias";
        }

        if (objPaciente.getEstaConsciente().equals("FALSO")) {
            if (!sintomas.isEmpty()) {
                sintomas = sintomas.concat(", ");
            }

            sintomas = sintomas.concat("esta inconsciente");
        }

        if (objPaciente.getEstaEmbarazada().equals("VERDADERO")) {
            if (!sintomas.isEmpty()) {
                sintomas = sintomas.concat(", ");
            }

            sintomas = sintomas.concat("esta embarazada");
        }

        sintomasTextView.setText(sintomas);

        if (objPaciente.getFueAtendido().equals("VERDADERO")) {
            atendidoTextView.setBackgroundResource(R.drawable.checked);
        } else {
            atendidoTextView.setBackgroundResource(R.drawable.cancel);
        }

        return view;
    }

}
