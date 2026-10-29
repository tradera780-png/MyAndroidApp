package com.pantry.smartpantrymanager.model;

import java.util.List;

public class Recipe {
    private long id;
    private String name;
    private String prepSteps;
    private String prepTime;
    private List<RecipeIngredient> ingredients;

    public Recipe(long id, String name, String prepSteps, String prepTime, List<RecipeIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.prepSteps = prepSteps;
        this.prepTime = prepTime;
        this.ingredients = ingredients;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getPrepSteps() { return prepSteps; }
    public String getPrepTime() { return prepTime; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public void setIngredients(List<RecipeIngredient> ingredients) { this.ingredients = ingredients; }
}
