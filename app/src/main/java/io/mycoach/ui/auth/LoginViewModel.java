package io.mycoach.ui.auth;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    public enum AuthState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,          // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }
    private MutableLiveData<AuthState> authState = new MutableLiveData<>();

    String username;

    public LoginViewModel() {
        authState.setValue(AuthState.UNAUTHENTICATED);
        username = "";
    }

    public void authenticate(String username, String password) {
        if (passwordIsValidForUsername(username, password)) {
            this.username = username;
            authState.setValue(AuthState.AUTHENTICATED);
        } else {
            authState.setValue(AuthState.INVALID_AUTHENTICATION);
        }
    }

    public void refuseAuthentication() {
        authState.setValue(AuthState.UNAUTHENTICATED);
    }


    private boolean passwordIsValidForUsername(String username, String password) {
        return true;
    }

    public MutableLiveData<AuthState> getAuthState() {
        return authState;
    }
}