package io.mycoach.ui.dashboard;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mycoach.R;
import io.mycoach.model.Workout;
import io.mycoach.ui.widget.WorkoutTimerActivity;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    private static final String TAG = "WorkoutAdapter";

    private List<Workout> workoutList;

    public WorkoutAdapter(List<Workout> workout) {
        this.workoutList = workout;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.workout_avatar)
        CircularImageView picture;

        @BindView(R.id.workout_name)
        TextView name;

        @BindView(R.id.workout_repeats)
        TextView repeats;

        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }


    @NotNull
    @Override
    public WorkoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_cell_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Workout workout = workoutList.get(position);
        holder.name.setText(workout.getName());
        holder.repeats.setText(workout.getSeries() + " SER | "+ workout.getRepeats() +  " REP");
        Picasso.get().load(workout.getPicture()).into(holder.picture);

        holder.itemView.setOnClickListener(view -> {
            Log.d(TAG, workout.toString());
            Intent intent = new Intent(view.getContext(), WorkoutTimerActivity.class);
            intent.putExtra("Workout", workout);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }
}
