package io.mycoach.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import io.mycoach.R;
import io.mycoach.ui.auth.LoginViewModel;
import io.mycoach.utils.MenuUtils;

public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";

    private LoginViewModel viewModel;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // configure nav controller
        navController = NavHostFragment.findNavController(this);

        // show menu
        MenuUtils.showNavigationMenu(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        viewModel.getAuthState().observe(getViewLifecycleOwner(),
                new Observer<LoginViewModel.AuthState>() {
                    @Override
                    public void onChanged(LoginViewModel.AuthState authState) {
                        switch (authState) {
                            case AUTHENTICATED:
                                //showWelcomeMessage();
                                break;
                            case UNAUTHENTICATED:
                                navController.navigate(R.id.entryFragment);
                                break;
                        }
                    }
                });
    }
}
