package utn.rodrigo_anton.primerparcial.activityes;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import utn.rodrigo_anton.primerparcial.adapters.UserAdapter;
import utn.rodrigo_anton.primerparcial.classes.DataBaseHelper;
import utn.rodrigo_anton.primerparcial.classes.User;
import utn.rodrigo_anton.primerparcial.R;

public class UserManager extends AppCompatActivity {

    // View objects
    public ListView UsersList;
    public User[] Users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Definimos la orientación como Portrait (vertical)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Seteamos la vista de la activity
        setContentView(R.layout.activity_user_manager);

        // Agrego la toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Editar usuarios");
        //toolbar.inflateMenu(R.menu.car_select_menu);
        toolbar.findViewById(R.id.txtAbSubTitulo).setVisibility(View.INVISIBLE);
        toolbar.findViewById(R.id.txtAbTitulo).setVisibility(View.INVISIBLE);

        // Hago las FindViews
        UsersList = (ListView) findViewById(R.id.userlist_id);

        // Botones de la toolbar
//        toolbar.setOnMenuItemClickListener(
//                new Toolbar.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        if (item.getItemId() == R.id.add_stock) {
//                        }
//                        return true;
//                    }
//                });

        // Traigo la informacion de la DB
        final DataBaseHelper user_db_helper = new DataBaseHelper(this, "users");
        SQLiteDatabase users = user_db_helper.getWritableDatabase();
        if (users.isOpen()) {
            String UserQuery = "SELECT user_info.username, user_info.first_name, user_info.last_name, access_code.name, user_access.access " +
                    "FROM user_info JOIN user_access JOIN access_code " +
                    "WHERE user_info._id=user_access.user AND user_access.access=access_code.access";
            Cursor user_cursor = users.rawQuery(UserQuery, null);
            if (user_cursor.moveToFirst()) {
                int i = 0;
                Users = new User[user_cursor.getCount()];
                user_cursor.moveToFirst();
                do {
                    Users[i] = new User(user_cursor.getString(0), user_cursor.getString(1), user_cursor.getString(2),
                            user_cursor.getString(3), user_cursor.getInt(4));
                    i++;
                } while (user_cursor.moveToNext());
            }
            // Cierro el cursor y la conexión con la base de datos
            user_cursor.close();
            users.close();
        }
        // Cierro el helper de la DB
        user_db_helper.close();

        // Levanto la listview con el car_adapter
        if (Users != null) {
            UserAdapter adaptador = new UserAdapter(this, Users);
            UsersList.setAdapter(adaptador);
        }
    }

    // Creamos el menu de botones de la toolbar
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.car_select_menu, menu);
//        menu.findItem(R.id.action_settings).setVisible(false);
//        menu.findItem(R.id.action_settings).setEnabled(false);
//        menu.findItem(R.id.add_stock).setVisible(true);
//        menu.findItem(R.id.add_stock).setEnabled(true);
//        menu.findItem(R.id.edit_user).setVisible(false);
//        menu.findItem(R.id.edit_user).setEnabled(false);
//        return true;
//    }
}
