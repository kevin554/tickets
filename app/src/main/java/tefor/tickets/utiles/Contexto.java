package tefor.tickets.utiles;

import android.app.Application;

public class Contexto extends Application {

    private static Contexto instancia;
    public static String APP_TAG = "tickets_app";

    @Override
    public void onCreate() {
        super.onCreate();
        instancia = this;
    }

    public static Contexto getInstancia() {
        return instancia;
    }

}