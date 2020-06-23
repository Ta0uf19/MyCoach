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

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.mycoach.R;
import io.mycoach.model.Workout;
import io.mycoach.repository.AuthRepository;
import io.mycoach.repository.WorkoutRepository;
import io.mycoach.service.Service;
import io.mycoach.service.WorkoutResponse;
import io.mycoach.ui.auth.LoginViewModel;
import io.mycoach.utils.MenuUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";

    private FirebaseAuth auth;
    private LoginViewModel viewModel;
    private NavController navController;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;

    private Service workoutService;

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

    public void initService() {
        workoutService = new Service();
    }

    public void initViews(View view) {

        DatePickerTimeline datePickerTimeline = view.findViewById(R.id.datePickerTimeline);
        progressBar = view.findViewById(R.id.loading_dashboard);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView = view.findViewById(R.id.workout_recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        List<Workout> workout = WorkoutRepository.fixtures();

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

                String date = year + "-" + month + "-" + day;
                Log.d(TAG, date);

                showProgress();
                workoutService
                        .getWorkoutsByDate(AuthRepository.getAuth().getCurrentUser().getEmail(), date)
                        .enqueue(new Callback<WorkoutResponse>() {
                            @Override
                            public void onResponse(Call<WorkoutResponse> call, Response<WorkoutResponse> response) {
                                if (response.isSuccessful()) {
                                    WorkoutResponse resp = response.body();

                                    List<Workout> workouts = resp.getWorkouts();
                                    Log.d(TAG, workouts.toString());

                                    WorkoutAdapter mAdapter = new WorkoutAdapter(workouts);
                                    recyclerView.setAdapter(mAdapter);

                                    hideProgress();


                                    //Log.d("Workout response : ", resp.getWorkouts().toString());

                                } else {
                                    System.out.println(response.errorBody());
                                }
                            }

                            @Override
                            public void onFailure(Call<WorkoutResponse> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                ;
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

        initService();
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

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
