package io.mycoach.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import io.mycoach.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private NavController currentNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            createBottomNavigation();
        }

//        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);
//        //Drawable icon = getResources().getDrawable(R.drawable.ic_dashboard);
//        BottomNavigationItemView btnView = bnve.getBottomNavigationItemView(2);
//
//        btnView.setY(-20);
//        btnView.setIconSize(80);

    }


    public void createBottomNavigation() {

        BottomNavigationViewEx bottomNavigationView =  findViewById(R.id.bottom_nav);
        // Setup the bottom navigation view with a list of navigation graphs

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        currentNav = navController;
    }
}
