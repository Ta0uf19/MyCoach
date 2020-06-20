package io.mycoach.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.mycoach.R;
import io.mycoach.databinding.FragmentRegisterBinding;
import io.mycoach.model.User;

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";

    private RegisterViewModel registerViewModel;
    private NavController navController;
    private FragmentRegisterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        View view = binding.getRoot();

        // init ViewModel
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setViewModel(registerViewModel);

        // configure nav controller
        navController = NavHostFragment.findNavController(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CircularProgressButton btnRegister = binding.btnRegister;
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRegister.startAnimation();
                registerViewModel.onLoginClicked();
            }
        });

        registerViewModel.getRegisterState().observe(getViewLifecycleOwner(), state -> {
            switch (state) {
                case FAILED:
                    Toast.makeText(this.getContext(), "Erreur inattendu! Réessayer plus tard", Toast.LENGTH_LONG).show();
                    break;
                case ERROR_TYPING:
                    Toast.makeText(this.getContext(), "Adresse éléctronique ou mot de passe invalide (doit comporter au moins 5 caractères)", Toast.LENGTH_LONG).show();
                    break;
                case REGISTERED_SUCCESS:
                    Toast.makeText(this.getContext(), "Votre compte a été crée avec succès.", Toast.LENGTH_LONG).show();
                    navController.navigate(R.id.nav_chatbot);

                    break;
                case ALREADY_EXISTS:
                    Toast.makeText(this.getContext(), "L'utilisateur existe déjà.", Toast.LENGTH_LONG).show();
                    break;
            }
            btnRegister.revertAnimation();
        });
    }
}
