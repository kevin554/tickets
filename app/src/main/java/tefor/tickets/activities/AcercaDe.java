package tefor.tickets.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;

import tefor.tickets.R;

public class AcercaDe extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
        dialogo.setView(R.layout.activity_acerca_de);

        return dialogo.create();
    }

}
