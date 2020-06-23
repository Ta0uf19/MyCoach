package io.mycoach.service;

public class WorkoutRequest {

    final String session; // email adresse of user
    final String date;

    public WorkoutRequest(String session, String date) {
        this.session = session;
        this.date = date;
    }
}
