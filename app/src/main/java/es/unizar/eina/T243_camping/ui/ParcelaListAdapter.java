package es.unizar.eina.T243_camping.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.T243_camping.database.Parcela;

public class ParcelaListAdapter extends ListAdapter<Parcela, ParcelaViewHolder> {
    private final OnItemClickListener itemClickListener;
    private final OnDeleteClickListener deleteListener;

    public ParcelaListAdapter(
            @NonNull DiffUtil.ItemCallback<Parcela> diffCallback,
            OnItemClickListener itemClickListener,
            OnDeleteClickListener deleteListener) {
        super(diffCallback);
        this.itemClickListener = itemClickListener;
        this.deleteListener = deleteListener;
    }

    @Override
    public ParcelaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ParcelaViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ParcelaViewHolder holder, int position) {
        Parcela current = getItem(position);
        holder.bind(current, itemClickListener, deleteListener);
    }

    // Interfaces para eventos de clic
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    static class ParcelaDiff extends DiffUtil.ItemCallback<Parcela> {
        @Override
        public boolean areItemsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            return oldItem.equals(newItem);
        }
    }
}
