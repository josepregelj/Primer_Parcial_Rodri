package utn.rodrigo_anton.primerparcial.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import utn.rodrigo_anton.primerparcial.R;
import utn.rodrigo_anton.primerparcial.classes.Auto;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarImageFragment extends Fragment {

    ImageView AutomakerView, CarView;
    Auto selected_car;

    public CarImageFragment() {
    }

    public static CarImageFragment newInstance(Auto selected_car_in) {
        CarImageFragment fragment = new CarImageFragment();
        fragment.setCar(selected_car_in);
        return fragment;
    }

    private void setCar(Auto selected_car) {
        this.selected_car = selected_car;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_image, container, false);
        // Find views
        CarView = (ImageView) view.findViewById(R.id.select_img_id);
        AutomakerView = (ImageView) view.findViewById(R.id.select_automaker_img);

        // Muetro las imagenes
        if (selected_car.getImage_id() != 0) {
            CarView.setImageResource(selected_car.getImage_id());
        }
        if (selected_car.getAutomaker_logo() != 0) {
            AutomakerView.setImageResource(selected_car.getAutomaker_logo());
        }

        // Inflate the layout for this fragment
        return view;
    }

}
