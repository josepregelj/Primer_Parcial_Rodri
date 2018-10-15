package utn.rodrigo_anton.primerparcial.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import utn.rodrigo_anton.primerparcial.classes.Auto;
import utn.rodrigo_anton.primerparcial.fragments.CarImageFragment;
import utn.rodrigo_anton.primerparcial.fragments.CarInfoFragment;

public class TabAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] =
            new String[] {"Información", "Foto"};
    private Auto selected_car;

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = null;

        switch(position) {
            case 0:
                f = CarInfoFragment.newInstance(selected_car);
                break;
            case 1:
                f = CarImageFragment.newInstance(selected_car);
                break;
        }

        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    public void setSelectedCar(Auto selected_car) {
        this.selected_car = selected_car;
    }
}
