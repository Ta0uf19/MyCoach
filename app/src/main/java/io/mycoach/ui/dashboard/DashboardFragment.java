package io.mycoach.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.mycoach.R;
import io.mycoach.model.Workout;
import io.mycoach.ui.auth.LoginViewModel;
import io.mycoach.utils.MenuUtils;

public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";

    private FirebaseAuth auth;
    private LoginViewModel viewModel;
    private NavController navController;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;

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


    public void initViews(View view) {

        DatePickerTimeline datePickerTimeline = view.findViewById(R.id.datePickerTimeline);
        progressBar = view.findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView = view.findViewById(R.id.workout_recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        List<Workout> workout = new ArrayList<Workout>() {{
            add(new Workout("Biceps - EZ bar Cur", "https://i.snipboard.io/P9umXU.jpg", 1,2, 10));
            add(new Workout("Biceps - EZ bar Cur", "https://i.snipboard.io/0lHijk.jpg", 1,2, 10));
            add(new Workout("Biceps - EZ bar Cur", "https://i.snipboard.io/P9umXU.jpg", 1,2, 10));
            add(new Workout("PULL-UP", "https://i.snipboard.io/0lHijk.jpg", 1,2, 10));
            add(new Workout("Biceps - EZ bar Cur", "https://i.snipboard.io/0lHijk.jpg", 1,2, 10));
            add(new Workout("Biceps - EZ bar Cur", "https://i.snipboard.io/P9umXU.jpg", 1,2, 10));
            add(new Workout("Biceps - EZ bar Cur", "https://i.snipboard.io/P9umXU.jpg", 1,2, 10));
        }};

        //static
        WorkoutAdapter mAdapter = new WorkoutAdapter(workout);
        recyclerView.setAdapter(mAdapter);

        // calendar setup
        Calendar calendar = Calendar.getInstance();
        datePickerTimeline.setActiveDate(calendar);

        // Set a Start date
        datePickerTimeline.setInitialDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Set a date Selected Listener
        datePickerTimeline.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                Log.d(TAG, year  + " " + month  + " " +  day);
            }

            @Override
            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {
                // Do Something
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

//        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
//
//        viewModel.getAuthState().observe(getViewLifecycleOwner(),
//                new Observer<LoginViewModel.AuthState>() {
//                    @Override
//                    public void onChanged(LoginViewModel.AuthState authState) {
//                        switch (authState) {
//                            case AUTHENTICATED:
//                                //showWelcomeMessage();
//                                break;
//                            case UNAUTHENTICATED:
//                                navController.navigate(R.id.entryFragment);
//                                break;
//                        }
//                    }
//        });

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            navController.navigate(R.id.entryFragment);
        }


    }
}
