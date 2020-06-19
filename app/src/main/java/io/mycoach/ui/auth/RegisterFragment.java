package io.mycoach.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import io.mycoach.R;
import io.mycoach.model.User;

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";

    private RegisterViewModel registerViewModel;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRegisterViewModel();
    }

    private void initRegisterViewModel() {
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
    }

    private void createNewUser(User newUser) {
        registerViewModel
                .createUser(newUser)
                .observe(this, user -> {
                    if (user.isCreated()) {
                        Toast.makeText(this.getContext(), "Bonjour " + user.getName() + "!\n" + "Votre compte a été crée avec succès.", Toast.LENGTH_LONG).show();
                    }
                    //goToMainActivity(user);
                });
    }
}
