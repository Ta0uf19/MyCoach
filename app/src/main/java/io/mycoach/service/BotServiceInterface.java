package io.mycoach.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface BotServiceInterface {

    @POST("/dialogflowGateway")
    Call<BotResponse> sendMessage(@Body BotRequest body);
}
