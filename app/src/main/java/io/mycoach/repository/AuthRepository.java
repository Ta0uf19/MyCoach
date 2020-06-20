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
import com.google.common.base.Strings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
    public static LiveData<User> find(String email) {
        MutableLiveData<User> userLive = new MutableLiveData<User>();

        DocumentReference uidRef = usersRef.document(email);
        uidRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if(document.exists()) {
                    document.getData();
                    User user = document.toObject(User.class);
                    userLive.setValue(user);
                    Log.d(TAG, "User :  " + user);
                }
                else {
                    Log.d(TAG, "no such user");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

        return userLive;
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
     * Update a user
     * @param user
     * @return
     */
    public static LiveData<Boolean> update(User user) {
        MutableLiveData<Boolean> status = new MutableLiveData<>(false);

        DocumentReference uidRef = usersRef.document(user.getEmail());
        uidRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if(document.exists()) {
                    document.getData();
                    uidRef.set(user).addOnCompleteListener(userCreationTask -> {
                        if (userCreationTask.isSuccessful()) {
                            Log.e(TAG, "updatedUser:ok");
                            status.setValue(true);
                        } else {
                            Log.e(TAG, userCreationTask.getException().getMessage());
                        }
                    });

                    Log.d(TAG, "update user :  " + document.toObject(User.class));
                }
                else {
                    Log.d(TAG, "no such user to update");
                }
            } else {
                Log.d(TAG, "update failed with ", task.getException());
            }
        });

        return status;
    }

    /**
     * Create a user
     * @param user
     * @return boolean to indicate if user is created or not
     */
    public static LiveData<Boolean> create(User user) {
        MutableLiveData<Boolean> status = new MutableLiveData<>(false);

        if(user == null || Strings.isNullOrEmpty(user.getEmail()) || Strings.isNullOrEmpty(user.getPassword())) {
            return status;
        }

        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    user.setId(auth.getCurrentUser().getUid());
                    user.setNew(true);

                    // update display name for auth user
                    updateAuthDisplayName(user.getName());

                    // save user in firestore database
                    saveInFireStore(user).observeForever( stat -> {
                        if(stat) status.setValue(true);
                    });
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
     * Logout user
     */
    public static void logout() {
        auth.signOut();
    }

    private static LiveData<Boolean> saveInFireStore(User user) {
        MutableLiveData<Boolean> status = new MutableLiveData<>(false);

        DocumentReference uidRef = usersRef.document(user.getEmail());
        uidRef.get().addOnCompleteListener(uidTask -> {
            if (uidTask.isSuccessful()) {
                DocumentSnapshot document = uidTask.getResult();

                // if user doesn't exist so we create it
                if (!document.exists()) {
                    uidRef.set(user).addOnCompleteListener(userCreationTask -> {
                        if (userCreationTask.isSuccessful()) {
                            Log.e(TAG, "createdUserInFireStore:ok");
                            status.setValue(true);
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

        return status;
    }

    private static void updateAuthDisplayName(String name) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();
        FirebaseUser firebaseUser = getAuth().getCurrentUser();
        firebaseUser.updateProfile(profileUpdates);

    }

    public static FirebaseAuth getAuth() {
        return auth;
    }
}
