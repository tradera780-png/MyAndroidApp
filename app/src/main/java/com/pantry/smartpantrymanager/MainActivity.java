package com.pantry.smartpantrymanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pantry.smartpantrymanager.ui.pantry.PantryFragment;
import com.pantry.smartpantrymanager.ui.recipes.RecipeListFragment;
import com.pantry.smartpantrymanager.ui.recipes.SuggestedRecipesFragment;
import com.pantry.smartpantrymanager.ui.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new PantryFragment())
                    .commit();
        }

        navView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_pantry) {
                selectedFragment = new PantryFragment();
            } else if (itemId == R.id.navigation_suggested) {
                selectedFragment = new SuggestedRecipesFragment();
            } else if (itemId == R.id.navigation_recipes) {
                selectedFragment = new RecipeListFragment();
            } else if (itemId == R.id.navigation_settings) {
                selectedFragment = new SettingsFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });
    }
}
