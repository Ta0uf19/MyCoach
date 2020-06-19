package io.mycoach.ui.widget;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.krtkush.lineartimer.LinearTimer;
import io.github.krtkush.lineartimer.LinearTimerStates;
import io.github.krtkush.lineartimer.LinearTimerView;
import io.mycoach.R;
import io.mycoach.model.Workout;

public class WorkoutTimerActivity extends AppCompatActivity implements LinearTimer.TimerListener {

    private static final String TAG = "WorkoutTimerActivity";


    @BindView(R.id.workout_timer_title)
    TextView title;

    @BindView(R.id.controlTimer)
    ImageButton controlTimer;

    private LinearTimer linearTimer;
    private TextView time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        ButterKnife.bind(this);

        // search data
        Workout workout = (Workout) getIntent().getSerializableExtra("Workout");
        title.setText(workout.getName());


        LinearTimerView linearTimerView = findViewById(R.id.linearTimer);
        time = findViewById(R.id.time);

        long duration = 10 * 1000;


        this.linearTimer = new LinearTimer.Builder()
                .linearTimerView(linearTimerView)
                .duration(duration)
                .timerListener(this)
                .getCountUpdate(LinearTimer.COUNT_DOWN_TIMER, 1000)
                .build();



        // resume and pause the timer
        controlTimer.setOnClickListener(view -> {
            LinearTimerStates state = linearTimer.getState();
            switch (state) {
                case INITIALIZED:
                    Log.d(TAG, "start the timer");
                    linearTimer.startTimer();
                    controlTimer.setImageResource(R.drawable.pause);
                    break;
                case PAUSED:
                    controlTimer.setImageResource(R.drawable.pause);
                    linearTimer.resumeTimer();
                    break;
                case ACTIVE:
                    controlTimer.setImageResource(R.drawable.play);
                    linearTimer.pauseTimer();
            }
        });


        // Reset the timer
        findViewById(R.id.stopTimer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    linearTimer.resetTimer();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    Toast.makeText(WorkoutTimerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void animationComplete() {
        Log.i(TAG, "complete");
    }

    @Override
    public void timerTick(long tickUpdateInMillis) {
        Log.i("Time left", String.valueOf(tickUpdateInMillis));

        String formattedTime = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(tickUpdateInMillis),
                TimeUnit.MILLISECONDS.toSeconds(tickUpdateInMillis)
                        - TimeUnit.MINUTES
                        .toSeconds(TimeUnit.MILLISECONDS.toHours(tickUpdateInMillis)));

        time.setText(formattedTime);
    }

    @Override
    public void onTimerReset() {
        time.setText("");
    }
}
