package utn.rodrigo_anton.primerparcial.activityes;


import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.ArrayList;
import java.util.List;

import utn.rodrigo_anton.primerparcial.R;
import utn.rodrigo_anton.primerparcial.classes.DataBaseHelper;

public class Preferences extends PreferenceActivity {

    public static class OpcionesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Levanto el xml con la vista de las preferencias
            addPreferencesFromResource(R.xml.preferences);
        }
    }

//    // View objects
//    public OpcionesFragment fragment;
//    MultiSelectListPreference multi_automaker;
//    MultiSelectListPreference multi_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Definimos la orientación como Portrait (vertical)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Seteamos la vista de la activity
        setContentView(R.layout.activity_preferences);

        // Levanto el fragment de preferencias
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new OpcionesFragment())
                .commit();

//        // Levanto las views de los items
//        multi_automaker = (MultiSelectListPreference) findPreference("automaker");
//        multi_type = (MultiSelectListPreference) findPreference("type");
//
//        //  Conecto a la base de datos
//        final DataBaseHelper CDbHelper = new DataBaseHelper(this, "cars");
//        SQLiteDatabase cars = CDbHelper.getWritableDatabase();
//
//        if (cars.isOpen()) {
//            // Traigo las automotrices
//            Cursor am = cars.rawQuery("SELECT _id, name, code FROM automaker_name", null);
//            if (am.moveToFirst()) {
//                List<CharSequence> entries = new ArrayList<CharSequence>();
//                List<CharSequence> entriesValues = new ArrayList<CharSequence>();
//                while (am.moveToNext()) {
//                    String name = am.getString(2);
//                    String displayName = am.getString(1);
//
//                    entries.add(name);
//                    entriesValues.add(displayName);
//                }
//                multi_automaker.setEntries(entries.toArray(new CharSequence[]{}));
//                multi_automaker.setEntryValues(entriesValues.toArray(new CharSequence[]{}));
//            }
//            // Traigo los tipos de auto
//            Cursor tp = cars.rawQuery("SELECT _id, name FROM car_type", null);
//            if (tp.moveToFirst()) {
//                List<CharSequence> entries = new ArrayList<CharSequence>();
//                List<CharSequence> entriesValues = new ArrayList<CharSequence>();
//                while (tp.moveToNext()) {
//                    String name = String.valueOf(tp.getInt(0));
//                    String displayName = tp.getString(1);
//
//                    entries.add(name);
//                    entriesValues.add(displayName);
//                }
//                multi_type.setEntries(entries.toArray(new CharSequence[]{}));
//                multi_type.setEntryValues(entriesValues.toArray(new CharSequence[]{}));
//            }
//        }

    }
}
