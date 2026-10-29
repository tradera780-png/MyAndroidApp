# Smart Pantry Manager

## Application Description
**Smart Pantry Manager** is a Java-based Android application designed to help users reduce food waste by tracking their home pantry inventory and strictly suggesting recipes that can be made using available ingredients without requiring a grocery trip.

## Core Features
1. **Pantry Management:** Full CRUD (Create, Read, Update, Delete) functionality for pantry items with quantities, units, and names.
2. **Pre-loaded Recipe Collection:** Over 15 diverse recipes pre-seeded into the local database upon first run.
3. **Strict-Matching Recipe Algorithm:** Suggests only recipes where every single required ingredient is present in sufficient quantity in the user's pantry (with intelligent case-insensitivity and plural/singular normalization).
4. **Interactive Navigation:** Bottom navigation bar seamlessly switching between Pantry, Suggested Recipes, All Recipes, and Settings screens.
5. **Data Persistence:** SQLite local database ensuring all user pantry data persists across app restarts.

## Database Choice & Justification
- **Database:** SQLite (via `SQLiteOpenHelper`)
- **Justification:** SQLite is implemented locally on-device, lightweight, highly robust, requires no internet connectivity or cloud account configuration, and guarantees immediate persistence of user pantry items between application sessions.

## Setup & Running Instructions
1. Clone or download the project repository.
2. Open the project in **Android Studio** (Hedgehog / Iguana / Jellyfish or newer).
3. Sync project with Gradle files.
4. Run the app on an Android Emulator (API 24 or higher) or physical Android device.
