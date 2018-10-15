package utn.rodrigo_anton.primerparcial.activityes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Set;

import utn.rodrigo_anton.primerparcial.classes.DataBaseHelper;
import utn.rodrigo_anton.primerparcial.R;

// Defino la activity
public class LogIn extends AppCompatActivity {

    // VIEW OBJECTS
    public Button log_in_button;
    public EditText edit_text_user;
    public EditText edit_text_pass;
    public TextView incorrect_msg;

    // LOCAL VARIABLES
    public String user;
    public String pass;
    public int access_code;
    public String access_name;
    public String nombre;
    public String apellido;
    public Context context;

    // Arranco la activity (constructor)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Definimos la orientación de la SplashScreen como Portrait (vertical)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Seteamos la vista de la activity
        setContentView(R.layout.activity_log_in);

        // Find views
        log_in_button = findViewById(R.id.loginbt);
        edit_text_user = (EditText) findViewById(R.id.user_id);
        edit_text_pass = (EditText) findViewById(R.id.pass_id);
        incorrect_msg = (TextView) findViewById(R.id.wrong_id);

        // Inicialiazaciín de Variables locales
        context = this;

        // Blanqueo el cuadro de error
        incorrect_msg.setText("");

        // Relleno el username si existe (Shared Preferences)
        user = getPreferenceUsername();
        if (user != null) {
            edit_text_user.setText(user);
        }

        // Accion cuando apreto el botón
        log_in_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // obtengo el largo de los campos (caracteres)
                int Length_user = edit_text_user.getText().length();
                int Length_pass = edit_text_pass.getText().length();

                // Chequeo si escribió algo
                if (Length_pass != 0 && Length_user != 0) {

                    //Copio los datos
                    pass = edit_text_pass.getText().toString();
                    user = edit_text_user.getText().toString();

                    // Chequeo SQL Inyection
                    if (!user.contains(";") && !pass.contains(";")) {
                        // Chequeo si el usuario existe
                        DataBaseHelper user_db_helper = new DataBaseHelper(LogIn.this, "users");
                        SQLiteDatabase users_db = user_db_helper.getWritableDatabase();
                        if (users_db.isOpen()) {
                            Cursor user_cursor = users_db.rawQuery("SELECT _id,username,first_name,last_name FROM user_info WHERE username='" + user.toLowerCase() + "'", null);
                            if (user_cursor.moveToFirst()) {
                                // Chequeo si la contraseña concuerda
                                Cursor pass_cursor = users_db.rawQuery("SELECT passwd FROM user_passwd WHERE user='" + user_cursor.getInt(0) + "'", null);
                                pass_cursor.moveToFirst();
                                if (pass.contentEquals(pass_cursor.getString(0))) {
                                    // Consigo el nivel de acceso del usuraio
                                    Cursor access_cursor = users_db.rawQuery("SELECT user_access.access, access_code.name FROM user_access JOIN access_code WHERE user_access.access=access_code.access AND user='" + user_cursor.getInt(0) + "'", null);
                                    if (access_cursor.moveToFirst()) {
                                        access_code = access_cursor.getInt(0);
                                        access_name = access_cursor.getString(1);
                                    }
                                    nombre = user_cursor.getString(2);
                                    apellido = user_cursor.getString(3);
                                    // Guardo el usuario en las preferencias
                                    setPreferenceUsername (user);
                                    // Hago un intent a la otra activity y le paso los datos del usuario
                                    Intent intent = new Intent(LogIn.this, CarSelection.class);
                                    intent.putExtra("Username", user);
                                    intent.putExtra("nombre", nombre);
                                    intent.putExtra("apellido", apellido);
                                    intent.putExtra("access_code", access_code);
                                    intent.putExtra("access_name", access_name);
                                    startActivity(intent);
                                    // Cierro los accesos a la DB
                                    user_cursor.close();
                                    pass_cursor.close();
                                    users_db.close();
                                    user_db_helper.close();
                                    // Matamos la activity para que el usuario no pueda volver para atrás con el botón de back
                                    finish();
                                } else {
                                    // Caso contraseña incorrecta
                                    edit_text_pass.getText().clear();
                                    incorrect_msg.setText("Contraseña incorrecta");
                                }
                                // Cierro el cursor de contraseña
                                pass_cursor.close();
                            } else {
                                // Caso el usuario no existe
                                incorrect_msg.setText("El usuario no existe");

                            }
                            // Cierro el cursor de usuario y al conexión con la DB
                            user_cursor.close();
                            users_db.close();
                        } else {
                            // Caso no se puede conectar a la base de datos
                            incorrect_msg.setText("No se pudo conectar a la base de datos.");
                        }
                        // Cierro el helper de la DB
                        user_db_helper.close();
                    }
                    else {
                        // Caso SQL Inyection
                        edit_text_pass.getText().clear();
                        edit_text_user.getText().clear();
                        incorrect_msg.setText("No se puede realizar tu SQL Injection.");
                    }
                }
                else {
                    // Caso vacío
                    incorrect_msg.setText("");
                }
            }
        });
    }

    // Obtengo de la defaultSharedPreference el usuario (si existe)
    String getPreferenceUsername() {
        SharedPreferences username_get = PreferenceManager.getDefaultSharedPreferences(context);
        return (username_get.getString("username",null));
    }

    // Seteo en la defaultSharedPreference el usuario
    void setPreferenceUsername (String username_in) {
        if (username_in != null ) {
            SharedPreferences mPreferences;
            SharedPreferences.Editor mEditor;
            mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            mEditor = mPreferences.edit();
            mEditor.putString("username",username_in);
            mEditor.commit();
        }
        return;
    }
}