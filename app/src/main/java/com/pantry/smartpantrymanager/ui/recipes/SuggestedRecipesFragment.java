package com.pantry.smartpantrymanager.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pantry.smartpantrymanager.R;
import com.pantry.smartpantrymanager.data.DatabaseHelper;
import com.pantry.smartpantrymanager.logic.RecipeMatcher;
import com.pantry.smartpantrymanager.model.PantryItem;
import com.pantry.smartpantrymanager.model.Recipe;
import com.pantry.smartpantrymanager.ui.adapters.RecipeAdapter;

import java.util.List;

public class SuggestedRecipesFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private DatabaseHelper databaseHelper;
    private RecipeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggested_recipes, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSuggested);
        textViewEmpty = view.findViewById(R.id.textViewEmptySuggestions);
        databaseHelper = new DatabaseHelper(requireContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadSuggestedRecipes();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        List<Recipe> allRecipes = databaseHelper.getAllRecipes();
        List<PantryItem> pantryItems = databaseHelper.getAllPantryItems();

        List<Recipe> suggestedRecipes = RecipeMatcher.getSuggestedRecipes(allRecipes, pantryItems);

        if (adapter == null) {
            adapter = new RecipeAdapter(suggestedRecipes, this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(suggestedRecipes);
        }

        if (suggestedRecipes.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(requireContext(), RecipeDetailActivity.class);
        intent.putExtra("recipe_name", recipe.getName());
        intent.putExtra("recipe_time", recipe.getPrepTime());
        intent.putExtra("recipe_steps", recipe.getPrepSteps());
        // Pass ingredients as well or load them in detail activity
        startActivity(intent);
    }
}
