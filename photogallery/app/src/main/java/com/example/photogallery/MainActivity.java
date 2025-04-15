package com.example.photogallery;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// MainActivity is the entry screen for the PhotoGallery app
public class MainActivity extends AppCompatActivity {

    // Declare the UI components used in this activity
    private CardView takePhotoLayout, viewGalleryLayout, manageFolderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for this activity from activity_main.xml
        setContentView(R.layout.activity_main);

        // Link UI elements (CardViews) to their XML counterparts by ID
        takePhotoLayout = findViewById(R.id.takePhotoLayout);
        viewGalleryLayout = findViewById(R.id.viewGalleryLayout);

        // Set a click listener on the "Take Photo" card
        // When clicked, it opens the CameraActivity screen
        takePhotoLayout.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        });

        // Set a click listener on the "View Gallery" card
        // When clicked, it opens the Gallery screen
        viewGalleryLayout.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Gallery.class);
            startActivity(intent);
        });

        // Note: manageFolderLayout is declared but not used here (maybe for future implementation)
    }
}
