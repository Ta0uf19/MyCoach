package io.mycoach.service;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.mycoach.model.Workout;

public class WorkoutResponse {

    @SerializedName(value = "workouts")
    List<WorkoutDate> workoutWithDates;

    public WorkoutResponse() {
        this.workoutWithDates = new ArrayList<>();
    }

    public List<WorkoutDate> getWorkoutWithDates() {
        return workoutWithDates;
    }

    /**
     * Get list of workout embedded
     * @return
     */
    public List<Workout> getWorkouts() {
       List<Workout> workouts = new ArrayList<Workout>();

        workoutWithDates.forEach(workoutDate -> {
            workouts.add(workoutDate.getWorkout());
        });
        return workouts;
    }

    /**
     * Get available date
     */


    class WorkoutDate {
        private Workout workout;
        private String date;

        public Workout getWorkout() {
            return workout;
        }

        @Override
        public String toString() {
            return "WorkoutDate{" +
                    "workout=" + workout +
                    ", date='" + date + '\'' +
                    '}';
        }
    }
}
