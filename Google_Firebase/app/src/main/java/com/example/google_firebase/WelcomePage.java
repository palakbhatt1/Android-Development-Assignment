package com.example.google_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomePage extends AppCompatActivity {
   ImageButton logout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_page);
        logout = findViewById(R.id.logout);
        TextView nametxtview = findViewById(R.id.userName);

        String username = getIntent().getStringExtra("name");
        nametxtview.setText(username);
         GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build());
         logout.setOnClickListener(v -> {
             FirebaseAuth.getInstance().signOut();
             Task<Void> voidTask = mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
                 startActivity(new Intent(WelcomePage.this, MainActivity.class));
                 finish();
             });
         });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.welcomepage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}