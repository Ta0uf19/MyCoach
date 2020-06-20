/**
 * Taoufik Tribki
 * @Ta0uf19
 *
 * Licence MIT
 */
package io.mycoach.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import io.mycoach.model.User;

public class AuthRepository {

    private static final String TAG = "AuthRepository";
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference usersRef =  db.collection("users");

    /**
     * Find a user by email
     * @param email
     */
    public static void find(String email) {

        DocumentReference uidRef = usersRef.document(email);
        uidRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if(document.exists()) {
                    document.getData();
                    Log.d(TAG, "User :  " + document.toObject(User.class));
                }
                else {
                    Log.d(TAG, "no such user");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    /**
     * Sign in user
     * @param email
     * @param password
     * @return
     */
    public static LiveData<Boolean> signIn(String email, String password) {

        MutableLiveData<Boolean> status = new MutableLiveData<>();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    status.setValue(true);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    status.setValue(false);
                }
            }
        });

        return status;
    }


    /**
     * Create a user
     * @param user
     * @return boolean to indicate if user is created or not
     */
    public static LiveData<Boolean> save(User user) {

        MutableLiveData<Boolean> status = new MutableLiveData<>();

        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    user.setId(auth.getCurrentUser().getUid());

                    // save user in firestore database
                    saveInFireStore(user);

                    status.setValue(true);
                }
                else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());

                    status.setValue(false);
                }
            }
        });

        return status;
    }


    /**
     * Se connecter avec Google
     *
     * @param googleAuthCredential
     * @return
     */
    public static LiveData<Boolean> signInWithGoogle(AuthCredential googleAuthCredential) {
        MutableLiveData<Boolean> status = new MutableLiveData<>();

        auth.signInWithCredential(googleAuthCredential).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                boolean isNewUser = authTask.getResult().getAdditionalUserInfo().isNewUser();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser != null) {
                    User user = new User();
                    user.setId(firebaseUser.getUid());
                    user.setEmail(firebaseUser.getEmail());
                    user.setName(firebaseUser.getDisplayName());
                    user.setAvatar(firebaseUser.getPhotoUrl().toString());

                    if(isNewUser)
                        // save user in firestore database
                        saveInFireStore(user);

                    status.setValue(true);
                }
            } else {
                status.setValue(false);
                Log.w(TAG, "signInWithGoogle:failure", authTask.getException());
            }
        });
        return status;
    }


    /**
     * Signout a user
     * @param user
     */
    private static void signOut(User user) {
        auth.signOut();
    }

    private static void saveInFireStore(User user) {

        DocumentReference uidRef = usersRef.document(user.getEmail());
        uidRef.get().addOnCompleteListener(uidTask -> {
            if (uidTask.isSuccessful()) {
                DocumentSnapshot document = uidTask.getResult();

                // if user doesn't exist so we create it
                if (!document.exists()) {
                    uidRef.set(user).addOnCompleteListener(userCreationTask -> {
                        if (userCreationTask.isSuccessful()) {
                            Log.e(TAG, "createdUserInFireStore:ok");
                        } else {
                            Log.e(TAG, userCreationTask.getException().getMessage());
                        }
                    });
                } else {
                    Log.e(TAG, "user:already_exists");
                }
            } else {
                Log.e(TAG, uidTask.getException().getMessage());
            }
        });
    }
}
