package utn.rodrigo_anton.primerparcial.activityes;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

import utn.rodrigo_anton.primerparcial.classes.DataBaseHelper;
import utn.rodrigo_anton.primerparcial.R;

// Defino la activity
public class SplashScreen extends AppCompatActivity {

    // Creo variables locales
    private static final long SPLASH_SCREEN_DELAY = 1000;

    // Arranco la activity (constructor)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Definimos la orientación de la SplashScreen como Portrait (vertical)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Escondemos el título de la app
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Seteamos la vista de la activity
        setContentView(R.layout.activity_splash_screen);

        // Levantamos la base de datos de usuarios
        DataBaseHelper UDbHelper = new DataBaseHelper(this, "users");
        UDbHelper.createDataBase();

        // Levantamos la base de datos de autos
        DataBaseHelper CDbHelper = new DataBaseHelper(this, "cars");
        CDbHelper.createDataBase();

            // TASK
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                // Ir a la activity para loguearse
                Intent mainIntent = new Intent().setClass(SplashScreen.this, LogIn.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(mainIntent);
                // Matamos la activity para que el usuario no pueda volver para atrás con el botón de back
                finish();
            }
        };
        // Simulamos con un timer un tiempo de espera definido en una constante al comienzo
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}