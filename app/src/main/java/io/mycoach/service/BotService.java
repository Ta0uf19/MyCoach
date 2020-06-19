package io.mycoach.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BotService {

    private Retrofit retrofit;
    private BotServiceInterface api;

    private static final String BASE_URL = "https://us-central1-mycoach-77f86.cloudfunctions.net/";


    public BotService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.api = retrofit.create(BotServiceInterface.class);
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

}
