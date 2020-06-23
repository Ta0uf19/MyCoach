package io.mycoach.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import io.mycoach.model.Workout;

public class WorkoutRepository {

    private static final String TAG = "WorkoutRepository";
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference usersRef =  db.collection("users");

    /**
     * Get workouts by a specific date
     * @deprecated using API instead
     * @param date
     * @return
     */
    public static void getWorkoutsByDate(String email, String date) {
        MutableLiveData<List<Workout>> filtred = new MutableLiveData<List<Workout>>();
    }

    /**
     * Test data
     * @return
     */
    public static List<Workout> fixtures() {
        List<Workout> workout = new ArrayList<Workout>() {{
            add(new Workout("Biceps - EZ bar Cur", "https://i.snipboard.io/P9umXU.jpg", "1",2, 10));
            add(new Workout("Biceps - EZ bar Cur", "https://i.snipboard.io/0lHijk.jpg", "1",2, 10));
            add(new Workout("Biceps - EZ bar Cur", "https://i.snipboard.io/P9umXU.jpg", "1",2, 10));
            add(new Workout("PULL-UP", "https://i.snipboard.io/0lHijk.jpg", "1",2, 10));
            add(new Workout("Biceps - EZ bar Cur", "https://i.snipboard.io/0lHijk.jpg", "1",2, 10));
            add(new Workout("Biceps - EZ bar Cur", "https://i.snipboard.io/P9umXU.jpg", "1",2, 10));
            add(new Workout("Biceps - EZ bar Cur", "https://i.snipboard.io/P9umXU.jpg", "1",2, 10));
        }};

        return workout;
    }
}
