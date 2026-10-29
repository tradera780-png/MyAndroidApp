package com.pantry.smartpantrymanager.ui.recipes;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pantry.smartpantrymanager.R;
import com.pantry.smartpantrymanager.data.DatabaseHelper;
import com.pantry.smartpantrymanager.model.Recipe;
import com.pantry.smartpantrymanager.model.RecipeIngredient;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textViewName, textViewTime, textViewIngredients, textViewSteps;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Recipe Details");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        textViewName = findViewById(R.id.textViewDetailName);
        textViewTime = findViewById(R.id.textViewDetailTime);
        textViewIngredients = findViewById(R.id.textViewDetailIngredients);
        textViewSteps = findViewById(R.id.textViewDetailSteps);

        databaseHelper = new DatabaseHelper(this);

        String name = getIntent().getStringExtra("recipe_name");
        String time = getIntent().getStringExtra("recipe_time");
        String steps = getIntent().getStringExtra("recipe_steps");

        textViewName.setText(name);
        textViewTime.setText("Prep Time: " + time);
        textViewSteps.setText(steps);

        // Fetch ingredients for this recipe from DB
        List<Recipe> allRecipes = databaseHelper.getAllRecipes();
        for (Recipe r : allRecipes) {
            if (r.getName().equals(name)) {
                StringBuilder sb = new StringBuilder();
                for (RecipeIngredient ri : r.getIngredients()) {
                    sb.append("- ").append(ri.getQuantity()).append(" ").append(ri.getUnit()).append(" ").append(ri.getIngredientName()).append("\n");
                }
                textViewIngredients.setText(sb.toString().trim());
                break;
            }
        }
    }
}
