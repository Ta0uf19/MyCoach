package io.mycoach.ui.auth;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import io.mycoach.repository.AuthRepository;

public class LoginViewModel extends ViewModel {

    public enum AuthState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,          // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }
    private MutableLiveData<AuthState> authState = new MutableLiveData<>();
    private Observer observer;
    String email;

    public LoginViewModel() {
        authState.setValue(AuthState.UNAUTHENTICATED);
        email = "";
    }

    public void authenticate(String email, String password) {

        this.observer = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean status) {
                if(status) authState.setValue(AuthState.AUTHENTICATED);
                else authState.setValue(AuthState.INVALID_AUTHENTICATION);
            }
        };
        AuthRepository.signIn(email, password).observeForever(observer);

    }

    public MutableLiveData<AuthState> getAuthState() {
        return authState;
    }
}