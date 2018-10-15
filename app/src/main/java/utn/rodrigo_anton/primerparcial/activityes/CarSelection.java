package utn.rodrigo_anton.primerparcial.activityes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import utn.rodrigo_anton.primerparcial.classes.Auto;
import utn.rodrigo_anton.primerparcial.classes.DataBaseHelper;
import utn.rodrigo_anton.primerparcial.R;
import utn.rodrigo_anton.primerparcial.adapters.CarAdapter;

public class CarSelection extends AppCompatActivity {

    // VIEW OBJECTS
    public RecyclerView cars_recycleview;
    public TextView name_msg;
    public TextView title_msg;
    public Toolbar toolbar;

    // LOCAL VARIABLES
    public String user;
    public int access_code;
    public String access_name;
    public String nombre;
    public String apellido;
    public String brand;
    public String msg_aux;
    public ArrayList<Auto> cars_array;
    public Context context;
    Set<String> preference_automaker;
    Set<String> preference_type;
    public CarAdapter car_adapter;
    public String acivity;
    DataBaseHelper cars_db_helper;
    AlertDialog.Builder delete_dialog;
    AlertDialog.Builder edit_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Definimos la orientación como Portrait (vertical)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Seteamos la vista de la activity
        setContentView(R.layout.activity_car_selection);

        // Traigo los datos de usuario del login
        user = getIntent().getStringExtra("Username");
        nombre = getIntent().getStringExtra("nombre");
        apellido = getIntent().getStringExtra("apellido");
        access_name = getIntent().getStringExtra("access_name");
        access_code = getIntent().getIntExtra("access_code",0);

        // Inicialiazaciín de Variables locales
        context = this;
        cars_db_helper = new DataBaseHelper(this, "cars");
        cars_array = new ArrayList<Auto>();
        acivity = "CarSelection";
        delete_dialog = new AlertDialog.Builder(this);
        edit_dialog = new AlertDialog.Builder(this);

        // Agrego la toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.inflateMenu(R.menu.car_select_menu);

        // Asigno las findviews
        title_msg = (TextView) toolbar.findViewById(R.id.txtAbTitulo);
        name_msg = (TextView) toolbar.findViewById(R.id.txtAbSubTitulo);
        cars_recycleview = (RecyclerView) findViewById(R.id.carlist_id);
        cars_recycleview.setHasFixedSize(true);


        // Creo el mensaje de bienvenida (nombre)
        msg_aux = nombre + " " + apellido + " (" + user + ")";
        name_msg.setText(msg_aux);

