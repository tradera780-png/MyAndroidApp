package com.pantry.smartpantrymanager.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pantry.smartpantrymanager.R;
import com.pantry.smartpantrymanager.model.PantryItem;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryItemList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    public PantryAdapter(List<PantryItem> pantryItemList, OnItemClickListener listener) {
        this.pantryItemList = pantryItemList;
        this.listener = listener;
    }

    public void updateData(List<PantryItem> newList) {
        this.pantryItemList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryItemList.get(position);
        holder.textViewName.setText(item.getName());
        holder.textViewQty.setText("QTY: " + item.getQuantity() + " Unit: " + item.getUnit());

        holder.buttonEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(item);
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return pantryItemList != null ? pantryItemList.size() : 0;
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewQty;
        ImageButton buttonEdit, buttonDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewItemName);
            textViewQty = itemView.findViewById(R.id.textViewItemQty);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
