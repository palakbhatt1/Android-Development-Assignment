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

    private TextView imageName, imageSize, imagePath, dateTaken;
    private Button deleteButton;
    private ImageView imageView; // For the image display
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_details);

        imageName = findViewById(R.id.imageName);
        imageSize = findViewById(R.id.imageSize);
        imagePath = findViewById(R.id.imagePath);
        dateTaken = findViewById(R.id.imageDate);
        deleteButton = findViewById(R.id.deleteButton);
        imageView = findViewById(R.id.imageView);

        String path = getIntent().getStringExtra("imagePath");
        imageFile = new File(path);

        // Set image details
        imageName.setText(imageFile.getName());
        imagePath.setText(imageFile.getAbsolutePath());
        imageSize.setText(String.valueOf(imageFile.length()));

        // Load the image into the ImageView
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

        deleteButton.setOnClickListener(view -> {
            new android.app.AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to delete this image?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteImage())
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void deleteImage() {
        if (imageFile.delete()) {
            Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete image", Toast.LENGTH_SHORT).show();
        }
    }
}
