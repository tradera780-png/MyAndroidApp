package com.pantry.smartpantrymanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pantry.smartpantrymanager.model.PantryItem;
import com.pantry.smartpantrymanager.model.Recipe;
import com.pantry.smartpantrymanager.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    // Pantry columns
    public static final String COLUMN_PANTRY_ID = "id";
    public static final String COLUMN_PANTRY_NAME = "name";
    public static final String COLUMN_PANTRY_QTY = "quantity";
    public static final String COLUMN_PANTRY_UNIT = "unit";
    public static final String COLUMN_PANTRY_EXPIRY = "expiry_date";

    // Recipe columns
    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_STEPS = "prep_steps";
    public static final String COLUMN_RECIPE_TIME = "prep_time";

    // Recipe Ingredient columns
    public static final String COLUMN_RI_ID = "id";
    public static final String COLUMN_RI_RECIPE_ID = "recipe_id";
    public static final String COLUMN_RI_NAME = "ingredient_name";
    public static final String COLUMN_RI_QTY = "quantity";
    public static final String COLUMN_RI_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PANTRY_TABLE = "CREATE TABLE " + TABLE_PANTRY + "("
                + COLUMN_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PANTRY_NAME + " TEXT,"
                + COLUMN_PANTRY_QTY + " REAL,"
                + COLUMN_PANTRY_UNIT + " TEXT,"
                + COLUMN_PANTRY_EXPIRY + " TEXT" + ")";

        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_RECIPE_NAME + " TEXT,"
                + COLUMN_RECIPE_STEPS + " TEXT,"
                + COLUMN_RECIPE_TIME + " TEXT" + ")";

        String CREATE_RECIPE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + "("
                + COLUMN_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_RI_RECIPE_ID + " INTEGER,"
                + COLUMN_RI_NAME + " TEXT,"
                + COLUMN_RI_QTY + " REAL,"
                + COLUMN_RI_UNIT + " TEXT" + ")";

        db.execSQL(CREATE_PANTRY_TABLE);
        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_RECIPE_INGREDIENTS_TABLE);

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        onCreate(db);
    }

    // --- Pantry CRUD Operations ---

    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PANTRY_NAME, item.getName().trim().toLowerCase());
        values.put(COLUMN_PANTRY_QTY, item.getQuantity());
        values.put(COLUMN_PANTRY_UNIT, item.getUnit());
        values.put(COLUMN_PANTRY_EXPIRY, item.getExpiryDate());

        long id = db.insert(TABLE_PANTRY, null, values);
        db.close();
        return id;
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> itemList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PANTRY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PANTRY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PANTRY_NAME));
                double qty = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PANTRY_QTY));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PANTRY_UNIT));
                String expiry = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PANTRY_EXPIRY));

                itemList.add(new PantryItem(id, name, qty, unit, expiry));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }

    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PANTRY_NAME, item.getName().trim().toLowerCase());
        values.put(COLUMN_PANTRY_QTY, item.getQuantity());
        values.put(COLUMN_PANTRY_UNIT, item.getUnit());
        values.put(COLUMN_PANTRY_EXPIRY, item.getExpiryDate());

        int rows = db.update(TABLE_PANTRY, values, COLUMN_PANTRY_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();
        return rows;
    }

    public void deletePantryItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, COLUMN_PANTRY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // --- Recipes & Seeding ---

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME));
                String steps = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_STEPS));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_TIME));

                List<RecipeIngredient> ingredients = getIngredientsForRecipe(id, db);
                recipeList.add(new Recipe(id, name, steps, time, ingredients));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recipeList;
    }

    public List<RecipeIngredient> getIngredientsForRecipe(long recipeId, SQLiteDatabase db) {
        List<RecipeIngredient> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_RECIPE_INGREDIENTS, null,
                COLUMN_RI_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_RI_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RI_NAME));
                double qty = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RI_QTY));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RI_UNIT));

                list.add(new RecipeIngredient(id, recipeId, name, qty, unit));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private void seedRecipes(SQLiteDatabase db) {
        // 1. Scrambled Eggs
        long r1 = insertRecipe(db, "Scrambled Eggs", "1. Crack eggs into a bowl.\n2. Add milk and salt, whisk well.\n3. Melt butter in a pan over medium heat.\n4. Pour mixture and stir gently until fluffy.", "5 mins");
        insertRecipeIngredient(db, r1, "egg", 3.0, "pcs");
        insertRecipeIngredient(db, r1, "milk", 30.0, "ml");
        insertRecipeIngredient(db, r1, "butter", 10.0, "g");
        insertRecipeIngredient(db, r1, "salt", 2.0, "g");

        // 2. Tomato Pasta
        long r2 = insertRecipe(db, "Tomato Pasta", "1. Boil pasta in salted water until al dente.\n2. Heat olive oil in a pan, sauté garlic and tomatoes.\n3. Toss pasta in tomato sauce and serve hot.", "15 mins");
        insertRecipeIngredient(db, r2, "pasta", 200.0, "g");
        insertRecipeIngredient(db, r2, "tomato", 3.0, "pcs");
        insertRecipeIngredient(db, r2, "garlic", 2.0, " cloves");
        insertRecipeIngredient(db, r2, "olive oil", 15.0, "ml");

        // 3. Garlic Bread
        long r3 = insertRecipe(db, "Garlic Bread", "1. Mix minced garlic with softened butter.\n2. Spread generously over bread slices.\n3. Toast in oven until golden brown.", "10 mins");
        insertRecipeIngredient(db, r3, "bread", 4.0, "slices");
        insertRecipeIngredient(db, r3, "butter", 20.0, "g");
        insertRecipeIngredient(db, r3, "garlic", 2.0, " cloves");

        // 4. Simple Omelette
        long r4 = insertRecipe(db, "Simple Omelette", "1. Whisk eggs with salt and pepper.\n2. Pour into a hot oiled skillet.\n3. Fold over and serve.", "7 mins");
        insertRecipeIngredient(db, r4, "egg", 2.0, "pcs");
        insertRecipeIngredient(db, r4, "salt", 1.0, "g");
        insertRecipeIngredient(db, r4, "butter", 10.0, "g");

        // 5. Peanut Butter Toast
        long r5 = insertRecipe(db, "Peanut Butter Toast", "1. Toast bread slices until crisp.\n2. Spread peanut butter evenly on top.", "3 mins");
        insertRecipeIngredient(db, r5, "bread", 2.0, "slices");
        insertRecipeIngredient(db, r5, "peanut butter", 30.0, "g");

        // 6. Fried Rice
        long r6 = insertRecipe(db, "Simple Fried Rice", "1. Heat oil in a pan.\n2. Add cooked rice, soy sauce, and diced vegetables.\n3. Stir fry for 5 minutes.", "12 mins");
        insertRecipeIngredient(db, r6, "rice", 250.0, "g");
        insertRecipeIngredient(db, r6, "soy sauce", 20.0, "ml");
        insertRecipeIngredient(db, r6, "vegetable oil", 15.0, "ml");

        // 7. Grilled Cheese Sandwich
        long r7 = insertRecipe(db, "Grilled Cheese Sandwich", "1. Place cheese between two slices of bread.\n2. Butter the outside.\n3. Grill in a pan until cheese melts.", "8 mins");
        insertRecipeIngredient(db, r7, "bread", 2.0, "slices");
        insertRecipeIngredient(db, r7, "cheese", 50.0, "g");
        insertRecipeIngredient(db, r7, "butter", 10.0, "g");

        // 8. Lemon Tea
        long r8 = insertRecipe(db, "Lemon Tea", "1. Boil water and steep tea bag.\n2. Add fresh lemon juice and sugar to taste.", "5 mins");
        insertRecipeIngredient(db, r8, "water", 250.0, "ml");
        insertRecipeIngredient(db, r8, "lemon", 0.5, "pcs");
        insertRecipeIngredient(db, r8, "sugar", 10.0, "g");

        // 9. Tuna Salad
        long r9 = insertRecipe(db, "Tuna Salad", "1. Mix canned tuna with mayonnaise and diced onions.\n2. Serve over lettuce.", "5 mins");
        insertRecipeIngredient(db, r9, "tuna", 1.0, "can");
        insertRecipeIngredient(db, r9, "mayonnaise", 30.0, "g");
        insertRecipeIngredient(db, r9, "onion", 0.5, "pcs");

        // 10. Vegetable Soup
        long r10 = insertRecipe(db, "Vegetable Soup", "1. Boil water with chopped carrots, potatoes, and onions.\n2. Simmer until tender and season with salt.", "20 mins");
        insertRecipeIngredient(db, r10, "water", 500.0, "ml");
        insertRecipeIngredient(db, r10, "carrot", 2.0, "pcs");
        insertRecipeIngredient(db, r10, "potato", 2.0, "pcs");
        insertRecipeIngredient(db, r10, "salt", 3.0, "g");

        // 11. Pasta Carbonara
        long r11 = insertRecipe(db, "Pasta Carbonara", "1. Cook pasta.\n2. Mix eggs and cheese in a bowl.\n3. Combine hot pasta with egg mixture and black pepper.", "15 mins");
        insertRecipeIngredient(db, r11, "pasta", 200.0, "g");
        insertRecipeIngredient(db, r11, "egg", 2.0, "pcs");
        insertRecipeIngredient(db, r11, "cheese", 40.0, "g");

        // 12. Bean Chili
        long r12 = insertRecipe(db, "Bean Chili", "1. Simmer beans, chopped tomatoes, and onions in a pot for 25 mins.", "25 mins");
        insertRecipeIngredient(db, r12, "beans", 200.0, "g");
        insertRecipeIngredient(db, r12, "tomato", 2.0, "pcs");
        insertRecipeIngredient(db, r12, "onion", 1.0, "pcs");

        // 13. French Toast
        long r13 = insertRecipe(db, "French Toast", "1. Whisk egg and milk.\n2. Dip bread slices in mixture.\n3. Fry in pan until golden.", "10 mins");
        insertRecipeIngredient(db, r13, "bread", 2.0, "slices");
        insertRecipeIngredient(db, r13, "egg", 1.0, "pcs");
        insertRecipeIngredient(db, r13, "milk", 50.0, "ml");

        // 14. Rice Pudding
        long r14 = insertRecipe(db, "Rice Pudding", "1. Simmer rice in milk with sugar until thick and creamy.", "20 mins");
        insertRecipeIngredient(db, r14, "rice", 100.0, "g");
        insertRecipeIngredient(db, r14, "milk", 300.0, "ml");
        insertRecipeIngredient(db, r14, "sugar", 30.0, "g");

        // 15. Mashed Potatoes
        long r15 = insertRecipe(db, "Mashed Potatoes", "1. Boil potatoes until soft.\n2. Mash with butter, milk, and salt.", "15 mins");
        insertRecipeIngredient(db, r15, "potato", 4.0, "pcs");
        insertRecipeIngredient(db, r15, "butter", 30.0, "g");
        insertRecipeIngredient(db, r15, "milk", 40.0, "ml");
        insertRecipeIngredient(db, r15, "salt", 3.0, "g");
    }

    private long insertRecipe(SQLiteDatabase db, String name, String steps, String time) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, name);
        values.put(COLUMN_RECIPE_STEPS, steps);
        values.put(COLUMN_RECIPE_TIME, time);
        return db.insert(TABLE_RECIPES, null, values);
    }

    private void insertRecipeIngredient(SQLiteDatabase db, long recipeId, String name, double qty, String unit) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RI_RECIPE_ID, recipeId);
        values.put(COLUMN_RI_NAME, name.trim().toLowerCase());
        values.put(COLUMN_RI_QTY, qty);
        values.put(COLUMN_RI_UNIT, unit);
        db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
    }
}
