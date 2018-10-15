package utn.rodrigo_anton.primerparcial.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import utn.rodrigo_anton.primerparcial.classes.Auto;
import utn.rodrigo_anton.primerparcial.R;

public class CarAdapter
        extends RecyclerView.Adapter<CarAdapter.CarViewHolder>
        implements View.OnClickListener {

    private View.OnClickListener listener;
    private ArrayList<Auto> cars_array;
    private int position;
    private String activity_adapter;
    private int access_code_adapter;

    public static class CarViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {

        private TextView car_name_textview;
        private TextView automaker_name_textview;
        private ImageView car_imageview;
        private String activity_holder;
        private int access_code_holder;


        public CarViewHolder(View item_view, String new_activity_holder, int new_access_code) {
            super(item_view);
            activity_holder = new_activity_holder;
            access_code_holder = new_access_code;
            car_name_textview = (TextView) item_view.findViewById(R.id.user_acces_item);
            automaker_name_textview = (TextView) item_view.findViewById(R.id.brandlist);
            car_imageview = (ImageView) item_view.findViewById(R.id.carimg);

            item_view.setOnCreateContextMenuListener(this);
        }

        public void bindCar(Auto car) {
            if (car != null) {
                car_name_textview.setText(car.getModel());
                automaker_name_textview.setText(car.getAutomaker_name());
                car_imageview.setImageResource(car.getImage_id());
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if ((access_code_holder>1) && (activity_holder.contentEquals("CarSelection"))) {
                MenuItem Edit = menu.add(Menu.NONE, 1, 1, "Editar");
                MenuItem Delete = menu.add(Menu.NONE, 2, 2, "Eliminar");
            }

        }
    }

    public CarAdapter(ArrayList<Auto> new_cars_array, String new_activity, int new_access_code) {
        cars_array = new_cars_array;
        activity_adapter = new_activity;
        access_code_adapter = new_access_code;
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.carselect_listview, viewGroup, false);

        itemView.setOnClickListener(this);

        CarViewHolder cvh = new CarViewHolder(itemView, activity_adapter, access_code_adapter);

        return cvh;
    }

    @Override
    public void onBindViewHolder(CarViewHolder viewHolder, final int pos) {
        Auto car = cars_array.get(pos);
        viewHolder.bindCar(car);

        // add OnLongClickListener on the holder.itemView to capture the position before the context menu is loaded:
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(pos);
                return false;
            }
        });
    }

    // remove the Listener so that there are no reference issues.
    @Override
    public void onViewRecycled(CarViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return cars_array.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public Auto getCarByPosition(int pos){

        return cars_array.get(pos);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void removeAt(int position) {
        cars_array.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, cars_array.size());
        notifyDataSetChanged();
    }
}
