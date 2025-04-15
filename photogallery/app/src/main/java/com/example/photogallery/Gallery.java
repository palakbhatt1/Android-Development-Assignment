package com.example.photogallery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class Gallery extends AppCompatActivity {

    // GridView to display the image thumbnails
    private GridView gridView;

    // Adapter that helps display images inside the GridView
    private ImageAdapter imageAdapter;

    // Array to hold the image files loaded from storage
    private File[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery); // Load the layout for the gallery screen

        // Link the GridView UI element with its ID
        gridView = findViewById(R.id.galleryGrid);

        // Load images from the "MyImages" folder
        loadImagesFromFolder();

        // When a user taps on an image, open the image details screen
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected image file
            File imageFile = images[position];

            // Create an Intent to move to ImageDetails activity
            Intent intent = new Intent(Gallery.this, ImageDetails.class);

            // Pass the path of the selected image to the next activity
            intent.putExtra("imagePath", imageFile.getAbsolutePath());

            // Start the ImageDetails activity
            startActivity(intent);
        });
    }

    // Method that loads images from the app's "MyImages" folder
    private void loadImagesFromFolder() {
        // Define the folder location inside app-specific external storage
        File folder = new File(getExternalFilesDir(null), "MyImages");

        // Check if the folder exists and is a directory
        if (folder.exists() && folder.isDirectory()) {
            // Get all files in the folder that are JPG or PNG
            images = folder.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));

            // If any images are found, display them in the GridView using the adapter
            if (images != null && images.length > 0) {
                imageAdapter = new ImageAdapter(this, images);
                gridView.setAdapter(imageAdapter);
            } else {
                // No images found in the folder
                Toast.makeText(this, "No images found", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Folder doesn't exist or isn't accessible
            Toast.makeText(this, "Folder not found", Toast.LENGTH_SHORT).show();
        }
    }
}
