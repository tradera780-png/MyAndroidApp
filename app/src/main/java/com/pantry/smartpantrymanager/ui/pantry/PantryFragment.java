package com.pantry.smartpantrymanager.ui.pantry;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pantry.smartpantrymanager.R;
import com.pantry.smartpantrymanager.data.DatabaseHelper;
import com.pantry.smartpantrymanager.model.PantryItem;
import com.pantry.smartpantrymanager.ui.adapters.PantryAdapter;

import java.util.List;

public class PantryFragment extends Fragment implements PantryAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private FloatingActionButton fabAdd;
    private DatabaseHelper databaseHelper;
    private PantryAdapter adapter;
    private List<PantryItem> pantryItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPantry);
        textViewEmpty = view.findViewById(R.id.textViewEmptyPantry);
        fabAdd = view.findViewById(R.id.fabAddPantry);

        databaseHelper = new DatabaseHelper(requireContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadPantryItems();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddEditPantryActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPantryItems();
    }

    private void loadPantryItems() {
        pantryItemList = databaseHelper.getAllPantryItems();
        if (adapter == null) {
            adapter = new PantryAdapter(pantryItemList, this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(pantryItemList);
        }

        if (pantryItemList.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEditClick(PantryItem item) {
        Intent intent = new Intent(requireContext(), AddEditPantryActivity.class);
        intent.putExtra("item_id", item.getId());
        intent.putExtra("item_name", item.getName());
        intent.putExtra("item_qty", item.getQuantity());
        intent.putExtra("item_unit", item.getUnit());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        databaseHelper.deletePantryItem(item.getId());
        loadPantryItems();
    }
}
