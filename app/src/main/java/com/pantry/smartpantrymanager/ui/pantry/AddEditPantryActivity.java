package com.pantry.smartpantrymanager.ui.pantry;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.pantry.smartpantrymanager.R;
import com.pantry.smartpantrymanager.data.DatabaseHelper;
import com.pantry.smartpantrymanager.model.PantryItem;

public class AddEditPantryActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextInputEditText editTextName, editTextQty, editTextUnit;
    private Button buttonSave;
    private DatabaseHelper databaseHelper;

    private boolean isEditMode = false;
    private long itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pantry);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewTitle = findViewById(R.id.textViewTitle);
        editTextName = findViewById(R.id.editTextName);
        editTextQty = findViewById(R.id.editTextQty);
        editTextUnit = findViewById(R.id.editTextUnit);
        buttonSave = findViewById(R.id.buttonSave);

        databaseHelper = new DatabaseHelper(this);

        if (getIntent().hasExtra("item_id")) {
            isEditMode = true;
            itemId = getIntent().getLongExtra("item_id", -1);
            textViewTitle.setText(R.string.edit_ingredient);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.edit_ingredient);
            }
            editTextName.setText(getIntent().getStringExtra("item_name"));
            editTextQty.setText(String.valueOf(getIntent().getDoubleExtra("item_qty", 0.0)));
            editTextUnit.setText(getIntent().getStringExtra("item_unit"));
        } else {
            textViewTitle.setText(R.string.add_ingredient);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.add_ingredient);
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        buttonSave.setOnClickListener(v -> savePantryItem());
    }

    private void savePantryItem() {
        String name = editTextName.getText() != null ? editTextName.getText().toString().trim() : "";
        String qtyStr = editTextQty.getText() != null ? editTextQty.getText().toString().trim() : "";
        String unit = editTextUnit.getText() != null ? editTextUnit.getText().toString().trim() : "";

        // Input validation
        if (name.isEmpty()) {
            editTextName.setError("Please enter ingredient name");
            editTextName.requestFocus();
            return;
        }

        if (qtyStr.isEmpty()) {
            editTextQty.setError("Please enter quantity");
            editTextQty.requestFocus();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(qtyStr);
        } catch (NumberFormatException e) {
            editTextQty.setError("Invalid quantity number");
            editTextQty.requestFocus();
            return;
        }

        if (unit.isEmpty()) {
            unit = "pcs";
        }

        if (isEditMode) {
            PantryItem item = new PantryItem(itemId, name, quantity, unit, "");
            databaseHelper.updatePantryItem(item);
            Toast.makeText(this, "Ingredient updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            PantryItem item = new PantryItem(name, quantity, unit, "");
            databaseHelper.addPantryItem(item);
            Toast.makeText(this, "Ingredient added successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
