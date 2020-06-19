package io.mycoach.utils;

import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import io.mycoach.R;

public class MenuUtils {

    public static void hideNavigationMenu(FragmentActivity view) {
        // Hide menu
        BottomNavigationViewEx bottomNavigationViewEx = view.findViewById(R.id.bottom_nav);
        bottomNavigationViewEx.setVisibility(View.GONE);

        //Hide Fab
        FloatingActionButton fab = view.findViewById(R.id.fab_chat);
        fab.hide();
    }

    public static void showNavigationMenu(FragmentActivity view) {
        // Hide menu
        BottomNavigationViewEx bottomNavigationViewEx = view.findViewById(R.id.bottom_nav);
        bottomNavigationViewEx.setVisibility(View.VISIBLE);

        //Hide Fab
        FloatingActionButton fab = view.findViewById(R.id.fab_chat);
        fab.show();
    }
}
