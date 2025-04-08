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
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;


import androidx.appcompat.app.AppCompatActivity;

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

        LottieAnimationView lottieView = findViewById(R.id.lottieView);
        lottieView.setRepeatCount(LottieDrawable.INFINITE);
        lottieView.playAnimation();

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

        // Text change listener to auto convert
        inputValue.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void afterTextChanged(android.text.Editable s) {
                convertUnits();
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // Spinner selection listeners
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

        // Swap button logic
        btnSwap.setOnClickListener(v -> {
            int fromPos = spinnerFrom.getSelectedItemPosition();
            int toPos = spinnerTo.getSelectedItemPosition();
            spinnerFrom.setSelection(toPos);
            spinnerTo.setSelection(fromPos);
        });
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

            double result = convert(value, fromUnit, toUnit);
            outputValue.setText(df.format(result));
        } catch (NumberFormatException e) {
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
