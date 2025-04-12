package com.example.a01_unitconverter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerFrom, spinnerTo;
    private EditText inputValue;
    private TextView outputValue;
    private ImageButton btnSwap;

    private final DecimalFormat df = new DecimalFormat("#.####");

    private final double feet_to_meter = 0.3048;
    private final double inch_to_meter = 0.0254;
    private final double cm_to_meter = 0.01;
    private final double yards_to_meter = 0.9144;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        inputValue = findViewById(R.id.inputValue);
        outputValue = findViewById(R.id.outputValue);
        btnSwap = findViewById(R.id.btnSwap);
        ImageButton settingsButton = findViewById(R.id.settings_button);

        // Theme Popup Menu
        settingsButton.setOnClickListener(v -> {
            androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(MainActivity.this, settingsButton);
            popupMenu.getMenuInflater().inflate(R.menu.theme_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.light_theme) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveThemePref(AppCompatDelegate.MODE_NIGHT_NO);
                    return true;
                } else if (id == R.id.dark_theme) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveThemePref(AppCompatDelegate.MODE_NIGHT_YES);
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });

        // Lottie Animation
        LottieAnimationView lottieView = findViewById(R.id.lottieView);
        lottieView.setRepeatCount(LottieDrawable.INFINITE);
        lottieView.playAnimation();

        // Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.length_units,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        spinnerFrom.setSelection(0);
        spinnerTo.setSelection(1);

        inputValue.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void afterTextChanged(android.text.Editable s) {
                convertUnits();
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        AdapterView.OnItemSelectedListener selectionListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convertUnits();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        spinnerFrom.setOnItemSelectedListener(selectionListener);
        spinnerTo.setOnItemSelectedListener(selectionListener);

        btnSwap.setOnClickListener(v -> {
            int fromPos = spinnerFrom.getSelectedItemPosition();
            int toPos = spinnerTo.getSelectedItemPosition();
            spinnerFrom.setSelection(toPos);
            spinnerTo.setSelection(fromPos);
        });

        loadThemePref();  // Optional: Apply saved theme on startup
    }

    private void saveThemePref(int mode) {
        SharedPreferences prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("theme_mode", mode);
        editor.apply();
    }

    private void loadThemePref() {
        SharedPreferences prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE);
        int mode = prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    private void convertUnits() {
        String input = inputValue.getText().toString();
        if (TextUtils.isEmpty(input)) {
            outputValue.setText("0");
            return;
        }

        try {
            double value = Double.parseDouble(input);
            String fromUnit = spinnerFrom.getSelectedItem().toString();
            String toUnit = spinnerTo.getSelectedItem().toString();

            if (fromUnit.equals(toUnit)) {
                Toast.makeText(this, "Same units selected. No conversion needed.", Toast.LENGTH_SHORT).show();
                outputValue.setText(df.format(value));
                return;
            }

            double result = convert(value, fromUnit, toUnit);
            outputValue.setText(df.format(result));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            outputValue.setText("Invalid input");
        }
    }

    private double convert(double value, String fromUnit, String toUnit) {
        double valueInMeters = switch (fromUnit) {
            case "Feet (ft)" -> value * feet_to_meter;
            case "Inches (in)" -> value * inch_to_meter;
            case "Centimeters (cm)" -> value * cm_to_meter;
            case "Meters (m)" -> value;
            case "Yards (yd)" -> value * yards_to_meter;
            default -> 0;
        };

        return switch (toUnit) {
            case "Feet (ft)" -> valueInMeters / feet_to_meter;
            case "Inches (in)" -> valueInMeters / inch_to_meter;
            case "Centimeters (cm)" -> valueInMeters / cm_to_meter;
            case "Meters (m)" -> valueInMeters;
            case "Yards (yd)" -> valueInMeters / yards_to_meter;
            default -> 0;
        };
    }
}
