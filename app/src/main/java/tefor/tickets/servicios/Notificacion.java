package tefor.tickets.servicios;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import tefor.tickets.R;
import tefor.tickets.activities.TicketRecibido;

public class Notificacion extends FirebaseMessagingService {

    public static final String TIPO = "tipo";
    public static final String MENSAJE = "mensaje";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() == 0) {
            return;
        }

        if (remoteMessage.getData().containsKey(TIPO)
                && remoteMessage.getData().get(TIPO).equals(MENSAJE)) {
            String paciente = remoteMessage.getData().get(MENSAJE);
            String pacienteID = remoteMessage.getData().get(TicketRecibido.PACIENTE_ID);
            // String sexo = remoteMessage.getData().get(TicketRecibido.SEXO);

            Intent intent = new Intent(this, TicketRecibido.class);
            intent.putExtra(TicketRecibido.PACIENTE_ID, pacienteID);
            intent.putExtra(MENSAJE, paciente);
            // intent.putExtra(TicketRecibido.SEXO, sexo);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle("NDR Message")
                    .setSmallIcon(R.drawable.ic_local_hospital_black_24dp)
                    .setContentText("Turno de " + paciente)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }
    }

}
