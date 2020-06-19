package io.mycoach.ui.auth;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.mycoach.model.User;

public class RegisterViewModel extends ViewModel {
    private static final String TAG = "RegisterViewModel";

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef =  db.collection("users");
    LiveData<User> createdUserLiveData;


    public RegisterViewModel() {}


    /**
     * Créer un utilisateur
     *
     * @param user aze
     * @return Le nouveau utilisateur crée
     */
    MutableLiveData<User> createUser(User user) {

        MutableLiveData<User> newUserMutableLiveData = new MutableLiveData<>();
        DocumentReference uidRef = usersRef.document(user.getId());
        uidRef.get().addOnCompleteListener(uidTask -> {
            if (uidTask.isSuccessful()) {
                DocumentSnapshot document = uidTask.getResult();
                if (!document.exists()) {
                    uidRef.set(user).addOnCompleteListener(userCreationTask -> {
                        if (userCreationTask.isSuccessful()) {
                            user.setCreated(true);
                            newUserMutableLiveData.setValue(user);
                        } else {
                            Log.e(TAG, userCreationTask.getException().getMessage());
                        }
                    });
                } else {
                    newUserMutableLiveData.setValue(user);
                }
            } else {
                Log.e(TAG, uidTask.getException().getMessage());
            }
        });

        return newUserMutableLiveData;
    }
}
