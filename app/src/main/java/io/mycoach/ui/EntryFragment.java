package io.mycoach.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.mycoach.R;
import io.mycoach.repository.AuthRepository;
import io.mycoach.utils.MenuUtils;

public class EntryFragment extends Fragment {
    private static final String TAG = "EntryFragment";

    @BindView(R.id.btn_login) Button login;
    @BindView(R.id.btn_register) Button register;
    @BindView(R.id.signInGoogle) Button signInGoogle;

    private GoogleSignInClient googleSignInClient;
    private static int RC_SIGN_IN = 007;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry, container, false);
        ButterKnife.bind(this, view);

        // configure navController
        navController = NavHostFragment.findNavController(this);

        // hide navigation menu
        MenuUtils.hideNavigationMenu(getActivity());

        // actions
        login.setOnClickListener(v -> navController.navigate(R.id.action_entryFragment_to_loginFragment));
        register.setOnClickListener(v -> navController.navigate(R.id.action_entryFragment_to_registerFragment));

        signInGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });


        initGoogleSignInClient();

        return view;
    }

    private void initGoogleSignInClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        this.googleSignInClient = GoogleSignIn.getClient(this.getContext(), gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) {
                    signInWithGoogleAuthCredential(googleSignInAccount);
                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void signInWithGoogleAuthCredential(GoogleSignInAccount googleSignInAccount) {

        String googleTokenId = googleSignInAccount.getIdToken();
        AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null);

        AuthRepository.signInWithGoogle(googleAuthCredential).observe(this, status -> {
            if(status)
                navController.navigate(R.id.action_entryFragment_to_loginFragment);
        });

    }

//    public void hideNavigationMenu() {
//        // Hide menu
//        BottomNavigationViewEx bottomNavigationViewEx = getActivity().findViewById(R.id.bottom_nav);
//        bottomNavigationViewEx.setVisibility(View.GONE);
//
//        //Hide Fab
//        FloatingActionButton fab = getActivity().findViewById(R.id.fab_chat);
//        fab.hide();
//    }
}
