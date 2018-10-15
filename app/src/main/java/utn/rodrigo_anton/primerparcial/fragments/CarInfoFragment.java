package utn.rodrigo_anton.primerparcial.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import utn.rodrigo_anton.primerparcial.R;
import utn.rodrigo_anton.primerparcial.classes.Auto;

public class CarInfoFragment extends Fragment {

    TextView AutomakerView, ModelView, TypeView, StockView, WebView;
    Auto selected_car;

    public CarInfoFragment() {
    }

    public static CarInfoFragment newInstance(Auto selected_car_in) {
        CarInfoFragment fragment = new CarInfoFragment();
            fragment.setCar(selected_car_in);
        return fragment;
    }

    private void setCar(Auto selected_car) {
        this.selected_car = selected_car;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_info, container, false);
        // Find views
        AutomakerView = (TextView) view.findViewById(R.id.select_automaker_id);
        ModelView = (TextView) view.findViewById(R.id.select_model_id);
        TypeView = (TextView) view.findViewById(R.id.select_type_id);
        StockView = (TextView) view.findViewById(R.id.select_stock_id);
        WebView = (TextView) view.findViewById(R.id.select_web_id);

        // Muestro los datos
        AutomakerView.setText("Automotriz: " + selected_car.getAutomaker_name());
        ModelView.setText("Modelo: " + selected_car.getModel());
        TypeView.setText("Tipo: " + selected_car.getType());
        String Stock = "Stock: " + String.valueOf(selected_car.getStock()) + " unidad";
        if (selected_car.getStock()>1) {
            Stock = Stock.concat("es");
        }
        StockView.setText(Stock);

        //Muestro la web
        String auxWeb = "<a href=\"" + selected_car.getWeb() + "\">Página web</a>";
        if(android.os.Build.VERSION.SDK_INT < 24){
            WebView.setText(Html.fromHtml(auxWeb));
        }
        else
        {
            WebView.setText(Html.fromHtml(auxWeb,Html.FROM_HTML_MODE_COMPACT));
        }
        WebView.setMovementMethod(LinkMovementMethod.getInstance());

        // Inflate the layout for this fragment
        return view;
    }

}
