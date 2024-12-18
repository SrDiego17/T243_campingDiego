package es.unizar.eina.T243_camping.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.T243_camping.database.Parcela;

public class ParcelaListAdapter extends ListAdapter<Parcela, ParcelaViewHolder> {
    private final OnEditClickListener editListener;
    private final OnDeleteClickListener deleteListener;


    public ParcelaListAdapter(@NonNull DiffUtil.ItemCallback<Parcela> diffCallback,
                              OnEditClickListener editListener, OnDeleteClickListener deleteListener) {
        super(diffCallback);
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @Override
    public ParcelaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ParcelaViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ParcelaViewHolder holder, int position) {
        Parcela current = getItem(position);
        holder.bind(current, editListener, deleteListener);
    }

    public interface OnEditClickListener {
        void onEditClick(int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    static class ParcelaDiff extends DiffUtil.ItemCallback<Parcela> {

        @Override
        public boolean areItemsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            //android.util.Log.d ( "ParcelaDiff" , "areItemsTheSame " + oldItem.getId() + " vs " + newItem.getId() + " " +  (oldItem.getId() == newItem.getId()));
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            //android.util.Log.d ( "ParcelaDiff" , "areContentsTheSame " + oldItem.getTitle() + " vs " + newItem.getTitle() + " " + oldItem.getTitle().equals(newItem.getTitle()));
            // We are just worried about differences in visual representation, i.e. changes in the title
            return oldItem.getNombre().equals(newItem.getNombre());
        }
    }
}



