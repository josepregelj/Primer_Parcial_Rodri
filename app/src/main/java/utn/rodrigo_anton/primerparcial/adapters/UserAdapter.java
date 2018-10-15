package utn.rodrigo_anton.primerparcial.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import utn.rodrigo_anton.primerparcial.R;
import utn.rodrigo_anton.primerparcial.classes.User;

public class UserAdapter extends ArrayAdapter<User> {
    public User[] users;

    public UserAdapter(Context context, User[] users) {
        super(context, R.layout.user_listview, users);
        this.users=users;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.user_listview, null);

        if (users[position] != null) {
            TextView UserName = (TextView) item.findViewById(R.id.user_name_item);
            String auxName = users[position].getApellido() + ", " + users[position].getNombre() + " (" + users[position].getUsername() + ")";
            UserName.setText(auxName);

            TextView AccessName = (TextView) item.findViewById(R.id.user_acces_item);
            AccessName.setText(users[position].getAccessName());
        }

        return(item);
    }
}
