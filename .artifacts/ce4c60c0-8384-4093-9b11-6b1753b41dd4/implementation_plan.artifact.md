# Implementation Plan - Smart Pantry Manager (Java Android App)

The Smart Pantry Manager is a Java-based Android application designed to help users reduce food waste by tracking their home pantry ingredients and strictly suggesting recipes that can be made using available ingredients without requiring a grocery trip.

## User Review Required

> [!IMPORTANT]
> - **Language:** Built entirely in **Java** as requested by the assignment brief.
> - **Database:** Local SQLite database using `SQLiteOpenHelper` with full CRUD support for pantry items and pre-loaded seeded recipes (15–20 recipes).
> - **Core Logic:** Implements the strict-matching rule (recipes shown only if every required ingredient is present in sufficient quantity, with case/plural normalization).
> - **UI / Navigation:** Material Design 3 UI with Bottom Navigation linking Pantry, Suggested Recipes, All Recipes, and Settings screens.

## Open Questions

None at this stage. All requirements align directly with the Mobile App Development 700 assignment specification.

## Proposed Changes

### Project Foundation & Build Setup
#### [NEW] [build.gradle (Project)](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/build.gradle)
#### [NEW] [build.gradle (Module)](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/app/build.gradle)
#### [NEW] [settings.gradle](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/settings.gradle)
#### [NEW] [AndroidManifest.xml](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/app/src/main/AndroidManifest.xml)

### Database & Models
#### [NEW] [DatabaseHelper.java](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/app/src/main/java/com/pantry/smartpantrymanager/data/DatabaseHelper.java)
- SQLite database helper managing `pantry_items`, `recipes`, and `recipe_ingredients` tables.
- Seeds 15-20 diverse recipes on initial creation.
- Provides full CRUD methods for pantry items.

#### [NEW] [PantryItem.java & Recipe.java](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/app/src/main/java/com/pantry/smartpantrymanager/model/PantryItem.java)
- Data models for pantry items, recipes, and recipe ingredients.

### Core Business Logic
#### [NEW] [RecipeMatcher.java](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/app/src/main/java/com/pantry/smartpantrymanager/logic/RecipeMatcher.java)
- Implements strict-matching algorithm comparing pantry stock against recipe requirements with normalization (case-insensitivity, singular/plural matching).

### UI Activities, Fragments & Adapters
#### [NEW] [MainActivity.java & activity_main.xml](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/app/src/main/java/com/pantry/smartpantrymanager/MainActivity.java)
- Main host activity with Bottom Navigation.

#### [NEW] [PantryFragment.java & fragment_pantry.xml](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/app/src/main/java/com/pantry/smartpantrymanager/ui/pantry/PantryFragment.java)
- RecyclerView displaying user pantry items with add, edit, and delete actions.

#### [NEW] [AddEditPantryActivity.java & activity_add_edit_pantry.xml](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/app/src/main/java/com/pantry/smartpantrymanager/ui/pantry/AddEditPantryActivity.java)
- Form with input validation for adding/editing pantry ingredients.

#### [NEW] [SuggestedRecipesFragment.java & fragment_suggested_recipes.xml](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/app/src/main/java/com/pantry/smartpantrymanager/ui/recipes/SuggestedRecipesFragment.java)
- Displays strictly matching recipes or empty state feedback ("No recipes match your pantry yet - add more ingredients").

#### [NEW] [RecipeListFragment.java](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/app/src/main/java/com/pantry/smartpantrymanager/ui/recipes/RecipeListFragment.java)
- Displays all pre-loaded recipes in the database.

#### [NEW] [RecipeDetailActivity.java & activity_recipe_detail.xml](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/app/src/main/java/com/pantry/smartpantrymanager/ui/recipes/RecipeDetailActivity.java)
- Shows full ingredient list and preparation steps for a selected recipe.

#### [NEW] [SettingsFragment.java & fragment_settings.xml](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/app/src/main/java/com/pantry/smartpantrymanager/ui/settings/SettingsFragment.java)
- Settings screen for alerts and unit preferences.

#### [NEW] [Adapters (PantryAdapter, RecipeAdapter, RecipeIngredientAdapter)](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/app/src/main/java/com/pantry/smartpantrymanager/ui/adapters/)
- Custom RecyclerView adapters.

### Documentation
#### [MODIFY] [README.md](file:///C:/Users/hp/OneDrive/Desktop/MyAndroidApp/README.md)
- Complete assignment documentation, database choice justification, setup instructions.

## Verification Plan

### Automated Tests
- Build verification via gradle (`gradlew assembleDebug`).

### Manual Verification
- Deploy to emulator/device, verify CRUD operations on pantry items, verify strict recipe matching logic, check navigation and persistence across app restarts.
