package com.example.photogallery;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private CardView takePhotoLayout, viewGalleryLayout, manageFolderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePhotoLayout = findViewById(R.id.takePhotoLayout);
        viewGalleryLayout = findViewById(R.id.viewGalleryLayout);

        takePhotoLayout.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        });

        viewGalleryLayout.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Gallery.class);
            startActivity(intent);
        });

    }
}
