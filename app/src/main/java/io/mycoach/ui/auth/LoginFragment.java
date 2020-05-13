package io.mycoach.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mycoach.R;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    @BindView(R.id.username_edit_text) EditText username;
    @BindView(R.id.pwd_edit_text) EditText password;
    @BindView(R.id.btn_login) Button login;

    private LoginViewModel viewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        // configure navController
        navController = NavHostFragment.findNavController(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        // listner
        login.setOnClickListener(v ->
                viewModel.authenticate(username.getText().toString(), password.getText().toString())
        );

        // observe state of auth
        viewModel.getAuthState().observe(getViewLifecycleOwner(), authState -> {
            switch (authState) {
                case AUTHENTICATED:
                    navController.navigate(R.id.nav_dashboard);
                    break;
                case INVALID_AUTHENTICATION:
                   Snackbar.make(getView(), "Erreur authentification", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
