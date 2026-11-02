package com.pantry.smartpantrymanager.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.pantry.smartpantrymanager.R;
import com.pantry.smartpantrymanager.data.DatabaseHelper;

public class SettingsFragment extends Fragment {

    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        databaseHelper = new DatabaseHelper(requireContext());

        Switch switchExpiryAlerts = view.findViewById(R.id.switchExpiryAlerts);
        Switch switchLowStock = view.findViewById(R.id.switchLowStock);
        Switch switchDarkMode = view.findViewById(R.id.switchDarkMode);
        Button buttonClearPantry = view.findViewById(R.id.buttonClearPantry);
        Button buttonResetDefault = view.findViewById(R.id.buttonResetDefault);
        TextView textViewAbout = view.findViewById(R.id.textViewAbout);

        // Check current night mode state
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        switchDarkMode.setChecked(nightMode == AppCompatDelegate.MODE_NIGHT_YES);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        switchExpiryAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(requireContext(), isChecked ? "Expiry alerts enabled" : "Expiry alerts disabled", Toast.LENGTH_SHORT).show();
        });

        switchLowStock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(requireContext(), isChecked ? "Low stock warnings enabled" : "Low stock warnings disabled", Toast.LENGTH_SHORT).show();
        });

        buttonClearPantry.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Clear Pantry")
                    .setMessage("Are you sure you want to delete all items in your pantry?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        databaseHelper.clearPantry();
                        Toast.makeText(requireContext(), "All pantry items cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        buttonResetDefault.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Reset Data")
                    .setMessage("Restore default pantry ingredients and recipes?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        databaseHelper.resetDatabase();
                        Toast.makeText(requireContext(), "Default pantry & recipes restored", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        textViewAbout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("About Smart Pantry Manager")
                    .setMessage("Smart Pantry Manager v1.0\n\nEasily manage your kitchen inventory, track expiry dates, monitor quantities, and discover matching recipes instantly based on what you have in stock.\n\nDeveloped for kitchen efficiency.")
                    .setPositiveButton("OK", null)
                    .show();
        });

        return view;
    }
}
