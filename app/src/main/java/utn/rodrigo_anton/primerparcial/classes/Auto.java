package utn.rodrigo_anton.primerparcial.classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Auto {
    private String automaker_code;
    private String automaker_name;
    private int automaker_logo;
    private String model;
    private String model_code;
    private int image_id;
    private int type_int;
    private String type;
    private int stock;
    private String web;
    private Context context;

    public Auto(String car_model_code, Context cont) {
        context = cont;
        final DataBaseHelper CDbHelper = new DataBaseHelper(context, "cars");
        SQLiteDatabase cars = CDbHelper.getWritableDatabase();
        if (cars.isOpen()) {
            Cursor car_cursor = cars.rawQuery(
                    "SELECT car_info.model, car_info.type, car_type.name, car_info.stock, automaker_name.code, automaker_name.name, car_info.automaker " +
                            "FROM car_info JOIN automaker_name JOIN car_type " +
                            "WHERE car_info.automaker=automaker_name._id AND car_info.type=car_type._id " +
                            "AND car_info.codename='" + car_model_code + "'", null);
            if (car_cursor.moveToFirst()) {
                model_code = car_model_code;
                model = car_cursor.getString(0);
                type_int = car_cursor.getInt(1);
                type = car_cursor.getString(2);
                stock = car_cursor.getInt(3);
                automaker_code = car_cursor.getString(4);
                automaker_name = car_cursor.getString(5);

                String aux = model_code;
                aux = aux.replace("-", "_").toLowerCase();
                aux = automaker_code + "_" + aux;
                image_id = context.getResources().getIdentifier(aux, "drawable", context.getPackageName());
                if (image_id == 0) {
                    image_id = context.getResources().getIdentifier("default_car", "drawable", context.getPackageName());
                }

                automaker_logo = context.getResources().getIdentifier(automaker_code.toLowerCase(),"drawable", context.getPackageName());
                if (automaker_logo == 0) {
                    automaker_logo = context.getResources().getIdentifier("default_logo","drawable", context.getPackageName());
                }

                int automaker_int = car_cursor.getInt(6);
                Cursor web_cursor = cars.rawQuery("SELECT car_web.web, car_web.ext FROM car_web " +
                        "WHERE car_web.type='" + String.valueOf(type_int) + "' AND " +
                        "car_web.automaker='" + String.valueOf(automaker_int) + "'"  ,null);
                if (web_cursor.moveToFirst()) {
                    web = web_cursor.getString(0) + model_code + web_cursor.getString(1);
                }
            }
            car_cursor.close();
            cars.close();

        }
        CDbHelper.close();
    }

    public String getModel() {
        return model;
    }

    public String getModelCode() {
        return model_code;
    }

    public String getType() {
        return type;
    }

    public int getImage_id() {
        return image_id;
    }

    public int getStock() {
        return stock;
    }

    public String getAutomaker_name() {
        return automaker_name;
    }

    public String getWeb() {
        return web;
    }

    public int getAutomaker_logo() {
        return automaker_logo;
    }
}
