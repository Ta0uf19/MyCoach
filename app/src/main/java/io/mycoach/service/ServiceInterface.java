package io.mycoach.service;

import io.mycoach.model.Workout;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface ServiceInterface {

    @POST("/dialogflowGateway")
    Call<BotResponse> sendMessage(@Body BotRequest body);

    @POST("/workouts")
    Call<WorkoutResponse> getWorkoutsByDate(@Body WorkoutRequest body);

}
