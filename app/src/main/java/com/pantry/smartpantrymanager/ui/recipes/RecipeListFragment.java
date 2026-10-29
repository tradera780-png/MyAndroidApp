package com.pantry.smartpantrymanager.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pantry.smartpantrymanager.R;
import com.pantry.smartpantrymanager.data.DatabaseHelper;
import com.pantry.smartpantrymanager.model.Recipe;
import com.pantry.smartpantrymanager.ui.adapters.RecipeAdapter;

import java.util.List;

public class RecipeListFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener {

    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private RecipeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewRecipes);
        databaseHelper = new DatabaseHelper(requireContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadAllRecipes();

        return view;
    }

    private void loadAllRecipes() {
        List<Recipe> allRecipes = databaseHelper.getAllRecipes();
        adapter = new RecipeAdapter(allRecipes, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(requireContext(), RecipeDetailActivity.class);
        intent.putExtra("recipe_name", recipe.getName());
        intent.putExtra("recipe_time", recipe.getPrepTime());
        intent.putExtra("recipe_steps", recipe.getPrepSteps());
        startActivity(intent);
    }
}
