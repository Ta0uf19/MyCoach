package io.mycoach.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mycoach.R;
import io.mycoach.utils.MenuUtils;

public class EntryFragment extends Fragment {
    private static final String TAG = "EntryFragment";

    @BindView(R.id.btn_login) Button login;
    @BindView(R.id.btn_register) Button register;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry, container, false);
        ButterKnife.bind(this, view);

        // configure navController
        navController = NavHostFragment.findNavController(this);

        // hide navigation menu
        MenuUtils.hideNavigationMenu(getActivity());

        // actions
        login.setOnClickListener(v -> navController.navigate(R.id.action_entryFragment_to_loginFragment));
        register.setOnClickListener(v -> navController.navigate(R.id.action_entryFragment_to_registerFragment));

        return view;
    }

//    public void hideNavigationMenu() {
//        // Hide menu
//        BottomNavigationViewEx bottomNavigationViewEx = getActivity().findViewById(R.id.bottom_nav);
//        bottomNavigationViewEx.setVisibility(View.GONE);
//
//        //Hide Fab
//        FloatingActionButton fab = getActivity().findViewById(R.id.fab_chat);
//        fab.hide();
//    }
}
