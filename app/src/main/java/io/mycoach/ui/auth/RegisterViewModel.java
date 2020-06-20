package io.mycoach.ui.auth;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import io.mycoach.model.User;
import io.mycoach.repository.AuthRepository;

public class RegisterViewModel extends ViewModel {
    private static final String TAG = "RegisterViewModel";

    public enum RegisterState {
        REGISTERED_SUCCESS,
        ERROR_TYPING,
        FAILED,
        ALREADY_EXISTS
    }
    private MutableLiveData<RegisterState> registerState = new MutableLiveData<>();
    private Observer observer;
    public User user;

    public RegisterViewModel() {
        this.user = new User();
    }

    public LiveData<RegisterState> getRegisterState() {
        return registerState;
    }

    public void onLoginClicked() {
        if (isInputDataValid()) {
            Log.d(TAG, "input is valid user:  " + user);
            createUser(user);
        }
        else {
            Log.d(TAG, "input is invalid user:  " + user);
            this.registerState.setValue(RegisterState.ERROR_TYPING);
        }
    }

    /**
     * Is valid ?
     * Refactor to model?
     * @return
     */
    public boolean isInputDataValid() {
        return !TextUtils.isEmpty(user.getEmail())
                && Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()
                && user.getPassword().length() > 6;
    }

    /**
     * Create a user state
     * @param user
     */
    private void createUser(User user) {

        observer = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean status) {
                    if(status)
                        registerState.setValue(RegisterState.REGISTERED_SUCCESS);
//                    else
//                        registerState.setValue(RegisterState.ALREADY_EXISTS);
            }
        };

        AuthRepository.create(user).observeForever(observer);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        AuthRepository.create(user).removeObserver(observer);
    }
}
