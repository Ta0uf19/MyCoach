package io.mycoach.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Service {

    private Retrofit retrofit;
    private ServiceInterface api;

    private static final String BASE_URL = "https://us-central1-mycoach-77f86.cloudfunctions.net/";


    public Service() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.api = retrofit.create(ServiceInterface.class);
    }

    /**
     * Send message to our bot
     * @param sessionId
     * @param message
     * @return
     */
    public Call<BotResponse> sendMessage(String sessionId, String message) {
        BotRequest botRequest = new BotRequest(sessionId, message);
        Call<BotResponse> call = this.api.sendMessage(botRequest);

        return call;
    }


    /**
     * Get workouts for user by a specific date
     * @param email (email)
     * @param date (string YYYY-MM-DD)
     *
     */

    public Call<WorkoutResponse> getWorkoutsByDate(String email, String date) {
        Log.d("Make request", email + " " + date);
        WorkoutRequest workoutRequest = new WorkoutRequest(email, date);
        Call<WorkoutResponse> call = this.api.getWorkoutsByDate(workoutRequest);
        return call;
    }

}