        // Hago un lisener con los botones de la toolbar y les asigno acciones
        toolbar.setOnMenuItemClickListener(
            new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_settings:
                            goToPreferences();
                            break;
                        case R.id.add_stock:
                            goToCarAdd();
                            break;
                        case R.id.edit_user:
                            goToUserManager();
                            break;
                    }
                    return true;
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualizo la lista cuando vuelvo a la pantalla de la activity
        if (acivity.contentEquals("CarSelection")) {
            preference_automaker = getPreferenceAutomaker();
            preference_type = getPreferenceType();
            getList();
            showList();
        }
    }

    // Creamos el menu de botones de la toolbar segun nivel de acceso
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.car_select_menu, menu);
        setMenuPerAccess(menu);
        return true;
    }

    // Botones del menú contextual
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 1: //Editar
                editCarStock(car_adapter.getPosition());
                break;
            case 2: //Eliminar
                removeCar(car_adapter.getPosition());
                break;
        }
        return super.onContextItemSelected(item);
    }

    // Tipo de menú por tipo de acceso
    void setMenuPerAccess(Menu menu){
        if (access_code >1 ) {
            menu.findItem(R.id.add_stock).setVisible(true);
            menu.findItem(R.id.add_stock).setEnabled(true);
            if (access_code > 2) {
                menu.findItem(R.id.edit_user).setVisible(true);
                menu.findItem(R.id.edit_user).setEnabled(true);
            }
        }
    }

    // Traigo de las preferencias la automotriz
    Set<String> getPreferenceAutomaker() {
        SharedPreferences filtros = PreferenceManager.getDefaultSharedPreferences(context);
        return (filtros.getStringSet("automaker",null));
    }

    // Traigo de las preferencias el tipo
    Set<String> getPreferenceType() {
        SharedPreferences filtros = PreferenceManager.getDefaultSharedPreferences(context);
        return (filtros.getStringSet("type",null));
    }

    // Armo la lista con los autos de las preferencencias
    void getList () {
        // Vacío el array de autos
        cars_array.clear();

        // Me fijo en que subactivity estoy
        switch (acivity) {
            case "CarSelection":
                // Chequeo si las preferencias vienen con algo
                if (preference_automaker != null && preference_type != null) {
                    if ((!preference_automaker.isEmpty()) && (!preference_type.isEmpty())) {
                        // Armo los querys
                        String list_query;
                        String automaker_query;
                        String type_query;
                        // Traigo las automotrices
                        String[] aux_iteration = preference_automaker.toArray(new String[]{});
                        automaker_query = "automaker_name.code = '" + aux_iteration[0].toLowerCase() + "'";
                        if (aux_iteration.length > 1) {
                            for (int i = 1; i < aux_iteration.length; i++) {
                                String aux = " OR automaker_name.code = '" + aux_iteration[i].toLowerCase() + "'";
                                automaker_query = automaker_query + aux;
                            }
                        }
                        // Traigo los tipos de auto
                        aux_iteration = preference_type.toArray(new String[]{});
                        type_query = "car_info.type = '" + aux_iteration[0] + "'";
                        if (aux_iteration.length > 1) {
                            for (int i = 1; i < aux_iteration.length; i++) {
                                String aux = " OR car_info.type = '" + aux_iteration[i] + "'";
                                type_query = type_query + aux;
                            }
                        }
                        // Armo el query final
                        list_query = "SELECT car_info.codename " +
                                "FROM car_info JOIN automaker_name " +
                                "WHERE car_info.automaker=automaker_name._id " +
                                "AND (" + automaker_query + ") " +
                                "AND (" + type_query + ") " +
                                "AND car_info.stock>0";

                        // Traigo la informacion de la DB (ejecuto el query y armo el array)
                        SQLiteDatabase cars_db = cars_db_helper.getWritableDatabase();
                        if (cars_db.isOpen()) {
                            Cursor car_cursor = cars_db.rawQuery(list_query, null);
                            if (car_cursor.moveToFirst()) {
                                do {
                                    cars_array.add(new Auto(car_cursor.getString(0), context));
                                } while (car_cursor.moveToNext());
                            }
                            car_cursor.close();
                            cars_db.close();
                        }
                    }
                }
                break;
            case "CarAdd":
                SQLiteDatabase cars_db = cars_db_helper.getWritableDatabase();
                if (cars_db.isOpen()) {
                    String CarQuery = "SELECT car_info.codename FROM car_info WHERE car_info.stock=0";
                    Cursor car_cursor = cars_db.rawQuery(CarQuery, null);
                    if (car_cursor.moveToFirst()) {
                        do {
                            String carcode = car_cursor.getString(0);
                            cars_array.add(new Auto(carcode, context));
                        } while (car_cursor.moveToNext());
                    }
                    car_cursor.close();
                    cars_db.close();
                }
                break;
        }
        return;
    }

    // Muestro la lista
    void showList() {
        // Creo el car_adapter y le asigno el array
        car_adapter = new CarAdapter(cars_array, acivity, access_code);
        cars_recycleview.setAdapter(car_adapter);
        cars_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        // Si el array está vacio, hago que la list sea invisible
        if (cars_array.isEmpty()) {
            cars_recycleview.setVisibility(View.INVISIBLE);
        }
        else {
            cars_recycleview.setVisibility(View.VISIBLE);
        }

        // Creo la opcion on click del car_adapter
        car_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acivity.contentEquals("CarSelection")) {
                    goToCarInfo(car_adapter.getCarByPosition(cars_recycleview.getChildAdapterPosition(v)).getModelCode());
                }
                if (acivity.contentEquals("CarAdd")) {
                    editCarStock(cars_recycleview.getChildAdapterPosition(v));
                }
            }
        });
    }

    void removeCar (final int position) {
        // Consulto si me pasaron un indice mayor al maximo
        if (position < cars_array.size()) {
            // Traigo el auto selecionado
            final Auto car_aux = cars_array.get(position);
            //Creo el dialogo de eliminar personalizado
            String delete_question = "¿Desea eliminar completamente el stock de " + car_aux.getAutomaker_name() + " " + car_aux.getModel() + "?";
            delete_dialog.setMessage(delete_question)
                    .setTitle("Confirmacion")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
                        public void onClick(DialogInterface dialog, int id) {
                            SQLiteDatabase cars_db = cars_db_helper.getWritableDatabase();
                            if (cars_db.isOpen()) {
                                String car_code = car_aux.getModelCode();
                                cars_db.execSQL("UPDATE car_info SET stock=0 WHERE codename='" + car_code + "'");
                                cars_db.close();
                                car_adapter.removeAt(position);
                            }
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            // Lo muestro
            delete_dialog.show();
        }
        return;
    }

    void editCarStock (final int position) {
        // Consulto si me pasaron un indice mayor al maximo
        if (position < cars_array.size()) {
            // Traigo el auto selecionado
            final Auto car_aux = cars_array.get(position);
            // Armo la pregunta
            String edit_question = new String();
            if (acivity.contentEquals("CarSelection")) {
                edit_question = "Ingrese el nuevo valor de stock (actual " + String.valueOf(car_aux.getStock()) + ") de " + car_aux.getAutomaker_name() + " " + car_aux.getModel();
            } else if (acivity.contentEquals("CarAdd")){
                edit_question = "Ingrese el valor de stock para agregar el " + car_aux.getAutomaker_name() + " " + car_aux.getModel();
            }
            // Edito el dialog
            edit_dialog.setTitle("Acualizar Stock");
            edit_dialog.setMessage(edit_question);

            // Agrego el cuadro de edittext
            final EditText input_stock = new EditText(this);
            LinearLayout.LayoutParams layout_for_edittext = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input_stock.setLayoutParams(layout_for_edittext);
            input_stock.setGravity(Gravity.CENTER);
            input_stock.setInputType(InputType.TYPE_CLASS_NUMBER);
            // Le asigno un maximo de caracteres
            input_stock.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(7) });
            edit_dialog.setView(input_stock);

            // Boton de Actualizar
            edit_dialog.setPositiveButton("Actualizar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            int new_stock = Integer.parseInt(input_stock.getText().toString());
                            if (new_stock > 0) {
                                SQLiteDatabase cars_db = cars_db_helper.getWritableDatabase();
                                if (cars_db.isOpen()) {
                                    String car_code = car_aux.getModelCode();
                                    cars_db.execSQL("UPDATE car_info SET stock=" + new_stock + " WHERE codename='" + car_code + "'");
                                    cars_db.close();
                                    if (acivity == "CarAdd") {
                                        car_adapter.removeAt(position);
                                    }
                                }
                            }
                        }
                    });

            // Boton de candelar
            edit_dialog.setNegativeButton("Cancelar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            edit_dialog.show();
        }
    }

    // Accion para ir a la activity con la info
    void goToCarInfo (String car_model_code) {
        if (car_model_code != null) {
            Intent car_info_intent = new Intent(CarSelection.this, CarInfo.class);
            car_info_intent.putExtra("CarCode", car_model_code);
            startActivity(car_info_intent);
        }
        return;

    }

    void goToPreferences () {
        startActivity(new Intent(CarSelection.this, Preferences.class));
        return;
    }

    void goToCarAdd () {
        acivity = "CarAdd";
        toolbar.findViewById(R.id.txtAbSubTitulo).setVisibility(View.INVISIBLE);
        title_msg.setText("Agregar al stock");
        toolbar.getMenu().clear();
        getList();
        showList();
        return;
    }

    void goToCarSelction () {
        acivity = "CarSelection";
        preference_automaker = getPreferenceAutomaker();
        preference_type = getPreferenceType();
        toolbar.findViewById(R.id.txtAbSubTitulo).setVisibility(View.VISIBLE);
        title_msg.setText("Bienvenido");
        toolbar.inflateMenu(R.menu.car_select_menu);
        setMenuPerAccess(toolbar.getMenu());
        getList();
        showList();

        return;

    }

    void goToUserManager () {
        startActivity(new Intent(CarSelection.this, UserManager.class));
        return;
    }

    // Redefino la acción del botón de back
    public void onBackPressed() {
        switch (acivity) {
            case "CarSelection":
                super.onBackPressed();
                break;
            case "CarAdd":
                goToCarSelction();
                break;
        }
    }
}
