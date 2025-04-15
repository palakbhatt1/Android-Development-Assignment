package com.example.photogallery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class Gallery extends AppCompatActivity {

    private GridView gridView;
    private ImageAdapter imageAdapter;
    private File[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        gridView = findViewById(R.id.galleryGrid);
        loadImagesFromFolder();

        // Set item click listener to open the image in a new activity
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            File imageFile = images[position];
            Intent intent = new Intent(Gallery.this, ImageDetails.class);
            intent.putExtra("imagePath", imageFile.getAbsolutePath());
            startActivity(intent);
        });
    }

    // Method to load images from the custom folder
    private void loadImagesFromFolder() {
        // Specify the path to the custom folder where images are saved
        File folder = new File(getExternalFilesDir(null), "MyImages");

        // Check if the folder exists and is a directory
        if (folder.exists() && folder.isDirectory()) {
            // Filter and list all jpg and png files from the folder
            images = folder.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));

            // If images are found, set the adapter
            if (images != null && images.length > 0) {
                imageAdapter = new ImageAdapter(this, images);
                gridView.setAdapter(imageAdapter);
            } else {
                // Show a message if no images are found
                Toast.makeText(this, "No images found", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Show a message if the folder doesn't exist
            Toast.makeText(this, "Folder not found", Toast.LENGTH_SHORT).show();
        }
    }
}
