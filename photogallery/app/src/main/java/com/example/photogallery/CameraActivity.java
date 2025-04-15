package com.example.photogallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;

    private ImageView capturedImageView;
    private ImageButton captureButton, retakeButton, saveButton;
    private Bitmap capturedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_screen);

        capturedImageView = findViewById(R.id.capturedImage);
        captureButton = findViewById(R.id.captureButton);
        retakeButton = findViewById(R.id.retakeButton);
        saveButton = findViewById(R.id.saveButton);

        captureButton.setOnClickListener(v -> openCamera());
        retakeButton.setOnClickListener(v -> openCamera());

        saveButton.setOnClickListener(v -> {
            if (capturedBitmap != null) {
                saveImageToGallery(capturedBitmap);
            } else {
                Toast.makeText(this, "No image to save!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } catch (Exception e) {
                Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            capturedBitmap = (Bitmap) data.getExtras().get("data");
            capturedImageView.setImageBitmap(capturedBitmap);
        }
    }

    private void saveImageToGallery(Bitmap bitmap) {
        try {
            // Create a directory "MyImages" in the app's private storage
            File folder = new File(getExternalFilesDir(null), "MyImages");
            if (!folder.exists()) {
                folder.mkdir();
            }

            // Create a unique file name
            String fileName = "CapturedPhoto_" + System.currentTimeMillis() + ".jpg";
            File imageFile = new File(folder, fileName);

            // Save the image as a JPEG file
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Toast.makeText(this, "Image saved to MyImages folder", Toast.LENGTH_SHORT).show();

            // Optional: Make the image visible in the gallery by scanning it
            MediaScannerConnection.scanFile(this, new String[]{imageFile.getAbsolutePath()}, null, null);

        } catch (Exception e) {
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
