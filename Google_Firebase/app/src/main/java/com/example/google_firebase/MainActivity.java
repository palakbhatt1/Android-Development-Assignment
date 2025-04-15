package com.example.google_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    ImageButton signInButton; // Google Sign-In button
    private static final int RC_SIGN_IN = 200; // Request code for sign-in result
    private GoogleSignInClient mGoogleSignInClient; // Google Sign-In client
    private FirebaseAuth mAuth; // Firebase authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Makes layout edge-to-edge for modern design
        setContentView(R.layout.activity_main);

        // Link UI elements
        signInButton = findViewById(R.id.btnGoogleSignIn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Get token for Firebase Auth
                .requestEmail() // Request user's email
                .build();

        // Build Google Sign-In client with the options
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Handle button click to start Google Sign-In Intent
        signInButton.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        // Ensure layout handles system bars correctly
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Callback for result of Google Sign-In activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // Get the Google Sign-In task result
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // If sign-in successful, get the account and authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Handle sign-in failure
                Log.e("Google SignIn", "Failed", e);
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Authenticate with Firebase using the Google account
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // Get the credentials from the Google account
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        // Sign in to Firebase with those credentials
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // If successful, launch WelcomePage and pass the user's name
                        String name = acct.getDisplayName();
                        Intent intent = new Intent(MainActivity.this, WelcomePage.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                        finish(); // Finish current activity
                    } else {
                        // If sign-in fails, show error
                        Toast.makeText(MainActivity.this, "Firebase Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
