package es.unizar.eina.T243_camping.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.T243_camping.database.Reserva;

public class ReservaListAdapter extends ListAdapter<Reserva, ReservaViewHolder> {
    private final OnEditClickListener editListener;
    private final OnDeleteClickListener deleteListener;


    public ReservaListAdapter(@NonNull DiffUtil.ItemCallback<Reserva> diffCallback,
                              OnEditClickListener editListener, OnDeleteClickListener deleteListener) {
        super(diffCallback);
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @Override
    public ReservaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ReservaViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ReservaViewHolder holder, int position) {
        Reserva current = getItem(position);
        holder.bind(current, editListener, deleteListener);
    }

    public interface OnEditClickListener {
        void onEditClick(int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    static class ReservaDiff extends DiffUtil.ItemCallback<Reserva> {
        @Override
        public boolean areItemsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
            //android.util.Log.d ( "ParcelaDiff" , "areItemsTheSame " + oldItem.getId() + " vs " + newItem.getId() + " " +  (oldItem.getId() == newItem.getId()));
            return oldItem.getID() == newItem.getID();
        }
        @Override
        public boolean areContentsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
            //android.util.Log.d ( "ParcelaDiff" , "areContentsTheSame " + oldItem.getTitle() + " vs " + newItem.getTitle() + " " + oldItem.getTitle().equals(newItem.getTitle()));
            // We are just worried about differences in visual representation, i.e. changes in the title
            return (oldItem.getNombreCliente().equals(newItem.getNombreCliente()) && oldItem.getTelefonoCliente().equals(newItem.getTelefonoCliente()) &&
                    oldItem.getFechaEntrada().equals(newItem.getFechaEntrada()) && oldItem.getFechaSalida().equals(newItem.getFechaSalida()));
        }
    }
}



