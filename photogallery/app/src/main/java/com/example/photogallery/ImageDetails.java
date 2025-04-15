package com.example.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class ImageDetails extends AppCompatActivity {

    // UI elements for displaying image information
    private TextView imageName, imageSize, imagePath, dateTaken;
    private Button deleteButton;
    private ImageView imageView; // To show the selected image

    // Reference to the image file being displayed
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_details); // Set the layout file

        // Link UI elements with their XML IDs
        imageName = findViewById(R.id.imageName);
        imageSize = findViewById(R.id.imageSize);
        imagePath = findViewById(R.id.imagePath);
        dateTaken = findViewById(R.id.imageDate);
        deleteButton = findViewById(R.id.deleteButton);
        imageView = findViewById(R.id.imageView);

        // Get the image path passed from the previous activity
        String path = getIntent().getStringExtra("imagePath");
        imageFile = new File(path);

        // Show the image name, full path, and file size
        imageName.setText(imageFile.getName());
        imagePath.setText(imageFile.getAbsolutePath());
        imageSize.setText(String.valueOf(imageFile.length()));

        // Decode the image file into a Bitmap and display it in the ImageView
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

        // Set up delete button with confirmation dialog
        deleteButton.setOnClickListener(view -> {
            new android.app.AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to delete this image?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteImage()) // Call method to delete if confirmed
                    .setNegativeButton("No", null) // Do nothing if canceled
                    .show();
        });
    }

    // Method to delete the image file from storage
    private void deleteImage() {
        if (imageFile.delete()) {
            Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity and return to the previous screen
        } else {
            Toast.makeText(this, "Failed to delete image", Toast.LENGTH_SHORT).show();
        }
    }
}
