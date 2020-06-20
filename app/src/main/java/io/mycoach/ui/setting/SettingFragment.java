package io.mycoach.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseUser;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.mycoach.R;
import io.mycoach.repository.AuthRepository;
import io.mycoach.ui.auth.LoginViewModel;

public class SettingFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    private LoginViewModel viewModel;

    @BindView(R.id.btn_logout)
    CircularProgressButton logout;

    @BindView(R.id.profil_name)
    TextView name;

    @BindView(R.id.profil_email)
    TextView email;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);

        // configure navController
        navController = NavHostFragment.findNavController(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);


        FirebaseUser user = AuthRepository.getAuth().getCurrentUser();

        name.setText(user.getDisplayName());
        email.setText(user.getEmail());

        logout.setOnClickListener(v -> {
            logout.startAnimation();
            AuthRepository.logout();
            Toast.makeText(getContext(), "Déconnexion réussi", Toast.LENGTH_SHORT);

            navController.navigate(R.id.entryFragment);
        });
    }
}
