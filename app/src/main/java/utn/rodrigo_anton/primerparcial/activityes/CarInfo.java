package utn.rodrigo_anton.primerparcial.activityes;

import android.content.pm.ActivityInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import utn.rodrigo_anton.primerparcial.adapters.TabAdapter;
import utn.rodrigo_anton.primerparcial.classes.Auto;
import utn.rodrigo_anton.primerparcial.R;

public class CarInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Definimos la orientación de la SplashScreen como Portrait (vertical)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_car_info);
        String carcode_intent = getIntent().getStringExtra("CarCode");

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabAdapter tab_adapter = new TabAdapter(getSupportFragmentManager());
        tab_adapter.setSelectedCar( new Auto(carcode_intent, this));
        viewPager.setAdapter(tab_adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.toolbartabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }
}
