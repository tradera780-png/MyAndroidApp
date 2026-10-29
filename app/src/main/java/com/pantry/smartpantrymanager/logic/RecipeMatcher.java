package com.pantry.smartpantrymanager.logic;

import com.pantry.smartpantrymanager.model.PantryItem;
import com.pantry.smartpantrymanager.model.Recipe;
import com.pantry.smartpantrymanager.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeMatcher {

    /**
     * Filters the list of recipes returning only those that strictly match the user's pantry.
     * Strict matching rule: EVERY required ingredient must be present in the user's pantry
     * in at least the required quantity, with robust name normalization (singular/plural, case).
     */
    public static List<Recipe> getSuggestedRecipes(List<Recipe> allRecipes, List<PantryItem> pantryItems) {
        List<Recipe> suggestedRecipes = new ArrayList<>();

        if (allRecipes == null || pantryItems == null || pantryItems.isEmpty()) {
            return suggestedRecipes;
        }

        for (Recipe recipe : allRecipes) {
            if (canMakeRecipe(recipe, pantryItems)) {
                suggestedRecipes.add(recipe);
            }
        }

        return suggestedRecipes;
    }

    private static boolean canMakeRecipe(Recipe recipe, List<PantryItem> pantryItems) {
        List<RecipeIngredient> requiredIngredients = recipe.getIngredients();
        if (requiredIngredients == null || requiredIngredients.isEmpty()) {
            return false;
        }

        for (RecipeIngredient req : requiredIngredients) {
            boolean foundAndSufficient = false;

            for (PantryItem pantry : pantryItems) {
                if (isIngredientMatch(req.getIngredientName(), pantry.getName())) {
                    // Check if quantity is sufficient
                    // Note: If units are compatible or comparable (or basic quantity check)
                    if (pantry.getQuantity() >= req.getQuantity()) {
                        foundAndSufficient = true;
                        break;
                    }
                }
            }

            if (!foundAndSufficient) {
                return false; // Missing at least one required ingredient or quantity insufficient
            }
        }

        return true;
    }

    /**
     * Normalizes and checks if a required ingredient name matches a pantry item name.
     * Handles case insensitivity, trim, and singular/plural variations (e.g. "tomato" vs "tomatoes", "egg" vs "eggs").
     */
    public static boolean isIngredientMatch(String required, String pantryItem) {
        if (required == null || pantryItem == null) return false;

        String reqClean = normalizeName(required);
        String pantryClean = normalizeName(pantryItem);

        if (reqClean.equals(pantryClean)) {
            return true;
        }

        // Handle common plural/singular forms
        if (reqClean.equals(pantryClean + "s") || pantryClean.equals(reqClean + "s")) {
            return true;
        }
        if (reqClean.equals(pantryClean + "es") || pantryClean.equals(reqClean + "es")) {
            return true;
        }

        // Substring check for compound matches (e.g. "olive oil" vs "oil")
        if (pantryClean.contains(reqClean) || reqClean.contains(pantryClean)) {
            return true;
        }

        return false;
    }

    private static String normalizeName(String name) {
        return name.toLowerCase().trim()
                .replaceAll("[^a-z0-9 ]", "");
    }
}
